<%-- 
    Document   : yearoverview
    Created on : 02.05.2014, 23:48:52
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3>
                Year Overview
                <small>Have a look at the average income, expense, and overall amount for each month of the year.</small>
            </h3>
        </div>
        <div class="panel-body">
            <div id="chart"></div>
        </div>
        
        <jsp:useBean class="beans.Date" id="year" />
        <c:if test="${requestScope.selectedYear ne null}">
            <jsp:setProperty name="year" property="calendar" value="${requestScope.selectedYear}" />
        </c:if>

        <div class="panel-footer">
            <div class="row">
                <div class="col-md-4 pull-right">
                    <div class="input-group input-group-sm year-selection">
                        <span class="input-group-btn">
                            <button id="decreaseYear" class="btn btn-primary" type="button"><span class="glyphicon glyphicon-chevron-left"></span></button>
                        </span>
                        <input type="text" id="year" class="form-control" readonly  value="${year.year}"/>
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
<script type="text/javascript">

$(document).ready(function(){
    //Year selection
    $('button').click(function(){
        var currentYear = $('#year').val();
        
        if ($(this).attr("id") == "increaseYear") {
            currentYear++;
        } else {
            currentYear--;
        }
        
        location.href = "/MyBooks-Bookkeeping/bsm/viewreport/report-yearoverview/year-" + currentYear;
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
        <c:forEach var="i" begin="0" end="11">
            {
                "month": AmCharts.monthNames[<c:out value="${i}" />],
                "income": <c:out value="${requestScope.monthIncomeAverage[i]}" />,
                "expenses": <c:out value="${requestScope.monthExpenseAverage[i]}" />,
                "overall": <c:out value="${requestScope.monthOverallAverage[i]}" />
            }

            <c:if test="${i lt 11}">
                                    ,
            </c:if>
        </c:forEach>];
    chart.valueAxes = [{
            "axisAlpha": 0,
            "position": "bottom"
    }];
    chart.startDuration = 1;
    chart.rotate = false;
    chart.categoryField = "month";
    chart.categoryAxis.gridPosition = "start";
    chart.categoryAxis.position = "left";
    
    var incomeGraph = new AmCharts.AmGraph();
    incomeGraph.balloonText = "Income:[[value]]";
    incomeGraph.fillAlphas = 0.8;
    incomeGraph.lineAlpha = 0.2;
    incomeGraph.lineColor = "#00b615";
    incomeGraph.title = "Income";
    incomeGraph.type = "column";
    incomeGraph.valueField = "income";
    var expenseGraph = new AmCharts.AmGraph();
    expenseGraph.balloonText = "Expenses:[[value]]";
    expenseGraph.fillAlphas = 0.8;
    expenseGraph.lineAlpha = 0.2;
    expenseGraph.lineColor = "#ff261c";
    expenseGraph.title = "Expenses";
    expenseGraph.type = "column";
    expenseGraph.valueField = "expenses";
    var overallGraph = new AmCharts.AmGraph();
    overallGraph.balloonText = "Overall:[[value]]";
    overallGraph.fillAlphas = 0.8;
    overallGraph.lineAlpha = 0.2;
    overallGraph.lineColor = "#999999";
    overallGraph.title = "Overall";
    overallGraph.type = "column";
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