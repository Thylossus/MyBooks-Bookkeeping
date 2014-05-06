<%-- 
    Document   : deleteusers
    Created on : 05.05.2014, 13:04:47
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>


<%@include file="header.jsp" %>

<div class="container">
    <div id="warning_noRecordSelected" class="alert alert-warning alert-dismissable">
        <button type="button" class="close" data-hide="#warning_noRecordSelected" aria-hidden="true">&times;</button>
        <strong>Warning!</strong> Please select a user.
    </div>
    
    <div id="warning_DeletionUnsuccessful" class="alert alert-warning alert-dismissable">
        <button type="button" class="close" data-hide="#warning_noRecordSelected" aria-hidden="true">&times;</button>
        <strong>Warning!</strong> Deleting the customer was not successful. This was probably caused by an internal error. Please try again.
    </div>

    <div id="success_UserDeleted" class="alert alert-success alert-dismissable">
        <button type="button" class="close" data-hide="#success_UserDeleted" aria-hidden="true">&times;</button>
        <strong>Success!</strong> The selected user was deleted.
    </div>
</div>

<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3>
                Delete Users
                <small>Select a user by clicking on his row. Use the button in the panel's footer to delete selected users.</small>
            </h3>
        </div>
        <table class="table table-hover">
            <thead>
                <tr>
                    <th title="Click on a column title to sort. Hold SHIFT and click to use multiple columns for sorting.">Id</th>
                    <th title="Click on a column title to sort. Hold SHIFT and click to use multiple columns for sorting.">Mail</th>
                    <th title="Click on a column title to sort. Hold SHIFT and click to use multiple columns for sorting.">User Type</th>
                    <th title="Click on a column title to sort. Hold SHIFT and click to use multiple columns for sorting.">Firstname</th>
                    <th title="Click on a column title to sort. Hold SHIFT and click to use multiple columns for sorting.">Lastname</th>
                    <th title="Click on a column title to sort. Hold SHIFT and click to use multiple columns for sorting.">Last Sign In</th>
                    <th title="Click on a column title to sort. Hold SHIFT and click to use multiple columns for sorting.">Sign Up</th>
                </tr>
            </thead>
            <tbody>
                <c:set var="rSum" value="0.0" />
                <sys:DataListOverview data="${dataList}" type="User">
                    <tr id="${id}" class="user">
                        <td>${id}</td>
                        <td>${mail}</td>
                        <td>${userType}</td>
                        <td>${firstname}</td>
                        <td>${lastname}</td>
                        <td>${lastSignInDate}</td>
                        <td>${signUpDate}</td>
                    </tr>
                </sys:DataListOverview>
            </tbody>
        </table>

        <div class="panel-footer">
            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#uDelete">Delete User</button>
        </div>
    </div>
</div>

<div class="modal fade" id="uDelete">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Warning</h4>
            </div>
            <div class="modal-body">
                <p>
                    Please confirm that you want to delete the selected user. After deleting the user it cannot be restored.
                </p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" id="delete">Delete</button>
            </div>
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
            "order": [[0, "asc"]],
            "drawCallback": function() {
                //Add click event to drawn table rows/users
                $('.user').click(function() {
                    $('.user-selected').removeClass("user-selected");
                    $(this).addClass("user-selected");
                });

                //No user is selected initially
                $('.user-selected').removeClass("user-selected");
            }
        });

        var recordTable = $('.table').DataTable();

        //Customise style of data table
        $('div.dataTables_wrapper').addClass("panel-body");
        $('.dataTables_filter > label > input').appendTo('.dataTables_filter');
        $('.dataTables_filter > label').remove();
        $("<span class=\"input-group-addon\"><span class=\"glyphicon glyphicon-search\"></span></span>").insertAfter('.dataTables_filter > input');
        $('.dataTables_filter').addClass("input-group");
        $('.dataTables_filter > input').attr("placeholder", "Enter a string to filter rows!");

        //Hide alert function
        $('[data-hide]').click(function() {
            $($(this).data("hide")).hide();
        });

        //Delete a user
        $('#delete').click(function() {
            if ($('.user-selected').length === 1) {
                var id = parseInt($('.user-selected').attr("id"));

                if (id !== undefined && id !== Number.NaN) {
                    $.ajax({
                        url: "${baseURL}/sysmgmt/deleteuser",
                        type: "POST",
                        data: "id=" + id,
                        success: function(msg) {
                            $('#uDelete').modal("hide");
                            if (msg.success) {                                
                                recordTable.row($('.user-selected')).remove().draw();
                                $('#success_UserDeleted').fadeIn();
                            } else {
                                $('#warning_DeletionUnsuccessful').fadeIn();
                            }
                        }
                    });

                } else {
                    $('#uDelete').modal("hide");
                    $('#warning_DeletionUnsuccessful').fadeIn();
                }
            } else {
                $('#uDelete').modal("hide");
                $('#warning_noRecordSelected').fadeIn();
            }
        });


    });
</script>
<%@include file="footer.jsp" %>