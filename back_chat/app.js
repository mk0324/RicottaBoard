'use strict';

const express = require("express");
const app = express();
const bodyParser = require("body-parser");
const fs = require('fs');
const cors = require("cors");
 
const db = require("./app/models");

app.use(cors({origin: true}));

//parse requests of content-type - application/json
app.use(bodyParser.json());

// parse requests of content-type - application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({ extended: true }));


// 디비 초기화
// db.sequelize.sync();
// db.sequelize.sync({ force: true }).then(() => {
//     console.log("Drop and re-sync db.");
//   });

require("./app/routes/chatlog.routes.js")(app);


const options = {
  key: fs.readFileSync('/etc/letsencrypt/live/k3a204.p.ssafy.io/privkey.pem'),
  cert: fs.readFileSync('/etc/letsencrypt/live/k3a204.p.ssafy.io/cert.pem'),
  ca: fs.readFileSync('/etc/letsencrypt/live/k3a204.p.ssafy.io/chain.pem'),
  requestCert: false,
  rejectUnauthorized: false
};

var server = require('https').createServer(options, app);

// http server를 socket.io server로 upgrade한다
var io = require('socket.io')(server);

var client_list = [];


// localhost:3000으로 서버에 접속하면 클라이언트로 index.html을 전송한다
app.get('/', function(req, res) {
    res.sendFile(__dirname + '/useTest.html');
});

app.get('/test', function(req, res) {
  res.send(server);
});

app.get('/index', function(req, res) {
  res.sendFile(__dirname + '/index.html');
});

app.get('/cam', function(req, res) {
    res.sendFile(__dirname + '/cam.html');
});

