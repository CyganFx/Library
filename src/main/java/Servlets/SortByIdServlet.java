package Servlets;


import Model.Book;
import dao.DB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "Servlets.SortByIdServlet")
public class SortByIdServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DB db = new DB();
        DB.getConnection();
        ArrayList<Book> bookList = db.read();
        Book book = new Book();
        String price = request.getParameter("sort");
        if (price.equals("low-to-high")) {
            bookList = book.lowToHigh(bookList);
        } else {
            bookList = book.highToLow(bookList);
        }
        request.removeAttribute("bookList");
        request.setAttribute("bookList", bookList);
        HttpSession session = request.getSession(true);
        if (session.getAttribute("username").equals("admin")) {
            request.getRequestDispatcher("librarian.jsp").forward(request,response);
        } else {
            request.getRequestDispatcher("reader.jsp").forward(request, response);
        }
    }
}


