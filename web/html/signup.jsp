<%-- 
    Document   : singup
    Created on : 24.03.2014, 00:54:54
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>

<%@include file="header.jsp" %>

<div class="container">

    <form class="form-signup" role="form" method="POST">
        <h2 class="form-signup-heading">Please sign up</h2>
        <input type="text" name="firstname" class="form-control first-child" placeholder="Forename" required autofocus />
        <input type="text" name="lastname" class="form-control" placeholder="Surname" required />
        <input type="email" name="mail" class="form-control" placeholder="Email Address" required />
        <input type="password" name="password" class="form-control" placeholder="Password" required />
        <input type="password" name="re-password" class="form-control last-child" placeholder="Retype Password" required />
        <button class="btn btn-lg btn-primary btn-block" name="submit" type="submit">Sign up</button>
    </form>

</div> <!-- /container -->

<%@include file="jslibraries.jsp" %>
<%-- Place JavaScript here (between inclusion of jslibraries.jsp and footer.jsp)! --%>
<%@include file="footer.jsp" %>