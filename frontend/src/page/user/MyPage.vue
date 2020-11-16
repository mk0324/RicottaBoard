<template>
  <div class="Profile" style="background-color:#f5f5ec">
    <div class="d-flex justify-content-center">
      <div class="profile col text-center mt-3">
        <h2 class="m-3">내 프로필</h2>
        <p class="lead m-3">비밀번호를 입력해야 수정할 수 있습니다.</p>
        <div>
          <label class="col text-left pb-0 pt-2">이메일</label>
          <input
            id="email"
            class="inputsForm col"
            v-model="userData.email"
            disabled
          >
        </div>
        <div>
          <label class="col text-left pb-0 pt-2">이름</label>
          <input
            id="name"
            class="inputsForm col"
            v-model="userData.name"
            placeholder="이름을 입력해주세요."
            @keypress.enter="submit()"
          >
        </div>
        <div>
          <label class="col text-left pb-0 pt-2">닉네임</label>
          <input
            id="nickname"
            class="inputsForm col"
            v-model="userData.nickname"
            placeholder="닉네임을 입력해주세요."
            @keypress.enter="submit()"
          >
        </div>
        <div>
          <label class="col text-left pb-0 pt-2">비밀번호</label>
          <input
            id="password"
            class="inputsForm col"
            type="password"
            v-model="passwordCheck"
            placeholder="비밀번호를 입력해주세요."
            @keypress.enter="submit()"
          >
        </div>
        <button
          @click="submit"
          class="updateButton text-white p-2 mt-5"
          type="button"
        >확  인</button>

        <div class="row px-2 py-1 justify-content-between">
          <p class="lead text-left m-2 my-0">비밀번호를 변경하시겠습니까?</p>
          <router-link to="/user/PasswordEdit">
            <p class="lead passwordButton text-right m-2 my-0">비밀번호 변경</p>
          </router-link>
        </div>
        
        <div class="row px-2 py-1 justify-content-between">
          <p class="lead text-left m-2 my-0">탈퇴하시겠습니까?</p>
          <router-link :to="{name:constants.URL_TYPE.USER.DELETEUSER}">
            <p class="lead outButton text-right m-2 my-0">회원 탈퇴</p>
          </router-link>
        </div>
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
      this.userData = this.$store.getters.userData;
    },
    methods: {   
      changeIt() {
        //console.log(this.$store.getters.userData.nickname)
        if (this.userData.name.length > 0 && this.userData.nickname.length > 0 && this.passwordCheck.length >= 8) {
          this.isChange = true
        } else {
          this.isChange = false
        }
      },      
      submit(){
        if (this.isChange) {
          const newUser = {
            "email": this.userData.email,
            "username": this.userData.name,
            "nickname": this.userData.nickname,
            "password": this.passwordCheck,
          }
          const config = {
            headers: {
              "Authorization" : "Bearer " + this.$store.getters.accessToken
            }
          }
          userApi.userInfo(newUser, config,
            res => {
              //console.log('good',res.status);
              this.$store.commit(constants.METHODS.USER_INFO, {
                  newUser
              });
              this.createSnackbar("프로필이 수정되었습니다.", 2000, "green");
              //console.log(this.$store.getters.userData);                    
            },
            err => {
              //console.log('error', err);
              this.createSnackbar("비밀번호가 틀렸습니다.", 2000, "error");
            });
            //console.log(real)
          this.passwordCheck = "";
        } else {
          this.createSnackbar("값이 모두 입력되지 않았습니다.", 2000, "error")
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
      'userData.name': function() {
          this.changeIt();
      },
      'userData.nickname': function() {
          this.changeIt();
      },
      passwordCheck: function() {
          this.changeIt();
      },
    },
    data: () => {
        return {
            userData:{
              email:'',
              name:'',
              password:'',
              nickname:'',
            },
            snackbar: {
              isPresent: false,
              text: "",
              timeout: 1000,
            },
            isCheck: false,
            isChange: false,
            constants,
            passwordCheck: '',
        }
    },
    mounted() {
      bus.$emit('end:Loading');
    },

}

</script>

<style scoped>

.Profile {
  background:#f5f5ec;
}
.profile {
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
  background-color:#0d875C;
  border:solid 0px;
}

.passwordButton {
  color:#0d875C;
}

.passwordButton:hover {
  text-decoration:underline;
  text-decoration-color:#0d875C;  
}

.outButton {
  color:red;
}

.outButton:hover {
  text-decoration:underline;
  text-decoration-color:red;  
}
</style>



