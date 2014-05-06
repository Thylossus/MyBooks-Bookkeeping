<%-- 
    Document   : balancesheets
    Created on : 27.04.2014, 19:19:59
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>

<%@include file="header.jsp" %>

<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3>
                Balance Sheet Management
                <small>Click on a balance sheet to select it.</small>
            </h3>

        </div>
        <div class="panel-body">
            <div class="balance-sheets">
                <ul class="balance-sheet-list">
                    <sys:DataListOverview data="${requestScope.dataList}" order="${requestScope.orderby}" type="BalanceSheet">
                        <li data-title="${bsTitle}">
                            <h4>
                                <c:out value="${bsTitle}" />
                            </h4>
                            <span class="glyphicon glyphicon-gbp"></span>
                            <div class="balance-sheet-info">
                                <span class="balance-sheet-edited">Edited: ${bsEdited}</span>
                                <span class="balance-sheet-created">Created: ${bsCreated}</span>
                            </div>                        
                        </li>
                    </sys:DataListOverview>
                </ul>
            </div>
        </div>
        <div class="panel-footer">
            <div class="row">
                <div class="col-lg-5">
                    <div class="btn-group">
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                            Order By <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="${baseURL}/bsm/Balancesheets/orderby-title">Title</a></li>
                            <li><a href="${baseURL}/bsm/Balancesheets/orderby-date_of_creation">Date of creation</a></li>
                            <li><a href="${baseURL}/bsm/Balancesheets/orderby-date_of_last_change">Date of last change</a></li>
                        </ul>
                    </div>
                </div>
                <div class="col-lg-2"><a id="titleInput"></a></div>
                <div class="col-lg-5">
                    <form action="${baseURL}/bsm/createbalancesheet" method="POST">
                        <div class="input-group">
                            <input type="text" class="form-control" name="title" placeholder="Enter a title for a new balance sheet" required>
                            <span class="input-group-btn">
                                <button class="btn btn-default" name="submit" type="submit"><span class="glyphicon glyphicon-plus"></span></button>
                            </span>
                        </div><!-- /input-group -->
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="jslibraries.jsp" %>
<%-- Place JavaScript here (between inclusion of jslibraries.jsp and footer.jsp)! --%>

<script type="text/javascript">
    $(document).ready(function() {
        var menuDisabled = true;

        $(".balance-sheet-list li").click(function() {
            $(".balance-sheet-list li.selected").removeClass("selected");
            $(this).addClass("selected");

            if (menuDisabled) {
                $("li.disabled > a").unbind("click");
                $("li.disabled").removeClass("disabled");
            }

            //Update balance sheet in session!
            $.ajax({
                url: "${baseURL}/bsm/loadbalancesheet",
                type: "POST",
                data: "title=" + $(".selected").data("title") + "&submit=true"
            });
        });
    });
</script>

<%@include file="footer.jsp" %>