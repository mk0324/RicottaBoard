<template>
  <div class="user" id="join" style="background-color:#f5f5ec">
    <div class="wrapC table" ref="needToRescale" style="transform-origin: top; width: 10vw;">

      <div style="text-align: center;">
        <router-link
       
        v-bind:to="{name:constants.URL_TYPE.POST.ENTER}"
      >
        <img
          src="../../assets/img/Logo3.png"
          style="width:20vw; height:auto; margin: 1vw 0 1.5vw 0;"
        /></router-link>
      </div>

      <div class="form-wrap" style="width: 20vw; padding-top:20px; margin:auto; font-size: 0.83vw;">
        <div class="input-wrap">
          <p style="text-align: left; margin-bottom:4px">이름</p>
          <input
            id="realName"
            v-model="realName"
            style="border:solid 1px #dadada;height: 2.5vw; background-color:white;"
            placeholder="이름을 입력해주세요"
            type="text"
            @keypress.enter="createUserRequest()"
          />
        </div>
        <div class="input-wrap">
          <p style="text-align: left; margin-bottom:4px">이메일</p>
          <input
            v-model.lazy="email"
            id="email"
            placeholder="이메일을 입력해주세요"
            style="border:solid 1px #dadada;height: 2.5vw; background-color:white;"
            type="text"
            @change="emailCheck"
            @keypress.enter="createUserRequest()"
          />
          <!-- 이메일이 사용중인지 체크 -->
          {{this.$store.getters.canIUseIt}}
        </div>
        <div class="input-wrap">
          <p style="text-align: left; margin-bottom:4px">닉네임</p>
          <input
            v-model="nickName"
            id="nickName"
            style="border:solid 1px #dadada;height: 2.5vw; background-color:white;"
            placeholder="닉네임을 입력해주세요"
            type="text"            
            @change="nicknameCheck"
            @keypress.enter="createUserRequest()"
          />
          <!-- 닉네임이 사용중인지 체크 -->
          {{this.$store.getters.canNameUseIt}}
        </div>

        <div class="input-wrap password-wrap">
          <p style="text-align: left; margin-bottom:4px">비밀번호</p>
          <input
            v-model="password"
            id="password"
            :type="passwordType"
            placeholder="최소 8자 이상으로 입력해주세요"
            style="border:solid 1px #dadada;height: 2.5vw; background-color:white;"
            @keypress.enter="createUserRequest()"
          />
          <span :class="{active : passwordType==='text'}">
            <i class="fas fa-eye"></i>
          </span>
        </div>

        <div class="input-wrap password-wrap">
          <p style="text-align: left; margin-bottom:4px">비밀번호 확인</p>
          <input
            v-model="passwordConfirm"
            id="passwordConfirm"
            style="border:solid 1px #dadada;height: 2.5vw; background-color:white;"
            :type="passwordConfirmType"
            placeholder="비밀번호를 다시 한 번 입력해주세요"
            @keypress.enter="createUserRequest()"
          />
          <span :class="{active : passwordConfirmType==='text'}">
            <i class="fas fa-eye"></i>
          </span>
        </div>
        <div style="height:30px; width:100% margin:auto">
      
            <v-dialog width="600px" style="text-align:center">
              <template v-slot:activator="{ on, attrs }" style="text-align:center">
                <div class="d-flex" style="width:400px">
                  <v-checkbox class="d-flex" style="margin:0" v-model="isTerm" />
                  
                  <p
                    class="d-flex"
                    v-bind="attrs"
                    style="text-decoration:underline"
                    v-on="on"
                  >개인정보처리방침</p>
                  <div class="d-flex" style="margin-right:5px">에 동의</div>
                </div>
              </template>
              <v-card>
                <v-card-title>
                  <span class="headline">개인정보처리방침</span>
                </v-card-title>
                <v-card-text>
                  '(주)헝그리버드'는 (이하 '회사'는) 고객님의 개인정보를 중요시하며, "정보통신망 이용촉진 및 정보보호"에 관한 법률을 준수하고 있습니다. 회사는 개인정보취급방침을 통하여 고객님께서 제공하시는 개인정보가 어떠한 용도와 방식으로 이용되고 있으며, 개인정보보호를 위해 어떠한 조치가 취해지고 있는지 알려드립니다.
                  <br />
                  <br />■ 수집하는 개인정보 항목
                  <br />수집항목 : 이름, 닉네임, 비밀번호, 이메일, , 서비스 이용기록, 접속 로그, 접속 IP 정보
                  <br />ο개인정보 수집방법 : 홈페이지(회원가입)
                  <br />
                  <br />■ 개인정보의 수집 및 이용목적
                  <br />회사는 수집한 개인정보를 다음의 목적을 위해 활용합니다.
                  <br />1.회원 관리
                  <br />- 회원제 서비스 이용에 따른 본인확인 , 개인 식별 , 가입 의사 확인 , 고지사항 전달
                  <br />
                  <br />■ 개인정보의 보유 및 이용기간
                  <br />1.홈페이지 회원가입 후 탈퇴시 까지
                  <br />
                  <br />■ 개인정보의 파기절차 및 방법
                  <br />회사는 원칙적으로 개인정보 수집 및 이용목적이 달성된 후에는 해당 정보를 지체없이 파기합니다. 파기절차 및 방법은 다음과 같습니다.
                  <br />1.파기절차 : 회원님이 회원가입 등을 위해 입력하신 정보는 목적이 달성된 후 별도의 DB로 옮겨져(종이의 경우 별도의 서류함) 내부 방침 및 기타 관련 법령에 의한 정보보호 사유에 따라(보유 및 이용기간 참조) 일정 기간 저장된 후 파기되어집니다.별도 DB로 옮겨진 개인정보는 법률에 의한 경우가 아니고서는 보유되어지는 이외의 다른 목적으로 이용되지 않습니다.
                  <br />2.파기방법 : 전자적 파일형태로 저장된 개인정보는 즉각 삭제하도록 하겠습니다.
                  <br />
                  <br />■ 개인정보 제공
                  <br />회사는 이용자의 개인정보를 원칙적으로 외부에 제공하지 않습니다. 다만, 아래의 경우에는 예외로 합니다.
                  <br />법령의 규정에 의거하거나, 수사 목적으로 법령에 정해진 절차와 방법에 따라 수사기관의 요구가 있는 경우
                  <br />
                  <br />■ 개인정보에 관한 민원서비스
                  <br />회사는 고객의 개인정보를 보호하고 개인정보와 관련한 불만을 처리하기 위하여 아래와 같이 개인정보관리책임자를 지정하고 있습니다.
                  <br />개인정보관리 책임자
                  <br />성명 : 최문경
                  <br />전화번호 : 02-3429-5817
                  <br />이메일 : munkyung0324@gmail.com
                </v-card-text>
              </v-card>
            </v-dialog>
          
        </div>
        <button
          style="margin-top:40px; background-color:#0d875C; border:solid 0px; height: 2vw; padding: 0.5vh; font-size: 0.7vw"
          @click="createUserRequest()"
          class="btn"
        >
        확인
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
import "../../assets/css/user.scss";
import constants from "../../lib/constants";
import * as userApi from '@/api/user.js';
import bus from "../../utils/bus.js"

