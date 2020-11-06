<%--
  Created by IntelliJ IDEA.
  User: w2
  Date: 26.10.2020
  Time: 20:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="css/form.css">
</head>
<body>
<center><h1>Welcome! Enter the data</h1></center>

<c:if test="${errorMessage != null}">
    <c:out value="${errorMessage}"/>
</c:if>


<div class="login-page">
    <div class="form">
        <form action="LoginServlet" method="post" class="login-form">
            <input type="text" name="username" placeholder="username"/>
            <input type="password" name="password" placeholder="password"/>
            <input type="submit" name="submit" value="Login">
        </form>
    </div>
</div>
</body>
</html>
