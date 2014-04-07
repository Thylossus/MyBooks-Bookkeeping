<%-- 
    Document   : error
    Created on : 24.03.2014, 01:06:25
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Error</h1>
        
        <c:import var="errormessages" url="../messages/errors.xml"/>
        <x:parse xml="${errormessages}" var="output" />
        <x:forEach select="$output/messages/msg" var="msg">
            <x:if select="$msg/errno = $requestScope:errno">
                <x:out select="$msg/errormsg" />
            </x:if>
        </x:forEach>
    </body>
</html>
