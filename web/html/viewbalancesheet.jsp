<%-- 
    Document   : viewbalancesheet
    Created on : 01.05.2014, 18:25:43
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
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3>
                View Balance Sheet
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


        <jsp:useBean class="beans.Date" id="today">
            <jsp:setProperty name="today" property="format" value="yyyy-MM-dd" />
        </jsp:useBean>

        <div class="panel-footer">
            <div class="row">
                <div class="col-md-8">
                    <button type="button" class="btn btn-primary modal-verification-required" data-target="#rView">View Record Details</button>
                </div>
                <div class="col-md-4">
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
                </div>
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
                //Copy record values into view
                $('#viewRecordTitle').text($('.record-selected > .rTitle').text());
                $('#viewRecordDescription').text($('.record-selected > .rDescription').data("fulltext"));
                $('#viewRecordAmount').text($('.record-selected > .rAmount').text());
                $('#viewRecordDate').text($('.record-selected > .rDate').text());
                $('#viewRecordCategory').text($('.record-selected > .rCatName').text());

                $($(this).data("target")).modal();
            } else {
                $('#warning_noRecordSelected').fadeIn();
            }
        });


    });
</script>
<%@include file="footer.jsp" %>