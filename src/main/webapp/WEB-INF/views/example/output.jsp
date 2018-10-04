<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h2>출력 화면</h2>
    <div>
        입력한 텍스트는 '<span><c:out value="${exampleForm.text}"/> </span>' 입니다.
    </div>
    <div>
        <a href='<c:url value="/example/"/>'>예제 목록 페이지로 이동</a>
    </div>
</body>
</html>
