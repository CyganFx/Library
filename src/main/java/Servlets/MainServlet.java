package Servlets;

import Model.Book;
import com.google.gson.Gson;
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

@WebServlet(name = "Servlets.MainServlet")
public class MainServlet extends HttpServlet {
    DB db = new DB();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String submit = request.getParameter("submit");
        switch (submit) {
            case "add": {
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                String bookName = request.getParameter("bookName");
                String bookAuthor = request.getParameter("bookAuthor");
                int countOfCopies = Integer.parseInt(request.getParameter("countOfCopies"));
                String coverURL = request.getParameter("imageURL");
                String isbn = request.getParameter("isbn");
                int added = db.addBook(bookId, bookName, bookAuthor, countOfCopies, coverURL, isbn);
                request.setAttribute("crud", "c" + added);
                break;
            }
            case "delete": {
                String bookId = request.getParameter("bookId");
                int deleted = db.deleteBook(bookId);
                request.setAttribute("crud", "d" + deleted);
                break;
            }
            case "update": {
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                String bookName = request.getParameter("bookName");
                String bookAuthor = request.getParameter("bookAuthor");
                int countOfCopies = Integer.parseInt(request.getParameter("countOfCopies"));
                String coverURL = request.getParameter("imageURL");

                int updated = db.updateBook(bookId, bookName, bookAuthor, countOfCopies, coverURL);
                request.setAttribute("crud", "u" + updated);
                break;
            }
            case "search": {
                String name = request.getParameter("name");
                ArrayList<Book> bookList = db.searchReader(name);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                String json = new Gson().toJson(bookList);
                response.getWriter().write(json);
                return;
            }
            default: {
                request.setAttribute("crud", "s0");
                break;
            }
        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Connection connection = DB.getConnection();
            ArrayList<Book> bookList = db.read(connection);
            connection.close();
            request.setAttribute("bookList", bookList);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        HttpSession session = request.getSession(true);
        if (request.getAttribute("registerReaderPage") != null) {
            request.getRequestDispatcher("registerReader.jsp");
        }
        if (session.getAttribute("username").equals("admin")) { //Librarian username
            request.getRequestDispatcher("librarian.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("reader.jsp").forward(request, response);
        }
    }
}
