<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>locale 메세지</h1>
    <h1>Hello World Spring MVC!</h1>
    <ul>
    <c:forEach begin="1" end="10" step="1">
        <li><a href='${spring:mvcUrl("EC#example").build()}'>example 어플리케이션으로 이동 </a></li>
    </c:forEach>
    </ul>
</body>
</html>
