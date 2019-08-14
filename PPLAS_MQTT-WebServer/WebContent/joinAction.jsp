<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="account.AccountDAO"%>
<%@ page import="java.io.PrintWriter"%>
<%
	request.setCharacterEncoding("UTF-8");
%>
<jsp:useBean id="account" class="account.Account" scope="page" />
<jsp:setProperty name="account" property="accountID" />
<jsp:setProperty name="account" property="accountPassword" />
<jsp:setProperty name="account" property="accountName" />
<jsp:setProperty name="account" property="accountResidentID" />
<jsp:setProperty name="account" property="accountAuthority" />
<jsp:setProperty name="account" property="accountPhone" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP 게시판 웹 사이트</title>
</head>
<body>
	<%
		String accountID = null;
		if (session.getAttribute("accountID") != null) {
			accountID = (String) session.getAttribute("accountID");
		}

		if (accountID != null) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('이미 로그인이 되어있습니다.')");
			script.println("location.href = 'main.jsp'");
			script.println("</script>");
		}

		if (account.getAccountID() == null || account.getAccountPassword() == null
				|| account.getAccountName() == null || account.getAccountResidentID() == null
				|| account.getAccountAuthority() == null || account.getAccountPhone() == null) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('입력이 안 된 사항이 있습니다.')");
			script.println("history.back()");
			script.println("</script>");
		} else {
			AccountDAO accountDAO = new AccountDAO();
			int result = accountDAO.join(account);
			if (result == -1) {
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('이미 존재하는 아이디입니다.')");
				script.println("history.back()");
				script.println("</script>");
			} else {
				session.setAttribute("accountID", account.getAccountID());
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("location.href = 'main.jsp'");
				script.println("</script>");
			}
		}
	%>
</body>
</html>