<%-- 
    Document   : balancesheeteditor
    Created on : 28.04.2014, 18:12:52
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>

<%@include file="header.jsp" %>

<div class="container">
    <div class="row">
        <div class="col-md-10">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3>
                        Balance Sheet Editor
                        <small>Edit your balance sheet here!</small>
                    </h3>
                </div>
                <div class="panel-body">

                </div>
            </div>
        </div><!-- Editor -->        
        <div class="col-md-2">
            <div class="btn-group-vertical">
                <button type="button" class="btn btn-primary">Create Record</button>
                <button type="button" class="btn btn-primary">View Record Details</button>
                <button type="button" class="btn btn-primary">Change Record</button>
                <button type="button" class="btn btn-primary">Delete Record</button>
                <button type="button" class="btn btn-primary">Category Management</button>
                <a href="${baseURL}/bsm/closebalancesheet" class="btn btn-primary">Close</a>
            </div>
        </div><!-- Menu -->
    </div>
</div>

<%@include file="footer.jsp" %>