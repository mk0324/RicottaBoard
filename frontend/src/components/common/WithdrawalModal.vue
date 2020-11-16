<template>
  <v-dialog max-width="600px" persistent v-model="dialog">
    <v-card>
  
      <v-snackbar
        app
        top
        v-model="snackbar"
        timeout="2000"
        color="success"
      >성공적으로 전송 되었습니다</v-snackbar>

      <v-card-title>
        <h3>모임 탈퇴</h3>
      </v-card-title>
      <v-card-text>
        <div class="withdrawal">
          <span>정말 탈퇴하실거에요?..😢</span><br>
          <ul class="list">
            <li> 사용자는 모든 서비스에 대한 액세스 권한을 즉시 상실하게 됩니다.</li>
            <li>사용자가 만든 콘텐츠는 서비스 내에 남습니다.</li>
          </ul>

          <div v-if="success" style="text-align: center;">✔성공적으로 탈퇴되었습니다</div>
        </div>
        <div v-if="loading" class="d-flex justify-content-center mb-2">
          <v-progress-circular
            :size="25"
            color="orange"
            indeterminate
          ></v-progress-circular>
        </div>
        <div class="text-center">
          <v-btn text class="red lighten-1 white--text mx-2 mt-3" @click="withdrawal">탈퇴</v-btn>
          <v-btn text class="orange lighten-2 white--text mx-2 mt-3" @click="close">닫기</v-btn>
        </div>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import http from "../../http-common.js";

export default {
  data() {
    return {
      loading: false,
      snackbar: false,
      success: false,
    };
  },
  computed: {
    dialog() {
      return this.$store.state.withdrawalModal;
    },
  },
  methods: {
    withdrawal() {       
      this.loading = true;
      const url = "/login";
      const mydata = {
        data: {
          "channelId": localStorage.getItem("wsboard.channelId"),
          "email": this.$cookie.get('AccessData').split(',')[0].split(':')[1], 
        }
      }
      setTimeout(() => {
        http.delete("/board/channel/withdrawal", mydata)
        .then((response) => {
          //console.log(response.data);
          this.success = true;
          setTimeout(() => {
            this.$store.state.withdrawalModal = false;
            this.$router.go(-1);
          },1000)
        }).catch((err) => {
          alert(err);
        }) 
        
        this.loading = false;
      },1000)
    },
    close() {
      this.$store.state.withdrawalModal = false;
    }
  }
};
</script>
<style scoped>
.withdrawal {
  color: rgba(0,0,0,0.8);
}
span {
  font-size: 1.05rem;
  margin-bottom: 10px;
}
.list {
  margin-top: 10px;
  padding-bottom: 20px;
  list-style-type: disc !important;
}
</style>