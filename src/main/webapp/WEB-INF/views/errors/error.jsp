<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <c:choose>
        <c:when test="${not empty message}">
            <h2>${message}</h2>
        </c:when>
        <c:otherwise>
            <h2>예기치 않은 에러가 발생하였습니다.</h2>
        </c:otherwise>
    </c:choose>
    <c:if test="${not empty errorStack}">
        <h3>ErrorStackTrace1 : ${errorStack}</h3>
    </c:if>
</body>
</html>
