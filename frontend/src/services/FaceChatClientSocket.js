///////////////////////////////////////////////// 초기화 구문 /////////////////////////////////////////////////
var localStream = new MediaStream();
const channelPeerConnectionsMap = new Map();
const streamMap = new Map();
const streamSenderMap = new Map();

var myInfo;
var users = new Set();

var isVideoOn = false;

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

// var socket = io.connect('https://k3a204.p.ssafy.io/api/facechat');
var socket;

//채널 접속
var channel = "tryIt";
///////////////////////////////////////////////// 초기화 구문 /////////////////////////////////////////////////



/////////////////////////////////////////////// 리스너 연결 구문 ///////////////////////////////////////////////

var localVideo;


/////////////////////////////////////////////// 리스너 연결 구문 ///////////////////////////////////////////////



////////////////////////////////////////////////// 실행 코드 //////////////////////////////////////////////////

if (location.hostname !== 'localhost') {
    requestTurn(
        'https://computeengineondemand.appspot.com/turn?username=41784574&key=4080218913'
    );
}

////////////////////////////////////////////////// 실행 코드 //////////////////////////////////////////////////



////////////////////////////////////////////////// 함수 영역 //////////////////////////////////////////////////

function loadChannelInfo(channelId, email, _socket) {


    channel = channelId;
    myInfo = email;
    socket = _socket;

    socket.on('member', member => {
        if (member != myInfo) {
            if (!users.has(member)) {
                users.add(member);
                createPeerConnection(member);
            }

        }
    })


    socket.on('new member', member => {
        //기존 Connection 정리
        if (users.has(member)) {
            channelPeerConnectionsMap.get(member).close();
            users.delete(member);
            channelPeerConnectionsMap.delete(member);
            streamMap.delete(member);
            streamSenderMap.delete(member);
        }

        var info = { channel: channel, member: myInfo };
        socket.emit('alert member', info);
    });

    socket.on('sender info', function(connect) {
        if (connect.sender != myInfo && connect.receiver == myInfo) {
            var sdp = connect.sdp;
            var sender = connect.sender;
            //console.log(myInfo, "peer connect to remote", sender);
            channelPeerConnectionsMap.get(sender).setRemoteDescription(new RTCSessionDescription(sdp));
            console.dir(new RTCSessionDescription(sdp));
            ///////////////////////////////////////////////
            if (!streamMap.get(sender))
                streamMap.set(sender, new MediaStream());

            var videoComponent = document.getElementById("video_" + sender);
            if (videoComponent != undefined) {
                videoComponent.srcObject = streamMap.get(sender);
            } else {
                //console.log("no component");
            }
            ///////////////////////////////////////////////
            doAnswer(sender);
        }

    });

    socket.on('receiver info', function(connect) {
        if (connect.sender != myInfo && connect.receiver == myInfo) {
            //console.log("receiver info", connect);
            var sdp = connect.sdp;
            var sender = connect.sender;

            //console.log(myInfo, "peer connect to remote", sender);
            channelPeerConnectionsMap.get(sender).setRemoteDescription(new RTCSessionDescription(sdp));
            console.dir(new RTCSessionDescription(sdp));
            console.dir(channelPeerConnectionsMap.get(sender))

        }

    });

    socket.on('candidate', async function(connect) {
        if (connect.sender != myInfo && connect.receiver == myInfo) {
            var candidate = new RTCIceCandidate(connect.candidate);
            if (candidate)
                await channelPeerConnectionsMap.get(connect.sender).addIceCandidate(candidate);
        }
    });

    socket.on('on video', member => {
        var videoComponent = document.getElementById("video_" + member);
        if (videoComponent && videoComponent.srcObject) {
            videoComponent.srcObject.getTracks()
                .forEach(track =>
                    track.enabled = true
                );
        }
    });

    socket.on('off video', member => {
        var videoComponent = document.getElementById("video_" + member);
        if (videoComponent && videoComponent.srcObject) {
            videoComponent.srcObject.getTracks()
                .forEach(track =>
                    track.enabled = false
                );
        }
    });

    socket.on('who is video on', () => {
        if (isVideoOn) {
            createOffer();
        }
    });


    var info = {
        channel: channel,
        member: myInfo
    }
    socket.emit('join channel', info);

}

