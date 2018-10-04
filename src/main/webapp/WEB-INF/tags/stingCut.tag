<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" display-name="paging" trimDirectiveWhitespaces="true" %>
<%@ attribute name="str" type="java.lang.String" required="true" %>
<%@ attribute name="len" type="java.lang.Integer" required="true" %>
<%
	if(str.length() >= len) {
		out.println(str.substring(0,len-2)+"...");
	}
	else {
		out.println(str);
	}
%>