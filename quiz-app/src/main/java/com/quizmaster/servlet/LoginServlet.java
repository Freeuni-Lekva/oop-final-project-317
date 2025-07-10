package com.quizmaster.servlet;

import DAO.UserSQLDao;
import com.quizmaster.util.PasswordUtil;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get form parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Simple validation for demo purposes
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email is required");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Password is required");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }

        UserSQLDao userSQLDao = (UserSQLDao) getServletContext().getAttribute("userSqlDao");
        if (userSQLDao == null) {
            request.setAttribute("error", "Database connection error. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }
        User user = userSQLDao.getUserByEmail(email);
        if (user != null) {
            String storedHash = user.getPassHash();
            String inputHash = PasswordUtil.hashPassword(password);
            if (storedHash.equals(inputHash)) {
                request.getSession().setAttribute("user", user);
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            } else {
                request.setAttribute("error", "Invalid password");
            }
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }else{
            request.setAttribute("error", "Invalid email or password");
        }

    }
}