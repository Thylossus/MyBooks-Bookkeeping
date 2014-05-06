<%-- 
    Document   : basicreports
    Created on : 02.05.2014, 16:16:12
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>

<%@include file="header.jsp" %>

<div class="container">
    <div class="jumbotron">
        <h1>Basic Reports</h1>
        <p>
            Get an overview of your income and expenses using the reports below. There are also more detailed reports for premium members.
        </p>
    </div>
    <div class="row">
        <div class="col-md-4">
            <h2>Month Overview</h2>
            <p>
                Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.
            </p>
            <p>
                <a class="btn btn-primary" href="${baseURL}/bsm/viewreport/report-monthoverview" role="button">View report &raquo;</a>
            </p>
        </div>
        <div class="col-md-4">
            <h2>Year Overview</h2>
            <p>
                Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.
            </p>
            <p>
                <a class="btn btn-primary" href="${baseURL}/bsm/viewreport/report-yearoverview" role="button">View report &raquo;</a>
            </p>
        </div>
        <div class="col-md-4">
            <h2>Cagegories</h2>
            <p>
                This report is not implemented yet.
            </p>
            <p>
                At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.
            </p>
            <p>
                <a class="btn btn-primary disabled" href="${baseURL}/bsm/viewreport/report-categories" role="button">View report &raquo;</a>
            </p>
        </div>
    </div>
</div>

<%@include file="jslibraries.jsp" %>
<%-- Place JavaScript here (between inclusion of jslibraries.jsp and footer.jsp)! --%>
<script type="text/javascript">
    $(document).ready(function() {
        $(".nav li.disabled a").click(function(e) {
            e.preventDefault();
            return false;
        });
    });
</script>
<%@include file="footer.jsp" %>