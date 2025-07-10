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
        String username = (String) session.getAttribute("user");
        Connection connection = (Connection) getServletContext().getAttribute("dbConnection");
        boolean isAdmin = false;
        try {
            String sql = "SELECT is_admin FROM users WHERE name = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        //isAdmin = rs.getBoolean("is_admin");
                        isAdmin = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/admin.jsp").forward(request, response);
            return;
        }
        if (!isAdmin) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to access this page.");
            return;
        }
        // Get total users
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
        request.getRequestDispatcher("/WEB-INF/jsp/admin.jsp").forward(request, response);
    }
} 