package com.quizmaster.servlet;

import DAO.NotificationDAO;
import models.Notification;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(
    name = "NotificationsServlet",
    urlPatterns = {"/notifications"},
    loadOnStartup = 1
)
public class NotificationsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        NotificationDAO notificationDAO = (NotificationDAO) request.getServletContext().getAttribute("notificationDAO");
        ArrayList<Notification> notifications = notificationDAO.getNotifications(currentUser.getId());
        int notificationCount = notificationDAO.getNotificationCount(currentUser.getId());

        request.setAttribute("notifications", notifications);
        request.setAttribute("notificationCount", notificationCount);
        request.getRequestDispatcher("notifications.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            String notificationId = request.getParameter("notificationId");
            if (notificationId != null && !notificationId.trim().isEmpty()) {
                try {
                    NotificationDAO notificationDAO = (NotificationDAO) request.getServletContext().getAttribute("notificationDAO");
                    notificationDAO.deleteNotification(Long.parseLong(notificationId));
                    response.sendRedirect("notifications"); // Redirect back to notifications page
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid notification ID format");
                    doGet(request, response);
                } catch (Exception e) {
                    request.setAttribute("error", "Error deleting notification");
                    doGet(request, response);
                }
            } else {
                request.setAttribute("error", "Notification ID is required");
                doGet(request, response);
            }
        } else {
            response.sendRedirect("notifications");
        }
    }
} 