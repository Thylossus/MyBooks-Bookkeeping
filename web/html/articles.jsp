<%-- 
    Document   : articles
    Created on : 05.05.2014, 20:59:05
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>


<%@include file="header.jsp" %>

<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3>
                Article Editor
                <small>Write or edit articles.</small>
            </h3>
        </div>
        <table class="table table-hover">
            <thead>
                <tr>
                    <th title="Click on a column title to sort. Hold SHIFT and click to use multiple columns for sorting.">Title</th>
                    <th title="Click on a column title to sort. Hold SHIFT and click to use multiple columns for sorting.">Date</th>
                    <th title="Click on a column title to sort. Hold SHIFT and click to use multiple columns for sorting.">Author</th>
                </tr>
            </thead>
            <tbody>
                <c:set var="rSum" value="0.0" />
                <sys:DataListOverview data="${articles}" type="Article">
                    <tr class="article">
                        <td>${title}</td>
                        <td>${date}</td>
                        <td>${author}</td>
                    </tr>
                </sys:DataListOverview>
            </tbody>
        </table>
        <div class="panel-footer">
            
        </div>
    </div>
</div>

<%@include file="jslibraries.jsp" %>
<%-- Place JavaScript here (between inclusion of jslibraries.jsp and footer.jsp)! --%>
<script type="text/javascript" src="${baseURL}/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="${baseURL}/js/datatables.js"></script>
<script type="text/javascript">
    $(document).ready(function() {

        //Init data table
        $('table.table').dataTable({
            "order": [[1, "desc"]]
        });

        //Customise style of data table
        $('div.dataTables_wrapper').addClass("panel-body");
        $('.dataTables_filter > label > input').appendTo('.dataTables_filter');
        $('.dataTables_filter > label').remove();
        $("<span class=\"input-group-addon\"><span class=\"glyphicon glyphicon-search\"></span></span>").insertAfter('.dataTables_filter > input');
        $('.dataTables_filter').addClass("input-group");
        $('.dataTables_filter > input').attr("placeholder", "Enter a string to filter rows!");

    });
</script>
<%@include file="footer.jsp" %>