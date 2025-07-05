package com.quizmaster.servlet;

import com.quizmaster.util.PasswordUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get form parameters
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validation
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("error", "Username is required");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }
        
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email is required");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }
        
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Password is required");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }
        
        if (password.length() < 8) {
            request.setAttribute("error", "Password must be at least 8 characters long");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }

        
        // Demo registration logic - replace with real database save later
        if ("admin@test.com".equals(email)) {
            request.setAttribute("error", "Email already exists");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
        } else {
            // Registration successful
            request.setAttribute("success", "Account created successfully! You can now login.");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
        }

        // Add User in database if email and username is unique

        Connection connection = (Connection) getServletContext().getAttribute("dbConnection");

        try {
            // Check if email already exists
            String checkQuery = "SELECT COUNT(*) FROM users WHERE email = ?";
            try (PreparedStatement ps = connection.prepareStatement(checkQuery)) {
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    rs.close();
                    request.setAttribute("error", "Email already registered");
                    request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
                    return;
                }
                rs.close();
            }

            // Check if username already exists
            String checkUsernameQuery = "SELECT COUNT(*) FROM users WHERE name = ?";
            try (PreparedStatement ps = connection.prepareStatement(checkUsernameQuery)) {
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    rs.close();
                    request.setAttribute("error", "Username already taken");
                    request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
                    return;
                }
                rs.close();
            }

            // Insert new user
            String insertQuery = "INSERT INTO users (name, email, pass_hash, passed_quizzes) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
                ps.setString(1, username);
                ps.setString(2, email);
                ps.setString(3, PasswordUtil.hashPassword(password));
                ps.setInt(4, 0);
                ps.executeUpdate();
            }

            response.sendRedirect(request.getContextPath() + "/login");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
        }
    }
}