<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ts" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><sitemesh:write property='title'/></title>
	<%@ include file="/WEB-INF/views/include/css.jsp" %>
	<sitemesh:write property='head'/>
</head>
<body>
	<c:set var="logoutUrl">
		<spring:eval expression="@prop['auth.logoutUrl']"/>
	</c:set>
	<h2>sitemesh 영역${logoutUrl}</h2>
	<!-- a태그를 이용한 get 방식으로는 로그아웃 절차가 진행되지 않는다. -->
	<form action="<c:url value='${logoutUrl}'/>" method="post">
		<sec:csrfInput/>
		<button>logout</button>
	</form>
	<sitemesh:write property='body'/>
	<script>

	</script>
</body>
</html>