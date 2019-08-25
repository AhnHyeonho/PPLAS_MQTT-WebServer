<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
    <title>Simple Map</title>
    <meta name="viewport" content="initial-scale=1.0">
    <meta charset="utf-8">
    <style>
      /* Always set the map height explicitly to define the size of the div
       * element that contains the map. */
      #map {
        height: 100%;
      }
      /* Optional: Makes the sample page fill the window. */
      html, body {
        height: 100%;
        margin: 0;
        padding: 0;
      }
    </style>
</head>
<body>
    <div id="map" style="height:350px; width:700px;"></div>
    <script>
      var map;
      function initMap() {
          var mapLocation = new google.maps.LatLng('37.582521', '127.010643'); // 지도에서 가운데로 위치할 위도와 경도 

          var markLocation = new google.maps.LatLng('37.582521', '127.010643'); // 마커가 위치할 위도와 경도
          
        map = new google.maps.Map(document.getElementById('map'), {
          center: mapLocation,
          zoom: 17
        });
          
        map.controls[google.maps.ControlPosition.TOP_CENTER].push(
                 document.getElementById('info'));

       marker1 = new google.maps.Marker({
    	   
           map: map,
           draggable: true,
           position: markLocation,
       });
       
       var infowindow = new google.maps.InfoWindow({ content: content});

       

       google.maps.event.addListener(marker, "click", function() {

           infowindow.open(map,marker);

       });
       
       poly = new google.maps.Polyline({
           strokeColor: '#FF0000',
           strokeOpacity: 1.0,
           strokeWeight: 3,
           map: map,
         });
      }
    </script>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCrsGow62wDXE8Yw7148CXZSVuaO2c9HsU&callback=initMap"
    async defer></script>
<!--     <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCrsGow62wDXE8Yw7148CXZSVuaO2c9HsU&libraries=geometry">
   </script>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCrsGow62wDXE8Yw7148CXZSVuaO2c9HsU&libraries=geometry,places">
   </script> -->
</body>
</html>