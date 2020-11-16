<template>
    <div id="sidebar">
        <div class="tag-list-wrap">
                <h4>Test Link</h4>
                <ul class="tag-list">
                    <li>
                        <router-link v-bind:to="{name:constants.URL_TYPE.POST.MAIN}" class="btn--text">
                          <img src="https://image.flaticon.com/icons/svg/846/846449.svg" alt="">
                        </router-link> 
                    </li>
                    <li>
                        <router-link v-bind:to="{name:constants.URL_TYPE.USER.JOIN}" class="btn--text">
                          <img src="https://image.flaticon.com/icons/svg/992/992675.svg" alt="">
                        </router-link> 
                    </li>
                    <li>
                        <router-link v-bind:to="{name:constants.URL_TYPE.USER.MYPAGE}" class="btn--text">
                          <img src="https://image.flaticon.com/icons/svg/1098/1098534.svg" alt="">
                        </router-link> 
                    </li>
                    <li>
                        <router-link v-bind:to="{name:constants.URL_TYPE.POST.TEST_BOARD}" class="btn--text">
                          <img src="https://image.flaticon.com/icons/svg/645/645822.svg" alt="">
                        </router-link> 
                    </li>

                    <li>
                        <router-link v-bind:to="{name:constants.URL_TYPE.POST.TEST_BOARD}" class="btn--text">
                         <img src="https://image.flaticon.com/icons/svg/615/615075.svg" alt="">
                        </router-link> 
                    </li>
                </ul>   
            </div>
    </div>
</template>

<script>
import constants from '../../lib/constants'
export default {
    
        name: 'Sidebar',
        props: ['isSidebar'],
        data: function() {
           return {
               constants,
           }
        },
}
</script>

<style scoped>
 .tag-list-wrap{
     background-color: white;
     width: 35px;
     /* top: auto; */
     border-right: 3px solid rgb(223, 223, 223);
     text-align: center;
     
     height: 290px;
     top: 85px;
     left: 5px;
 }
 
 .tag-list{
     padding-left: 0;
     text-align: center;
 }

 img{
     margin-left: 3px;
     height: 25px;
     width: 25px;
 }
</style>