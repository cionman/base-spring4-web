<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h2>출력 화면</h2>
    <div><c:out value="${accountCreateForm.name}"/></div>
    <div><c:out value="${accountCreateForm.tel}"/></div>
    <div><c:out value="${accountCreateForm.date}"/></div>
    <div><c:out value="${accountCreateForm.email}"/></div>
    <div>
        <a href='<c:url value="/example/"/>'>예제 목록 페이지로 이동</a>
    </div>
</body>
</html>
