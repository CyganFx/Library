package Servlets;

import Model.Book;
import dao.DB;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "Servlets.LoginServlet")
public class LoginServlet extends HttpServlet {
    DB db = new DB();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (DB.checkReader(username, password)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("username", username);
            Cookie websiteVisitCounter = new Cookie("counter", "1");
            websiteVisitCounter.setMaxAge(3600);
            response.addCookie(websiteVisitCounter);
            response.sendRedirect(request.getContextPath() + "/MainServlet");
        } else {
            request.setAttribute("errorMessage", "Incorrect username or password");
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
            requestDispatcher.forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Connection connection = DB.getConnection();
            ArrayList<Book> bookList = db.read(connection);
            connection.close();
            request.setAttribute("bookList", bookList);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
