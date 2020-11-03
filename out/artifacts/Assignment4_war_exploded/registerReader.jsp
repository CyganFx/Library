<%--
  Created by IntelliJ IDEA.
  User: Думан
  Date: 01.11.2020
  Time: 15:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Register Reader</title>
    <link rel="stylesheet" href="css/form.css">
    <style>
        body {
            background-image: url("image/library.jpg");
        }
    </style>
</head>
<body>

<%
    Cookie[] cookies = request.getCookies();
    int counter;
    for (Cookie cookie : cookies) {
        if (cookie.getName().equals("counter")) {
            counter = Integer.parseInt(cookie.getValue());
            counter++;
            cookie.setValue(counter + "");
            response.addCookie(cookie);
        }
    }
%>

<a href="${pageContext.request.contextPath}/MainServlet"><h3 style="color: dodgerblue">Go Back</h3></a>

<div class="form">
    <center><h2>Register User</h2></center>
    <c:set var="errorMessage" value='${errorMessage}'/>
    <c:choose>
        <c:when test="${!errorMessage.equals('ok')}">
            <center><h3 style="color: red"><c:out value="${errorMessage}"/></h3></center>
        </c:when>
        <c:when test="${errorMessage.equals('ok')}">
            <c:set var="crud" value='${crud}'/>
            <c:if test="${crud != null}">
                <c:choose>
                    <c:when test="${crud.charAt(0) == 'c'.charAt(0) }">
                        <h3 style="color: #4CAF50"><c:out value="The New Reader Was Successfully Added"/></h3>
                        <c:out value="You can find it in the list"/>
                    </c:when>
                    <c:otherwise>
                        <center><h3 style="color: #EF3B3A"><c:out value="Something is wrong"/></h3></center>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </c:when>
    </c:choose>
    <form action="RegisterReader" method="post" class="login-form">
        <input type="number" name="reader_id" placeholder="reader ID"/>
        <input type="text" name="username" placeholder="username"/>
        <input type="password" name="password" placeholder="password"/>
        <input type="text" name="address" placeholder="address"/>
        <input type="phone" name="phone" placeholder="phone number"/>
        <input type="submit" name="submit" value="Register">
    </form>
</div>

</body>
</html>
