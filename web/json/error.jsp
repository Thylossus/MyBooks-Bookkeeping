<%@page contentType="application/json" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
{
   "success": false, 
   "message": 
   <c:import var="errormessages" url="../messages/errors.xml"/>
    <x:parse xml="${errormessages}" var="output" />
    <x:forEach select="$output/messages/msg" var="msg">
        <x:if select="$msg/errno = $requestScope:errno">
            <x:out select="$msg/errormsg" />
        </x:if>
    </x:forEach>
}