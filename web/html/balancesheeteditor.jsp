<%-- 
    Document   : balancesheeteditor
    Created on : 28.04.2014, 18:12:52
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>

<%@include file="header.jsp" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container">
    <div id="warning_noRecordSelected" class="alert alert-warning alert-dismissable">
        <button type="button" class="close" data-hide="#warning_noRecordSelected" aria-hidden="true">&times;</button>
        <strong>Warning!</strong> Please select a record.
    </div>
</div>

<div class="container">
    <div class="row">
        <div class="col-md-10">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3>
                        Balance Sheet Editor
                        <small>Edit your balance sheet here! Select a record by clicking on its row.</small>
                    </h3>
                </div>
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th title="Click on a column title to sort. Hold SHIFT and click to use multiple columns for sorting.">Date</th>
                            <th title="Click on a column title to sort. Hold SHIFT and click to use multiple columns for sorting.">Title</th>
                            <th title="Click on a column title to sort. Hold SHIFT and click to use multiple columns for sorting.">Amount</th>
                            <th title="Click on a column title to sort. Hold SHIFT and click to use multiple columns for sorting.">Description</th>
                            <th title="Click on a column title to sort. Hold SHIFT and click to use multiple columns for sorting.">Category</th>
                            <th title="Click on a column title to sort. Hold SHIFT and click to use multiple columns for sorting." class="rCatColour"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="rSum" value="0.0" />
                        <sys:DataListOverview data="${records}" type="Record">
                            <c:choose>
                                <c:when test="${rAmount>=0.0}">
                                    <c:set var="rType" value="income" />
                                </c:when>

                                <c:otherwise>
                                    <c:set var="rType" value="expense" />
                                </c:otherwise>
                            </c:choose>

                            <c:set var="rSum" value="${rSum + rAmount}" />

                            <tr id="${rId}" class="${rType} record" data-bsid="${rBalanceSheet}" data-recordid="${rId}">
                                <td>${rDate}</td>
                                <td>${rTitle}</td>
                                <td><fmt:formatNumber type="currency" currencySymbol="&pound;" pattern="#,##0.00 ¤;-#,##0.00 ¤" value="${rAmount}" /></td>
                                <td data-fulltext="${rDescription}">${rDescription}</td>
                                <td>${rCatName}</td>
                                <td data-catid="${rCatId}" style="background-color: #${rCatColour};"></td>
                            </tr>
                        </sys:DataListOverview>
                    </tbody>

                    <tfoot>
                    <th></th>
                    <th class="rSumLabel">&Sum;</th>
                    <th class="rSum"></th>
                    <th></th>
                    <th></th>
                    <th></th>
                    </tfoot>

                </table>
            </div>
        </div><!-- Editor -->       

        <jsp:useBean class="beans.Date" id="today">
            <jsp:setProperty name="today" property="format" value="yyyy-MM-dd" />
        </jsp:useBean>

        <div class="col-md-2">
            <div class="btn-group-vertical">
                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#rCreate">Create Record</button>
                <button type="button" class="btn btn-primary modal-verification-required" data-target="#rView">View Record Details</button>
                <button type="button" class="btn btn-primary modal-verification-required" data-target="#rChange">Change Record</button>
                <button type="button" class="btn btn-primary modal-verification-required" data-target="#rDelete">Delete Record</button>
                <button type="button" class="btn btn-primary">Category Management</button>
                <a href="${baseURL}/bsm/closebalancesheet" class="btn btn-primary">Close</a>
            </div>
            <div class="input-group input-group-sm month-selection">
                <span class="input-group-btn">
                    <button id="decreaseMonth" class="btn btn-primary" type="button"><span class="glyphicon glyphicon-chevron-left"></span></button>
                </span>
                <input type="text" id="month" class="form-control" readonly data-value="${today.month}"/>
                <span class="input-group-btn">
                    <button id="increaseMonth" class="btn btn-primary" type="button"><span class="glyphicon glyphicon-chevron-right"></span></button>
                </span>
            </div>
            <div class="input-group input-group-sm year-selection">
                <span class="input-group-btn">
                    <button id="decreaseYear" class="btn btn-primary" type="button"><span class="glyphicon glyphicon-chevron-left"></span></button>
                </span>
                <input type="text" id="year" class="form-control" readonly  value="${today.year}"/>
                <span class="input-group-btn">
                    <button id="increaseYear" class="btn btn-primary" type="button"><span class="glyphicon glyphicon-chevron-right"></span></button>
                </span>
            </div>
        </div><!-- Menu -->
    </div>
</div>

