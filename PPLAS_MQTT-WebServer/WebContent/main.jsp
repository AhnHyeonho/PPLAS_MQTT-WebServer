<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
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
				<li class="active"><a href="main.jsp">메인</a></li>
				<li><a href="log.jsp">로그</a></li>
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
		<div class="jumbotron">
			<div class="continer">
				<!-- <h1>웹 사이트 소개</h1>
				<p>이 웹사이트는 부트스트랩으로 만든 JSP 웹 사이트입니다. 최소한의 간단한 로직만을 이용해서 개발했습니다.<br>
				디자인 템플릿으로는 부트스트랩을 이용했습니다.<br>
				이 웹사이트는 나동빈님의 강좌를 참고하여 작성했습니다.<br><br>
				아래 SNS이미지를 클릭하면 해당 SNS사이트로 이동합니다.<br>
				아래 링크는 이 웹사이트의 제작자 안현호의 Github 저장소입니다.</p>
				<p><a class="btn btn-primary btn-pull" href="https://github.com/AhnHyeonho" role="button">AhnHyeonho's Git</a></p> -->
			</div>
		</div>
	</div>
	<!-- <div class="container">
		<div id="myCarousel" class="carousel slide" data-ride="carousel">
			<ol class="carousel-indicators">
				<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
				<li data-target="#myCarousel" data-slide-to="1" ></li>
				<li data-target="#myCarousel" data-slide-to="2" ></li>
			</ol>
			<div class="carousel-inner">
				<div class="item active">
					<a href="https://twitter.com/"><img class="img-responsive center-block" src="images/1.png"></a>
				</div>
				<div class="item">
					<a href="https://www.instagram.com/"><img class="img-responsive center-block" src="images/2.png"></a>
				</div>
				<div class="item">
					<a href="https://www.facebook.com/"><img class="img-responsive center-block" src="images/3.png"></a>
				</div>
			</div>
			<a class="carousel-control left" href="#myCarousel" data-slide="prev">
				<span class="glyphicon glyphicon-chevron-left"></span>
		    </a>
		    <a class="carousel-control right" href="#myCarousel" data-slide="next">
				<span class="glyphicon glyphicon-chevron-right"></span>
		    </a>	
		</div>
	</div> -->
	<script
		src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.3.1.min.js"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>