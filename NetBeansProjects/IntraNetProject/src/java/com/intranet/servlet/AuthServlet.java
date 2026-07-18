package com.intranet.servlet;

import com.intranet.dao.UserDAO;
import com.intranet.model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/login", "/register", "/logout"})
public class AuthServlet extends HttpServlet {

    private  UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/login".equals(path)) {
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        } else if ("/register".equals(path)) {
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        } else if ("/logout".equals(path)) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/register".equals(path)) {
            User newUser = new User(
                request.getParameter("username"),
                request.getParameter("email"),
                request.getParameter("password")
            );

            if (userDAO.register(newUser)) {
                response.sendRedirect(request.getContextPath() + "/login?success=true");
            } else {
                request.setAttribute("error", "Nom d'utilisateur ou email déjà existant.");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            }
        } 
        else if ("/login".equals(path)) {
            User user = userDAO.login(
                request.getParameter("username"),
                request.getParameter("password")
            );

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("loggedUser", user);
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                request.setAttribute("error", "Identifiants incorrects.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        }
    }
}