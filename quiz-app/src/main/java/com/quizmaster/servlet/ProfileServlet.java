package com.quizmaster.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import DAO.UserSQLDao;
import DAO.QuizHistorySQLDao;
import DAO.NotificationsSQLDAO;
import models.User;
import models.QuizHistory;
import models.Notification;
import java.util.ArrayList;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        String username = user.getName();
        long userId = user.getId();
        Connection connection = (Connection) getServletContext().getAttribute("dbConnection");
        
        try {
            // Get user details from database
            String userQuery = "SELECT name, email, created_at FROM users WHERE name = ?";
            try (PreparedStatement ps = connection.prepareStatement(userQuery)) {
                ps.setString(1, username);
                
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        request.setAttribute("userEmail", rs.getString("email"));
                        request.setAttribute("userCreatedAt", rs.getTimestamp("created_at"));
                    }
                }
            }

            // Get user statistics
            getQuizStatistics(request, connection, username);

            // Fetch recent activities
            QuizHistorySQLDao quizHistoryDao = new QuizHistorySQLDao(connection);
            ArrayList<QuizHistory> recentActivities = quizHistoryDao.getUserQuizHistory(userId);
            if (recentActivities.size() > 5) {
                recentActivities = new ArrayList<>(recentActivities.subList(0, 5));
            }
            request.setAttribute("recentActivities", recentActivities);

            // Fetch friends
            UserSQLDao userDao = new UserSQLDao(connection);
            ArrayList<User> friendsList = userDao.getFriends(userId);
            request.setAttribute("friendsList", friendsList);

            // Fetch achievements (using notifications as a placeholder for now)
            NotificationsSQLDAO notificationsDao = new NotificationsSQLDAO(connection);
            ArrayList<Notification> achievementsList = notificationsDao.getNotifications(userId);
            request.setAttribute("achievementsList", achievementsList);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        User user = (User) session.getAttribute("user");
        String username = user.getName();
        long userId = user.getId();
        Connection connection = (Connection) getServletContext().getAttribute("dbConnection");
        
        try {
            if ("changeEmail".equals(action)) {
                String newEmail = request.getParameter("newEmail");
                if (newEmail != null && !newEmail.trim().isEmpty()) {
                    String checkQuery = "SELECT id FROM users WHERE LOWER(email) = ? AND id != ?";
                    try (PreparedStatement ps = connection.prepareStatement(checkQuery)) {
                        ps.setString(1, newEmail.toLowerCase().trim());
                        ps.setLong(2, userId);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            request.setAttribute("error", "Email is already in use.");
                            request.getRequestDispatcher("/profile.jsp").forward(request, response);
                            return;
                        }
                    }
                    String updateQuery = "UPDATE users SET email = ? WHERE id = ?";
                    try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                        ps.setString(1, newEmail);
                        ps.setLong(2, userId);
                        ps.executeUpdate();
                    }
                    request.setAttribute("success", "Email updated successfully.");
                }
                response.sendRedirect(request.getContextPath() + "/profile");
                return;
            } else if ("changeUsername".equals(action)) {
                String newUsername = request.getParameter("newUsername");
                if (newUsername != null && !newUsername.trim().isEmpty()) {
                    String checkQuery = "SELECT id FROM users WHERE LOWER(name) = ? AND id != ?";
                    try (PreparedStatement ps = connection.prepareStatement(checkQuery)) {
                        ps.setString(1, newUsername.toLowerCase().trim());
                        ps.setLong(2, userId);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            request.setAttribute("error", "Username is already in use.");
                            request.getRequestDispatcher("/profile.jsp").forward(request, response);
                            return;
                        }
                    }
                    String updateQuery = "UPDATE users SET name = ? WHERE id = ?";
                    try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                        ps.setString(1, newUsername);
                        ps.setLong(2, userId);
                        ps.executeUpdate();
                    }
                    // Update session user name
                    user.setName(newUsername);
                    session.setAttribute("user", user);
                    request.setAttribute("success", "Username updated successfully.");
                }
                response.sendRedirect(request.getContextPath() + "/profile");
                return;
            } else if ("deactivateAccount".equals(action)) {
                // Remove user from DB
                String deleteQuery = "DELETE FROM users WHERE id = ?";
                try (PreparedStatement ps = connection.prepareStatement(deleteQuery)) {
                    ps.setLong(1, userId);
                    ps.executeUpdate();
                }
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            } else if ("updateProfile".equals(action)) {
                updateProfile(request, response, connection, username);
            } else {
                // Default action - redirect back to profile page
                response.sendRedirect(request.getContextPath() + "/profile");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
        }
    }
    
    private void getQuizStatistics(HttpServletRequest request, Connection connection, String username) throws SQLException {
        // Get quizzes created by user
        String quizzesCreatedQuery = "SELECT COUNT(*) as count FROM quizzes WHERE creator_name = ?";
        try (PreparedStatement ps = connection.prepareStatement(quizzesCreatedQuery)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    request.setAttribute("quizzesCreated", rs.getInt("count"));
                }
            }
        }
        
        // Get quizzes taken by user
        String quizzesTakenQuery = "SELECT COUNT(*) as count FROM quiz_attempts WHERE user_name = ?";
        try (PreparedStatement ps = connection.prepareStatement(quizzesTakenQuery)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    request.setAttribute("quizzesTaken", rs.getInt("count"));
                }
            }
        }
        
        // Get average score
        String avgScoreQuery = "SELECT AVG(score) as avg_score FROM quiz_attempts WHERE user_name = ?";
        try (PreparedStatement ps = connection.prepareStatement(avgScoreQuery)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double avgScore = rs.getDouble("avg_score");
                    request.setAttribute("averageScore", avgScore > 0 ? String.format("%.1f", avgScore) : "0");
                }
            }
        }
        
        // Get recent activity (last 5 quiz attempts)
        String recentActivityQuery = "SELECT q.title, qa.score, qa.attempt_date FROM quiz_attempts qa " +
                                   "JOIN quizzes q ON qa.quiz_id = q.id " +
                                   "WHERE qa.user_name = ? ORDER BY qa.attempt_date DESC LIMIT 5";
        try (PreparedStatement ps = connection.prepareStatement(recentActivityQuery)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                // Store recent activity in request attributes
                // This would typically be stored in a list and passed to JSP
                // For simplicity, we'll just set a flag indicating recent activity exists
                request.setAttribute("hasRecentActivity", rs.next());
            }
        }
    }
    
    private void updateProfile(HttpServletRequest request, HttpServletResponse response, 
                             Connection connection, String username) throws SQLException, IOException {
        
        String email = request.getParameter("email");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        
        // Validate current password if password change is requested
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            if (currentPassword == null || currentPassword.trim().isEmpty()) {
                request.setAttribute("error", "Current password is required to change password");
                return;
            }
            
            // Verify current password
            String verifyQuery = "SELECT pass_hash FROM users WHERE name = ?";
            try (PreparedStatement ps = connection.prepareStatement(verifyQuery)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String storedHash = rs.getString("pass_hash");
                        String inputHash = com.quizmaster.util.PasswordUtil.hashPassword(currentPassword);
                        
                        if (!storedHash.equals(inputHash)) {
                            request.setAttribute("error", "Current password is incorrect");
                            return;
                        }
                    }
                }
            }
        }
        
        // Update user information
        StringBuilder updateQuery = new StringBuilder("UPDATE users SET ");
        boolean hasUpdates = false;
        
        if (email != null && !email.trim().isEmpty()) {
            updateQuery.append("email = ?");
            hasUpdates = true;
        }
        
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            if (hasUpdates) {
                updateQuery.append(", ");
            }
            updateQuery.append("pass_hash = ?");
            hasUpdates = true;
        }
        
        if (hasUpdates) {
            updateQuery.append(" WHERE name = ?");
            try (PreparedStatement ps = connection.prepareStatement(updateQuery.toString())) {
                int paramIndex = 1;
                
                if (email != null && !email.trim().isEmpty()) {
                    ps.setString(paramIndex++, email);
                }
                if (newPassword != null && !newPassword.trim().isEmpty()) {
                    String newHash = com.quizmaster.util.PasswordUtil.hashPassword(newPassword);
                    ps.setString(paramIndex++, newHash);
                }
                ps.setString(paramIndex, username);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    request.setAttribute("success", "Profile updated successfully");
                } else {
                    request.setAttribute("error", "Failed to update profile");
                }
            }
        }
        response.sendRedirect(request.getContextPath() + "/profile");
    }
} 