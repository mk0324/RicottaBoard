<template>
  <div class="history-box">
    <v-btn @click="showHistory()" class="main-button" fab width="50px" height="50px">
      <v-icon size="36px">mdi-update</v-icon>
    </v-btn>
    <div v-if="isHistory" class="main">
      <div class="hint"  stlye="padding-bottom: 0px;">최근 수정사항 20개를 보여줍니다</div><hr/>
      <div class="history-list">
        <div v-for="(history, idx) in historyList" :key="idx" class="history-item">
          <span class="user"> <strong>{{ history.editUser }}</strong> 님이 </span><br>
          <div class="module">
            <span class="module-name" v-for="(editModule, i) in history.editModule" :key="i"> {{ editModule }} </span>
            <span> 을 수정했습니다. </span><br>
          </div>
          <span class="time">{{ history.editTime.substr(0,10) }} {{ history.editTime.substr(11,8)}}</span><hr>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as boardApi from "../../api/board.js";

export default {
  data() {
    return {
      isHistory: false,
      historyList: [],
    };
  },
  created() {
    this.fetchHistory();
  },
  methods: {
    showHistory() {
      this.isHistory = !this.isHistory;
      if(!this.isHistory) 
        this.fetchHistory();
    },
    fetchHistory() {
      boardApi.getHistories( this.$route.params.channelId,
        (response) => {
          //console.log("History Response 정보: ", response);
          this.historyList = response.data;
        },
        (err) => {
          //console.log("History 불러오기 에러: ", err);
        }
      );
    },
  },
};
</script>

<style>
.history-box {
  display: flex;
  flex-direction: column;
  margin: 16px;
  
}

.main-button {
  margin-left: auto;
  cursor: pointer;
  width: 50px;
  height: 50px;
}


.main {
  padding: 5px;
  background-color: white;
  border-radius: 20px;
  /* border: 2px solid rgba(104, 104, 104, 0.5); */
  box-shadow: 0px 5px 5px -3px rgba(0, 0, 0, 0.2),
    0px 8px 10px 1px rgba(0, 0, 0, 0.14), 0px 3px 14px 2px rgba(0, 0, 0, 0.12);
  overflow-y: auto;
}

.hint {
  padding: 12px;
}

.history-list {
  width: 300px;
  max-height: 400px;
  background-color: white;
  padding: 12px;
}

.history-item {
  /* background-color: white; */
  padding: 4px;
  border-radius: 8px;
}

.module {
  background-color: #f5f5ec;
}

.module-name {
  color: #0d875c;
}

.time {
  text-align: right;
  font-size: 0.8rem;
}
</style>