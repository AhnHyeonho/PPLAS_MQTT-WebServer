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
      var myLat;
      var myLng;
      var myLatlng = new LatLng(myLat, myLng);
      
      function initMap() {	  	
    	map = new google.maps.Map(document.getElementById('map'), {
          center: myLatlng,
          zoom: 18
        });
        map.controls[google.maps.ControlPosition.TOP_CENTER].push(
        	      document.getElementById('info'));

    	marker = new google.maps.Marker({
    	    map: map,
    	    draggable: true,
    	    position: myLatlng,
    		title: 'ȯ�� �߻� ��ġ'
    	});
    	
    	var contentString = "ȯ�� �߻� ��ġ<br/> 37.581810,127.009857";	// mouseover �� ǥ�õǴ� ����
    	var infowindow = new google.maps.InfoWindow({	// info ����
            content: contentString
          }); 
    	marker.addListener('mouseover', function() {	// marker�� ������ ���
            infowindow.open(map, marker);
          });
    	marker.addListener('mouseout', function() {		// marker�� ������ ���
            infowindow.close();
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