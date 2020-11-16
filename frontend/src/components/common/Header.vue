<template>
  <div id="header">
    <button @click="goWhere()">
      <img
        style="position:absolute ;top:0px;  margin: 10px 20px; width:auto;height:50px;"
        src="../../assets/img/Logo.png"
      />
    </button>

    <div class="right">
      <template v-if="this.$store.getters.accessToken != ''">
        <div class="headBox mt-1">
          <router-link style="margin-left: 20px; padding-top:5px"
            :to="{name:constants.URL_TYPE.USER.MYPAGE}"
            class="btn--text"
          >
          {{(this.$store.getters.userData.nickname)}}
          </router-link>
          님 환영합니다!
          <v-btn dark class="allbtn" outlined color="white" @click="logout">Logout</v-btn>
        </div>
      </template>
      <template v-if="this.$store.getters.accessToken == ''">
        <div class="headBox mt-1">
          <router-link style="margin-left: 20px; padding-top:5px"
            :to="{name:constants.URL_TYPE.USER.JOIN}"
          >
            <v-btn dark class="allbtn px-5 py-2" outlined color="white">Sign up</v-btn>
          </router-link>

          <v-dialog  width="350px ">
            <template v-slot:activator="{ on, attrs }">                                    
              <v-btn
                dark 
                class="allbtn px-5 py-2"
                v-bind="attrs"
                v-on="on"
                outlined
                color="white"
                v-if="$store.getters.accessToken == ''"
              >
              LOGIN
              </v-btn>
            </template>
            <v-card style="width:350px; height:280px">
              <v-card-title>LOGIN</v-card-title>
              <v-card-text style=" background-color:white; height:90px; padding-bottom:0"><Login style="height:120px;padding-bottom:0"/></v-card-text>
            </v-card>
          </v-dialog>          
        </div>
      </template>

      <!-- <button @click="check">
                        스토어 체크
      </button>-->
    </div>
  </div>
</template>   

<script>
import constants from "../../lib/constants";
import cookies from "vue-cookie";
import logo from "../../assets/img/Logo.png";
import Login from '@/page/user/Login'

export default {
  name: "Header",
  components: {
    Login,
  },
  props: ["isHeader"],
  watch: {
  },
  created() {
    const arr = document.cookie.split(";");
  },
  computed: {},
  methods: {
    goWhere() {
      if (this.$store.getters.accessToken == '') {
        this.$router.push({ name: constants.URL_TYPE.POST.ENTER })
      } else {
        this.$router.push({ name: constants.URL_TYPE.POST.MAIN })
      }
    },
    async logout() {
      this.$store.commit(constants.METHODS.LOGOUT_USER);
      try {
        await this.$router.push("/");
      } catch (error) {
        //console.log("route same path!");
      }
    },
    check() {
      //console.log(this.$store.state);
    },
  },
  data: function () {
    return {
      constants,
      userinfo: "",
      email: "",
      password: "",
    };
  },
};
</script>
<style>
#header {
  background:#0d875C;
  border:solid 0px;
  height: 70px;
  padding: 12px 40px 12px 0px;
}

#header a{
  color: white;
}
.toolbar {
  background:#0d875C;
  width: 100vw;
}

.allbtn {
  text-align: center;
  color: white;
}

.headBox{
  color:rgba(255, 255, 255, 0.75);
  line-height: 25px;
}
</style>




