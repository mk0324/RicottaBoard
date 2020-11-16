<template>
  <div class="user" id="passwordFind" style="background-color:#f5f5ec">
    <div class="wrapC table">
      <div class="myBox">
        <div class="middle">
          <div class="form-wrap" v-if="this.$store.getters.status == ''">
            <h3>비밀번호 재설정</h3>
            <div class="input-wrap">
              <label for></label>
              <input
                type="text"
                id="email"
                class="inputBox"
                v-model="userEmail"
                placeholder="이메일을 입력하세요."
              />
            </div>

            <div class="input-wrap">
              <button class="btn" v-on:click="findMyPassword" style="background-color:#0d875C;">비밀번호 재설정</button>
            </div>
          </div>
          <div class="form-wrap" v-else>{{this.$store.getters.status}}</div>
        </div>
      </div>
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
import bus from '../../utils/bus';

export default {
  components: {},
  methods: {
    findMyPassword() {
        if(this.userEmail == ""){
            alert("이메일을 입력해주세요.");
            return;
        }

      this.$store.dispatch(
        constants.METHODS.RESETMYPASSWORDREQ,
        this.userEmail
      );
    },
    teamPage() {
      this.$router.push('/@hungrybird')
    },
  },
  data: () => {
    return {
      userEmail: "",
      constants,
    };
  },
  created() {
    this.$store.commit("toggleModal");
  },
  mounted() {
    bus.$emit('end:Loading');
  }
};
</script>

<style lang="scss" scoped>
.input-wrap {
  input {
    border: 1px solid #dadada;
    padding: 7px 15px;
    box-sizing: border-box;
    height: 50px;
    background-color: white;
  }
}

h3{
    text-align: center;
    margin-top: 15%;
    margin-bottom: 40%;
}
</style>
