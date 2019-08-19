<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="mqtt.Publisher" %>
<%@ page import="mqtt.Subscriber" %>

<%@ page import="hospital.HospitalDAO"%>
<%@ page import="hospital.Hospital"%>
<%@ page import="distance.LocationDistance"%>

<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0">
<meta charset="utf-8">
<title>Insert title here</title>

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
<%
 	/* Publisher pb = new Publisher();
	pb.setBrokerUrl("tcp://113.198.84.52:1883");
	pb.setClientId("JavaSample");
	pb.setTopic("user/patient/shseo");
	pb.setContent("150%39.8%37.56653:126.9779691900000");
	pb.myPublish();  */ 

	/*  	Subscriber sb = new Subscriber();
	sb.setBrokerUrl("tcp://localhost:1883");
	sb.setClientId("JavaSample");
	sb.setTopic("user/patient/shseo");
	sb.subscribe(); 

	HospitalDAO hosDAO = new HospitalDAO();
	ArrayList<Hospital> test = hosDAO.getList();
	int i = 0;
	while(i< test.size()){
		System.out.println("hosName : " + test.get(i).getHospitalName());
		System.out.println("hosPhone : " + test.get(i).getHospitalPhone());
		System.out.println("hosLatitude : " + test.get(i).getHospitalLocationInfo().getLatitude());
		System.out.println("hosLongtitude : " + test.get(i).getHospitalLocationInfo().getLongitude());
		i++;
	}; */
	
	
%>

<div id="map"></div>
    <script>
      var map;
      function initMap() {
        map = new google.maps.Map(document.getElementById('map'), {
          center: {lat: -34.397, lng: 150.644},
          zoom: 8
        });
      }
    </script>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyB39VIra86A8oJg4ZavCCYy4cPQAFA5OHM&callback=initMap"
    async defer></script>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyB39VIra86A8oJg4ZavCCYy4cPQAFA5OHM&libraries=geometry,places">
	</script>
    
</body>
</html>