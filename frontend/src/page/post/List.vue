<template>
  <div style="background-color:#f5f5ec">
    <v-container fluid>
      <div class="d-flex justify-space-between head">
        <div class="head-title">나의 모임목록 </div>
      </div>
      <v-row>
        <div class="p-3" v-if="channels.length === 0">
          <span>생성하거나 가입된 모임이 없습니다 🤦‍♀️🤦‍♂️</span>
        </div>
      </v-row>
      <v-btn icon @click="showAlbum">
        <v-icon>mdi-view-module</v-icon>
      </v-btn>
      <v-btn icon @click="showList">
        <v-icon>mdi-view-list</v-icon>
      </v-btn>

      <v-row style="width:80%;" v-if="howto">
        <v-col
          v-for="card in listChannels"
          :key="card.channelId"
          cols="4"
          md="4"
          @click="enterRoom(card.channelId, card.channelName)"
        >
          <v-hover v-slot:default="{ hover }">
            <v-card class="card-channel">
              <v-img
                :src="getRandomImage(card.channelName)"
                class="white--text align-end"
                gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
                height="300px"
              >
                <v-expand-transition>
                  <div
                    v-if="hover"
                    flex
                    class="d-flex transition-fast-in-fast-out black darken-2 v-card--reveal display-3 white--text"
                    style="height: 100%; align-items: center;
                          bottom: 0;
                          justify-content: center;
                          opacity: .5;
                          position: absolute;
                          width: 100%; "
                  >
                    <br />
                    <br />
                    <div class="channel-hover">입장하기</div>
                  </div>
                </v-expand-transition>
                <v-card-title v-text="card.channelName"></v-card-title>
              </v-img>
            </v-card>
          </v-hover>
        </v-col>
        <!-- 새로운 모임 -->
        <v-col
          cols="4"
          md="4"
          @click="openModal"
        >
          <v-hover v-slot:default="{ hover }">
            <v-card class="card-channel">
              <v-img
                class="white--text align-end"
                gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
                height="300px"
              >
                <v-expand-transition>
                  <div
                    v-if="hover"
                    flex
                    class="d-flex transition-fast-in-fast-out black darken-2 v-card--reveal display-3 white--text"
                    style="height: 100%; align-items: center; bottom: 0; justify-content: center; opacity: .5; position: absolute; width: 100%; ">
                    <br />
                    <br />
                    <span class="channel-hover">생성하기</span>
                  </div>
                </v-expand-transition>
                <v-card-title >
                  <v-icon size="x-large" class="mr-2">mdi-expand-all</v-icon>
                  새로운 모임</v-card-title>
              </v-img>
            </v-card>
          </v-hover>
        </v-col>
      </v-row>
      <ul style="width:80%; margin:auto;" class="list-group" v-if="!howto">
        <li
          class="list-group-item list-group-item-action"
          v-for="item in listChannels"
          v-bind:key="item.channelId"
          style
          v-on:click="enterRoom(item.channelId, item.channelName)"
        >
          <h6 style="text-align:left">{{item.channelName}}</h6>
        </li>
      </ul>
    </v-container>

    <v-dialog max-width="600px" persistent v-model="modal">
      <v-card>
      <v-card-title>
        <h3>모임 생성</h3>
      </v-card-title>
      <v-card-text>
        <v-form ref="form" v-model="valid" @submit.prevent>
          <div class="mail-form">
            <v-text-field
              label="모임 이름"
              v-model="channel_name"
              prepend-icon="mdi-account-supervisor"
              :rules="rules"
              counter="20"
               @keyup.enter="createChannel(valid)"
            ></v-text-field>
          </div>
        </v-form>
        <div class="text-center">
          <v-btn text class="primary white--text mx-2 mt-3" @click="createChannel(valid)">생성</v-btn>
          <v-btn text class="primary white--text mx-2 mt-3" @click="close">닫기</v-btn>
        </div>
      </v-card-text>
    </v-card>
  </v-dialog>
  </div>
</template>

<script>
import "../../assets/css/post.scss";
import constants from "../../lib/constants";
import SockJS from "sockjs-client";
import Stomp from "stomp-websocket";
import axios from "axios";
import http from "../../http-common.js";
import lodash from "lodash";
import * as channelApi from "../../api/channel";
import bus from '../../utils/bus';

export default {
  data: () => ({
    channel_name: "",
    channels: [],
    howto: true,
    modal: false,
    rules: [v => ((4 <= v.length) && (v.length<= 20 ))|| '모임 이름은 4-20자여야 합니다!'],
    valid: false,
  }),
  created() {
    this.findAllChannel();
  },
  methods: {
    showAlbum() {
      this.howto = true;
    },
    showList() {
      this.howto = false;
    },
    getRandomImage(idString) {
      return `https://picsum.photos/seed/${idString}/200/300`;
    },
    findAllChannel() {
      let data = {email: this.$store.state.userData.email}
      let config = {
        headers: {
          "Authorization" : "Bearer " + this.$store.getters.accessToken
        }
      }
      channelApi.findAllChannel(data, config, 
        (response) => {
          //console.log(response);
          // prevent html, allow json array
          if (
            Object.prototype.toString.call(response.data) === "[object Array]"
          )
            this.channels = response.data;
        },
        (err) => {
          //console.log(err)
        }
      );
    },
    createChannel: function (valid) {
      if(this.$cookie.get('AccessToken') === null){
        return
      }
      if (!valid) {
        alert("모임 이름을 입력해 주십시오.");
        return;
      } else {
        const params = {
          channelName: this.channel_name,
          email: this.$store.state.userData.email,
        };
        //console.log(params);
        const config = {
          headers: {
            Authorization: "Bearer " + this.$store.getters.accessToken,
          },
        };
        channelApi.createChannel(params, config,
          (response) => {
            alert(response.data.channelName + "채널 개설에 성공하였습니다.");
            this.channel_name = "";
            this.findAllChannel();
          },
          (err) => {
            alert("채널 개설에 실패하였습니다.");
          }
        );
      }
      this.modal=false;
    },
    enterRoom: function (channelId, channelName) {
      // localStorage.setItem("wsboard.channelId", channelId);
      // localStorage.setItem("wsboard.channelName", channelName);
      // location.href = "/channel/" + channelId;
      this.$router.push(`/channel/${channelId}/${channelName}`)
    },
    
    openModal() { // 모임생성 모달
        // 로그인 되어있지 않은 사용자 -> 로그인 모달띄우기 
      if(this.$cookie.get('AccessToken') === null){
        //console.log('openMODAL');
        this.$store.commit("toggleModal");
        return
      } else { // 로그인 되어있는 사용자 -> 모임 생성 모달 띄우기
        this.modal = true;
      }
    },
    close() {
      this.modal = false;
    },
  },
  computed: {
    listChannels() {
      return _.orderBy(this.channels, "channelName", "asc");
    },
  },
  mounted() {
    bus.$emit('end:Loading');
  },
};
</script>

<style src="../../assets/css/my-component.css"></style>
<style scoped>
.head {
  height: 40px;
}
.card-channel {
  cursor: pointer;
  transition: opacity 2s ease-in-out;
}
.head-title {
  font-size: 1.875em;
  font-weight: 600;
}
.channel-hover {
  font-size: 1.5rem;
  color: white;
}
</style>