export default {
  components: {},
  watch: {},
  created() {
    this.$store.commit("toggleModal");
  },
  mounted() {
    bus.$emit('end:Loading');
  },
  methods: {
    createUserRequest: function () {
      var exptext = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;
      var passwordExp = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d$@$!%*#?&]{8,}$/;
      if (nickName.value == "") {
        this.createSnackbar("닉네임을 입력해주세요.", 2000, "error");
      } else if (exptext.test(email.value) == false) {
        //이메일 형식이 알파벳+숫자@알파벳+숫자.알파벳+숫자 형식이 아닐경우
        this.createSnackbar("닉네임을 입력해주세요.", 2000, "error");
        alert("이메일형식이 올바르지 않습니다.");
      } else if (realName.value == "") {
        this.createSnackbar("이름을 입력해주세요.", 2000, "error");
      } else if (password.value == "") {
        this.createSnackbar("비밀번호를 입력해주세요.", 2000, "error");
      } else if (passwordExp.test(password.value) == false) {
        this.createSnackbar("비밀번호 형식이 잘못되었습니다.", 2000, "error");
      } else if (password.value != passwordConfirm.value) {
        this.createSnackbar("비밀번호가 동일하지않습니다. 다시 입력해주세요.", 2000, "error");
      } else if (!this.isTerm) {
        this.createSnackbar("약관을 읽어보시고, 동의란에 체크해주세요.", 2000, "error");
      } else {
        this.$store.commit(constants.METHODS.EMAILCHECK, "reset");
        const data = {
          "email": this.email,
          "password": this.password,
          "registerAsAdmin": false,
          "username": this.realName,
          "nickname": this.nickName,
        };
        //console.log(data)
        userApi.createUser(data,
          () => {
            //console.log("create req success")
          },
          err => {
            //console.log('error', err);
            this.createSnackbar("비밀번호가 틀렸습니다.", 2000, "error");
          });
        this.$router.push(constants.URL_TYPE.USER.JOINDONE);
      }
    },
    emailCheck: function () {
      this.$store.dispatch(constants.METHODS.EMAILCHECK, email.value);
    },
    nicknameCheck: function () {
      this.nickName = this.nickName.trim();
      this.$store.dispatch(constants.METHODS.NICKNAMECHECK, this.nickName);
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
  watch: {},
  data: () => {
    return {
      email: "",
      nickName: "",
      realName: "",
      password: "",
      passwordConfirm: "",
      isTerm: false,
      passwordType: "password",
      passwordConfirmType: "password",
      constants,
      snackbar: {
        isPresent: false,
        text: "",
        timeout: 2000,
      },
    };
  },
};
</script>

<style scoped>
.allbtn {
  margin-top: 40px;
  border: solid 0px;
}

.footerText {
    height:40px;
    text-decoration: none;
    color:#000000;
    background:#f5f5ec;
    margin-bottom: 0;
    font-size: 0.85vw;
}
</style>
