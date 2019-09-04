<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ page import="android.ConnectDB"%>
<%
	request.setCharacterEncoding("UTF-8");

	String id = request.getParameter("accountID");
	String pw = request.getParameter("accountPassword");
	String name = request.getParameter("accountName");
	String type = request.getParameter("accountType"); //로그인, 회원가입 구별
	String authority = request.getParameter("accountAuthority");
	String phone = request.getParameter("accountPhone");
	String resident_id = request.getParameter("accountResidentID");

	ConnectDB connectDB = ConnectDB.getInstance();

	if (type.equals("login")) {
		String returns = connectDB.loginDB(id, pw, authority);
		out.print(returns);
	} else if (type.equals("join")) {
		String returns = connectDB.joinDB(id, pw, name, resident_id, phone, authority);
		out.print(returns);
	} else if (type.equals("searchName")) {
		String returns = connectDB.searchNameDB(id);
		out.print(returns);
	}
%>