<template>
  <div class="changePassword" style="background-color:#f5f5ec">
    <div class="d-flex justify-content-center">
      <div class="changePasswordForm col text-center mt-3">
        <h2 class="m-3">비밀번호 변경</h2>
        <p class="lead m-3">비밀번호를 변경하시겠습니까?</p>
        <div>
          <label class="col text-left pb-0 pt-2">기존 비밀번호</label>
          <input
            id="password"
            class="inputsForm col"
            type="password"
            placeholder="기존 비밀번호를 입력해주세요."
            v-model="password.origin"
            @change="changeOrigin()"
            @keypress.enter="submit()"
          >
        </div>
        <div>
          <label class="col text-left pb-0 pt-2">새 비밀번호</label>
          <input
            id="newPassword"
            class="inputsForm col"
            type="password"
            v-model="password.new"
            placeholder="새 비밀번호를 입력해주세요."
            @change="changeNew()"
            @keypress.enter="submit()"
          >
        </div>
        <div>
          <label class="col text-left pb-0 pt-2">비밀번호 확인</label>
          <input
            id="passwordConfirm"
            class="inputsForm col"
            type="password"
            v-model="password.confirm"
            placeholder="비밀번호를 한번더 입력해주세요."
            @change="changeConfirm()"
            @keypress.enter="submit()"
          >
        </div>
        <button
          @click="submit"
          class="updateButton text-white p-2 mt-5"
          type="button"
        >
          <span>변 경</span>
        </button>
      </div>
      <v-snackbar
          bottom
          v-model="snackbar.isPresent"
          :timeout="snackbar.timeout"
          :color="snackbar.color"
      >{{ snackbar.text }}</v-snackbar>
    </div>

    <footer
      class="mx-auto wrap"
      style="text-align:center; position:absolute; bottom:10px;"
    >
      <p class="footerText" @click="teamPage()">ⓒHungrybird</p>
    </footer>
  </div>
</template>



<script>
import '../../assets/css/user.scss'
import constants from '../../lib/constants'
import * as userApi from '@/api/user.js';
import bus from '../../utils/bus';

export default {
    components: {
    },
    created(){
    },
    methods: {  
      changeOrigin() {
        if (this.password.origin.length > 7) {
          this.isChange.origin = true;
        } else {
          this.isChange.origin = false;
        }
      },
      changeNew() {
        if (this.password.new.length >= 8) {
          this.isChange.new = true;
        } else {
          this.isChange.new = false;
        } 
      },
      changeConfirm() {
        if (this.password.new == this.password.confirm) {
          this.isChange.confirm = true;
        } else {
          this.isChange.confirm = false;
        }
      },      
      submit(){
        if (!this.isChange.origin || !this.isChange.new) {
          this.createSnackbar("비밀번호는 8자리 이상입니다.", 2000, "error");
        } else if (!this.isChange.confirm) {
          this.createSnackbar("비밀번호가 일치하지 않습니다.", 2000, "error");
        } else {
          //console.log('good?')
          const updatePasswordRequest = {
            "oldPassword":this.password.origin,
            "newPassword":this.password.new,
          }
          const config = {
            headers: {
              "Authorization" : "Bearer " + this.$store.getters.accessToken
            }
          }
          userApi.updatePassword(updatePasswordRequest, config,
            res => {
              //console.log('good');
              this.createSnackbar("비밀번호가 변경되었습니다.", 2000, "green");                    
            },
            err => {
              //console.log('error', err);
              this.createSnackbar("비밀번호가 틀렸습니다.", 2000, "error");
            });
            this.password.origin = '';
            this.password.new = '';
            this.password.confirm = '';
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
    computed:{
        
    },
    watch: {
      'password.origin': function() {
        //console.log(this.password.origin);
        this.changeOrigin();
      },
      'password.new': function() {
        this.changeNew();
      },
      'password.confirm': function() {
        this.changeConfirm();
      },
    },
    data: () => {
        return {
            password:{
              origin:'',
              new:'',
              confirm:'',
            },
            snackbar: {
              isPresent: false,
              text: "",
              timeout: 1000,
            },
            isChange: {
              origin: false,
              new: false,
              confirm: false,
            },
            constants,
        }
    },
    mounted() {
      bus.$emit('end:Loading');
    },

}

</script>

<style scoped>

.changePassword {
  background:#f5f5ec;
}
.changePasswordForm {
  max-width: 500px;
}
.lh-condensed { line-height: 1.25; }

.inputsForm {
  border:solid 1px #dadada;
  height: 45px;
  border-radius: 5px;
  background-color:white;
}

.updateButton {
  border-radius: 5px;
  width: 100%;
  background-color:#0d875C;
  border:solid 0px;
}
</style>



