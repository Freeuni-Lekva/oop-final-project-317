package com.quizmaster.servlet;

import models.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet("/achievements")
public class AchievementsServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        Long userId = user.getId();
        Connection connection = (Connection) getServletContext().getAttribute("dbConnection");

        try {
            // Get number of quizzes created
            String quizzesCreatedQuery = "SELECT COUNT(*) as count FROM quizzes WHERE created_by = ?";
            try (PreparedStatement ps = connection.prepareStatement(quizzesCreatedQuery)) {
                ps.setLong(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        request.setAttribute("quizzesCreated", rs.getInt("count"));
                    }
                }
            }

            // Get number of quizzes taken
            String quizzesTakenQuery = "SELECT COUNT(*) as count FROM quiz_history WHERE user_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(quizzesTakenQuery)) {
                ps.setLong(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        request.setAttribute("quizzesTaken", rs.getInt("count"));
                    }
                }
            }

            // Get number of best scores (highest score in a quiz)
            String bestScoresQuery = "SELECT COUNT(*) as count FROM quiz_results qr1 " +
                    "WHERE score = (SELECT MAX(score) FROM quiz_results qr2 WHERE qr2.quiz_id = qr1.quiz_id) " +
                    "AND user_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(bestScoresQuery)) {
                ps.setLong(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        request.setAttribute("bestScores", rs.getInt("count"));
                    }
                }
            }

            // Check if user has taken any practice mode quizzes
            String practiceModeQuery = "SELECT COUNT(*) as count FROM quiz_results WHERE user_id = ? AND is_practice_mode = true";
            try (PreparedStatement ps = connection.prepareStatement(practiceModeQuery)) {
                ps.setLong(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        request.setAttribute("practiceQuizzes", rs.getInt("count"));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
        }

        request.getRequestDispatcher("/achievements.jsp").forward(request, response);
    }
} 