<template>
  <div id="loading" v-if="loading">
      <img class="loadingIcon" src="../../assets/img/loading_kh.gif" v-if="num==0" alt="">
      <img class="loadingIcon" src="../../assets/img/loading_yo.gif" v-if="num==1" alt="">
      <img class="loadingIcon" src="../../assets/img/loading_jg.gif" v-if="num==2" alt="">
      <img class="loadingIcon" src="../../assets/img/loading_jh.gif" v-if="num==3" alt="">
      
  </div>
</template>

<script>
export default {
    props: {
        loading: {
            type: Boolean,
            required: true,
        },
        num: {
            required: true,
        }
    }

}
</script>

<style scoped>
    #loading {
        height: 100vh;
        width: 100vw;
        background-color: rgba(0, 0, 0, 0.568);    
        position: absolute;
        z-index: 10;
    }
    .loadingIcon {
        position: absolute;
        top: 45vh;
        left: 45vw;
        width: 10vw;
    }

</style>