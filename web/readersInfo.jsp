<%@ page import="java.io.PrintWriter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Readers Info</title>
    <link rel="stylesheet" href="css/display.css">
    <style>
        body {
            background-image: url("image/library.jpg");
        }
    </style>
</head>
<body>

<%
    Cookie[] cookies = request.getCookies();
    PrintWriter pw = response.getWriter();
    for (Cookie cookie : cookies) {
        if (cookie.getName().equals("counter")) {
            pw.println("This session visited website " + cookie.getValue() + " times (Cookie)");
        }
    }
%>

<a href="${pageContext.request.contextPath}/MainServlet"><h3>Go Back</h3></a>

<c:set var="errorMessage" value='${errorMessage}'/>
<c:choose>
    <c:when test="${!errorMessage.equals('ok')}">
        <center><h3 style="color: red"><c:out value="${errorMessage}"/></h3></center>
    </c:when>
    <c:when test="${errorMessage.equals('ok')}">
        <c:set var="crud" value='${crud}'/>
        <c:if test="${crud != null}">
            <c:choose>
                <c:when test="${crud.charAt(0) == 'u'.charAt(0) }">
                    <h3 style="color: #4CAF50"><c:out
                            value="User was updated successfully"/></h3>
                </c:when>
                <c:when test="${crud.charAt(0) == 'd'.charAt(0) }">
                    <h3 style="color: #4CAF50"><c:out
                            value="User was removed successfully"/></h3>
                </c:when>
            </c:choose>
        </c:if>
    </c:when>
</c:choose>


<c:forEach items="${readersStack}" var="reader">
    <div class="table-users">
        <div class="header">User ID: <c:out value="${reader.getReaderId()}"/></div>
        <table style="width:100%" cellspacing="0">
            <tr>
                <th>Username</th>
                <th>Address</th>
                <th>Phone</th>
                <th>Borrowed Amount</th>
                <th>Book ID</th>
                <th>Update</th>
                <th>Remove User</th>
            </tr>
            <tr>
                <form action="ReadersInfo" method="post">
                    <td><input type="text" name="username" value="<c:out value="${reader.getUsername()}"/>"/></td>
                    <td><input type="text" name="address" value="<c:out value="${reader.getAddress()}"/>"/></td>
                    <td><input type="text" name="phone" value="<c:out value="${reader.getPhone()}"/>"/></td>
                    <td><input type="number" name="amount" value="<c:out value="${reader.getBorrowedAmount()}"/>"/></td>
                    <td><p><c:out value="${reader.getBorrowedBookId()}"/></p></td>
                    <input type="hidden" name="readerId" value="<c:out value="${reader.getReaderId()}"/>"/>
                    <input type="hidden" name="bookId" value="<c:out value="${reader.getBorrowedBookId()}"/>"/>
                    <td><input type="submit" name="submit" value="update"></td>
                    <td><input type="submit" name="submit" value="removeUser"></td>
                </form>
            </tr>
        </table>
    </div>
</c:forEach>
</body>
</html>
