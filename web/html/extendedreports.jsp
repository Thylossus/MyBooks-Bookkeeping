<%-- 
    Document   : extendedreports
    Created on : 02.05.2014, 16:16:37
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>

<%@include file="header.jsp" %>

<div class="container">
    <div class="jumbotron">
        <h1>Extended Reports</h1>
        <p>
            Get detailed reports of your balance sheet.
        </p>
    </div>
    <div class="row">
        <div class="col-md-4">
            <h2>Development of Costs</h2>
            <p>
                This report is not implemented yet.
            </p>
            <p>
                At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.
            </p>
            <p>
                <a class="btn btn-primary disabled" href="${baseURL}/bsm/viewreport/report-developmentofcosts" role="button">View report &raquo;</a>
            </p>
        </div>
        <div class="col-md-4">
            <h2>Top 10 Expenses</h2>
            <p>
                Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.
            </p>
            <p>
                <a class="btn btn-primary" href="${baseURL}/bsm/viewreport/report-toptenexpenses" role="button">View report &raquo;</a>
            </p>
        </div>
        <div class="col-md-4">
            <h2>Top 10 Incomes</h2>
            <p>
                Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.
            </p>
            <p>
                <a class="btn btn-primary" href="${baseURL}/bsm/viewreport/report-toptenincomes" role="button">View report &raquo;</a>
            </p>
        </div>
    </div>
</div>

<%@include file="jslibraries.jsp" %>
<%-- Place JavaScript here (between inclusion of jslibraries.jsp and footer.jsp)! --%>
<%@include file="footer.jsp" %>