<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form method="post" action='<c:url value="/test/upload"/>'>
		<input type="file" name="file"/>
		<input type="submit" value="전송"/>
	</form>
</body>
<script>
	const msg = '${msg}';
	alert(msg);
</script>
</html>