<div class="modal fade" id="rCreate">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Create a record</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">

                    <div class="form-group">
                        <label class="control-label col-xs-2" for="newRecordTitle">Title</label>
                        <div class="controls col-xs-10">
                            <input id="newRecordTitle" type="text" placeholder="Enter a title for the new record." class="form-control" required autofocus>
                            <p class="help-block"></p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-xs-2" for="newRecordDescription">Description</label>
                        <div class="controls col-xs-10">
                            <textarea id="newRecordDescription" placeholder="Enter a description for the new record." maxlength="1500" rows="7" class="form-control" required></textarea>
                            <p class="help-block"></p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-xs-2" for="newRecordAmount">Amount</label>
                        <div class="controls col-xs-10">
                            <input id="newRecordAmount" type="number" step="0.01" placeholder="Positive amount = income; negative amount = expense" class="form-control" required>
                            <p class="help-block"></p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-xs-2" for="newRecordDate">Date</label>
                        <div class="controls col-xs-10">
                            <input id="newRecordDate" type="date" value="${today}" class="form-control" required>
                            <p class="help-block"></p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-xs-2" for="newRecordCategory">Category</label>
                        <div class="controls col-xs-10">
                            <select id="newRecordCategory" placeholder="Select a category." class="form-control" required>
                                <sys:DataListOverview data="${requestScope.userCategories}" type="Category">
                                    <option value="${cId}">${cName}</option>
                                </sys:DataListOverview>
                            </select>
                            <p class="help-block"></p>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" id="create">Create</button>
            </div>
        </div>
    </div>
</div>    

<div class="modal fade" id="rView">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title" id="viewRecordTitle">Replace by record title</h4>
            </div>
            <div class="modal-body">
                <p>
                    <span class="text-primary">Amount: </span>
                    <span id="viewRecordAmount"></span>
                </p>
                <p>
                    <span class="text-primary">Category: </span>
                    <span id="viewRecordCategory"></span>
                </p>
                <p>
                    <span class="text-primary">Description:</span>
                </p>
                <p id="viewRecordDescription"></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div> 


<div class="modal fade" id="rChange">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Replace by record title</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">

                    <div class="form-group">
                        <label class="control-label col-xs-2" for="changeRecordTitle">Title</label>
                        <div class="controls col-xs-10">
                            <input id="changeRecordTitle" type="text" class="form-control" required autofocus>
                            <p class="help-block"></p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-xs-2" for="changeRecordDescription">Description</label>
                        <div class="controls col-xs-10">
                            <textarea id="changeRecordDescription" maxlength="1500" rows="7" class="form-control" required></textarea>
                            <p class="help-block"></p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-xs-2" for="changeRecordAmount">Amount</label>
                        <div class="controls col-xs-10">
                            <input id="changeRecordAmount" type="number" step="0.01" class="form-control" required>
                            <p class="help-block"></p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-xs-2" for="changeRecordDate">Date</label>
                        <div class="controls col-xs-10">
                            <input id="changeRecordDate" type="date" value="${today}" class="form-control" required>
                            <p class="help-block"></p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-xs-2" for="changeRecordCategory">Category</label>
                        <div class="controls col-xs-10">
                            <select id="changeRecordCategory" placeholder="Select a category." class="form-control" required>
                                <sys:DataListOverview data="${requestScope.userCategories}" type="Category">
                                    <option value="${cId}">${cName}</option>
                                </sys:DataListOverview>
                            </select>
                            <p class="help-block"></p>
                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" id="change">Apply</button>
            </div>
        </div>
    </div>
</div> 

