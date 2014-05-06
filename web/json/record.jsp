<%@page contentType="application/json" %>
<%@taglib prefix="sys" uri="/WEB-INF/tlds/system" %>

<sys:ObjectToJSON object="${requestScope.record}" />