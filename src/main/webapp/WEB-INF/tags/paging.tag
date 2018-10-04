<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" display-name="paging" trimDirectiveWhitespaces="true" %>
<%@ tag import="java.util.Enumeration" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="totalCount" type="java.lang.Integer" required="true" %>
<%@ attribute name="page" type="java.lang.Integer" required="true" %>
<%@ attribute name="pageScale" type="java.lang.Integer" required="true" %>
<%@ attribute name="scale" type="java.lang.Integer" required="true" %>
<%
if(totalCount > 0) {
int totalPage = (totalCount + scale - 1) / scale;
int start = page%pageScale == 0 ? (((page/pageScale) - 1) * pageScale) + 1 : ((page / pageScale) * pageScale) + 1;
int end = start+pageScale-1;
if(totalPage < end) {
	end = totalPage;
}
%>
	<div class="paging">
		<div class="paging-navi">
			<nav>
				<ul class="pagination">
					<li class="paginate_button <%if(page <= 1) {%> disabled<%} %>" data-dt-idx="1"><a href="#pageLink"><span class="glyphicon glyphicon-fast-backward"></span></a></li>
					<li class="paginate_button <%if(page < pageScale) {%> disabled<%} %>" data-dt-idx="<%=start-1%>"><a href="#pageLink"><span class="glyphicon glyphicon-backward"></span></a></li>
<%
if(start > 0) {
	for(int a = start ; a <= end ; a++) {
	
%>
					<li class="paginate_button <% if(a==page){%> active<%}%>" data-dt-idx="<%=a%>"><a href="#pageLink"><%= a%></a></li>
<%	
	}
}
%>
					<li class="paginate_button <%if(end == totalPage) { %> disabled<%} %>" data-dt-idx="<%=end+1%>"><a href="#pageLink"><span class="glyphicon glyphicon-forward"></span></a></li>
					<li class="paginate_button <%if(page >= totalPage) { %> disabled<%} %>" data-dt-idx="<%=totalPage%>"><a href="#pageLink"><span class="glyphicon glyphicon-fast-forward"></span></a></li>
				</ul>
			</nav>			
		</div>
	</div>

<form name="formPage" id="formPage" method="get">
<input type="hidden" name="page" value="<%=page%>">
<input type="hidden" name="scale" value="<%=scale%>">
<input type="hidden" name="pageScale" value="<%=pageScale%>">
<%
Enumeration<String> en = request.getParameterNames();
String param = null;
String name = null;
while(en.hasMoreElements()) {
	name = en.nextElement();
	if(!"page".equals(name)&&!"pageScale".equals(name)&&!"scale".equals(name)) {
		param = request.getParameter(name);
		param = param.replaceAll("\"", "&quot;");
%>
<c:set var="p"><c:out value="<%=param %>" escapeXml="true"></c:out></c:set>
<input type="hidden" name="<%=name%>" value="${p}">
<%
	}
}
%>
</form>
<script type="text/javascript">
$().ready(function(e){
	$('.paginate_button').click(function(e){
		if(!$(this).hasClass('active')&&!$(this).hasClass('disabled')) {
			$('#formPage').find('input[name="page"]').val($(this).data('dt-idx'));
			$('#formPage')[0].submit();
		}
	});
});
</script>
<%
}
%>
