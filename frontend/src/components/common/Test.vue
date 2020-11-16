<template>
  <div class="page-map">
    <button @click="kaka">kakao</button>
    <button @click="mulcam">멀캠</button><br>
    <input v-model="search" type="text" />
    <button @click="searchEvent">search</button>
    <div id="map"></div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      search: "카카오",
      map: Object,
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
      var container = document.getElementById("map");

      var options = {
        center: new kakao.maps.LatLng(0, 0),
        level: 3,
      };

      var map = new kakao.maps.Map(container, options);
      this.map = map
      this.map.setMapTypeId(kakao.maps.MapTypeId.HYBRID);

      
      var moveLatLon = new kakao.maps.LatLng(33.452701, 126.570667);
      this.map.panTo(moveLatLon);
    },
    kaka() {
      var moveLatLon = new kakao.maps.LatLng(33.452701, 126.570667);
      this.map.panTo(moveLatLon);
    },
    mulcam() {
      var moveLatLon = new kakao.maps.LatLng(37.501715, 127.039703);
      this.map.panTo(moveLatLon);
    },
    searchEvent() {
      // 마커를 클릭하면 장소명을 표출할 인포윈도우 입니다
      var infowindow = new kakao.maps.InfoWindow({zIndex:1});
      // 장소 검색 객체를 생성합니다
      var ps = new kakao.maps.services.Places(); 
      var map = this.map;
      // 키워드로 장소를 검색합니다
      ps.keywordSearch(this.search, placesSearchCB);

      // 키워드 검색 완료 시 호출되는 콜백함수 입니다
      function placesSearchCB(data, status, ) {
        if (status === kakao.maps.services.Status.OK) {
          // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
          // LatLngBounds 객체에 좌표를 추가합니다
          var bounds = new kakao.maps.LatLngBounds();

          for (var i = 0; i < data.length; i++) {
            displayMarker(data[i]);
            bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
          }

          // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
          map.setBounds(bounds);
        }
      }

      // 지도에 마커를 표시하는 함수입니다
      function displayMarker(place) {
        // 마커를 생성하고 지도에 표시합니다
        var marker = new kakao.maps.Marker({
          map: map,
          position: new kakao.maps.LatLng(place.y, place.x),
        });
        

        // 마커에 클릭이벤트를 등록합니다
        kakao.maps.event.addListener(marker, "click", function () {
          // 마커를 클릭하면 장소명이 인포윈도우에 표출됩니다

          infowindow.setContent('<div style="padding:5px;font-size:12px;">' + place.place_name + '</div>');
          infowindow.open(map, marker);
     
        
        });
      }
    },
  },
};
</script>

<style>
#map {
  width: 600px;
  height: 500px;
  left: 200px;
  margin-top: 20px;
}
.page-map button {
  border: solid 1px;
  border-radius: 5px;
  margin-left: 5px;
}
.page-map input {
  border: solid 1px;
  border-radius: 5px;
  margin-left: 5px;
}
</style>