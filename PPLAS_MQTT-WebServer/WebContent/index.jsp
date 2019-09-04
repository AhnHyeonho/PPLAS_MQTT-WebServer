<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="mqtt.Subscriber"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="css/custom.css">
<title>MQTT 환자관리 웹 사이트</title>
</head>
<body>
	<%
		Subscriber sb = new Subscriber("192.168.100.115", "server", "user/#"); /* 브로커 아이피, 브로커 이름, subscribe할 topic 순으로 입력 */
		sb.subscribe();
	%>
	<script>
		location.href = 'main.jsp';
	</script>
</body>
</html>