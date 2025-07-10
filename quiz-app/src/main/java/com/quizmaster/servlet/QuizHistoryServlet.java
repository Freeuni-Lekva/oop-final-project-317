package com.quizmaster.servlet;

import DAO.QuizHistorySQLDao;
import models.QuizHistory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

@WebServlet("/quiz-history")
public class QuizHistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = (String) request.getSession().getAttribute("user");
        if (username == null) {
            response.sendRedirect("login");
            return;
        }

        Connection connection = (Connection) getServletContext().getAttribute("dbConnection");
        QuizHistorySQLDao quizHistoryDao = new QuizHistorySQLDao(connection);

        // Get user ID from database using username
        long userId = getUserIdByUsername(connection, username);

        if (userId != -1) {
            // Get quiz history for the user
            ArrayList<QuizHistory> quizHistory = quizHistoryDao.getUserQuizHistory(userId);
            request.setAttribute("quizHistory", quizHistory);
        }

        // Forward to quiz history JSP
        request.getRequestDispatcher("/QuizHistory.jsp").forward(request, response);
    }

    // Helper method to get user ID by username
    private long getUserIdByUsername(Connection connection, String username) {
        try {
            String query = "SELECT id FROM users WHERE name = ?";
            try (java.sql.PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, username);
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getLong("id");
                    }
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}