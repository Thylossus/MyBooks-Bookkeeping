<%-- 
    Document   : balancesheets
    Created on : 06.05.2014, 17:37:13
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>

<%@page contentType="text/xml" pageEncoding="UTF-8" %>
<%@taglib prefix="sys" uri="/WEB-INF/tlds/system" %>

<!DOCTYPE data [
    <!ELEMENT data (success, balanceSheetList)>
    <!ELEMENT success (#PCDATA)>
    <!ELEMENT balanceSheetList (balanceSheet*)>
    <!ELEMENT balanceSheet (id, title, dateOfLastChange, dateOfCreation)>
    <!ELEMENT id (#PCDATA)>
    <!ELEMENT title (#PCDATA)>
    <!ELEMENT dateOfLastChange (#PCDATA)>
    <!ELEMENT dateOfCreation (#PCDATA)>
    ]>

    <data>
        <success>true</success>
        <balanceSheetList>
            <sys:DataListOverview data="${requestScope.dataList}" order="${requestScope.orderby}" type="BalanceSheet">
                <balanceSheet>
                    <id>${bsId}</id>
                    <title>${bsTitle}</title>
                    <dateOfLastChange>${bsEdited}</dateOfLastChange>
                    <dateOfCreation>${bsCreated}</dateOfCreation>
                </balanceSheet>
            </sys:DataListOverview>
        </balanceSheetList>
    </data>

