<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>example페이지</h1>
    <ul>
        <li><a href='${spring:mvcUrl("EC#exampleView").build()}'>웹페이지 단순이동 </a></li>
        <li><a href='${spring:mvcUrl("EC#exampleInput").build()}'>spring form 예제1 </a></li>
        <li><a href='${spring:mvcUrl("EC#exampleInput2").build()}'>spring form 예제2 </a></li>
        <li><a href='${spring:mvcUrl("AC#profile").build()}'>profile</a></li>
    </ul>
</body>
</html>
