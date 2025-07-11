package com.quizmaster.servlet;

import DAO.QuizHistorySQLDao;
import DAO.QuizDAO;
import DAO.QuizHistoryDAO;
import models.QuizHistory;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/quiz-history")
public class QuizHistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        long userId = user.getId();

        // Get quiz history (taken quizzes)
        QuizHistoryDAO quizHistoryDao = (QuizHistoryDAO) getServletContext().getAttribute("quizHistoryDAO");
        if (quizHistoryDao == null) {
            // Fallback to creating new instance if not in context
            Connection connection = (Connection) getServletContext().getAttribute("dbConnection");
            quizHistoryDao = new QuizHistorySQLDao(connection);
        }
        
        ArrayList<QuizHistory> quizHistory = quizHistoryDao.getUserQuizHistory(userId);

        // Get quiz names for each quiz in history
        QuizDAO quizDAO = (QuizDAO) getServletContext().getAttribute("quizDAO");
        Map<Long, String> quizNames = new HashMap<>();
        if (quizDAO != null && quizHistory != null) {
            for (QuizHistory history : quizHistory) {
                Quiz quiz = quizDAO.getQuiz(history.getQuizId());
                if (quiz != null) {
                    quizNames.put(quiz.getId(), quiz.getTitle());
                }
            }
        }

        request.setAttribute("quizHistory", quizHistory);
        request.setAttribute("quizNames", quizNames);

        // Forward to quiz history JSP
        request.getRequestDispatcher("/QuizHistory.jsp").forward(request, response);
    }
}