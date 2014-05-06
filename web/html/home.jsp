<%-- 
    Document   : home
    Created on : 24.03.2014, 00:54:54
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>


<%@include file="header.jsp" %>

<div class="container">
    <div class="jumbotron">
        <h1>
            MyBooks - Bookkeeping
        </h1>
    </div>
    <sys:DataListOverview data="${articles}" type="Article">
        <div class="container">
            <h2>
                <c:out value="${title}" />
                <small>
                    <c:out value="${date}" />
                </small>
            </h2>
            <p>
                <c:out value="${content}" />
            </p>
            <p>
                Written by <c:out value="${author}" />. 
                <c:if test="${editor ne null}">
                    Last edited by <c:out value="${editor}" /> on <c:out value="${editDate}" />.
                </c:if>
            </p>
        </div>
    </sys:DataListOverview>
</div>

<%@include file="jslibraries.jsp" %>
<%-- Place JavaScript here (between inclusion of jslibraries.jsp and footer.jsp)! --%>
<%@include file="footer.jsp" %>