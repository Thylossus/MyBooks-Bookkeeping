<%@page contentType="application/json" %>
<%@taglib prefix="sys" uri="/WEB-INF/tlds/system" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="first" value="true" />
{
"size": <c:out value="${requestScope.numberOfRecords}" />,
"records" : [
<c:forEach items="${requestScope.records}" var="record">
    <c:choose>
        <c:when test="${first}">
            <sys:ObjectToJSON object="${record}" />
            <c:set var="first" value="false" />
        </c:when>
        <c:otherwise>
            , <sys:ObjectToJSON object="${record}" />
        </c:otherwise>
    </c:choose>    
</c:forEach>
]
}