<%-- 
    Document   : userprofile
    Created on : 01.05.2014, 19:07:16
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="header.jsp" %>

<jsp:useBean class="database.User" id="user" scope="session" />

<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3>
                User Profile
                <small>
                    View your profile, change your data or purchase a premium membership.
                </small>
            </h3>
        </div>
        <div class="panel-body">
            <h3>Your data</h3>
            <div class="row">
                <div class="col-xs-2">
                    <span class="text-primary pull-right">Name</span>
                </div>
                <div class="col-xs-10">
                    <c:out value="${user.firstname}" /> <c:out value="${user.lastname}" />
                </div>
            </div>
            <div class="row">
                <div class="col-xs-2">
                    <span class="text-primary pull-right">E-Mail</span>
                </div>
                <div class="col-xs-10">
                    <c:out value="${user.mail}" />
                </div>
            </div>
            <div class="row">
                <div class="col-xs-2">
                    <span class="text-primary pull-right">Last signed in</span>
                </div>
                <div class="col-xs-10">
                    <fmt:formatDate value="${user.lastSignInDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                </div>
            </div>
            <hr />
            <h3>Change your name</h3>
            <div class="row">
                <form class="form-horizontal" action="changename" method="POST">

                    <div class="form-group">
                        <label class="control-label col-xs-2" for="firstname">Forename</label>
                        <div class="controls col-xs-5">
                            <input type="text" placeholder="Enter your forename." id="firstname" name="firstname" value="${user.firstname}" class="form-control" required>
                            <p class="help-block"></p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-xs-2" for="lastname">Surname</label>
                        <div class="controls col-xs-5">
                            <input type="text" placeholder="Enter your surname." id="lastname" name="lastname" value="${user.lastname}" class="form-control" required>
                            <p class="help-block"></p>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-5"></div>
                        <div class="col-xs-2">
                            <button type="submit" name="submit" class="btn btn-primary pull-right">Change Name</button>
                        </div>
                    </div>

                </form>
            </div>
            <hr />
            <h3>Change your password</h3>
            <div class="row">
                <form class="form-horizontal" action="changepassword" method="POST">

                    <div class="form-group">
                        <label class="control-label col-xs-2" for="password">Password</label>
                        <div class="controls col-xs-5">
                            <input type="password" placeholder="Please enter a new password." id="password" name="password" class="form-control" required>
                            <p class="help-block"></p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-xs-2" for="re-password">Retyped Password</label>
                        <div class="controls col-xs-5">
                            <input type="password" placeholder="Please re-enter the password" id="re-password" name="re-password" class="form-control" required>
                            <p class="help-block"></p>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-5"></div>
                        <div class="col-xs-2">
                            <button type="submit" name="submit" class="btn btn-primary pull-right">Change Password</button>
                        </div>
                    </div>

                </form>
            </div>
            <c:if test="${user.userType eq 'PREMIUM_USER' or user.userType eq 'STANDARD_USER'}">
                <hr />
                <c:choose>
                    <c:when test="${user.userType eq 'PREMIUM_USER'}">
                        <h3>Your premium membership</h3>
                        <div class="row">
                            <div class="col-xs-12">
                                Your premium membership expires on <c:out value="${requestScope.lastExpirationDate}" />. 
                                If you want to extend your premium membership click <a href="#extendForm" data-toggle="collapse">here</a>.
                            </div>
                        </div>
                        <div class="row collapse" id="extendForm">
                        </c:when>
                        <c:otherwise>
                            <h3>Purchase a premium membership <small>Save money by purchasing a longer period!</small></h3>
                            <div class="row">
                            </c:otherwise>
                        </c:choose>

                        <form class="form-horizontal" action="purchasepremiummembership" method="POST" autocomplete="off">

                            <div class="form-group">
                                <label class="control-label col-xs-2" for="period">Period</label>
                                <div class="controls col-xs-5">
                                    <select name="period" id="period" class="form-control" required>
                                        <option value="1">One month (&pound;5.00)</option>
                                        <option value="3">Three months (&pound;12.00)</option>
                                        <option value="6">Six months (&pound;20.00)</option>
                                        <option value="12">Twelve months (&pound;35.00)</option>
                                    </select>
                                    <p class="help-block"></p>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-xs-2" for="cFirstname">Cardholder Forename</label>
                                <div class="controls col-xs-5">
                                    <input type="text" placeholder="Please enter the cardholder's forename." value="${user.firstname}" name="cFirstname" id="cFirstname" class="form-control" required>
                                    <p class="help-block"></p>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-xs-2" for="cLastname">Cardholder Surname</label>
                                <div class="controls col-xs-5">
                                    <input type="text" placeholder="Please enter the cardholder's surname." value="${user.lastname}" name="cLastname" id="cLastname" class="form-control" required>
                                    <p class="help-block"></p>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-xs-2" for="creditCardType">Credit Card Type</label>
                                <div class="controls col-xs-5">
                                    <select name="creditCardType" id="creditCardType" class="form-control" required>
                                        <option value="1">Visa</option>
                                        <option value="2">Mastercard</option>
                                        <option value="3">American Express</option>
                                    </select>
                                    <p class="help-block"></p>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-xs-2" for="creditCardNumber">Credit Card Number</label>
                                <div class="controls col-xs-5">
                                    <input type="text" placeholder="Enter a credit card number." name="creditCardNumber" id="creditCardNumber" class="form-control" required>
                                    <p class="help-block"></p>
                                </div>
                            </div>

                            <div class="form-group">
                                <div class="col-xs-5"></div>
                                <div class="col-xs-2">
                                    <button type="submit" name="submit" class="btn btn-primary pull-right">Purchase</button>
                                </div>
                            </div>

                        </form>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <%@include file="jslibraries.jsp" %>
    <%-- Place JavaScript here (between inclusion of jslibraries.jsp and footer.jsp)! --%>
    <%@include file="footer.jsp" %>