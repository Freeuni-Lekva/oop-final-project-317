package com.quizmaster.servlet;

import DAO.NotificationDAO;
import DAO.UserDAO;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/friendship")
public class FriendshipServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        // Check if user is logged in
        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        if ("accept".equals(action)) {
            // Get parameters
            long fromUserId = Long.parseLong(request.getParameter("fromUserId"));
            long notificationId = Long.parseLong(request.getParameter("notificationId"));

            // Get DAOs
            UserDAO userDAO = (UserDAO) request.getServletContext().getAttribute("userSqlDao");
            NotificationDAO notificationDAO = (NotificationDAO) request.getServletContext().getAttribute("notificationDAO");

            try {
                // Add friendship
                userDAO.addFriendship(currentUser.getId(), fromUserId);

                // Delete the notification
                notificationDAO.deleteNotification(notificationId);

                // Redirect back to notifications page
                response.sendRedirect("notifications");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error accepting friend request");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }
} 