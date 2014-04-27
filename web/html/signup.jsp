<%-- 
    Document   : singup
    Created on : 24.03.2014, 00:54:54
    Author     : Tobias Kahse <tobias.kahse@outlook.com>
--%>

<%@include file="header.jsp" %>

<div class="container">
    <form method="POST" action="">
        <label for="firstname">Forename</label>
        <input type="text" id="firstname" name="firstname" />
        <br />
        <label for="lastname">Surname</label>
        <input type="text" id="lastname" name="lastname" />
        <br />
        <label for="mail">E-Mail Address</label>
        <input type="email" id="mail" name="mail" />
        <br />
        <label for="password">Password</label>
        <input type="password" id="password" name="password" />
        <br />
        <label for="re-password">Password</label>
        <input type="password" id="re-password" name="re-password" />
        <br />
        <input type="submit" name="submit" value="Sign up" />
    </form>
</div>

<%@include file="footer.jsp" %>