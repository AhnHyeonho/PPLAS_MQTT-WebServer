<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="account.Account"%>
<%@ page import="account.AccountDAO"%>
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
				<li><a href="log.jsp">로그</a></li>
				<li class="active"><a href="monitoring.jsp">실시간 모니터링</a>
			</ul>
			<%
				if (accountID == null) {
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false">접속하기<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="login.jsp">로그인</a></li>
						<li><a href="join.jsp">회원가입</a></li>
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
						<th style="background-color: #eeeeee; text-align: center;">환자아이디</th>
						<th style="background-color: #eeeeee; text-align: center;">환자아이디</th>
						<th style="background-color: #eeeeee; text-align: center;">환자이름</th>
						<th style="background-color: #eeeeee; text-align: center;">환자핸드폰</th>
					</tr>
				</thead>
				<tbody>
					<%
						AccountDAO accountDAO = new AccountDAO();
						ArrayList<Account> list = accountDAO.getList(pageNumber);

						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).getAccountAuthority().equals("patient")) {
					%>
					<tr>
						<td><%=list.get(i).getAccountID()%></td>
						<td><a
							href="monitoring_view.jsp?acID=<%=list.get(i).getAccountID()%>">
								<%=list.get(i).getAccountID()%>
						</a></td>
						<td><%=list.get(i).getAccountName()%></td>
						<td><%=list.get(i).getAccountPhone()%></td>
					</tr>
					<%
						}
						}
					%>
				</tbody>
			</table>
			<%
				if (pageNumber != 1) {
			%>
			<a href="monitoring.jsp?pageNumber=<%=pageNumber - 1%>"
				class="btn btn-success btn-arrow-left">이전</a>
			<%
				}
				if (accountDAO.nextPage(pageNumber + 1)) {
			%>
			<a href="monitoring.jsp?pageNumber=<%=pageNumber + 1%>"
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