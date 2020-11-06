package Servlets;

import dao.DB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "Servlets.RegisterReaderServlet")
public class RegisterReaderServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DB db = new DB();
        int id = Integer.parseInt(request.getParameter("reader_id"));
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String errorMessage = db.addReader(id, username, password, address, phone);
        request.setAttribute("errorMessage", errorMessage);
        request.setAttribute("crud", "c");
        request.setAttribute("registerReaderPage", "registerReaderPage");
        request.getRequestDispatcher("registerReader.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("registerReader.jsp").forward(request, response);
    }
}


