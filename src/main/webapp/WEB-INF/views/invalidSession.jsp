<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <div id="wrapper">
        <h1>세션이 종료되었습니다.</h1>
    </div>
    <script>
        setTimeout(function () {
            location.href='${spring:mvcUrl("AC#login").build()}';
        }, 2000);
    </script>
</body>
</html>
