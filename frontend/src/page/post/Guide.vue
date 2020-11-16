<template>
    
    <div class="guideBody" ref="guide" style=" padding:0px; margin:0px; background:#f5f5ec;" @wheel="check">
        <div class="intro row">
            <div class="introText" style="text-align:left ">
                <h3>Welcome to <br><strong> Ricotta Board! </strong></h3>
                <h4>리코타보드는 다양한 기술을 탐구하고 적용하여 사용자들이 최상의 서비스를 경험할 수 있도록 노력하고 있습니다</h4>
                <h4>아래로 스크롤하여 리코타보드에서 사용할 수 있는 모듈들에 대해 알아보세요!</h4>
            </div>
            <div class="introImg">
                <img src="../../assets/img/Used.png" />
            </div>
        </div>
        <div class="guide row">
            <div class="guideCarousel">
                <v-carousel  style="width:90%; height:100% " hide-delimiters @change="changePage">
                    <v-carousel-item style="height: 55vh;" v-for="(item, idx) in items" :key="idx" :src="item.src" lg3></v-carousel-item>
                </v-carousel>
            </div>
            <div class="guideText">
                <h4 style=" font-size: 1.6vw;">{{ explain }}</h4>
            </div>

            <footer
            style="width:100vw; text-align:center; bottom:10px;">
            <p class="footerText" @click="teamPage()">ⓒHungrybird</p>
            
            </footer>
        </div>
        
    </div>
</template>

<script>
import guide1 from "../../assets/img/guide1.gif";
import guide2 from "../../assets/img/guide2.gif";
import guide3 from "../../assets/img/guide3.gif";
import guide4 from "../../assets/img/guide4.gif";
import guide5 from "../../assets/img/guide5.gif";
import guide6 from "../../assets/img/guide6.gif";
import guide7 from "../../assets/img/guide7.gif";
import bus from '../../utils/bus';

export default {
    data() {
        return {            
            items: [
                {src: guide1,},
                {src: guide2,},
                {src: guide3,},
                {src: guide4,},
                {src: guide5,},
                {src: guide6,},
                {src: guide7,},
            ],
            page: 0,
            explain: "post-it을 이용해서 회의용 메모를 이용해 보세요!"
        }
    },
    methods: {
        changePage(page) {
            if (page == 0) {
                this.explain = "post-it을 이용해서 회의용 메모를 작성해보세요."
            } else if (page == 1) {
                this.explain = "poll으로 팀원들과 투표를 통해 결정을 내려보세요!"
            } else if (page == 2) {
                this.explain = "kanban은 그날 할 작업을 관리하여 작업을 효율적으로 진행하도록 도와줍니다."
            } else if (page == 3) {
                this.explain = "scheduler를 통해 작업 스케쥴을 편리하게 관리할 수 있습니다!"
            } else if (page == 4){
                this.explain = "editor를 사용해 마크다운으로 문서를 작성하고 저장하세요."
            } else if (page == 5){
                this.explain = "face-chat은 화상으로 효율적인 회의를 진행하도록 도와줍니다."
            } else if (page == 6){
                this.explain = "chat으로 실시간으로 의견을 주고 받을 수 있습니다."
            }
            //console.log('change!')
            //console.log('test', dataset)
        },
        teamPage() {
            this.$router.push('/@hungrybird')
        },
        check(e) {

            // if(e.deltaY > 0) {
            //     //console.log("down!");

            // } else if(e.deltaY < 0) {
            //     //console.log("up!");
            // }

        }
    },
    mounted() {
        bus.$emit('end:Loading');
    }
}
</script>

<style scoped>
.intro {
    width: 100vw;
    height: 100vh;
    background:#f5f5ec;
    /* background: blue; */
}

.introText{
    margin: auto;
    text-align: left;
    width: 31vw;
    margin-left: 19vw;
    margin-right: 2vw;
}

.introImg{
    width:35vw;
    margin:auto auto auto 0;
}

.introImg img {
    width:35vw;
}

.guide {
    width: 100vw;
    height:100vh;
    background: #f5f5ec;
    /* background: red; */
}

.guideCarousel {
    width: 50vw;
    padding: 3rem;
    margin: 20vh 0 auto auto
}

.guideText {
    width: 28vw;
    text-align: center;
    margin: auto auto 27vh 0;
    margin-left: 2vw;
    padding: 2rem;
    padding-left: 0;
}

.footerText {
    height:5vh;
    text-decoration: none;
    color:#000000;
    background:#f5f5ec;
    margin-bottom: 0;
    font-size: 0.85vw;
}


h3 {
    font-size: 2vw;
    margin-bottom: 3vh;
    line-height: 130%;
    width: 24vw;
}

h4 {
    font-size: 1.2vw;
    line-height: 150%;
    width: 24vw;
}

.guideBody{
    /* scroll-snap-type: mandatory; */
    height: 200vh;
    width: 110vw;
    overflow: scroll;
    /* scroll-snap-points-y: repeat(300px); */
    scroll-snap-type: y mandatory;

}

.row {
    scroll-snap-align: center;
}

footer {
    margin-top: auto
}

</style>