// var nodeStatic = require('node-static')
const app = require('express')();
const https = require('https');
const fs = require('fs');
const http = require('http');

const options = {
    key: fs.readFileSync('/etc/letsencrypt/live/k3a204.p.ssafy.io/privkey.pem'),
    cert: fs.readFileSync('/etc/letsencrypt/live/k3a204.p.ssafy.io/cert.pem'),
    ca: fs.readFileSync('/etc/letsencrypt/live/k3a204.p.ssafy.io/chain.pem'),
    requestCert: false,
    rejectUnauthorized: false
};

var server = https.createServer(options, app);
// var server = http.createServer(app);

var io = require('socket.io')(server);

// io.set('transports', ['websocket']);

app.all('/*', function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "X-Requested-With");
    next();
});

// app.get('/', function(req, res) {
// console.log("in socket");
// res.sendFile('Hellow Chating App Server');
// });



console.log("rtc server socket on");
// io.sockets.on('connection', function(socket) {
io.on('connection', function(socket) {
    socket.on('add candidate', function(connect) {
        var channel = connect.channel;

        io.sockets.in(channel).emit('candidate', connect);
    });

    socket.on('offer connect', function(connect) {
        var channel = connect.channel;

        io.sockets.in(channel).emit('sender info', connect);
    });

    socket.on('answer connect', function(connect) {
        var channel = connect.channel;

        io.sockets.in(channel).emit('receiver info', connect);
    });

    socket.on('join channel', info => {

        socket.join(info.channel);

        io.sockets.in(info.channel).emit('new member', info.member);

        setTimeout(() => {
            io.sockets.in(info.channel).emit('who is video on');
        }, 3000);

        setTimeout(() => {
            io.sockets.in(info.channel).emit('who is video on');
        }, 5000);

        setTimeout(() => {
            io.sockets.in(info.channel).emit('who is video on');
        }, 7000);
    });

    socket.on('alert member', info => {
        io.sockets.in(info.channel).emit('member', info.member);

    });

    socket.on('on video', info => {
        io.sockets.in(info.channel).emit('on video', info.member);
    })

    socket.on('off video', info => {
        io.sockets.in(info.channel).emit('off video', info.member);
    });

});

server.listen(3031, function() {
    console.log("server listening on port 3031");
});