//비디오 실행
async function onVideo(vdId) {

    localVideo = document.getElementById(vdId);

    if (isVideoOn) {
        var info = {
            member: myInfo,
            channel: channel
        }
        socket.emit('on video', info);

        return;
    }
    // localVideo = document.querySelector('#video_' + myInfo);
    const stream = await navigator.mediaDevices.getUserMedia({
        audio: true,
        video: true
    });
    gotStream(stream);
}

//비디오 종료
function offVideo() {

    // let tracks = localStream.getTracks();
    // tracks.forEach((track) => {
    //     track.stop();
    // });
    // isVideoOn = false;
    // localStream.srcObject = null;

    var info = {
        member: myInfo,
        channel: channel
    }
    socket.emit('off video', info);
}

function createOffer() {
    channelPeerConnectionsMap.forEach((value, key) => {
        if (streamSenderMap.has(key)) {
            return;
        }
        for (const track of localStream.getTracks()) {
            var sender = value.addTrack(track);

            streamSenderMap.set(key, sender);
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
    });

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
            streamMap.set(member, new MediaStream());
            //console.log(member, "와의 커넥션 객체 생성");
            var pc = new RTCPeerConnection(pcConfig);
            pc.onicecandidate = (event) => {
                handleIceCandidate(event, member);
            };
            // pc.onremovestream = (event) => {
            //     handleRemoteStreamRemoved(event, member);
            // };
            pc.ontrack = event => {
                handleTrack(event, member);
            };
            // pc.oniceconnectionstatechange = event => {
            //     handleConnectionEvent(event, member);
            // }

            channelPeerConnectionsMap.set(member, pc);
        }

    } catch (e) {
        //console.log('Failed to create PeerConnection, exception: ' + e.message);
        // alert('Cannot create RTCPeerConnection object.');
        return;
    }
}

function handleTrack(event, member) {

    if (event.streams && event.streams[0]) {
        event.streams[0].getTracks().forEach(track => {
            streamMap.get(member).addTrack(track, streamMap.get(member));
        });
    } else {
        streamMap.get(member).addTrack(event.track, streamMap.get(member));
    }
}

//peer listener//
function handleIceCandidate(event, member) {
    //console.log('icecandidate event: ', event, new Date().getTime());
    if (event.candidate) {
        var connect = {
            sender: myInfo,
            receiver: member,
            candidate: event.candidate,
            channel: channel
        };
        socket.emit('add candidate', connect);
    } else {
        //console.log('End of candidates.');
    }
}

/////////////////////////////////


// peer connection result//

function setLocalAndSendMessage(sessionDescription, user) {
    //console.log("set local", channelPeerConnectionsMap.get(user).connectionState)
    //console.log(myInfo, "set local peer to??", user);
    channelPeerConnectionsMap.get(user).setLocalDescription(sessionDescription);
    //console.log('setLocalAndSendMessage sending message', sessionDescription);

    if (sessionDescription.type == "offer") {
        //console.log(myInfo, "offer to", user)
        var connect = {};
        connect.channel = channel;
        connect.sdp = sessionDescription;
        connect.sender = myInfo;
        connect.receiver = user

        socket.emit('offer connect', connect);
    } else if (sessionDescription.type == "answer") {
        //console.log(myInfo, "answer to", user);
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



function gotStream(stream) {
    //console.log('Adding local stream.', localVideo);
    localStream = stream;
    localVideo.srcObject = stream;

    isVideoOn = true;
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
        //console.log('Getting TURN server from ', turnURL);
        // No TURN server. Get one from computeengineondemand.appspot.com:
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4 && xhr.status === 200) {
                var turnServer = JSON.parse(xhr.responseText);
                //console.log('Got TURN server: ', turnServer);
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



export { loadChannelInfo, onVideo, offVideo }