<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="account.Account"%>
<%@ page import="account.AccountDAO"%>
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
<script type="text/javascript"
	src="http://code.jquery.com/jquery-3.2.1.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.js"></script>

<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/custom.css">
<%
	String accountID = null;
	if (session.getAttribute("accountID") != null) {
		accountID = (String) session.getAttribute("accountID");
	}
	String acID = null;
	if (request.getParameter("acID") != null) {
		acID = request.getParameter("acID").toString();
	}

	Account account = new AccountDAO().getInfo(acID);
%>

<script type="text/javascript">


</script>
<title>MQTT 환자관리 웹 사이트</title>
</head>
<body>
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
				<li><a href="log.jsp">로그</a></li>
				<li class="acitve"><a href="monitoring.jsp">실시간 모니터링</a>
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
						<td colspan="2"><%=account.getAccountID()%>
					</tr>
					<tr>
						<td>환자 이름</td>
						<td colspan="2"><%=account.getAccountName()%>
					</tr>
					<tr>
						<td>환자 핸드폰</td>
						<td colspan="2"><%=account.getAccountPhone()%>
					</tr>
				</tbody>
			</table>
			<div style="width: 100%;">
				<canvas id="mycanvas"></canvas>
			</div>
		</div>
	</div>

	<script type="text/javascript">	
function deleteline(){
	if(myChart.data.datasets[0].data.length > 10 && myChart.data.datasets[1].data.length > 10){
		
		myChart.data.datasets.forEach(function(a) {
			a.data.shift();
		});
		myChart.data.labels.shift();
	}
}	
</script>

	<script type="text/javascript">
  						
	
function searchFunction() {
  	var acID = "<%=acID%>";
			$.ajax({
				type : "POST",
				dataType : "json",
				url : "/PPLAS_MQTT-WebServer/checkSearchServlet",
				data : {
					"acID" : acID
				},
				success : function(data) {

					now = new Date();

					myChart.data.labels.push(now.getHours() + ":"
							+ (now.getMinutes() < 10 ? '0' : '')
							+ now.getMinutes() + ":"
							+ (now.getSeconds() < 10 ? '0' : '')
							+ now.getSeconds());
					myChart.data.datasets[0].data.push(data.pulse);
					myChart.data.datasets[1].data.push(data.temp+3);
					deleteline();
					myChart.update();
				},
				error : function(error) {
					console.log("error");
				}
			});

		}

		// create initial empty chart
		var ctx_live = document.getElementById("mycanvas");
		var myChart = new Chart(ctx_live, {
			type : 'line',
			data : {
				labels : [],
				datasets : [ {
					data : [],
					fill : false,
					borderWidth : 1,
					borderColor : '#00c0ef',
					label : "맥박",
				}, {
					data : [],
					lineTension : 0,
					borderColor : '#FF5E00',
					label : "체온"
				} ]
			},
			options : {
				responsive : true,
				legend : {
					display : true
				},
				scales : {
					yAxes : [ {
						gridLines : false,
						ticks : {
							beginAtZero : true,
						}
					} ]
				}
			}
		});

		var func = setInterval(searchFunction, 2000);

		function view_monitoring() {
			clearInterval(func);
			location.href = 'monitoring.jsp';
		}
	</script>
	<button class="btn btn-success btn-arrow-left"
		onclick="view_monitoring();">목록</button>

	<script
		src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.3.1.min.js"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>