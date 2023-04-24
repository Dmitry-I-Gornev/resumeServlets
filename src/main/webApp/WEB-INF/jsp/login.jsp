<<%@ page import="ru.inock.webServletResime.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- %@ include file="header.jsp" % -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML lang="ru">
<HEAD>
    <TITLE><%= (String) request.getAttribute("title") %>
    </TITLE>
    <META content="text/html; charset=utf-8" http-equiv=Content-Type>
    <meta name="Other.Language" content="Russian">
    <meta name="distribution" content="global">
    <meta name="rating" content="General">
    <meta name="ROBOTS" content="INDEX,FOLLOW">
    <META http-equiv="Content-Script-Type" content="text/javascript">
</HEAD>
<BODY>

<h1>Пожалуйста, авторизуйтесь</h1>
<form action="login" method="Post" id="autorisation">
    <input type="hidden" name="section" value="LogIn">
   LogIn: <input type="text" title="LogIn" placeholder="LogIn" size="20"
           maxlength="50" name="LogIn" value=""    /><br>
    Passowrd: <input name="userPassword" type="password"><br>
    <input type="submit">

</form>
<%@ include file="footer.jsp" %>