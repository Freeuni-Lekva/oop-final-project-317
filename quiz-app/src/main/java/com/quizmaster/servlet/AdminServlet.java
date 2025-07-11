package com.quizmaster.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DAO.UserSQLDao;
import DAO.QuizSQLDao;
import models.User;
import models.Quiz;
import java.util.ArrayList;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        // Only allow admin users
        User user = (User) session.getAttribute("user");
        Connection connection = (Connection) getServletContext().getAttribute("dbConnection");
        boolean isAdmin = user.getIfAdmin();
        if (!isAdmin) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to access this page.");
            return;
        }
        // Get total users and quizzes
        int totalUsers = 0;
        int totalQuizzes = 0;
        try {
            String userCountSql = "SELECT COUNT(*) AS count FROM users";
            try (PreparedStatement ps = connection.prepareStatement(userCountSql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        totalUsers = rs.getInt("count");
                    }
                }
            }
            String quizCountSql = "SELECT COUNT(*) AS count FROM quizzes";
            try (PreparedStatement ps = connection.prepareStatement(quizCountSql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        totalQuizzes = rs.getInt("count");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
        }
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("totalQuizzes", totalQuizzes);

        // Handle view switching
        String view = request.getParameter("view");
        if (view != null) {
            if (view.equals("users")) {
                UserSQLDao userDao = new UserSQLDao(connection);
                ArrayList<User> allUsers = userDao.getAllUsers();
                request.setAttribute("allUsers", allUsers);
                request.setAttribute("showUsers", true);
            } else if (view.equals("quizzes")) {
                QuizSQLDao quizDao = new QuizSQLDao(connection);
                ArrayList<Quiz> allQuizzes = quizDao.getAllQuizzes();
                request.setAttribute("allQuizzes", allQuizzes);
                request.setAttribute("showQuizzes", true);
            }
        }
        request.getRequestDispatcher("/admin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("user");
        Connection connection = (Connection) getServletContext().getAttribute("dbConnection");
        boolean isAdmin = user.getIfAdmin();
        if (!isAdmin) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to access this page.");
            return;
        }
        String action = request.getParameter("action");
        QuizSQLDao quizDao = new QuizSQLDao(connection);
        if ("remove-quizzes".equals(action)) {
            // Remove selected quizzes
            String[] quizIds = request.getParameterValues("quizIds");
            if (quizIds != null) {
                for (String idStr : quizIds) {
                    try {
                        long id = Long.parseLong(idStr);
                        models.Quiz quiz = quizDao.getQuiz(id);
                        if (quiz != null) quizDao.removeQuiz(quiz);
                    } catch (NumberFormatException ignored) {}
                }
            }
            // Remove single quiz if requested
            String removeSingle = request.getParameter("removeSingle");
            if (removeSingle != null && !removeSingle.isEmpty()) {
                try {
                    long id = Long.parseLong(removeSingle);
                    models.Quiz quiz = quizDao.getQuiz(id);
                    if (quiz != null) quizDao.removeQuiz(quiz);
                } catch (NumberFormatException ignored) {}
            }
            // Redirect back to all quizzes view
            response.sendRedirect("admin?view=quizzes");
            return;
        }
        if ("remove-users".equals(action)) {
            // Remove selected users
            String[] userIds = request.getParameterValues("userIds");
            UserSQLDao userDao = new UserSQLDao(connection);
            if (userIds != null) {
                for (String idStr : userIds) {
                    try {
                        long id = Long.parseLong(idStr);
                        models.User u = userDao.getUser(id);
                        if (u != null) userDao.removeUser(u);
                    } catch (NumberFormatException ignored) {}
                }
            }
            // Remove single user if requested
            String removeUserSingle = request.getParameter("removeUserSingle");
            if (removeUserSingle != null && !removeUserSingle.isEmpty()) {
                try {
                    long id = Long.parseLong(removeUserSingle);
                    models.User u = userDao.getUser(id);
                    if (u != null) userDao.removeUser(u);
                } catch (NumberFormatException ignored) {}
            }
            // Redirect back to all users view
            response.sendRedirect("admin?view=users");
            return;
        }
        // Default: redirect to admin
        response.sendRedirect("admin");
    }
} 