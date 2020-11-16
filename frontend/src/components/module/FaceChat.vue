<template>
  <div
    class="MoveableBox video video_container">
    <p class="video_nickname">{{userNickname}}</p>
    <video
      class="video_component"
      :id="videoInfo.vdId"
      src=""
      :muted="mute ? true : false"
      autoplay playsinline
      ></video>
      <button 
      class="video_btn"
      v-show="userEmail==myEmail"
      @click="onClickVideo">{{onVideoBtnText}}</button>
      <button
      class="video_btn"
      v-show="userEmail!=myEmail"
      @click="muteVideo"
      >{{muteBtnText}}</button>
  </div>
</template>


<script>
import { loadChannelInfo, onVideo, offVideo } from "../../services/FaceChatClientSocket.js"

export default {
  props:{
    videoInfo : Object,
    channelId : String,
    userEmail : String,
    userNickname : String,
    myEmail : String
  },
  methods: {
    onClickVideo() {
      if(!this.videoStream) {
        onVideo(this.videoInfo.vdId);
        this.videoStream = true;
        this.onVideoBtnText = "off";
      } else {
        offVideo();
        this.videoStream = false;
        this.onVideoBtnText = "on";
      }
    },
    muteVideo() {
      if(!this.mute) {
        this.mute = true;
        this.muteBtnText = "soundOn";
      } else {
        this.mute = false;
        this.muteBtnText = "mute";
      }
    }
  },
  data() {
    return {
      videoStream : false,
      onVideoBtnText : "on",
      mute : false,
      muteBtnText : "mute"
    }
  },
  created() {
    if(this.userEmail==this.myEmail) {
      this.mute = true;
    }
  },
};
</script>

<style>
.video_container {
  width: 560px;
  height: 630px;
  background-color: white;
  /* border: solid 2px gray;  */
  border-radius: 5px;
  box-shadow: .5rem 1rem 2rem rgba(0,0,0,.3)!important;
}
.video_component {
  width: 500px;
  height: 500px;
  background-color: white;

  transform:rotate(90deg);
  -moz-transform: scaleX(-1); 
  -o-transform: scaleX(-1); 
  -webkit-transform: scaleX(-1); 
  transform: scaleX(-1);   
  filter: FlipH;
  -ms-filter: "FlipH";
}
.video_btn {
  float: right;
  background-color: #1976D2;
  border-radius: 2px;
  padding: 5px 10px;
  color: white;
}
.video_nickname{
  font-weight: bold;
  font-size: 20px;
}
</style>

