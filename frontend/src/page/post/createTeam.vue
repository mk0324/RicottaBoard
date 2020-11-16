<template>
    <div class="box3 d-flex black--text" style="width: 100vw; height: 100vh; background:#f5f5ec;">
        <div
            class="white--text"
            style="width:40%; padding:10vw; padding-top: 35vh; background:#0d875C; "
        >
            <h2>TEAM<br />Hungry Bird</h2>
        </div>
        <div class="mx-auto" style="width:60%; padding:5vw; padding-top:5vh;">
            열정적인 리코타보드!
            <p />
            <h4 style=" font-size:30px;">리코타보드는 열정적인 개발자들과 함께 하고 있습니다</h4>
            <v-row>
            <v-col v-for="(slide, i) in slides" :key="i" cols="6" md="4">
                <v-hover v-slot:default="{ hover }">
                <v-card style="width:210px; height:250px;">
                    <v-img
                    :src="slide.src"
                    class="white--text align-end"
                    style="width:210px; height:250px; object-fit: cover;"
                    gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
                    >
                    <v-expand-transition>
                        <div
                        v-if="hover"
                        flex
                        class="transition-fast-in-fast-out text-center black white--text darken-3 v-card--reveal"
                        style="font-size:15px; padding:10px ;height: 100%; align-items: center; bottom: 0; justify-content: center; opacity:.5; position: absolute; width: 100%;"
                        >
                        <h2>{{slide.name}}</h2>
                        <br />
                        <br />
                        <p>{{slide.text}}</p>
                        </div>
                    </v-expand-transition>
                    </v-img>
                </v-card>
                </v-hover>
            </v-col>
            </v-row>
        </div>
    </div>
</template>

<script>
import img1 from "../../assets/img/ProDeveloper1.gif";
import img2 from "../../assets/img/ProDeveloper2.jpg";
import img3 from "../../assets/img/ProDeveloper3.gif";
import img4 from "../../assets/img/ProDeveloper4.jpg";
import img5 from "../../assets/img/ProDeveloper5.jpg";
import bus from '../../utils/bus';

export default {
    data() {
        return {
            slides: [
      {
        name: "Kim Kanghyeon",
        src: img1,
        text: "아이고~~뒷목이야",
      },
      {
        name: "Lee Jeonghoon",
        src: img2,
        text:
          "엑조디아 팀장",
      },
      {
        name: "Jeong Yongwoo",
        src: img3,
        text: "뱅뱅뱅 빵야빵야빵야",
      },
      {
        name: "Jeong Jingueon",
        src: img4,
        text:
          "이제 잘 시간인가?",
      },
      {
        name: "Choi Munkyung",
        src: img5,
        text: "빼꼼",
      },
    ],
        }
    },
    mounted() {
        bus.$emit('end:Loading');
    }
}
</script>

<style scoped>

</style>