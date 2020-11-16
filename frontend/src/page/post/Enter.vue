<template>
  <div class="enter-page" style="padding: 0px; margin: 0px">
    <transition name="loader">
      <div class="Loading" v-if="!isStarting">
        <img
          src="../../assets/img/Logo.png"
          style="margin: 20vw; margin-top: 40vh; width: 30%; height: auto"
        />
        <footer
          class="text-white"
          style="
            width: 100vw;
            text-align: center;
            position: absolute;
            bottom: 10px;
          "
        >
          <p>ⓒHungrybird</p>
        </footer>
      </div>
    </transition>
    <transition name="starter">
      <div v-show="isStarting" class="mainEnter">
        <div class="container cards" ref="needToRescale" style="transform-origin: top; transform: scale(1); min-width: 800px;" >
          <div class="row">
            <h4 style="font-size: 1.55vw; line-height: 150%; padding-left: 40px">
              리코타보드는 온라인 및 오프라인 모임을 지원하는 협업 툴
              플랫폼입니다
            </h4>
          </div>
          <div class="row justify-content-between" style="width: 72.9vw">
            <div class="col-4 card">
              <div class="card-inner">
                <img
                  class="card-img-top"
                  src="../../assets/img/enter2.svg"
                  alt=""
                  style="width: 18vw;"
                />
                <div class="card-body">
                  <h4 class="card-title">다양한 기능</h4>
                  <p class="card-text">
                    리코타보드에는 다양한 모듈을 통해 온라인 모임 관리를
                    도와줍니다. 포스트잇, 칸반보드, 화상채팅 등 10여 가지의
                    모듈의 사용법을 알아보아요!
                  </p>
                </div>
                <router-link to="/guide">
                  <v-btn class="vutton"  style="font-size: 0.74vw; height: 1.9vw;">Learn More</v-btn>
                </router-link>
              </div>
            </div>
            <div class="col-4 card">
              <div class="card-inner">
                <img
                  class="card-img-top"
                  src="../../assets/img/enter3.svg"
                  alt=""
                />
                <div class="card-body">
                  <h4 class="card-title">테스트 페이지</h4>
                  <p class="card-text">
                    회원가입 없이 간단하게 리코타보드의 기능을 체험해봅시다!
                    아래의 버튼을 클릭하면 비회원도 사용해 볼 수 있는 튜토리얼 페이지로
                    이동됩니다.
                  </p>
                </div>
                <router-link to="channel/earlyBird10TeamTestChannel1/Tutorial">
                  <v-btn class="vutton" style="font-size: 0.74vw; height: 1.9vw">Try It!</v-btn>
                </router-link>
              </div>
            </div>
            <div class="col-4 card">
              <div class="card-inner">
                <img
                  class="card-img-top"
                  src="../../assets/img/enter1.svg"
                  alt=""
                />
                <div class="card-body" v-if="$store.getters.accessToken == ''">
                  <h4 class="card-title">회원 관리</h4>
                  <p class="card-text">
                    리코타보드는 메일을 통한 인증을 통해 회원과 모임 채널의
                    가입이 가능합니다. 복잡한 인증 절차 없이 이메일 확인을 통해
                    쉽게 사용해보세요!
                  </p>
                </div>
                <div class="card-body" v-if="!$store.getters.accessToken == ''">
                  <h4 class="card-title">모임 관리</h4>
                  <p class="card-text">
                    리코타보드의 모임 관리 페이지는 만든 모임을 확인하고 모임에 들어갈 수 있습니다. 이제 리코타보드를 활용해보세요!
                  </p>
                </div>
                <div class="button-user">
                    <div class="d-flex justify-content-center">
                        <v-dialog width="350px ">
                            <template v-slot:activator="{ on, attrs }">
                                <v-btn
                                    class="vutton allbtn px-5 py-2"
                                    v-bind="attrs"
                                    v-on="on"
                                    color="#0d875C"
                                    v-if="$store.getters.accessToken == ''"
                                    style="font-size: 0.74vw; height: 1.9vw; margin-top: auto;"
                                >
                                    LOGIN
                                </v-btn>
                                <v-btn
                                    class="vutton allbtn px-5 py-2"
                                    color="#0d875C"
                                    v-if="$store.getters.accessToken !== ''"
                                    style="font-size: 0.74vw; height: 1.9vw; margin-top: auto;"
                                    @click="checkLogin"
                                >
                                    Main
                                </v-btn>
                            </template>
                            <v-card class="vutton" style="width: 350px; height: 280px">
                                <v-card-title>LOGIN</v-card-title>
                                <v-card-text
                                    style="
                                        background-color: white;
                                        height: 90px;
                                        padding-bottom: 0;
                                    "
                                    ><Login style="height: 120px; padding-bottom: 0"
                                /></v-card-text>
                            </v-card>
                        </v-dialog>
                        <router-link to="user/signup" v-if="$store.getters.accessToken == ''">
                            <v-btn class="vutton" style="font-size: 0.84vw; height: 1.9vw">Sign up</v-btn>
                        </router-link>
                    </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <footer
          style="
            width: 100vw;
            text-align: center;
            position: absolute;
            bottom: 10px;
          "
        >
          <p class="footerText" @click="teamPage()">ⓒHungrybird</p>
        </footer>
      </div>
    </transition>
  </div>
