<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="mqtt.Publisher" %>
<%@ page import="java.io.PrintWriter" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
<%
	Publisher pb = new Publisher();
	pb.setBrokerUrl("tcp://116.126.97.126:1883");
	pb.setClientId("JavaSample");
	pb.setTopic("jmlee");
	pb.setContent("test Message");
	pb.myPublish();
%>

</body>
</html>