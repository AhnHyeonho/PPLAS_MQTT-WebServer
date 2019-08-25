<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ page import="android.ConnectDB" %>
<%

	request.setCharacterEncoding("UTF-8");
	
	String id = request.getParameter("id");
	String pw = request.getParameter("pw");
	String name = request.getParameter("name");
	String type = request.getParameter("type");		//로그인, 회원가입 구별
	String authority = request.getParameter("authority"); 
	String phone = request.getParameter("phone");
	String resident_id = request.getParameter("resident_id");
	
	ConnectDB connectDB = ConnectDB.getInstance();
	
	if(type.equals("login")){
		String returns = connectDB.loginDB(id,pw,authority);
		out.print(returns);
	}
	else if(type.equals("join")){
		String returns = connectDB.joinDB(id,pw,name,resident_id,phone,authority);
		out.print(returns);
	}
	else if(type.equals("searchName")){
		String returns = connectDB.searchNameDB(id);
		out.print(returns);
	} 
%>