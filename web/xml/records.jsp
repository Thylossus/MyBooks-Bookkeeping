<%-- 
    Document   : reports
    Created on : 06.05.2014, 21:51:07
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>


<%@page contentType="text/xml" pageEncoding="UTF-8" %>
<%@taglib prefix="sys" uri="/WEB-INF/tlds/system" %>

<!DOCTYPE data [
    <!ELEMENT data (success, recordList)>
    <!ELEMENT success (#PCDATA)>
    <!ELEMENT recordList (record*)>
    <!ELEMENT record (id, title, description, amount, recordDate, catName)>
    <!ELEMENT id (#PCDATA)>
    <!ELEMENT title (#PCDATA)>
    <!ELEMENT description (#PCDATA)>
    <!ELEMENT amount (#PCDATA)>
    <!ELEMENT recordDate (#PCDATA)>
    <!ELEMENT catName (#PCDATA)>
    ]>

    <data>
        <success>true</success>
        <recordList>
            <sys:DataListOverview data="${requestScope.dataList}" type="Record">
                <record>
                    <id>${rId}</id>
                    <title>${rTitle}</title>
                    <description>${rDescription}</description>
                    <amount>${rAmount}</amount>
                    <recordDate>${rDate}</recordDate>
                    <catName>${rCatName}</catName>
                </record>
            </sys:DataListOverview>
        </recordList>
    </data>

