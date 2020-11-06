package Servlets;

import Model.Book;
import Model.Reader;
import dao.DB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Stack;

@WebServlet(name = "Servlets.ReadersInfoServlet")
public class ReadersInfoServlet extends HttpServlet {
    DB db = new DB();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String errorMessage = "ok";
        int queryResult;
        String submit = request.getParameter("submit");
        int readerId = Integer.parseInt(request.getParameter("readerId"));
        if (submit.equals("update")) {
            String username = request.getParameter("username");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            int bookAmount = Integer.parseInt(request.getParameter("amount"));
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            queryResult = db.updateReader(readerId, username, address, phone, bookAmount, bookId);
            if (queryResult == 0) {
                errorMessage = "Problems with update";
            }
            request.setAttribute("crud", "u");
        } else if (submit.equals("removeUser")) {
            queryResult = db.deleteReader(readerId);
            if (queryResult == -1) {
                errorMessage = "You can't remove admin";
            } else if (queryResult == 0) {
                errorMessage = "User has some borrowed books";
            }
            request.setAttribute("crud", "d");
        }
        request.setAttribute("errorMessage", errorMessage);
        doGet(request, response);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DB.getConnection();
        ArrayList<Book> bookList = db.read();
        Stack<Reader> readersStack = db.getAllReaders();
        request.setAttribute("bookList", bookList);
        request.setAttribute("readersStack", readersStack);
        request.getRequestDispatcher("readersInfo.jsp").forward(request, response);
    }
}
