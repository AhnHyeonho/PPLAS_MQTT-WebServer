<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="log.Log"%>
<%@ page import="log.LogDAO"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="account.Account"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="viewport" content="initial-scale=1.0">
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no">
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
<script
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCrsGow62wDXE8Yw7148CXZSVuaO2c9HsU"></script>




<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/custom.css">
<title>MQTT 환자관리 웹 사이트</title>
</head>
<body>
	<%
		String accountID = null;
		if (session.getAttribute("accountID") != null) {
			accountID = (String) session.getAttribute("accountID");
		}
		int logID = 0;

		if (request.getParameter("logID") != null) {
			logID = Integer.parseInt(request.getParameter("logID"));
		}

		if (logID == 0) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('유효하지 않은 글입니다.')");
			script.println("location.href = 'log.jsp'");
			script.println("</script>");
		}
		Log log = new LogDAO().getLog(logID);
	%>
	<nav class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
				aria-expanded="false">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="main.jsp">MQTT 환자관리 웹 사이트</a>
		</div>
		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li><a href="main.jsp">메인</a></li>
				<li class="active"><a href="log.jsp">로그</a></li>
				<li><a href="monitoring.jsp">실시간 모니터링</a>
			</ul>
			<%
				if (accountID == null) {
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false">접속하기<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="login.jsp">관리자 로그인</a></li>
					</ul></li>
			</ul>
			<%
				} else {
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false">회원관리<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="logoutAction.jsp">로그아웃</a></li>
					</ul></li>
			</ul>
			<%
				}
			%>
		</div>
	</nav>

	<div class="container">
		<div class="row">
			<table class="table table-striped"
				style="text-align: center; border: 1px solid #dddddd">
				<thead>
					<tr>
						<th colspan="3"
							style="background-color: #eeeeee; text-align: center;">응급환자
							정보</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td style="width: 20%;">환자 아이디</td>
						<td colspan="2"><%=log.getAccountInfo().getAccountID()%>
					</tr>
					<tr>
						<td>환자 이름</td>
						<td colspan="2"><%=log.getAccountInfo().getAccountName()%>
					</tr>
					<tr>
						<td>작성일자</td>
						<td colspan="2"><%=log.getDate().substring(0, 11) + log.getDate().substring(11, 13) + "시"
					+ log.getDate().substring(14, 16) + "분"%>
					</tr>
					<tr>
						<td>위치</td>
						<td colspan="2" style="min-height: 200px; text-align: left;">
							<div id="map" style="height:350px; width:700px;"></div>
						    <script>
						      
						      /* var location = new google.maps.LatLng(log.getLatitude(), log.getLongtitude()); */
						      /* var location = new google.maps.LatLng(37.582520, 127.010731); */
						      function initMap() {
						    	var map;
						    	var location = {lat : <%= log.getLatitude() %>  , lng: <%= log.getLongtitude() %>};
						    	map = new google.maps.Map(document.getElementById('map'), {
						          center: location,
						          zoom: 18
						        });
						        map.controls[google.maps.ControlPosition.TOP_CENTER].push(
						        	      document.getElementById('info'));
						
						    	marker = new google.maps.Marker({
						    	    map: map,
						    	    draggable: true,
						    	    position: location,
						    		title: '환자 발생 위치'
						    	});
						    	
						    	var contentString = "환자 발생 위치<br> 좌표 : <%= log.getLatitude()%> , <%= log.getLongtitude() %> <br> 맥박 : <%= log.getPulse()%><br> 체온 : <%= log.getTemp()%>";	// mouseover 시 표시되는 문구
						    	var infowindow = new google.maps.InfoWindow({	// infoWindow 생성
						            content: contentString,
						            maxWidth: 200
						          }); 
						    	marker.addListener('mouseover', function() {	// marker에 리스너 등록
						    		infowindow.open(map, marker);
						          });
						    	marker.addListener('mouseout', function() {		// marker에 리스너 등록
						    		infowindow.close();
						          });
						      }
						    </script>
						    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCrsGow62wDXE8Yw7148CXZSVuaO2c9HsU&callback=initMap" 
						    async defer></script>
						</td>

					</tr>
				</tbody>
			</table>
		</div>
		<a href="log.jsp" class="btn btn-primary pull-right">목록으로</a>
	</div>
	<script
		src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.3.1.min.js"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>