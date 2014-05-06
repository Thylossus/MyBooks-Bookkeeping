<%-- 
    Document   : user
    Created on : 06.05.2014, 14:44:16
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>

<%@page pageEncoding="UTF-8" contentType="text/xml" %>

<!DOCTYPE data [
    <!ELEMENT data (success, user)>
    <!ELEMENT success (#PCDATA)>
    <!ELEMENT user (mail, lastname, firstname, userType)>
    <!ELEMENT mail (#PCDATA)>
    <!ELEMENT lastname (#PCDATA)>
    <!ELEMENT firstname (#PCDATA)>
    <!ELEMENT userType (#PCDATA)>
    ]>

    <data>
        <success>true</success>
        <user>
            <mail>${sessionScope.user.mail}</mail>
            <lastname>${sessionScope.user.lastname}</lastname>
            <firstname>${sessionScope.user.firstname}</firstname>
            <userType>${sessionScope.user.userType.identifier}</userType>
        </user>
    </data>