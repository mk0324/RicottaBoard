<template>
  <div class="chat">
    <v-responsive>
      <v-btn
        class="chat-button justify-center ma-3"
        fab
        @click="toggleChattingBox()"
        width="50px"
        height="50px"
        :color="gradColor()"
        @mouseover="isHover=true"
        @mouseout="isHover=false"
      >
        <v-icon color="white" size="28px">mdi-chat-processing</v-icon>
        <div v-show="notread != 0" class="dot"></div>
      </v-btn>
      <transition name="fade">
        <v-responsive
          class="hover chat-hover"
          v-show="isHover "
          >
          <strong>안 읽은 메시지</strong> | {{ notread }}개
        </v-responsive>
      </transition>
    </v-responsive>
    <v-navigation-drawer
      v-model="chattingBox"
      :permanent="chattingBox"
      app
      overflow
      right
      width="380px"
    >
      <div class="chat-container" id="chatContainer">
        <div id="chattingBox">
          <div class="chat-header" id="chatHeader">
            <div class="header-top">
              <span id="username">{{ this.naname }}</span><br>
              <v-icon @click="chattingBox = false" >mdi-close</v-icon>
            </div>
            <div id="clientList" class="client-list">
              <span id="user" v-for="(user, index) in clientList" :key="index">
                {{ user }}
              </span>
            </div>
          </div>

          <div class="chatbox" id="chatBox">
            <div class="goodchat-bubble bubble">매너 채팅 해주세요 :)</div>
          </div>

          <form>
            <div class="text-box" id="textBox">
              <v-text-field label="메시지 전송"
                v-model="chatlog.message"
                id="msgForm"
                @keyup.enter="enter"
              >
              </v-text-field>
              <input type="text" style="display:none;"/> 
              <v-icon 
                @click="sendChat"
                class="text-box-icon"
                v-show="!chatlog.message"
              >mdi-send</v-icon>
              <v-icon 
                @click="sendChat"
                class="text-box-icon"
                v-show="chatlog.message"
                color="blue"
              >mdi-send</v-icon>
            </div>
          </form>
        </div>
      </div>
    </v-navigation-drawer>

  </div>
</template>



<script>
// import ChatlogDataService from "../../services/ChatlogDataService"

