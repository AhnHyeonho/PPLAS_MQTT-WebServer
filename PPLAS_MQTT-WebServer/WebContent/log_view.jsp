<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="log.Log" %>
<%@ page import="log.LogDAO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="account.Account" %>
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
		if(session.getAttribute("accountID") != null){
			accountID = (String) session.getAttribute("accountID");
		}
		int logID = 0;
		
		if(request.getParameter("logID") != null){
			logID = Integer.parseInt(request.getParameter("logID"));
		}
		
		if(logID == 0) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('유효하지 않은 글입니다.')");
			script.println("location.href = 'log.jsp'");
			script.println("</script>");
		}
		Log log = new LogDAO().getLog(logID);
	%>
			<%= logID %>
	<nav class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
				aria-expanded="false">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="main.jsp">MQTT 환자관리 웹 사이트</a>
		</div>		
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li class="active"><a href="main.jsp">메인</a></li>
				 <li><a href="log.jsp">응급 리스트</a></li> 
			</ul>
			<%
				if(accountID == null) {
			%>
				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown"> 
						<a href="#" class="dropdown-toggle"
							data-toggle="dropdown" role="button" aria-haspopup="true"
							aria-expanded="false">접속하기<span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="login.jsp">로그인</a></li>
							<li><a href="join.jsp">회원가입</a></li>
						</ul>
					</li>
				</ul>
			<%
				} else {
			%>	
				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown"> 
						<a href="#" class="dropdown-toggle"
							data-toggle="dropdown" role="button" aria-haspopup="true"
							aria-expanded="false">회원관리<span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="logoutAction.jsp">로그아웃</a></li>
						</ul>
					</li>
				</ul>	 
			<%
				}
			%>	
		</div>
	</nav>
	
	<div class="container">
		<div class="row">
			<table class="table table-striped" style="text-align: center; border: 1px solid #dddddd">
				<thead>
					<tr>
						<th colspan="3" style="background-color: #eeeeee; text-align: center;">응급환자 정보</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td style= "width: 20%;">환자 아이디</td>
						<td colspan="2"><%= log.getAccountInfo().getAccountID() %>
					</tr>
					<tr>
						<td>환자 이름</td>
						<td colspan="2"><%= log.getAccountInfo().getAccountName() %>
					</tr>
					<tr>
						<td>작성일자</td>
						<td colspan="2"><%= log.getDate().substring(0, 11) + log.getDate().substring(11, 13) + "시" + log.getDate().substring(14, 16) + "분" %>
					</tr>
					<tr>
						<td>위치</td>
						<td colspan="2" style ="min-height: 200px; text-align: left;">여기다가 네이버 지도 API 넣을거임 
					</tr>
				</tbody>
			</table>
			<a href="log.jsp" class="btn btn-primary">목록</a>
			
			<input type="submit" class="btn btn-primary pull-right" value="글쓰기">
			
		</div>
	</div>
	<script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.3.1.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>