// connection event handler
// connection이 수립되면 event handler function의 인자로 socket인 들어온다
io.on('connection', function(socket) {
    var room = '';


    // 접속한 클라이언트의 정보가 수신되면
    socket.on('login', function(data) {
        console.log('Client logged-in:\n name: ' + data.name + '\n userid: ' + data.userid);


        // socket에 클라이언트 정보를 저장한다
        socket.name = data.name;
        socket.userid = data.userid;
        
        //socket.nickname = data.name;

        // socket.id = data.name;
        data.socketid = socket.id;


        // room 조인
        room = socket.room = data.channelId;
        console.log('('+socket.name+')'+ 'room : '+room);
        socket.join(room);
        console.log('socket.id: '+socket.id);
      
        //socket.broadcast.emit('enter', data.name);
        io.to(room).emit('enter', data.name);
        
        
        //var clients = io.sockets.clients(room); -> 최신버전에서는 작동 x
        var clientList = new Array();
        io.of('/').in(room).clients(function(error,roster){
          //console.log(io.sockets.sockets[roster[0]].name);
          for(var i=0; i<roster.length; i++){
            //console.log(io.sockets.sockets[roster[i]]);
            clientList.push(io.sockets.sockets[roster[i]].name);
          }
        });
        setTimeout(function() {
          console.log(clientList);
          io.to(room).emit('clientList', clientList); // 들어가는데에 시간이 조금 걸림.
        }, 1000);
        
    });
    // 클라이언트로부터의 메시지가 수신되면
    socket.on('chat', function(data) {
        console.log('Message from %s, 내용 : %s', socket.name, data.msg);
      
        var date = new Date();
        var msg = {
          to: {
              name: '',
          },
          from: {
              name: socket.name, // 내 소켓 네임
              userid: socket.userid
          },
          msg: data.msg,
          id: '',
          time: ("00" + date.getHours()).slice(-2) + ':' + ("00" + date.getMinutes()).slice(-2)
      };


        io.to(room).emit('s2c_chat', msg);
        console.log("상대한테 메시지 보내기 : "+data.msg);

        socket.emit('s2c_chat_me', msg);
        console.log("나한테 메시지 보내기 : "+data.msg);
    });

    // 클라이언트로부터의 메시지가 수신되면
    socket.on('text', function(data) {
      console.log('Message from %s, 내용 : %s', socket.name, data.msg);

      var msg = {
          to: {
              name: '',
          },
          from: {
              name: socket.name, // 내 소켓 네임
              userid: socket.userid
          },
          msg: data.msg,
          id: '',
      };
      
      io.to(room).emit('s2c_text', msg);
      console.log("상대한테 메시지 보내기 : "+data.msg);
     
  });

    // force client disconnect from server
    socket.on('forceDisconnect', function() {
        console.log(socket.name + "님이 소켓 연결을 끊으셨습니다.")
        socket.disconnect();
    })


    socket.on('disconnect2', function() {
      console.log(socket.name + "님이 페이지 이동으로 연결이 끊겼습니다.");
      var msg = {
          from: {
              name: socket.name,
              userid: socket.userid
          },
      };

      io.to(room).emit('out', msg);

      // 소켓 room에서 빠져나온 뒤 clientList 다시 프론트로 전송
      socket.leave(room);
      var clientList = new Array();
      io.of('/').in(room).clients(function(error,roster){
        for(var i=0; i<roster.length; i++){
          clientList.push(io.sockets.sockets[roster[i]].name);
        }
      });
      setTimeout(function() {
        io.to(room).emit('clientList', clientList); // 들어가는데에 시간이 조금 걸림.
      }, 1000);
    })

    socket.on('disconnect', function() {
        console.log(socket.name + "님이 연결을 끓으셨습니다.");
        var msg = {
            from: {
                name: socket.name,
                userid: socket.userid
            },
        };

        io.to(room).emit('out', msg);

        // 소켓 room에서 빠져나온 뒤 clientList 다시 프론트로 전송
        socket.leave(room);
        var clientList = new Array();
        io.of('/').in(room).clients(function(error,roster){
          for(var i=0; i<roster.length; i++){
            clientList.push(io.sockets.sockets[roster[i]].name);
          }
        });
        setTimeout(function() {
          io.to(room).emit('clientList', clientList); // 들어가는데에 시간이 조금 걸림.
        }, 1000);
       
    });

    /////////////////////////////// Cam

    // convenience function to log server messages on the client
  function log() {
    var array = ['Message from server:'];
    array.push.apply(array, arguments);
    socket.emit('log', array);
  }

  socket.on('message', function(message) {
    log('Client said: ', message);
    // for a real app, would be room-only (not broadcast)
    socket.broadcast.emit('message', message);
  });

  socket.on('create or join', function(room) {
    log('Received request to create or join room ' + room);
    console.log('_______________________________________\n'+
    'Received request to create or join room ' + room);


    var clientsInRoom = io.sockets.adapter.rooms[room];
    var numClients = clientsInRoom ? Object.keys(clientsInRoom.sockets).length : 0;
    log('Room ' + room + ' now has ' + numClients + ' client(s)');
    console.log('Room ' + room + ' now has ' + numClients + ' client(s)');

    if (numClients === 0) {
      socket.join(room);

      log('Client ID ' + socket.id + ' created room ' + room);
      console.log('Client ID ' + socket.id + ' created room ' + room);

      socket.emit('created', room, socket.id);
      console.log('첫번째 if문');
    } else if (numClients === 1) {
      log('Client ID ' + socket.id + ' joined room ' + room);
      io.sockets.in(room).emit('join', room);
      socket.join(room);
      socket.emit('joined', room, socket.id);
      io.sockets.in(room).emit('ready');
    } else { // max two clients
      socket.emit('full', room);
    }
  });

  socket.on('ipaddr', function() {
    var ifaces = os.networkInterfaces();
    for (var dev in ifaces) {
      ifaces[dev].forEach(function(details) {
        if (details.family === 'IPv4' && details.address !== '127.0.0.1') {
          socket.emit('ipaddr', details.address);
        }
      });
    }
  });

  socket.on('bye', function(){
    console.log('received bye');
  });





});





server.listen(3030, function() {
    console.log('Socket IO server listening on port 3030');
});

