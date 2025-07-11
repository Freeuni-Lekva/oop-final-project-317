package com.quizmaster.servlet;

import models.Quiz;

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
import java.util.*;

@WebServlet("/popular-quizzes")
public class PopularQuizzesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirect to login page if user not authenticated
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Connection connection = (Connection) getServletContext().getAttribute("dbConnection");
        if (connection == null) {
            throw new ServletException("Database connection not initialized");
        }

        List<Quiz> popularQuizzes = new ArrayList<>();

        // Simple query to get quizzes ordered by times_taken
        String sql = "SELECT * FROM quizzes ORDER BY times_taken DESC LIMIT 10";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Quiz quiz = new Quiz(
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getLong("created_by")
                    );
                    quiz.setId(rs.getLong("id"));
                    if (rs.getTimestamp("created_date") != null) {
                        quiz.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
                    }
                    if (rs.getTimestamp("last_modified") != null) {
                        quiz.setLastModified(rs.getTimestamp("last_modified").toLocalDateTime());
                    }
                    quiz.setRandomizeQuestions(rs.getBoolean("randomize_questions"));
                    quiz.setOnePage(rs.getBoolean("one_page"));
                    quiz.setImmediateCorrection(rs.getBoolean("immediate_correction"));
                    quiz.setPracticeMode(rs.getBoolean("practice_mode"));
                    try {
                        quiz.setTimeLimit(rs.getInt("time_limit"));
                    } catch (SQLException ignore) {}
                    try {
                        quiz.setTimesTaken(rs.getInt("times_taken"));
                    } catch (SQLException ignore) {}
                    popularQuizzes.add(quiz);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Failed to retrieve popular quizzes", e);
        }

        request.setAttribute("popularQuizzes", popularQuizzes);
        request.getRequestDispatcher("/popular-quizzes.jsp").forward(request, response);
    }
} 