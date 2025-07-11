package com.quizmaster.servlet;

import DAO.QuizDAO;
import models.Quiz;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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

        QuizDAO quizDAO = (QuizDAO) getServletContext().getAttribute("quizDAO");
        if (quizDAO == null) {
            throw new ServletException("QuizDAO not initialized");
        }

        try {
            // Get all quizzes and filter/sort by times_taken
            List<Quiz> allQuizzes = quizDAO.getUserQuizzes(0L); // This gets all quizzes for user 0, but we'll use a different approach
            
            // Actually, let's get all quizzes properly by modifying our approach
            // For now, let's create a simple list and sort by times_taken
            List<Quiz> popularQuizzes = getAllQuizzesOrderedByPopularity(quizDAO);
            
            // Limit to top 10
            if (popularQuizzes.size() > 10) {
                popularQuizzes = popularQuizzes.subList(0, 10);
            }

            request.setAttribute("popularQuizzes", popularQuizzes);
            request.getRequestDispatcher("/popular-quizzes.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Failed to retrieve popular quizzes", e);
        }
    }
    
    private List<Quiz> getAllQuizzesOrderedByPopularity(QuizDAO quizDAO) {
        // For simplicity, we'll use a SQL query directly here
        // In a production system, you'd add a method to QuizDAO for this
        List<Quiz> quizzes = new ArrayList<>();
        
        java.sql.Connection connection = (java.sql.Connection) getServletContext().getAttribute("dbConnection");
        String sql = "SELECT * FROM quizzes ORDER BY times_taken DESC LIMIT 10";
        
        try (java.sql.PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
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
                    quiz.setTimesTaken(rs.getInt("times_taken"));
                    quizzes.add(quiz);
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        
        return quizzes;
    }
} 