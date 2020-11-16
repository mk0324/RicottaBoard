<template>
  <div style="background-color:#f5f5ec">
    <div class="d-flex justify-content-center" v-if="this.$store.getters.status == ''">
      <div class="outService col text-center mt-3" style="max-width: 500px;">
        <h2 class="m-3">회원 탈퇴</h2>
        <p class="lead m-3">정말로 탈퇴를 하시겠습니까?</p>
        <div class="mb-5">
          <p class="lead m-3">탈퇴를 원하신다면 비밀번호를 입력해주세요.</p>
          <div class="mb-5">
            <label class="col text-left pb-0 pt-2">비밀번호</label>
            <input
              id="password"
              type="password"
              class="inputsForm col"
              v-model="password"
              placeholder="확인을 위한 비밀번호를 입력해주세요."
              @keypress.enter="deleteMyAccount()"
            />
          </div>
          <div class="mb-5">
            <label class="col text-left pb-0 pt-2">비밀번호 확인</label>
            <input
              id="passwordConfirm"
              type="password"
              class="inputsForm col"
              v-model="passwordConfirm"
              placeholder="비밀번호를 다시 입력해주세요."
              @keypress.enter="deleteMyAccount()"
            />
          </div>
          <button
            @click="deleteMyAccount"
            class="updateButton text-white p-2 mt-5"
            type="button"
          >계정 탈퇴</button>
        </div>
      </div> 
      <v-snackbar
        bottom
        v-model="snackbar.isPresent"
        :timeout="snackbar.timeout"
        :color="snackbar.color"
      >{{ snackbar.text }}</v-snackbar>     
    </div>
    <div class="form-wrap" v-else>{{this.$store.getters.status}}</div>
    <footer
      class="mx-auto wrap"
      style="text-align:center; position:absolute; bottom:10px;"
    >
      <p class="footerText" @click="teamPage()">ⓒHungrybird</p>
    </footer>
  </div>
</template>

<script>
import "../../assets/css/user.scss";
import constants from "../../lib/constants";
import * as userApi from '@/api/user.js';
import bus from '../../utils/bus';

export default {
  data: () => {
    return {
      snackbar: {
        isPresent: false,
        text: "",
        timeout: 1000,
      },
      password: "",
      passwordConfirm: "",
    };
  },
  methods: {
    deleteMyAccount() {
      const token = this.$route.query.token;

      if (this.password.length < 8) {
        this.createSnackbar("비밀번호는 8자리 이상이어야 합니다.", 2000, "error");
        return;
      }
      if (this.password != this.passwordConfirm) {
        this.createSnackbar("비밀번호가 일치하지 않습니다.", 2000, "error");
        return;
      } else {
        const data = {
          email: this.$store.getters.userData.email,
          password: this.password,
        };
        userApi.deleteUser(data, this.$store.getters.accessToken,
          res => {
            if (res.data == "success") {
              this.createSnackbar("계정이 삭제되었습니다. 지금까지 이용해주셔서 감사합니다.\n 3초뒤 되돌아갑니다.", 3000, "error");
              setTimeout(() => {
                  this.$store.commit("reSetAll");
                  router.push('/');
              }, 3000)
            }
          },
          err => {
            this.createSnackbar("비밀번호가 일치하지 않습니다.", 2000, "error");
          })
        //console.log(data);
      }
    },
    teamPage() {
          this.$router.push('/@hungrybird')
    },
    createSnackbar(text, timeout, color) {
      this.snackbar.isPresent = true;
      this.snackbar.text = text;
      this.snackbar.timeout = timeout;
      this.snackbar.color = color;
    },
  },
  mounted() {
    bus.$emit('end:Loading');
  },
};
</script>

<style>
.outService {
  max-width: 500px;
}

.inputsForm {
  border:solid 1px #dadada;
  height: 45px;
  border-radius: 5px;
  background-color:white;
}

.updateButton {
  border-radius: 5px;
  width: 100%;
  background-color:red;
  border:solid 0px;
}
</style>