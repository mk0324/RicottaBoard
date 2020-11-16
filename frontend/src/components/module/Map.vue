<template>
  <div class="page-kakaoMap MoveableBox">
    <v-row>
      <v-col cols="9">
        <v-text-field 
        v-model="search"
        @keyup.enter="searchEvent" 
        label="Place"></v-text-field>
      </v-col>
      <v-col cols="3">
        <v-btn @click="searchEvent">search</v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <div id="kakaoMap"></div>
      </v-col>
    </v-row>
    <v-btn @click="saveEvent">저장</v-btn>
    {{ map }}
  </div>
</template>

<script>
export default {
    computed: {
    map() {
      return this.$store.state.map;
    },
  },
  data() {
    return {
      search: "멀티캠퍼스 역삼",
      kakaoMap: Object,
      markers: [],
      coord: {
        place_name: "",
        x: null,
        y: null,
      }
    };
  },
  mounted() {
    if (window.kakao && window.kakao.maps) {
      this.initMap();
    } else {
      const script = document.createElement("script");
      /* global kakao */
      script.onload = () => kakao.maps.load(this.initMap);
      script.src =
        "http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=506c56730f934be446be361e5bbd02f4&libraries=services";
      document.head.appendChild(script);
    }
  },
  methods: {
    initMap() {
      var container = document.getElementById("kakaoMap");

      var options = {
        center: new kakao.maps.LatLng(0, 0),
        level: 3,
      };

      var kakaoMap = new kakao.maps.Map(container, options);
      this.kakaoMap = kakaoMap;
      this.kakaoMap.setMapTypeId(kakao.maps.MapTypeId.HYBRID);
      var initLatLng = new kakao.maps.LatLng(this.map.coord.y, this.map.coord.x);
      this.kakaoMap.panTo(initLatLng);
      this.kakaoMap.setLevel(2);
      //console.log(this.map.coord);
      var initMarker = new kakao.maps.Marker({
        map: this.kakaoMap,
        position: initLatLng,
        title: this.map.coord.place_name,
      });
    },
    searchEvent() {
      // 이미 찍힌 마커들을 보이지 않게 하고 마커배열을 초기화합니다
      var markers = this.markers;
      for (var i=0; i < markers.length; i++) {
        markers[i].setMap(null);
      };
      markers = [];

      // 마커를 클릭하면 장소명을 표출할 인포윈도우 입니다
      var infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });
      // 장소 검색 객체를 생성합니다
      var ps = new kakao.maps.services.Places();
      var kakaoMap = this.kakaoMap;
      var coord = this.coord;
      // 키워드로 장소를 검색합니다
      ps.keywordSearch(this.search, placesSearchCB);

      // 키워드 검색 완료 시 호출되는 콜백함수 입니다
      function placesSearchCB(data, status) {
        if (status === kakao.maps.services.Status.OK) {
          // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
          // LatLngBounds 객체에 좌표를 추가합니다
          var bounds = new kakao.maps.LatLngBounds();

          for (var i = 0; i < data.length; i++) {
            displayMarker(data[i]);
            bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
          }
          for (var i = 0; i< markers.length; i++) {
            markers[i].setMap(kakaoMap);
          }
          // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
          kakaoMap.setBounds(bounds);
        }
      }

      // 지도에 마커를 표시하는 함수입니다
      function displayMarker(place) {
        // 마커를 생성하고 지도에 표시합니다
        var marker = new kakao.maps.Marker({
          position: new kakao.maps.LatLng(place.y, place.x),
        });

        // 마커에 클릭이벤트를 등록합니다
        kakao.maps.event.addListener(marker, "click", function () {
          // 마커를 클릭하면 장소명이 인포윈도우에 표출됩니다
          coord.place_name = place.place_name,
          coord.x = place.x;
          coord.y = place.y;
          infowindow.setContent(
            '<div style="padding:5px;font-size:12px;">' +
              place.place_name +
              "</div>"
          );
          infowindow.open(kakaoMap, marker);
        });
        
        markers.push(marker);
      }
    },
    saveEvent() {
      this.$store.state.map.coord = this.coord
      // 서버에 coord 값 보내기
      //console.log('저장@');
    }
  },
};
</script>

<style>
#kakaoMap {
  height: 500px;
  width: 550px;
  margin-top: 20px;
}
.page-kakaoMap {
  width: 600px;
  height: 550px;
}
/* .page-kakaoMap button {
  border: solid 1px;
  border-radius: 5px;
  margin-left: 5px;
} */
/* .page-kakaoMap input {
  border: solid 1px;
  border-radius: 5px;
  margin-left: 5px;
} */
</style>