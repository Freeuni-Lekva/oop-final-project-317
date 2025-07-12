package com.quizmaster.servlet;

import DAO.UserDAO;
import DAO.NotificationDAO;
import DAO.QuizHistorySQLDao;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@WebServlet("/user-profile")
public class UserProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            System.out.println("=== UserProfileServlet START ===");
            java.util.Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String paramValue = request.getParameter(paramName);
                System.out.println("Parameter: " + paramName + " = " + paramValue);
            }
            
            HttpSession session = request.getSession(false);
            System.out.println("Session exists: " + (session != null));
            
            if (session == null || session.getAttribute("user") == null) {
                System.out.println("No session or user, redirecting to login");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            User currentUser = (User) session.getAttribute("user");
            System.out.println("Current user: " + currentUser.getName() + " (ID: " + currentUser.getId() + ")");
            
            UserDAO userDAO = (UserDAO) getServletContext().getAttribute("userSqlDao");
            Connection connection = (Connection) getServletContext().getAttribute("dbConnection");
            
            System.out.println("UserDAO: " + (userDAO != null ? "NOT NULL" : "NULL"));
            System.out.println("Connection: " + (connection != null ? "NOT NULL" : "NULL"));

            if (userDAO == null || connection == null) {
                System.out.println("UserDAO or Connection is null - setting error and forwarding");
                request.setAttribute("error", "Service temporarily unavailable");
                request.getRequestDispatcher("/user-profile.jsp").forward(request, response);
                return;
            }

            String userIdStr = request.getParameter("id");
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                userIdStr = request.getParameter("userId");
            }
            
            System.out.println("userIdStr: " + userIdStr);
            
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                System.out.println("No userId provided - redirecting to friends");
                response.sendRedirect(request.getContextPath() + "/friends");
                return;
            }

            long userId = Long.parseLong(userIdStr);
            System.out.println("Parsed userId: " + userId);

            if (userId == currentUser.getId()) {
                System.out.println("User viewing own profile - redirecting to /profile");
                response.sendRedirect(request.getContextPath() + "/profile");
                return;
            }

            User profileUser = userDAO.getUser(userId);
            System.out.println("Profile user: " + (profileUser != null ? profileUser.getName() : "NULL"));

            if (profileUser == null) {
                System.out.println("Profile user not found - setting error and forwarding");
                request.setAttribute("error", "User not found");
                request.getRequestDispatcher("/user-profile.jsp").forward(request, response);
                return;
            }

            // Check friendship status
            boolean areFriends = userDAO.checkIfFriends(currentUser.getId(), userId);
            request.setAttribute("areFriends", areFriends);
            
            // Check for pending friend request
            NotificationDAO notificationDAO = (NotificationDAO) getServletContext().getAttribute("notificationDAO");
            boolean isPendingRequest = false;
            if (notificationDAO != null) {
                isPendingRequest = hasAnyPendingFriendRequest(currentUser.getId(), userId, notificationDAO);
            }
            request.setAttribute("isPendingRequest", isPendingRequest);
            
            // Get friends list for the profile user
            ArrayList<User> friendsList = userDAO.getFriends(userId);
            System.out.println("UserProfileServlet: Found " + (friendsList != null ? friendsList.size() : 0) + " friends for user " + userId);
            request.setAttribute("friendsList", friendsList);
            
            // Get achievements list for the profile user
            notificationDAO = (NotificationDAO) getServletContext().getAttribute("notificationDAO");
            ArrayList<models.Notification> achievementsList = new ArrayList<>();
            if (notificationDAO != null) {
                ArrayList<models.Notification> allNotifications = notificationDAO.getNotifications(userId);
                // Filter out friend request notifications to only show actual achievements
                for (models.Notification notification : allNotifications) {
                    if (!models.Notification.FRIEND_REQUEST_NOTIFICATION.equals(notification.getQuestionType())) {
                        achievementsList.add(notification);
                    }
                }
            }
            request.setAttribute("achievementsList", achievementsList);
            
            // Get recent activities for the profile user
            try {
                QuizHistorySQLDao historyDao = new QuizHistorySQLDao(connection);
                ArrayList<models.QuizHistory> recentActivities = historyDao.getUserQuizHistory(userId);
                if (recentActivities.size() > 5) {
                    recentActivities = new ArrayList<>(recentActivities.subList(0, 5));
                }
                request.setAttribute("recentActivities", recentActivities);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            request.setAttribute("profileUser", profileUser);
            request.setAttribute("currentUser", currentUser);
            request.setAttribute("isOwnProfile", false);
            
            // Format the date to show only date, not time
            if (profileUser.getCreatedAt() != null) {
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMM dd, yyyy");
                String formattedDate = dateFormat.format(profileUser.getCreatedAt());
                request.setAttribute("userCreatedAt", formattedDate);
            } else {
                request.setAttribute("userCreatedAt", "Unknown");
            }
            
            System.out.println("About to forward to user-profile.jsp");

            request.getRequestDispatcher("/user-profile.jsp").forward(request, response);
            
            System.out.println("=== UserProfileServlet END ===");

        } catch (Exception e) {
            System.out.println("=== UserProfileServlet EXCEPTION ===");
            System.out.println("Exception type: " + e.getClass().getName());
            System.out.println("Exception message: " + e.getMessage());
            e.printStackTrace();
            
            // Don't redirect, just set error and forward
            request.setAttribute("error", "Error loading profile: " + e.getMessage());
            request.getRequestDispatcher("/user-profile.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        UserDAO userDAO = (UserDAO) getServletContext().getAttribute("userSqlDao");

        String action = request.getParameter("action");
        String userIdStr = request.getParameter("userId");

        if (userDAO == null || action == null || userIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/friends");
            return;
        }

        try {
            long userId = Long.parseLong(userIdStr);

            if ("addFriend".equals(action)) {
                userDAO.addFriendship(currentUser.getId(), userId);
            }

            response.sendRedirect(request.getContextPath() + "/user-profile?id=" + userId);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/friends");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/user-profile?id=" + userIdStr);
        }
    }

    private void getQuizStatistics(HttpServletRequest request, Connection connection, String username)
            throws SQLException {

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
    
    private boolean hasPendingFriendRequest(long fromUserId, long toUserId, NotificationDAO notificationDAO) {
        // Check if there's a pending friend request notification from fromUserId to toUserId
        try {
            java.util.ArrayList<models.Notification> notifications = notificationDAO.getNotifications(toUserId);
            for (models.Notification notification : notifications) {
                if (notification.getFromUserId() == fromUserId && 
                    models.Notification.FRIEND_REQUEST_NOTIFICATION.equals(notification.getQuestionType())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean hasAnyPendingFriendRequest(long userId1, long userId2, NotificationDAO notificationDAO) {
        // Check if there's any pending friend request between the two users
        try {
            // Check notifications for user1
            java.util.ArrayList<models.Notification> notifications1 = notificationDAO.getNotifications(userId1);
            for (models.Notification notification : notifications1) {
                if (notification.getFromUserId() == userId2 && 
                    models.Notification.FRIEND_REQUEST_NOTIFICATION.equals(notification.getQuestionType())) {
                    return true;
                }
            }
            
            // Check notifications for user2
            java.util.ArrayList<models.Notification> notifications2 = notificationDAO.getNotifications(userId2);
            for (models.Notification notification : notifications2) {
                if (notification.getFromUserId() == userId1 && 
                    models.Notification.FRIEND_REQUEST_NOTIFICATION.equals(notification.getQuestionType())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}