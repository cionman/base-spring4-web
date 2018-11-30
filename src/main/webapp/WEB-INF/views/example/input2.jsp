<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h2>입력 화면</h2>
    <form:form modelAttribute="accountCreateForm">
        <span>이름</span><form:input path="name"/><form:errors path="name"/><br/>
        <span>Tel</span><form:input path="tel"/><form:errors path="tel"/><br/>
        <span>생년월일</span><form:input path="dateOfBirth" type="date"/><form:errors path="dateOfBirth"/><br/>
        <span>E-mail</span><form:input path="email" type="email"/><form:errors path="email"/><br/>
        <div>
            <form:button>전송</form:button>
        </div>
    </form:form>
</body>
</html>
