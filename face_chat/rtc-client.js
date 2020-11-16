///////////////////////////////////////////////// 초기화 구문 /////////////////////////////////////////////////
var localStream = new MediaStream();
var remoteStream = new MediaStream();
const channelPeerConnectionsMap = new Map();
var isConnect = new Map();
var isIceCandidate = new Map();

var myInfo;
var users = new Set();



var pcConfig = {
    'iceServers': [{
            'urls': 'stun:stun.l.google.com:19302'
        },
        { "urls": "turn:numb.viagenie.ca", "username": "webrtc@live.com", "credential": "muazkh" }
    ]
};

// Set up audio and video regardless of what devices are present.
var sdpConstraints = {
    offerToReceiveAudio: true,
    offerToReceiveVideo: true
};

var socket = io.connect('https://k3a204.p.ssafy.io/api/facechat');

//채널 접속
var channel = "tryIt";
///////////////////////////////////////////////// 초기화 구문 /////////////////////////////////////////////////



/////////////////////////////////////////////// 리스너 연결 구문 ///////////////////////////////////////////////
// socket.emit('join channel', channel);

socket.on('member', member => {
    if (member != myInfo) {
        if (!users.has(member)) {
            console.log("새 멤버", member);
            users.add(member);
            createPeerConnection(member);
        }

    }
})

socket.on('alert', () => {
    var info = { channel: channel, member: myInfo };
    socket.emit('alert member', info);
})

socket.on('new member', () => {
    // if (myInfo == undefined) {
    // myInfo = user;
    // console.log("myInfo : ", user);
    // } else {
    // var info = { user: myInfo, channel: channel };
    socket.emit('new member', channel);
    // }
})

socket.on('sender info', function(connect) {
    if (connect.sender != myInfo && connect.receiver == myInfo) {
        var sdp = connect.sdp;
        var sender = connect.sender;
        console.log(myInfo, "peer connect to remote", sender);
        channelPeerConnectionsMap.get(sender).setRemoteDescription(new RTCSessionDescription(sdp));
        console.dir(new RTCSessionDescription(sdp));
        doAnswer(sender);
    }

});

socket.on('receiver info', function(connect) {
    if (connect.sender != myInfo && connect.receiver == myInfo) {
        console.log("receiver info", connect);
        var sdp = connect.sdp;
        var sender = connect.sender;

        console.log(myInfo, "peer connect to remote", sender);
        channelPeerConnectionsMap.get(sender).setRemoteDescription(new RTCSessionDescription(sdp));
        console.dir(new RTCSessionDescription(sdp));
        console.dir(channelPeerConnectionsMap.get(sender))

    }

});

socket.on('candidate', function(connect) {
    if (connect.sender != myInfo && connect.receiver == myInfo && !isIceCandidate.get(connect.sender)) {
        // var candidate = new RTCIceCandidate({
        //     sdpMLineIndex: connect.candidate.sdpMLineIndex,
        //     candidate: connect.candidate
        // });
        var candidate = new RTCIceCandidate(connect.candidate);
        channelPeerConnectionsMap.get(connect.sender).addIceCandidate(candidate,
            (res) => {
                console.log("SUCCESS!!!!!!!", res);
            },
            (err) => {
                console.log("ERR!!!!!!!!!!!!", err);
            });
        isIceCandidate.set(connect.sender, true);
    }
});

var localVideo = document.querySelector('#localVideo');
var remoteVideo = document.querySelector('#remoteVideo');
var btn = document.querySelector('#btn');
var show = document.querySelector('#show');
var add = document.querySelector('#add');

show.onclick = function() {
    channelPeerConnectionsMap.forEach((value, key, mapObject) => {
        console.log(key + ' , ' + value);
        console.dir(value);
    });

}

add.onclick = function() {
    gotStream(localStream);
}

btn.onclick = async function() {
    const stream = await navigator.mediaDevices.getUserMedia({
        audio: false,
        video: true
    });
    // navigator.mediaDevices.getUserMedia({
    //         audio: false,
    //         video: true
    //     })
    //     .then(gotStream)
    //     .catch(function(e) {
    //         alert('getUserMedia() error: ' + e.name);
    //         alert(e.toString());
    //     });
    gotStream(stream)
};
/////////////////////////////////////////////// 리스너 연결 구문 ///////////////////////////////////////////////



////////////////////////////////////////////////// 실행 코드 //////////////////////////////////////////////////
// createPeerConnection();
if (location.hostname !== 'localhost') {
    requestTurn(
        'https://computeengineondemand.appspot.com/turn?username=41784574&key=4080218913'
    );
}

////////////////////////////////////////////////// 실행 코드 //////////////////////////////////////////////////



////////////////////////////////////////////////// 함수 영역 //////////////////////////////////////////////////

function loadChannelInfo(channel, email) {
    this.channel = channel;
    this.myInfo = email;
    socket.emit('join channel', channel);
}