export default {
  name: "Chat",
  props:{
    channelId : String,
  },
  created() {
    var myname = this.$store.getters.userData.nickname;
    if (this.$store.getters.userData.nickname == "") {
      myname = "Unknown_" + this.makeRandomName();
    }
    var chatcontainer = document.getElementById("chatContainer");
    var chatheader = document.getElementById("chatHeader");
    var chatbox = document.getElementById("chatBox");
    var textbox = document.getElementById("textBox");
    var $msgForm = $("#msgForm").val();
    this.naname = myname;
    this.Channel = this.channelId;
    //console.log('chat : ' + this.Channel)

    // //console.log('name is: ' + this.naname);
    // //console.log('channel is: ' + this.Channel);

    // //console.log("SOCKET IS @@@@@@: ");
    // //console.log(this.$socket);
    this.$socket._callbacks.$clientList = undefined;
    this.$socket._callbacks.$enter = undefined;
    this.$socket._callbacks.$out = undefined;
    this.$socket._callbacks.$s2c_chat = undefined;
    this.$socket._callbacks.$s2c_chat_me = undefined;
    this.$socket._callbacks.$s2c_text = undefined;

    this.$socket.emit("login", {
      name: this.naname,
      userid: this.naname,
      channelId: this.Channel,
    });

    this.$socket.on("enter", (data) => {
      // this.chatLogs.push(data.name + "님이 접속하셨습니다");
      // this.chatComes.push(data.name);
      $(".chatbox").append(
        '<div class="inout-bubble">' + data + "님이 입장하셨습니다.</div>"
      );
      setTimeout(function () {
        $(".chatbox").scrollTop($(".chatbox").prop("scrollHeight"));
      }, 50);
    });

    this.$socket.on("clientList", (data) => {
      var exceptme = [];
      //this.clientList = data;
      for (var i = 0; i < data.length; i++) {
        if (data[i] === myname) {
          continue;
        } else exceptme.push(data[i]); // 배열에 나의 정보는 빼고 넣어주었다
      }
      this.clientList = exceptme;
    });

    // 내 메시지는 띄우지 말야아함.
    this.$socket.on("s2c_chat", (data) => {
      var name = data.from.name;
      var msg = data.msg;
      var time = data.time;
      if (name === this.naname) {
        // 내 이름하고 같을 경우 채팅창에 띄워주지 않는다.
        //console.log("지금 내 이름 : " + this.naname);
      } else
        $(".chatbox").append(
          '<div class="friend-bubble bubble"><span>' + name + "</span><br>" + msg + '<span class="chat-time">' + time + "</span>" + "</div>"
        );

      if (!this.chattingBox) {
        this.notread += 1;
        //console.log("안읽은 메시지 수 : " + this.notread);
      }

      setTimeout(function () {
        $(".chatbox").scrollTop($(".chatbox").prop("scrollHeight"));
      }, 50);
    });

    this.$socket.on("s2c_text", (data) => {
      var name = data.from.name;
      var msg = data.msg;

      $(".textBoard").append("<h3><span>" + msg + "</span></h3>");
    });

    this.$socket.on("s2c_chat_me", (data) => {
      var name = data.from.name;
      var msg = data.msg;
      var time = data.time;
      $(".chatbox").append('<div class="my-bubble bubble">' + msg + '<span class="chat-time">' + time + "</span>" + "</div>");

      setTimeout(function () {
        $(".chatbox").scrollTop($(".chatbox").prop("scrollHeight"));
      }, 50);
    });

    this.$socket.on("out", (data) => {
      // if(!(data.from.name).eqauls("undefnied"))
      $(".chatbox").append(
        '<div class="inout-bubble">' +
          data.from.name +
          "님이 나가셨습니다.</div>"
      );
      setTimeout(function () {
        $(".chatbox").scrollTop($(".chatbox").prop("scrollHeight"));
      }, 50);
    });
  },

  destroyed() {
    this.$socket.emit("disconnect2", {});
  },

  data() {
    return {
      chatlogs: [],
      chattingBox: false,
      isList: false,
      clientList: [],
      textarea: "",
      message: "",
      chatmem: [],
      chatLogs: [],
      chatComes: [],
      chatNames: [],
      chatMsgs: [],
      naname: "",
      notread: 0,
      isHover: false,
      chatlog: {
        id: null,
        message: "",
        userid: "",
        roomid: "",
      },
      chatDrawer: false,
    };
  },
  methods: {
    toggleChattingBox() {
      this.notread = 0
      this.chattingBox = !this.chattingBox;
    },
    saveChatlog() {
      event.preventDefault(); // 줄바꿈 방지?
      event.stopPropagation();

      var data = {
        message: this.chatlog.message,
        userid: this.naname,
        roomid: this.Channel,
      };
    },

    sendChat() {
      event.preventDefault(); // 줄바꿈 방지?
      event.stopPropagation();
      //console.log("msgForm : " + this.chatlog.message);
      //console.log("channel : " + this.Channel);

      if (this.chatlog.message === "") pass;
      else {
        this.$socket.emit("chat", { msg: this.chatlog.message });
        this.chatlog.message = "";
        // this.saveChatlog();
      }
    },

    sendToBoard() {
      event.preventDefault(); // 줄바꿈 방지?
      event.stopPropagation();
      var $msgForm = $("#msgForm").val();

      this.$socket.emit("text", { msg: $msgForm });
      $("#msgForm").val("");

      //this.saveChatlog();
    },

    enter() {
      // 엔터 처리
      var code = event.keyCode;
      if (code == 13) {
        if (event.shiftKey === true) {
          // Shift + Enter 처리
        } else {
          this.sendChat();
          //this.saveChatlog();
        }
      }
    },

    makeRandomName() {
      var name = "";
      var possible = "abcdefghijklmnopqrstuvwxyz";
      for (var i = 0; i < 3; i++) {
        name += possible.charAt(Math.floor(Math.random() * possible.length));
      }
      return name;
      // var name = 1;
      // return name + Math.random()*10;
    },

    showList() {
      if (this.isList) this.isList = false;
      else this.isList = true;
    },

    gradColor() {
      if(this.chattingBox) {
        return "#08543A"
      }
      return "#0d875c"
    }
  },
};
</script>

<style>
.chat-button {
  position: fixed;
  bottom: 140px;
  left: 12px;
  width: 50px;
  height: 50px;
}


