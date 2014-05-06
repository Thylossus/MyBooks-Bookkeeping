<%-- 
    Document   : error
    Created on : 24.03.2014, 01:06:25
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>

<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@include file="header.jsp" %>

<div class="container">
    <div class="jumbotron">
        <h1>Error</h1>

        <c:import var="errormessages" url="../messages/errors.xml"/>
        <x:parse xml="${errormessages}" var="output" />
        <x:forEach select="$output/messages/msg" var="msg">
            <x:if select="$msg/errno = $requestScope:errno">
                <x:out select="$msg/errormsg" />
            </x:if>
        </x:forEach>
    </div>
</div>
<%@include file="jslibraries.jsp" %>
<%-- Place JavaScript here (between inclusion of jslibraries.jsp and footer.jsp)! --%>
<%@include file="footer.jsp" %>
