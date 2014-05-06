<%-- 
    Document   : monthoverview
    Created on : 02.05.2014, 23:48:52
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="sys" uri="/WEB-INF/tlds/system" %>

<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3>
                Top 10 Income Records
                <small>Have a look at the ten highest income records.</small>
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
                <sys:DataListOverview data="${records}" type="Record">
                    <c:choose>
                        <c:when test="${rAmount>=0.0}">
                            <c:set var="rType" value="income" />
                        </c:when>

                        <c:otherwise>
                            <c:set var="rType" value="expense" />
                        </c:otherwise>
                    </c:choose>

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
</div>


<%@include file="../jslibraries.jsp" %>
<%-- Place JavaScript here (between inclusion of jslibraries.jsp and footer.jsp)! --%>
<script type="text/javascript" src="/MyBooks-Bookkeeping/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="/MyBooks-Bookkeeping/js/datatables.js"></script>
<script type="text/javascript" src="/MyBooks-Bookkeeping/js/custom-currency.sorting.js"></script>
<script type="text/javascript" src="/MyBooks-Bookkeeping/js/jQuery.succinct.min.js"></script>
<script type="text/javascript" src="/MyBooks-Bookkeeping/js/months.js"></script>
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

        
        //Init data table
        $('table.table').dataTable({
            "order": [[2, "desc"]],
            "autoWidth": false,
            "paging": false,
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
            }
        });

        $('.table').DataTable();

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


    });
</script>