.chat-container {
  padding-left: 16px;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.5);
  transition: width 0.3s ease;
  position: absolute;
  top: 70px;
  height: 100%;
  width: 100%;
}

.chat-header {
  display: flex;
  flex-direction: column;
  background-color: white;
  border-bottom: 2px solid rgba(0, 0, 0, 0.3);
  padding: 8px 8px 8px 8px;
}

.chat-header button{
  margin-bottom: auto;
}

.chat-header #username {
  vertical-align: middle;
  font-size: 17px;
  font-weight: 500;
  margin-right: 20px;
  color: #343434;
}

.chat-header #userList {
  margin-right: 5%;
}
.chat-header #user {
  font-size: 13px;
  background-color: #F5F5EC;
  border-radius: 10px;
  padding: 2px 8px;
  float: left;
  clear: both;
}
.header-top {
  display: flex;
  justify-content: space-between;
  margin-bottom: 4px;
}
.client-list {
  display: flex;
  overflow-x: auto;
}
.content {
  position: absolute;
  top: 10%;
  left: 5%;
  transform: translate(-50%, -50%);
  font-size: 10px;
  color: red;
  z-index: 2;
  text-align: center;
}
/* chat box */

.chatbox {
  height: calc(100vh - 220px);
  background-color: white;
  padding: 10px;
  overflow-y: scroll;
  position: relative;
}

.bubble {
  margin: 5px 0;
  display: inline-block;
  max-width: 300px;
  font-size: 14px;
  position: relative;
}

.inout-bubble {
  background-color: #D3D3CA;
  border-radius: 14px 14px 14px 14px;
  padding: 7px 50px 7px 50px;
  float: left;
  clear: both;
  color: rgba(0,0,0,0.87);
  margin: 5px 0;
  max-width: 300px;
  font-size: 14px;
  text-align: center;
  position: relative;
}

.friend-bubble {
  background-color: #d7e4f2;
  border-radius: 14px 14px 14px 0;
  padding: 7px 15px 7px 15px;
  float: left;
  clear: both;
}

.friend-bubble span{
  font-size: 0.2rem;
  float: left;
  clear: both;
}
.friend-bubble .chat-time{
  margin-left: 20px;
}
.goodchat-bubble {
  background-color: lightpink;
  border-radius: 14px 14px 14px 14px;
  padding: 7px 15px 7px 15px;
  float: left;
  clear: both;
}

.my-bubble {
  background-color: #fff46d;
  border-radius: 14px 14px 0px 14px;
  padding: 7px 15px 7px 15px;
  float: right;
  clear: both;
}

.my-bubble .chat-time{
  left: -32px;
}

.chat-time {
  position: absolute;
  bottom: 0px;
  font-size: 0.7rem !important;
}
/* text box */

.text-box {
  width: 100%;
  position: fixed;
  bottom:0;
  display: flex;
  background-color: white;
  border-top: 2px solid rgba(0, 0, 0, 0.3);
  padding: 0px 10px;
}

.text-box-icon {
  margin-top: auto;
  margin-bottom: auto;
  margin-left: 10px;
  margin-right: 10px;
  padding: 2px;
}

.dot {
  position: absolute;
  bottom: 0px;
  right: 10px;
  background-color: rgb(255, 89, 34);
  border-radius: 50%;
  padding: 5px;
}

.hover {
  background-color: white;
  width: auto;
  height: auto;
  position: fixed;
  z-index: 2;
  left: 90px;
  padding: 8px 16px;
  border-radius: 4px;
  box-shadow: 0px 5px 5px -3px rgba(0, 0, 0, 0.1),
    0px 8px 10px 1px rgba(0, 0, 0, 0.08), 0px 3px 14px 2px rgba(0, 0, 0, 0.05);
}

.chat-hover{
  bottom: 157px;
}



#sendToBoard {
  background-color: skyblue;
  width: 60px;
  height: 60px;
  color: white;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  margin-left: 10px;
  float: left;
}



h3 {
  /* margin: 20px; */
  font-family: "Paytone One";
  color: #202020;
  text-transform: uppercase;
  letter-spacing: -2px;
}
h3 span {
  display: block;
  margin: 11px 0 17px 0;
  font-size: 80px;
  line-height: 80px;
  color: orange;
  text-shadow: 0 13.36px 8.896px #c4b59d, 0 -2px 1px #fff;
  letter-spacing: -4px;
}
</style>