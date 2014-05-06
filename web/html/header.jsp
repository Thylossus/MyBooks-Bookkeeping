<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="sys" uri="/WEB-INF/tlds/system" %>
<!DOCTYPE html>
<%-- 
    Document   : header
    Created on : 22.04.2014, 08:26:29
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>
<!--
Copyright (C) 2014 Tobias Kahse <tobias.kahse@outlook.com>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
-->
<html>
    <head>
        <title>
            <c:out default="MyBooks - Bookkeeping" value="${requestScope.title}" />
        </title>
        <c:set value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}" var="baseURL" />
        <c:set value="${fn:substringBefore(fn:substringAfter(pageContext.request.servletPath, '/html/'), '.jsp')}" var="pageName" />
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width">
        <link type="text/css" rel="stylesheet" href="${baseURL}/css/bootstrap.min.css" />
        <link type="text/css" rel="stylesheet" href="${baseURL}/css/bootstrap-theme.min.css" />
        <link type="text/css" rel="stylesheet" href="${baseURL}/css/theme.css" />
        <link type="text/css" rel="stylesheet" href="${baseURL}/css/datatables.css" />
        <link type="text/css" rel="stylesheet" href="${baseURL}/css/${pageName}.css" />
    </head>
    <body>
        
        <c:catch>
            <sys:MainMenu/>
        </c:catch>        