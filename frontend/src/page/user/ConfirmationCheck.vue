<template>
  <div class="user" id="ConfirmDone" style="background-color:#f5f5ec">
        <div class="wrapC table">
            <div class="middle" style="text-align:center;">
                <h1>회원 인증이 완료되었습니다.</h1>

                <h3>환영합니다!</h3>
                <router-link v-bind:to="{name:constants.URL_TYPE.POST.ENTER}" class="login-btn">
                돌아가기 
                </router-link>  
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
        components: {
        },
        data: () => {
            return {
                constants
            }
        },
        methods: {
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