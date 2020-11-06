<%--
  Created by IntelliJ IDEA.
  User: w2
  Date: 29.10.2020
  Time: 19:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Reader Page</title>
    <link rel="stylesheet" href="css/form.css">
    <style>
        body {
            background-image: url("image/library.jpg");
        }
    </style>

    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.min.js"
            type="text/javascript"></script>
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
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

    $(document).ready(function () {
        $(document).on("click", "#recommendationButton", function () {
            $.get("Reader", function (responseText) {
                $("#recommendation").text(responseText);
            });
        });
    });
</script>

<body>
<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect("index.jsp");
    }
%>
<center><h1 style="color:greenyellow;">Welcome Reader! Choose your book</h1></center>
<div style="margin: auto; width: 40%; border: 5px solid green; padding: 10px; background-color: aquamarine">
    <input type="text" id="searchId" style="width: 80%;" placeholder="book or author name" name="search">
    <input type="button" style="width: 15%;" name="search" value="search" onclick="searchBook()">
    <center><input type="button" id="recommendationButton" style="width: 30%;" name="recommendationButton"
                   value="Recommendation"></center>
    <p type="text" id="recommendation" name="recommendation"></p>

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
                        <h3 style="color: #4CAF50"><c:out
                                value="The New Book Was Successfully Added To Your Borrowed List"/></h3>
                    </c:when>
                </c:choose>
            </c:if>
        </c:when>
    </c:choose>

    <form action="${pageContext.request.contextPath}/Reader" method="post">
        <table style="border-collapse: collapse; border: 3px solid black; width: 100%" id="book-">
            <tr>
                <td>
                    <input style="width: 100%" type="hidden" id="id" name="bookId" value=""/>&nbsp;&nbsp;
                    <input type="submit" name="submit" value="borrow">&nbsp;&nbsp;
                </td>
                <td style="width:30%" rowspan="6"><img id="bookImage" src="" alt="book poster" width="128px"
                                                       height="192px"/></td>
            </tr>
            <tr>
                <td><input style="width: 100%" type="text" id="bookName" name="bookName" placeholder="book name"
                           value=""/></td>
            </tr>
            <tr>
                <td><input style="width: 100%" type="text" id="bookAuthor" name="bookAuthor" placeholder="book author"
                           value=""/></td>
            </tr>
            <tr>
                <td><input style="width: 100%" type="text" id="countOfCopies" name="countOfCopies" placeholder="amount"
                           value=""/></td>
            </tr>
            <tr>
                <td><input style="width: 100%" type="url" id="imageURL" name="imageURL" placeholder="image URL"
                           value=""/></td>
            </tr>
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
    <c:forEach var="book" items="${bookList}">
        <form action="${pageContext.request.contextPath}/Reader" method="post">
            <table style="border-collapse: collapse; border: 1px solid black; width: 100%"
                   id="book-<c:out value="${book.id}"/>">
                <tr>
                    <td>
                        <input style="width: 100%" type="hidden" name="bookId"
                               value="<c:out value="${book.id}"/> }}">
                        <input type="submit" name="submit" value="borrow">&nbsp;&nbsp;
                    </td>
                    <td style="width:30%" rowspan="7"><img src="<c:out value="${book.imageURL}"/>"
                                                           width="128px"
                                                           height="215px"/></td>
                </tr>
                <tr>
                    <td><input style="width: 100%" type="number" name="bookId" placeholder="book ID"
                               value="<c:out value="${book.id}"/>"></td>
                </tr>
                <tr>
                    <td><input style="width: 100%" type="text" name="bookName" placeholder="book name"
                               value="<c:out value="${book.name}"/>"></td>
                </tr>
                <tr>
                    <td><input style="width: 100%" type="text" name="bookAuthor" placeholder="book author"
                               value="<c:out value="${book.author}"/>"></td>
                </tr>
                <tr>
                    <td><label>Books left: <c:out value="${book.countOfCopies}"/></label><input
                            style="width: 100%" type="number" name="countOfCopies" placeholder="amount"/>
                    </td>
                </tr>
                <tr>
                    <td><input style="width: 100%" type="url" name="imageURL" placeholder="image URL"
                               value="<c:out value="${book.imageURL}"/>"></td>
                </tr>
                <tr>
                    <td><input style="width: 100%" type="text" name="isbn" placeholder="book ISBN" value="<c:out value="${book.isbn}"/>"></td>
                </tr>
            </table>
        </form>
        <br>
    </c:forEach>
</div>


</body>
</html>