//비디오 실행
async function onVideo() {
    const stream = await navigator.mediaDevices.getUserMedia({
        audio: false,
        video: true
    });
    gotStream(stream)
}

function createOffer() {
    channelPeerConnectionsMap.forEach((value, key) => {
        if (!isConnect.get(key)) {
            for (const track of localStream.getTracks()) {
                value.addTrack(track, localStream);
            };
            value.createOffer()
                .then(
                    sessionDescription => {
                        setLocalAndSendMessage(sessionDescription, key);
                    })
                .catch(
                    err =>
                    alert(err)
                );
            isConnect.set(key, true);
        }
    })
}

function doAnswer(sender) {
    channelPeerConnectionsMap.get(sender).createAnswer().then(
        sessionDescription => {
            setLocalAndSendMessage(sessionDescription, sender);
        },
        onCreateSessionDescriptionError
    );
}

function createPeerConnection(member) {
    try {

        if (!channelPeerConnectionsMap.has(member)) {
            var pc = new RTCPeerConnection(pcConfig);
            pc.onicecandidate = (event) => {
                handleIceCandidate(event, member);
            };
            // pc.onaddstream = handleRemoteStreamAdded;
            pc.onremovestream = handleRemoteStreamRemoved;

            pc.ontrack = handleTrack;

            channelPeerConnectionsMap.set(member, pc);
            isConnect.set(member, false);
            isIceCandidate.set(member, false);
            console.log(member, "와의 커넥션 객체 생성");
        }

    } catch (e) {
        console.log('Failed to create PeerConnection, exception: ' + e.message);
        alert('Cannot create RTCPeerConnection object.');
        return;
    }
}

function handleTrack(event) {
    remoteStream = event.streams[0];
    remoteVideo.srcObject = remoteStream;
}

//peer listener//
function handleIceCandidate(event, member) {
    console.log('icecandidate event: ', event, new Date().getTime());
    if (event.candidate) {
        var connect = {
            sender: myInfo,
            receiver: member,
            candidate: event.candidate,
            channel: channel
        };
        socket.emit('add candidate', connect);
    } else {
        console.log('End of candidates.');
    }
}

//해당 유저 리모트 화면에 Stream을 보여줘야 한다.
// function handleRemoteStreamAdded(event) {
//     console.dir(event.stream);
//     console.log('Remote stream added.');
//     remoteStream = event.stream;
//     // remoteStream = window.URL.createObjectURL(event.stream);
//     remoteVideo.srcObject = remoteStream;
// }

function handleRemoteStreamRemoved(event) {
    console.log('Remote stream removed. Event: ', event);
}

/////////////////////////////////


// peer connection result//

function setLocalAndSendMessage(sessionDescription, user) {
    console.log("set local", channelPeerConnectionsMap.get(user).connectionState)
    console.log(myInfo, "set local peer to??", user);
    channelPeerConnectionsMap.get(user).setLocalDescription(sessionDescription);
    console.log('setLocalAndSendMessage sending message', sessionDescription);

    if (sessionDescription.type == "offer") {
        console.log(myInfo, "offer to", user)
        var connect = {};
        connect.channel = channel;
        connect.sdp = sessionDescription;
        connect.sender = myInfo;
        connect.receiver = user

        socket.emit('offer connect', connect);
    } else if (sessionDescription.type == "answer") {
        console.log(myInfo, "answer to", user);
        var connect = {};
        connect.channel = channel;
        connect.sdp = sessionDescription;
        connect.sender = myInfo;
        connect.receiver = user;
        console.dir(channelPeerConnectionsMap.get(user));
        socket.emit('answer connect', connect);
    }

}

function onCreateSessionDescriptionError(error) {
    trace('Failed to create session description: ' + error.toString());
}


function handleCreateOfferError(event) {
    console.log('createOffer() error: ', event);
}

/////////////////////////////////



function gotStream(stream) {
    console.log('Adding local stream.');
    localStream = stream;
    localVideo.srcObject = stream;

    createOffer();
}

function requestTurn(turnURL) {
    var turnExists = false;
    for (var i in pcConfig.iceServers) {
        if (pcConfig.iceServers[i].urls.substr(0, 5) === 'turn:') {
            turnExists = true;
            break;
        }
    }
    if (!turnExists) {
        console.log('Getting TURN server from ', turnURL);
        // No TURN server. Get one from computeengineondemand.appspot.com:
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4 && xhr.status === 200) {
                var turnServer = JSON.parse(xhr.responseText);
                console.log('Got TURN server: ', turnServer);
                pcConfig.iceServers.push({
                    'urls': 'turn:' + turnServer.username + '@' + turnServer.turn,
                    'credential': turnServer.password
                });
            }
        };
        xhr.open('GET', turnURL, true);
        xhr.send();
    }
}


////////////////////////////////////////////////// 함수 영역 //////////////////////////////////////////////////