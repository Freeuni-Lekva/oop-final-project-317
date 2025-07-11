package com.quizmaster.servlet;

import DAO.QuizDAO;
import DAO.QuizResultDAO;
import models.Quiz;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/delete-quiz")
public class DeleteQuizServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quizIdParam = request.getParameter("quizId");
        if (quizIdParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "quizId required");
            return;
        }
        long quizId;
        try {
            quizId = Long.parseLong(quizIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid quizId");
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        User currentUser = (User) session.getAttribute("user");

        QuizDAO quizDAO = (QuizDAO) getServletContext().getAttribute("quizDAO");
        if (quizDAO == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "quizDAO not initialized");
            return;
        }
        Quiz quiz = quizDAO.getQuiz(quizId);
        if (quiz == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Quiz not found");
            return;
        }

        // Authorisation: owner or admin
        boolean isOwner = quiz.getCreatedBy() != null && quiz.getCreatedBy() == currentUser.getId();
        if (!(isOwner || currentUser.getIfAdmin())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Not authorised to delete this quiz");
            return;
        }

        // Delete quiz results first (they lack FK constraints)
        Connection conn = (Connection) getServletContext().getAttribute("dbConnection");
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM quiz_results WHERE quiz_id = ?")) {
                ps.setLong(1, quizId);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }

        // Now delete quiz (questions cascade via FK)
        quizDAO.removeQuiz(quiz);

        response.sendRedirect(request.getContextPath() + "/my-creations");
    }
} 