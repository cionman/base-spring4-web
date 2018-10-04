<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h2>입력 화면</h2>
    <form:form modelAttribute="exampleForm">
        <div>텍스트를 입력해 주세요</div>
        <div>
            <form:input path="text" />
            <form:errors path="text" />
        </div>
        <div>
            <form:button>전송</form:button>
        </div>
    </form:form>
</body>
</html>
