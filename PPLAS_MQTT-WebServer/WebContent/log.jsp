<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="log.Log"%>
<%@ page import="log.LogDAO"%>
<%@ page import="java.util.ArrayList"%>
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
		int pageNumber = 1;
		if (accountID == null || !(accountID.equals("admin"))) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('관리자 로그인이 필요합니다.')");
			script.println("location.href = 'login.jsp'");
			script.println("</script>");
		}
		if (request.getParameter("pageNumber") != null) {
			pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
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
						<th style="background-color: #eeeeee; text-align: center;">번호</th>
						<th style="background-color: #eeeeee; text-align: center;">환자아이디</th>
						<th style="background-color: #eeeeee; text-align: center;">환자이름</th>
						<th style="background-color: #eeeeee; text-align: center;">발병일</th>
					</tr>
				</thead>
				<tbody>
					<%
						LogDAO logDAO = new LogDAO();
						ArrayList<Log> list = logDAO.getList(pageNumber);

						for (int i = 0; i < list.size(); i++) {
					%>
					<tr>
						<td><%=list.get(i).getLogID()%></td>
						<td><a href="log_view.jsp?logID=<%=list.get(i).getLogID()%>">
								<%=list.get(i).getAccountInfo().getAccountID()%>
						</a></td>
						<td><%=list.get(i).getAccountInfo().getAccountName()%></td>
						<td><%=list.get(i).getDate().substring(0, 11) + list.get(i).getDate().substring(11, 13) + "시"
						+ list.get(i).getDate().substring(14, 16) + "분"%></td>
					</tr>
					<%
						}
					%>

				</tbody>
			</table>
			<%
				if (pageNumber != 1) {
			%>
			<a href="log.jsp?pageNumber=<%=pageNumber - 1%>"
				class="btn btn-success btn-arrow-left">이전</a>
			<%
				}
				if (logDAO.nextPage(pageNumber + 1)) {
			%>
			<a href="log.jsp?pageNumber=<%=pageNumber + 1%>"
				class="btn btn-success btn-arrow-right">다음</a>
			<%
				}
			%>

		</div>
	</div>
	<script
		src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.3.1.min.js"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>