<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <div id="wrapper">
        <h3>로그인 폼!!!!</h3>
        <c:if test="${param.containsKey('error')}">
            <span style="color:red;">
                <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
            </span>
        </c:if>
        <form:form>
            <table>
                <tr>
                    <td><label for="loginId">사용자명</label></td>
                    <td><input type="text" id="loginId" name="loginId"/></td>
                </tr>
                <tr>
                    <td><label for="pwd">패스워드</label></td>
                    <td><input type="password" id="pwd" name="pwd"/></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><button>로그인</button></td>
                </tr>
            </table>
        </form:form>
        <img src='<c:url value="/static/image/test.jpg"/>'/>
    </div>
</body>
</html>
