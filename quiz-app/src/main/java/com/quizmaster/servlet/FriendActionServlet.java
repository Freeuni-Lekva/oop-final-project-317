package com.quizmaster.servlet;

import DAO.UserDAO;
import DAO.NotificationDAO;
import models.User;
import notifications.FriendRequestNotification;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/friend-action")
public class FriendActionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        String userIdParam = request.getParameter("userId");

        if (action == null || userIdParam == null) {
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        try {
            long targetUserId = Long.parseLong(userIdParam);
            UserDAO userDAO = (UserDAO) getServletContext().getAttribute("userSqlDao");
            NotificationDAO notificationDAO = (NotificationDAO) getServletContext().getAttribute("notificationDAO");

            if (userDAO == null || notificationDAO == null) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Service unavailable");
                return;
            }

            switch (action) {
                case "add":
                    handleAddFriend(request, response, currentUser, targetUserId, userDAO, notificationDAO);
                    break;
                case "unfriend":
                    handleUnfriend(request, response, currentUser, targetUserId, userDAO);
                    break;
                case "cancel":
                    handleCancelRequest(request, response, currentUser, targetUserId, notificationDAO);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/profile");
                    break;
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred");
        }
    }

    private void handleAddFriend(HttpServletRequest request, HttpServletResponse response,
                                User currentUser, long targetUserId, UserDAO userDAO, NotificationDAO notificationDAO)
            throws IOException {
        // Check if they're already friends
        if (userDAO.checkIfFriends(currentUser.getId(), targetUserId)) {
            response.sendRedirect(request.getContextPath() + "/user-profile?userId=" + targetUserId);
            return;
        }
        // Check if there's already a pending request
        if (hasAnyPendingFriendRequest(currentUser.getId(), targetUserId, notificationDAO)) {
            response.sendRedirect(request.getContextPath() + "/user-profile?userId=" + targetUserId);
            return;
        }
        // Get the target user to create the notification
        User targetUser = userDAO.getUser(targetUserId);
        if (targetUser == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }
        // Create and send friend request notification
        FriendRequestNotification notification = new FriendRequestNotification(
                currentUser.getId(),
                targetUserId,
                "Friend Request",
                currentUser.getName() + " wants to be your friend!"
        );

        notificationDAO.sendNotification(notification);

        // Redirect back to the user's profile
        response.sendRedirect(request.getContextPath() + "/user-profile?userId=" + targetUserId);
    }

    private void handleUnfriend(HttpServletRequest request, HttpServletResponse response,
                               User currentUser, long targetUserId, UserDAO userDAO)
            throws IOException {
        // Remove the friendship from both sides
        userDAO.removeFriendship(currentUser.getId(), targetUserId);
        userDAO.removeFriendship(targetUserId, currentUser.getId());

        // Redirect back to the user's profile
        response.sendRedirect(request.getContextPath() + "/user-profile?userId=" + targetUserId);
    }

    private void handleCancelRequest(HttpServletRequest request, HttpServletResponse response,
                                    User currentUser, long targetUserId, NotificationDAO notificationDAO)
            throws IOException {
        try {
            // Find and delete the pending friend request notification
            java.util.ArrayList<models.Notification> notifications = notificationDAO.getNotifications(targetUserId);
            for (models.Notification notification : notifications) {
                if (notification.getFromUserId() == currentUser.getId() && 
                    models.Notification.FRIEND_REQUEST_NOTIFICATION.equals(notification.getQuestionType())) {
                    notificationDAO.deleteNotification(notification.getId());
                    break;
                }
            }
            
            // Also check notifications for current user (in case the request was sent the other way)
            notifications = notificationDAO.getNotifications(currentUser.getId());
            for (models.Notification notification : notifications) {
                if (notification.getFromUserId() == targetUserId && 
                    models.Notification.FRIEND_REQUEST_NOTIFICATION.equals(notification.getQuestionType())) {
                    notificationDAO.deleteNotification(notification.getId());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Redirect back to the user's profile
        response.sendRedirect(request.getContextPath() + "/user-profile?userId=" + targetUserId);
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