<div class="modal fade" id="rDelete">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Warning</h4>
            </div>
            <div class="modal-body">
                <p>
                    Please confirm that you want to delete the selected record. After deleting the record it cannot be restored.
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
<script type="text/javascript" src="${baseURL}/js/custom-currency.sorting.js"></script>
<script type="text/javascript" src="${baseURL}/js/jQuery.succinct.min.js"></script>
<script type="text/javascript" src="${baseURL}/js/months.js"></script>
<script type="text/javascript">
    $(document).ready(function() {

        /**
         * Convert a floating point number into a string representation of its currency value in pounds.
         * Negative values have a leading '-' and the pound sign is the currency's prefix.
         * @param {Number} number 
         * @returns {String} 
         */
        function toGBP(number) {
            var numberStr = number.toString(),
                    pounds = '$' + numberStr.split('.')[0],
                    pennies = (numberStr.split('.')[1] || '') + '00';
            pounds = pounds.split('').reverse().join('')
                    .replace(/(\d{3}(?!(\D)))/g, '$1,')
                    .replace('$', '')
                    .split('').reverse().join('');
            return pounds + '.' + pennies.slice(0, 2) + ' \u00A3';
        }

        
        //Init month selection
        $('#month').val(MONTH.monthByValue($('#month').data("value")).name);

        //Init data table
        $('table.table').dataTable({
            "order": [[0, "desc"], [2, "desc"]],
            "autoWidth": false,
            "aoColumns": [
                {"sClass": "rDate", "sWidth": "10%"},
                {"sClass": "rTitle", "sWidth": "15%"},
                {"sClass": "rAmount"},
                {"sClass": "rDescription", "sWidth": "40%"},
                {"sClass": "rCatName"},
                {"sClass": "rCatColour", "orderable": false}
            ],
            "fnFooterCallback": function(nRow, aaData, iStart, iEnd, aiDisplay) {
                /* Calculate the sum on this page */
                var iSum = 0;
                var cellValue;
                for (var i = iStart; i < iEnd; i++)
                {
                    cellValue = aaData[ aiDisplay[i] ][2].replace(/,/g, "");
                    cellValue = cellValue.substring(0, cellValue.length-2);
                    iSum += Math.round(parseFloat(cellValue) * 100.0) / 100.0;
                }

                $('.rSum').text(Math.round(iSum * 100.0) / 100.0 + " \u00A3");
            },
            "drawCallback": function() {
                //Add click event to drawn table rows/records
                $('.record').click(function() {
                    $('.record-selected').removeClass("record-selected");
                    $(this).addClass("record-selected");
                });

                //No record is selected initially
                $('.record-selected').removeClass("record-selected");
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

        //Truncate descriptions
        $('td.rDescription').succinct({
            size: 120
        });

        //Month selection
        $('.month-selection > .input-group-btn > button, .year-selection > .input-group-btn > button').click(function() {
            var month = MONTH.monthByValue($('#month').data("value"));
            var year = $('#year').val();

            switch ($(this).attr('id')) {
                case "decreaseMonth":
                    month = MONTH.previous(month);
                    if (month === MONTH.DEC) {
                        year--;
                    }
                    break;
                case "increaseMonth":
                    month = MONTH.next(month);
                    if (month === MONTH.JAN) {
                        year++;
                    }
                    break;
                case "decreaseYear":
                    year--;
                    break;
                case "increaseYear":
                    year++;
                    break;
            }

            $('#month').val(month.name);
            $('#month').data("value", month.value);
            $('#year').val(year);

            $.ajax({
                url: "${baseURL}/editor/loadrecordsofmonth",
                type: "POST",
                data: "month=" + month.value + "&year=" + year,
                success: function(msg) {
                    recordTable.clear();
                    if (msg.size !== 0) {
                        msg.records.forEach(function(record) {
                            var newRow = recordTable.row.add([
                                record.date,
                                record.title,
                                toGBP(record.amount),
                                record.description,
                                record.catName,
                                record.catColour
                            ]).draw().node();

                            if (record.amount >= 0.0) {
                                $(newRow).addClass("income");
                            } else {
                                $(newRow).addClass("expense");
                            }

                            $(newRow).addClass("record");

                            $(newRow).attr("data-bsid", record.balanceSheet);
                            $(newRow).attr("data-recordid", record.id);
                            $(newRow).attr("id", record.id);

                            var catColour = $(newRow).children('.rCatColour');
                            catColour.css("background-color", "#" + catColour.text());
                            catColour.attr("data-catid", record.catId);
                            catColour.text("");

                            $(newRow).children('.rDescription').attr("data-fulltext", record.description);

                        });
                    }

                    recordTable.draw();
                    //Truncate descriptions
                    $('td.rDescription').succinct({
                        size: 120
                    });
                }
            });
        });

        //Hide alert function
        $('[data-hide]').click(function() {
            $($(this).data("hide")).hide();
        });

        //Check if record selected before opening modal.
        $('.modal-verification-required').click(function() {
            if ($('.record-selected').length === 1) {
                var target = $(this).data("target");

                switch (target) {
                    case "#rChange":
                        //Copy record values into form
                        $('#changeRecordTitle').val($('.record-selected > .rTitle').text());
                        $('#changeRecordDescription').val($('.record-selected > .rDescription').data("fulltext"));
                        $('#changeRecordAmount').val(Math.round(parseFloat($('.record-selected > .rAmount').text().replace(/,/g, '')) * 100.0) / 100.0);
                        $('#changeRecordDate').val($('.record-selected > .rDate').text());
                        $('#changeRecordCategory').val($('.record-selected > .rCatColour').data("catid"));
                        break;
                    case "#rView":
                        //Copy record values into view
                        $('#viewRecordTitle').text($('.record-selected > .rTitle').text());
                        $('#viewRecordDescription').text($('.record-selected > .rDescription').data("fulltext"));
                        $('#viewRecordAmount').text($('.record-selected > .rAmount').text());
                        $('#viewRecordDate').text($('.record-selected > .rDate').text());
                        $('#viewRecordCategory').text($('.record-selected > .rCatName').text());
                        
                        break;
                }

                $(target).modal();
            } else {
                $('#warning_noRecordSelected').fadeIn();
            }
        });

        //Create record button
        $('#create').click(function() {
            var title = $('#newRecordTitle').val();
            var description = $('#newRecordDescription').val();
            var amount = $('#newRecordAmount').val();
            var date = $('#newRecordDate').val();
            var category = $('#newRecordCategory').val();

            $.ajax({
                url: "${baseURL}/editor/createrecord",
                type: "POST",
                data: "submit=true&title=" + title + "&description=" + description + "&amount=" + amount + "&date=" + date + "&category=" + category,
                success: function(msg) {
                    $('#rCreate').modal('hide');
                    $('#newRecordTitle').val("");
                    $('#newRecordDescription').val("");
                    $('#newRecordAmount').val("");

                    //Check whether the record should be shown or not
                    var displayedMonth = parseInt($('#month').data("value")) + 1;
                    var displayedYear = parseInt($('#year').val());
                    var recordMonth = parseInt(msg.date.split("-")[1]);
                    var recordYear = parseInt(msg.date.split("-")[0]);

                    if (displayedMonth === recordMonth && displayedYear === recordYear) {
                        var newRow = recordTable.row.add([
                            msg.date,
                            msg.title,
                            toGBP(msg.amount),
                            msg.description,
                            msg.catName,
                            msg.catColour
                        ]).draw().node();

                        if (msg.amount >= 0.0) {
                            $(newRow).addClass("income");
                        } else {
                            $(newRow).addClass("expense");
                        }
                        $(newRow).addClass("record");

                        $(newRow).attr("data-bsid", msg.balanceSheet);
                        $(newRow).attr("data-recordid", msg.id);
                        $(newRow).attr("id", msg.id);

                        var catColour = $(newRow).children('.rCatColour');
                        catColour.css("background-color", "#" + catColour.text());
                        catColour.attr("data-catid", msg.catId);
                        catColour.text("");

                        $(newRow).children('.rDescription').attr("data-fulltext", msg.description);

                        //Redraw table
                        recordTable.draw();
                        //Truncate descriptions
                        $('td.rDescription').succinct({
                            size: 120
                        });
                    }
                }
            });
        });

        $('#delete').click(function() {
            var bsid = $('.record-selected').data("bsid");
            var recordid = $('.record-selected').data("recordid");

            if (bsid !== undefined && recordid !== undefined) {
                $.ajax({
                    url: "${baseURL}/editor/deleterecord",
                    type: "POST",
                    data: "bsid=" + bsid + "&recordid=" + recordid,
                    success: function(msg) {
                        if (msg.success) {
                            $('#rDelete').modal("hide");
                            recordTable.row($('.record-selected')).remove().draw();
                        } else {
                            console.log(msg);
                        }
                    }
                });
            }
        });

        $('#change').click(function() {
            var recordid = $('.record-selected').data("recordid");
            var title = $('#changeRecordTitle').val();
            var description = $('#changeRecordDescription').val();
            var amount = $('#changeRecordAmount').val();
            var date = $('#changeRecordDate').val();
            var category = $('#changeRecordCategory').val();

            $.ajax({
                url: "${baseURL}/editor/changerecord",
                type: "POST",
                data: "submit=true&recordid=" + recordid + "&title=" + title + "&description=" + description + "&amount=" + amount + "&date=" + date + "&category=" + category,
                success: function(msg) {
                    $('#rChange').modal('hide');

                    $('.record-selected > .rCatColour').css("background-color", "#" + msg.catColour).data("catid", msg.catId);
                    $('.record-selected > .rDescription').attr("data-fulltext", msg.description);

                    if (msg.amount >= 0.0) {
                        $('.record-selected').removeClass("expense").addClass("income");
                    } else {
                        $('.record-selected').removeClass("income").addClass("expense");
                    }

                    recordTable.row($('.record-selected'))
                            .data([
                                msg.date,
                                msg.title,
                                toGBP(msg.amount),
                                msg.description,
                                msg.catName,
                                ""
                            ])
                            .draw();

                    //Truncate descriptions
                    $('td.rDescription').succinct({
                        size: 120
                    });
                }
            });
        });

    });
</script>
<%@include file="footer.jsp" %>