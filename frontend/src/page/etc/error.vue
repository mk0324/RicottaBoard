<template>
    <div class="etc justify-content-center text-center" id="error" style="background: #f5f5ec;">
        <div style="padding-top:100px">
            <h1 class="my-5">ERROR!</h1>
            <p class="my-5">요청 오류 페이지!</p>
            <p class="my-5">{{this.$store.state.errorcode}}</p>
        </div>
    </div>
</template>

<script>
    import '../../assets/css/user.scss';
    import constants from '../../lib/constants';
    import bus from '../../utils/bus';
    
    export default {
        
        created(){
            
        },
        data: () =>{
            return{
                
            }
        },
        mounted() {
            bus.$emit('end:Loading');
        }
    }
</script>