</template>

<script>
import Login from "@/page/user/Login";
import bus from '../../utils/bus';
export default {
  components: {
    Login,
  },
  data() {
    return {
      isStarting: false,
    };
  },
  methods: {
    changeScreen() {
      this.isStarting = true;
      //console.log("after", this.isStarting);
    },
    teamPage() {
      this.$router.push("/@hungrybird");
    },
    localSave() {
      localStorage.setItem("wsboard.channelId", "earlyBird10TeamTestChannel1");
      localStorage.setItem("wsboard.channelName", "Tutorial Channel");
    },
    checkLogin() {
      if (this.$store.getters.accessToken !== "") {
        this.$router.push("/main");
      }
    },
  },
  created() {
    this.isStarting = false;
    setTimeout(() => (this.isStarting = true), 1000);
  },
  mounted() {
    bus.$emit('end:Loading');
  }
  
};
</script>

<style scoped>
* {
  box-sizing: border-box;
}

.enter-page {
  /* min-height: 1200px; */
  background-color: #F5F5EC !important;
}
.container {
  margin-left: 13.55vw;
  margin-right: 13.55vw;
  max-width: 1400px;
}
.cards .card-inner {
  margin: 1.22vw;
  padding: 1.22vw;
  background-color: #F5F5EC;
  box-shadow: 0.25rem 0.5rem 1rem rgba(0, 0, 0, 0.35) !important;
  border-radius: 8px;
  width: 20.3vw;
}
.cards .card {
  background-color: transparent;
  border: 0px;
}
.cards .card-text {
  padding-top: 10px;
  /* min-height: 100px; */
  font-size: 0.84vw;
}
.cards .img {
  /* margin-top: 20px; */
  width: auto;
}

.button-user button {
  margin-left: 10px;
  margin-right: 10px;
  background-color: #FC776B !important;
  color: white;
}

.Loading {
  width: 100%;
  height: 100vh;
  left: 0px;
  top: 0px;
  background: #0d875C;
  text-align: center;
  z-index: 1;
}

.mainEnter {
  width: 100%;
  height: 100vh;
  left: 0px;
  top: 0px;
  background: #f5f5ec;
  text-align: center;
  z-index: 0;
  background-image: url("../../assets/img/EnterBack.jpg");
  background-repeat: no-repeat;
  background-size: cover;
  padding-top: 15vh;
}

.comment {
  width: 100%;
  height: 100%;
  font-size: 24px;
  line-height: 130%;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

button {
  color: white !important;
  background-color: #FC776B !important;
  
  height: 2.3vw;
  font-size: 0.84vw;
}

.loader-leave,
.loader-leave-to {
  opacity: 0;
}

.loader-leave-active {
  transition: opacity 1s;
}

.starter-enter,
.starter-enter-to {
  opacity: 0;
}

.starter-enter-active {
  transition: opacity 1s;
}

.footerText {
  text-decoration: none;
  color: #000000;
  font-size: 0.85vw;
}


h4 {
  font-size: 1.2vw
}

.card-body{
  padding: 2vh;
}


</style>