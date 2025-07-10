package main.java.com.quizmaster.servlet;

import com.quizmaster.util.PasswordUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get form parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Simple validation for demo purposes
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email is required");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Password is required");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }

        Connection connection = (Connection) getServletContext().getAttribute("dbConnection");

        try {
            String query = "SELECT name, pass_hash FROM users WHERE email = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, email);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String storedHash = rs.getString("pass_hash");
                        String username = rs.getString("name");
                        String inputHash = PasswordUtil.hashPassword(password);
                        if (storedHash.equals(inputHash)) {
                            request.getSession().setAttribute("user", username);
                            response.sendRedirect(request.getContextPath() + "/index.jsp");
                            return;
                        } else {
                            request.setAttribute("error", "Invalid password");
                        }
                    } else {
                        request.setAttribute("error", "User not found");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }
}