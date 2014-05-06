<%-- 
    Document   : monthoverview
    Created on : 02.05.2014, 23:48:52
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3>
                Month Overview
                <small>Have a look at the average income, expense, and overall amount for each day of the month.</small>
            </h3>
        </div>
        <div class="panel-body">
            <div id="chart"></div>
        </div>
        
        <jsp:useBean class="beans.Date" id="month" />
        <c:if test="${requestScope.selectedMonth ne null}">
            <jsp:setProperty name="month" property="calendar" value="${requestScope.selectedMonth}" />
        </c:if>

        <div class="panel-footer">
            <div class="row">
                <div class="col-md-4 pull-right">
                    <div class="input-group input-group-sm month-selection">
                        <span class="input-group-btn">
                            <button id="decreaseMonth" class="btn btn-primary" type="button"><span class="glyphicon glyphicon-chevron-left"></span></button>
                        </span>
                        <input type="text" id="month" class="form-control" readonly data-value="${month.month}"/>
                        <span class="input-group-btn">
                            <button id="increaseMonth" class="btn btn-primary" type="button"><span class="glyphicon glyphicon-chevron-right"></span></button>
                        </span>
                    </div>
                    <div class="input-group input-group-sm year-selection monthoverview-year-selection">
                        <span class="input-group-btn">
                            <button id="decreaseYear" class="btn btn-primary" type="button"><span class="glyphicon glyphicon-chevron-left"></span></button>
                        </span>
                        <input type="text" id="year" class="form-control" readonly  value="${month.year}"/>
                        <span class="input-group-btn">
                            <button id="increaseYear" class="btn btn-primary" type="button"><span class="glyphicon glyphicon-chevron-right"></span></button>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../jslibraries.jsp" />
<%-- Place JavaScript here (between inclusion of jslibraries.jsp and footer.jsp)! --%>
<script src="/MyBooks-Bookkeeping/js/amcharts/amcharts.js" type="text/javascript"></script>
<script src="/MyBooks-Bookkeeping/js/amcharts/serial.js" type="text/javascript"></script>
<script type="text/javascript" src="/MyBooks-Bookkeeping/js/months.js"></script>
<script type="text/javascript">

$(document).ready(function(){
    //Init month selection
    $('#month').val(MONTH.monthByValue($('#month').data("value")).name);
    
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

        //Add 1 to month for human readability
        location.href = "/MyBooks-Bookkeeping/bsm/viewreport/report-monthoverview/year-" + year + "/month-" + (month.value + 1);
    });
    
    var $chart = $('#chart');
    var width = $chart.parent().width();
    var height = (1/1.618) * width;
    
    //Init chart
    $chart.attr("width", width).width(width);
    $chart.attr("height", height).height(height);
    var chart = new AmCharts.AmSerialChart();
    chart.type = "serial";
    chart.theme = "none";
    chart.columnWidth = 0.6;
    chart.columnSpacing = 5;
    chart.dataProvider = [
        <c:forEach var="i" begin="0" end="${requestScope.lastDayOfMonth}">
            {
                "day": "${i}",
                "income": 
                <c:choose>
                    <c:when test="${requestScope.dayIncomeAverage[i] ne null}">
                        <c:out value="${requestScope.dayIncomeAverage[i]}" />
                    </c:when>
                    <c:otherwise>
                                    0
                    </c:otherwise>
                </c:choose>,
                "expenses": 
                <c:choose>
                    <c:when test="${requestScope.dayExpenseAverage[i] ne null}">
                        <c:out value="${requestScope.dayExpenseAverage[i]}" />
                    </c:when>
                    <c:otherwise>
                                    0
                    </c:otherwise>
                </c:choose>,
                "overall": 
                <c:choose>
                    <c:when test="${requestScope.dayOverallAverage[i] ne null}">
                        <c:out value="${requestScope.dayOverallAverage[i]}" />
                    </c:when>
                    <c:otherwise>
                                    0
                    </c:otherwise>
                </c:choose>                
            }
            <c:if test="${i lt requestScope.lastDayOfMonth}">
                                ,
            </c:if>
        </c:forEach>
    ];
    chart.valueAxes = [{
            "axisAlpha": 0,
            "position": "bottom"
    }];
    chart.startDuration = 1;
    chart.categoryField = "day";
    chart.categoryAxis.gridPosition = "start";
    chart.categoryAxis.position = "left";
    
    var incomeGraph = new AmCharts.AmGraph();
    incomeGraph.balloonText = "Income:[[value]]";
    incomeGraph.bullet = "round";
    incomeGraph.bulletSize = 2;
    incomeGraph.fillAlphas = 0.5;
    incomeGraph.lineAlpha = 1;
    incomeGraph.lineColor = "#00b615";
    incomeGraph.title = "Income";
    incomeGraph.type = "line";
    incomeGraph.valueField = "income";
    var expenseGraph = new AmCharts.AmGraph();
    expenseGraph.balloonText = "Expenses:[[value]]";
    expenseGraph.bullet = "round";
    expenseGraph.bulletSize = 2;
    expenseGraph.fillAlphas = 0.5;
    expenseGraph.lineAlpha = 1;
    expenseGraph.lineColor = "#ff261c";
    expenseGraph.title = "Expenses";
    expenseGraph.type = "line";
    expenseGraph.valueField = "expenses";
    var overallGraph = new AmCharts.AmGraph();
    overallGraph.balloonText = "Overall:[[value]]";
    overallGraph.bullet = "round";
    overallGraph.bulletSize = 2;
    overallGraph.fillAlphas = 0.5;
    overallGraph.lineAlpha = 1;
    overallGraph.lineColor = "#999999";
    overallGraph.title = "Overall";
    overallGraph.type = "line";
    overallGraph.valueField = "overall";
    
    chart.addGraph(incomeGraph);
    chart.addGraph(expenseGraph);
    chart.addGraph(overallGraph);
    
    chart.write("chart");
    
    //Handle responsive behavior
    $(window).resize(function() {
        width = $chart.parent().width();
        height = (1 / 2) * width;
        $chart.attr("width", width).width(width);
        $chart.attr("height", height).height(height);
        chart.validateNow();
    });
});

    

</script>