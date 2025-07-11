package com.quizmaster.servlet;

import DAO.*;
import models.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

@WebServlet("/quiz-result")
public class QuizResultServlet extends HttpServlet {

    private QuizDAO quizDAO;
    private QuizResultDAO quizResultDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        quizDAO = (QuizDAO) getServletContext().getAttribute("quizDAO");
        quizResultDAO = (QuizResultDAO) getServletContext().getAttribute("quizResultDAO");
        userDAO = (UserDAO) getServletContext().getAttribute("userDAO");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String resIdParam = request.getParameter("resultId");
        if (resIdParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "resultId required");
            return;
        }
        long resultId;
        try {
            resultId = Long.parseLong(resIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid resultId");
            return;
        }

        if (quizResultDAO == null || quizDAO == null || userDAO == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DAO not initialized");
            return;
        }

        try {
            QuizResult result = quizResultDAO.getQuizResult(resultId);
            if (result == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Result not found");
                return;
            }
            Quiz quiz = quizDAO.getQuiz(result.getQuizId());
            if (quiz == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Quiz not found");
                return;
            }

            // Past results for user on this quiz
            List<QuizResult> userPast = quizResultDAO.getUserQuizResults(result.getUserId(), quiz.getId());
            // Sort by most recent first
            Collections.reverse(userPast);

            // Top performers
            List<QuizResult> allResults = quizResultDAO.getQuizResults(quiz.getId());
            allResults.sort((a,b) -> Double.compare(b.getPercentage(), a.getPercentage()));
            List<QuizResult> top10 = allResults.subList(0, Math.min(10, allResults.size()));

            // Get user information for all results
            Map<Long, User> userMap = new HashMap<>();
            for (QuizResult r : top10) {
                User user = userDAO.getUser(r.getUserId());
                if (user != null) {
                    userMap.put(r.getUserId(), user);
                }
            }

            HttpSession session = request.getSession(false);
            User currentUser = session != null ? (User) session.getAttribute("user") : null;

            request.setAttribute("quiz", quiz);
            request.setAttribute("quizResult", result);
            request.setAttribute("userPastResults", userPast);
            request.setAttribute("topResults", top10);
            request.setAttribute("currentUser", currentUser);
            request.setAttribute("userMap", userMap);

            RequestDispatcher rd = request.getRequestDispatcher("/quiz-result.jsp");
            rd.forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
} 