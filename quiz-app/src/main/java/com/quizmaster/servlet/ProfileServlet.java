package com.quizmaster.servlet;

import DAO.UserDAO;
import DAO.UserSQLDao;
import DAO.QuizHistorySQLDao;
import DAO.NotificationsSQLDAO;
import models.User;
import models.QuizHistory;
import models.Notification;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
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

        User currentUser = (User) session.getAttribute("user");
        Connection connection = (Connection) getServletContext().getAttribute("dbConnection");
        UserDAO userDAO = (UserDAO) getServletContext().getAttribute("userSqlDao");

        // Determine whose profile to show
        String userIdParam = request.getParameter("userId");
        User profileUser = currentUser;
        boolean isOwnProfile = true;
        long profileUserId = currentUser.getId();

        if (userIdParam != null && !userIdParam.trim().isEmpty()) {
            try {
                long requestedId = Long.parseLong(userIdParam.trim());
                if (requestedId != currentUser.getId()) {
                    User other = userDAO.getUser(requestedId);
                    if (other == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                        return;
                    }
                    profileUser = other;
                    isOwnProfile = false;
                    profileUserId = requestedId;
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
                return;
            }
        }

        try {
            // 1) Quiz statistics
            getQuizStatistics(request, connection, profileUser.getName());

            // 2) Recent activities (limit 5)
            QuizHistorySQLDao historyDao = new QuizHistorySQLDao(connection);
            ArrayList<QuizHistory> recentActivities = historyDao.getUserQuizHistory(profileUserId);
            if (recentActivities.size() > 5) {
                recentActivities = new ArrayList<>(recentActivities.subList(0, 5));
            }
            request.setAttribute("recentActivities", recentActivities);

            // 3) Friends list
            UserSQLDao userSqlDao = new UserSQLDao(connection);
            ArrayList<User> friendsList = userSqlDao.getFriends(profileUserId);
            System.out.println("ProfileServlet: Found " + (friendsList != null ? friendsList.size() : 0) + " friends for user " + profileUserId);
            request.setAttribute("friendsList", friendsList);

            // 4) Achievements / Notifications
            NotificationsSQLDAO notesDao = new NotificationsSQLDAO(connection);
            ArrayList<Notification> achievementsList = notesDao.getNotifications(profileUserId);
            request.setAttribute("achievementsList", achievementsList);

            // 5) Core profile attributes
            request.setAttribute("profileUser", profileUser);
            request.setAttribute("isOwnProfile", isOwnProfile);
            request.setAttribute("userEmail", profileUser.getEmail());
            
            // Format the date to show only date, not time
            if (profileUser.getCreatedAt() != null) {
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMM dd, yyyy");
                String formattedDate = dateFormat.format(profileUser.getCreatedAt());
                request.setAttribute("userCreatedAt", formattedDate);
            } else {
                request.setAttribute("userCreatedAt", "Unknown");
            }

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
                    // Check uniqueness
                    String checkSql = "SELECT id FROM users WHERE LOWER(email)=? AND id!=?";
                    try (PreparedStatement ps = connection.prepareStatement(checkSql)) {
                        ps.setString(1, newEmail.toLowerCase().trim());
                        ps.setLong(2, userId);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            request.setAttribute("error", "Email is already in use.");
                            request.getRequestDispatcher("/profile.jsp").forward(request, response);
                            return;
                        }
                    }
                    // Update
                    String updSql = "UPDATE users SET email=? WHERE id=?";
                    try (PreparedStatement ps = connection.prepareStatement(updSql)) {
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
                    String checkSql = "SELECT id FROM users WHERE LOWER(name)=? AND id!=?";
                    try (PreparedStatement ps = connection.prepareStatement(checkSql)) {
                        ps.setString(1, newUsername.toLowerCase().trim());
                        ps.setLong(2, userId);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            request.setAttribute("error", "Username is already in use.");
                            request.getRequestDispatcher("/profile.jsp").forward(request, response);
                            return;
                        }
                    }
                    String updSql = "UPDATE users SET name=? WHERE id=?";
                    try (PreparedStatement ps = connection.prepareStatement(updSql)) {
                        ps.setString(1, newUsername);
                        ps.setLong(2, userId);
                        ps.executeUpdate();
                    }
                    user.setName(newUsername);
                    session.setAttribute("user", user);
                    request.setAttribute("success", "Username updated successfully.");
                }
                response.sendRedirect(request.getContextPath() + "/profile");
                return;

            } else if ("deactivateAccount".equals(action)) {
                String delSql = "DELETE FROM users WHERE id=?";
                try (PreparedStatement ps = connection.prepareStatement(delSql)) {
                    ps.setLong(1, userId);
                    ps.executeUpdate();
                }
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;

            } else if ("updateProfile".equals(action)) {
                updateProfile(request, response, connection, username);
                // updateProfile handles its own redirect
                return;

            } else {
                response.sendRedirect(request.getContextPath() + "/profile");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        }
    }

    private void getQuizStatistics(HttpServletRequest request, Connection connection, String username) throws SQLException {
        // Quizzes created
        String sqlCreated = "SELECT COUNT(*) AS count FROM quizzes WHERE creator_name=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlCreated)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    request.setAttribute("quizzesCreated", rs.getInt("count"));
                }
            }
        }

        // Quizzes taken
        String sqlTaken = "SELECT COUNT(*) AS count FROM quiz_attempts WHERE user_name=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlTaken)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    request.setAttribute("quizzesTaken", rs.getInt("count"));
                }
            }
        }

        // Average score
        String sqlAvg = "SELECT AVG(score) AS avg_score FROM quiz_attempts WHERE user_name=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlAvg)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double avg = rs.getDouble("avg_score");
                    request.setAttribute("averageScore", avg > 0 ? String.format("%.1f", avg) : "0");
                }
            }
        }
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response,
                               Connection connection, String username)
            throws SQLException, IOException {
        
        String email = request.getParameter("email");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        boolean changingPassword = newPassword != null && !newPassword.trim().isEmpty();

        // If password change is requested, verify current password
        if (changingPassword) {
            if (currentPassword == null || currentPassword.trim().isEmpty()) {
                request.setAttribute("error", "Current password is required to change password");
                return;
            }
            String verifySql = "SELECT pass_hash FROM users WHERE name=?";
            try (PreparedStatement ps = connection.prepareStatement(verifySql)) {
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

        // Build dynamic UPDATE query
        StringBuilder sb = new StringBuilder("UPDATE users SET ");
        boolean first = true;
        if (email != null && !email.trim().isEmpty()) {
            sb.append("email=?");
            first = false;
        }
        if (changingPassword) {
            if (!first) sb.append(", ");
            sb.append("pass_hash=?");
        }
        sb.append(" WHERE name=?");

        try (PreparedStatement ps = connection.prepareStatement(sb.toString())) {
            int idx = 1;
            if (email != null && !email.trim().isEmpty()) {
                ps.setString(idx++, email);
            }
            if (changingPassword) {
                String newHash = com.quizmaster.util.PasswordUtil.hashPassword(newPassword);
                ps.setString(idx++, newHash);
            }
            ps.setString(idx, username);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                request.setAttribute("success", "Profile updated successfully");
            } else {
                request.setAttribute("error", "Failed to update profile");
            }
        }
        response.sendRedirect(request.getContextPath() + "/profile");
    }
}
