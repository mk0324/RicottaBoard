<template>
    <div class="user" id="passwordReset" style="background: #f5f5ec;">
        <div class="wrapC table">
            <div class="middle" style="text-align:center;">
                <h2>비밀번호 재설정</h2>
                <div class="form-wrap" style="width:90%; padding-top:20px; margin:auto" v-if="this.$store.getters.status == ''">
                    <div class="input-wrap password-wrap">
                        <p style="text-align: left; margin-bottom:4px">비밀번호 재설정</p>
                        <input
                            v-model="password"
                            id="password"
                            type="password"
                            style="border:solid 1px #dadada;height:50px; background-color:white;"
                            placeholder="새 비밀번호를 입력해주세요."
                        />
                    </div>
                    <div class="input-wrap password-wrap">
                        <p style="text-align: left; margin-bottom:4px">비밀번호 확인</p>
                        <input
                            v-model="passwordConfirm"
                            id="passwordConfirm"
                            style="border:solid 1px #dadada;height:50px; background-color:white;"
                            type="password"
                            placeholder="비밀번호를 다시 입력해주세요."
                        />
                    </div>
                    <button
                        @click="resetMyPassword"
                        style="margin-top:40px; background-color:#0d875C; border:solid 0px;"
                        class="btn"
                    >
                        <span>비밀번호 재설정</span>
                    </button>
                </div>
                <div class="form-wrap" v-if="this.$store.getters.status != ''">
                    {{this.$store.getters.status}}
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
    import '../../assets/css/user.scss'
    import constants from '../../lib/constants'
import bus from '../../utils/bus'

    export default {
        data: () =>{
            return {
                password:'',
                passwordConfirm:'',
            }
        },
        methods:   {
            resetMyPassword() {
                const token = this.$route.query.token;

                if(this.password != this.passwordConfirm){
                    return;
                }

                const newData = {
                    "password": this.password,
                    "passwordConfirm": this.passwordConfirm,
                    "token": token,
                }

                this.$store.dispatch(constants.METHODS.RESETMYPASSWORD, newData);
            },
            teamPage() {
                this.$router.push('/@hungrybird')
            },
        },
        mounted() {
            bus.$emit('end:Loading');
        },
    }
</script>

<style>

</style>