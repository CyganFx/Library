<%--
  Created by IntelliJ IDEA.
  User: w2
  Date: 29.10.2020
  Time: 17:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Librarian Page</title>
    <style>
        body {
            background-image: url("image/library.jpg");
        }
    </style>
    <script src="http://code.jquery.com/jquery-2.2.4.js"
            type="text/javascript"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.min.js"
            type="text/javascript"></script>
</head>
<script>
    function searchBook() {
        var xhttp = new XMLHttpRequest();
        var name = document.getElementById("searchId").value;
        if (name == "") {
            document.getElementById("searchId").value = "Please Enter something...";
            return;
        }
        xhttp.onreadystatechange = function () {
            if (this.readyState === 4 && this.status === 200) {
                var bookList = JSON.parse(this.responseText);
                if (bookList.length > 0) {
                    document.getElementById("id").value = bookList[0].id;
                    document.getElementById("bookName").value = bookList[0].name;
                    document.getElementById("bookAuthor").value = bookList[0].author;
                    document.getElementById("countOfCopies").value = bookList[0].countOfCopies;
                    document.getElementById("imageURL").value = bookList[0].imageURL;
                    document.getElementById("bookImage").src = bookList[0].imageURL;
                } else {
                    document.getElementById("searchId").value = "Not found";
                }
            }
        };
        xhttp.open("POST", "${pageContext.request.contextPath}/MainServlet", true);
        xhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
        xhttp.send("submit=search&name=" + name);
    }

</script>
<body>
<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect("index.jsp");
    } else if (!session.getAttribute("username").equals("admin")) { //Librarian username
        response.sendRedirect("reader.jsp");
    }
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

<div style="margin: auto; width: 40%; border: 5px solid green; padding: 10px; background-color: aquamarine">
    <a href="${pageContext.request.contextPath}/RegisterReader"><h1>Register Reader</h1></a>
    <a href="${pageContext.request.contextPath}/ReadersInfo"><h1>Readers Info</h1></a>
    <input type="text" id="searchId" style="width: 80%;" placeholder="book or author name" name="search">
    <input type="button" style="width: 15%;" name="search" value="search" onclick="searchBook()">
    <h2>
        <c:set var="crud" value='${requestScope.crud}' />
        <c:if test="${crud != null}">
            <c:choose>
                <c:when test="${crud.charAt(1) == '1'.charAt(0) && crud.charAt(0) == 'c'.charAt(0) }">
                    <h3 style="color: #4CAF50"><c:out value="The New Book Was Successfully Added" /></h3>
                    <c:out value="You can find it in the list" />
                </c:when>
                <c:when test="${crud.charAt(1) == '1'.charAt(0) && crud.charAt(0) == 'd'.charAt(0)}">
                    <h3 style="color: #4CAF50"><c:out value="The Book Was Successfully Deleted" /></h3>
                </c:when>
                <c:when test="${crud.charAt(1) == '1'.charAt(0) && crud.charAt(0) == 'u'.charAt(0)}">
                    <h3 style="color: #4CAF50"><c:out value="The Book Was Successfully Updated" /></h3>
                </c:when>
                <c:otherwise>
                    <h3 style="color: #EF3B3A"><c:out value="Something is wrong" /></h3>
                </c:otherwise>
            </c:choose>
        </c:if>
    </h2>
    <form action="${pageContext.request.contextPath}/MainServlet" method="post">
        <table style="border-collapse: collapse; border: 3px solid black; width: 100%" id="book-">
            <tr>
                <td>
                    <input style="width: 100%" type="hidden" id="id" name="bookId" value=""/>&nbsp;&nbsp;
                    <input type="submit" name="submit" value="update">&nbsp;&nbsp;
                    <input type="submit" name="submit" value="delete">
                </td>
                <td style="width:30%" rowspan="6"><img id="bookImage" src="" alt="book poster" width="128px" height="192px" /></td>
            </tr>
            <tr><td><input style="width: 100%" type="text" id="bookName" name="bookName" placeholder="book name" value=""/></td></tr>
            <tr><td><input style="width: 100%" type="text" id="bookAuthor" name="bookAuthor" placeholder="book author" value=""/></td></tr>
            <tr><td><input style="width: 100%" type="text" id="countOfCopies" name="countOfCopies" placeholder="amount" value=""/></td></tr>
            <tr><td><input style="width: 100%" type="url" id="imageURL" name="imageURL" placeholder="image URL" value=""/></td></tr>
        </table>
    </form>

    <h3>Sort by(ID):</h3>
    <form action="Sort" method="post">
        <select name="sort">
            <option value="low-to-high">Low to high</option>
            <option value="high-to-high">high to low</option>
        </select>
        <input type="submit" value="Sort">
    </form>

    <c:forEach var="book" items="${bookList}" >
        <form action="${pageContext.request.contextPath}/MainServlet" method="post">
            <table style="border-collapse: collapse; border: 1px solid black; width: 100%" id="book-<c:out value="${book.id}"/>">
                <tr>
                    <td>
                        <input style="width: 100%" type="hidden" name="bookId" value="<c:out value="${book.id}"/>">&nbsp;&nbsp;
                        <input type="submit" name="submit" value="update">&nbsp;&nbsp;
                        <input type="submit" name="submit" value="delete">
                    </td>
                    <td style="width:30%" rowspan="7"><img src="<c:out value="${book.imageURL}"/>" width="128px" height="215px" /></td>
                </tr>
                <tr><td><input style="width: 100%" type="number" name="bookId" placeholder="book ID" value="<c:out value="${book.id}"/>"></td></tr>
                <tr><td><input style="width: 100%" type="text" name="bookName" placeholder="book name" value="<c:out value="${book.name}"/>"></td></tr>
                <tr><td><input style="width: 100%" type="text" name="bookAuthor" placeholder="book author" value="<c:out value="${book.author}"/>"></td></tr>
                <tr><td><input style="width: 100%" type="number" name="countOfCopies" placeholder="amount" value="<c:out value="${book.countOfCopies}"/>"/></td></tr>
                <tr><td><input style="width: 100%" type="url" name="imageURL" placeholder="image URL" value="<c:out value="${book.imageURL}"/>"></td></tr>
                <tr><td><input style="width: 100%" type="text" name="isbn" placeholder="book ISBN" value="<c:out value="${book.isbn}"/>"></td></tr>
            </table>
        </form>
        <br>
    </c:forEach>
    <h1>add a new book</h1>
    <form action="${pageContext.request.contextPath}/MainServlet" method="post">
        <table style="width: 100%">
            <tr><td><input style="width: 100%" type="number" name="bookId" placeholder="book ID"></td></tr>
            <tr><td><input style="width: 100%" type="text" name="bookName" placeholder="book name"></td></tr>
            <tr><td><input style="width: 100%" type="text" name="bookAuthor" placeholder="author"></td></tr>
            <tr><td><input style="width: 100%" type="number" name="countOfCopies" placeholder="amount"/></td></tr>
            <tr><td><input style="width: 100%" type="url" name="imageURL" placeholder="image URL"></td></tr>
            <tr><td><input style="width: 100%" type="text" name="isbn" placeholder="book ISBN"></td></tr>
            <tr><td><input type="submit" name="submit" value="add"></td></tr>
        </table>
    </form>
</div>

</body>
</html>
