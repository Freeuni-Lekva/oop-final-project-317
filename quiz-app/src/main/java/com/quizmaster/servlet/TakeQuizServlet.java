package com.quizmaster.servlet;

// Add new servlet for displaying and submitting quizzes
import DAO.*;
import models.Quiz;
import models.Question;
import models.QuizResult;
import models.QuizHistory;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/take-quiz")
public class TakeQuizServlet extends HttpServlet {

    private QuizDAO quizDAO;
    private QuestionDAO questionDAO;
    private QuizResultDAO quizResultDAO;
    private QuizHistoryDAO quizHistoryDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        quizDAO = (QuizDAO) getServletContext().getAttribute("quizDAO");
        questionDAO = (QuestionDAO) getServletContext().getAttribute("questionDAO");
        quizResultDAO = (QuizResultDAO) getServletContext().getAttribute("quizResultDAO");
        
        // Initialize QuizHistoryDAO
        Connection connection = (Connection) getServletContext().getAttribute("dbConnection");
        if (connection != null) {
            quizHistoryDAO = new QuizHistorySQLDao(connection);
        }
    }

    private void ensureDAOsInitialized() {
        if (quizDAO == null) {
            quizDAO = (QuizDAO) getServletContext().getAttribute("quizDAO");
        }
        if (questionDAO == null) {
            questionDAO = (QuestionDAO) getServletContext().getAttribute("questionDAO");
        }
        if (quizResultDAO == null) {
            quizResultDAO = (QuizResultDAO) getServletContext().getAttribute("quizResultDAO");
        }
        if (quizHistoryDAO == null) {
            Connection connection = (Connection) getServletContext().getAttribute("dbConnection");
            if (connection != null) {
                quizHistoryDAO = new QuizHistorySQLDao(connection);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String quizIdParam = request.getParameter("quizId");
        if (quizIdParam == null || quizIdParam.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quiz ID is required");
            return;
        }

        long quizId;
        try {
            quizId = Long.parseLong(quizIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quiz ID format");
            return;
        }

        // Ensure all DAOs are initialized
        ensureDAOsInitialized();

        if (quizDAO == null || questionDAO == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DAO initialization problem");
            return;
        }

        Quiz quiz = quizDAO.getQuiz(quizId);
        if (quiz == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Quiz not found");
            return;
        }

        List<Question> questions = questionDAO.getQuizQuestions(quizId);
        if (quiz.isRandomizeQuestions()) {
            Collections.shuffle(questions);
        }
        quiz.setQuestions(questions);

        // Store quiz info in session for grading later
        HttpSession session = request.getSession();
        session.setAttribute("currentQuiz", quiz);
        session.setAttribute("currentQuizQuestions", questions);
        session.setAttribute("quizStartTime", System.currentTimeMillis()); // Store start time

        // Forward to JSP page to render the quiz
        request.setAttribute("quiz", quiz);
        request.setAttribute("questions", questions);
        request.getRequestDispatcher("/take-quiz.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login");
            return;
        }

        // Ensure all DAOs are initialized
        ensureDAOsInitialized();

        Quiz quiz = (Quiz) session.getAttribute("currentQuiz");
        @SuppressWarnings("unchecked")
        List<Question> questions = (List<Question>) session.getAttribute("currentQuizQuestions");
        if (quiz == null || questions == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quiz session expired. Please restart the quiz.");
            return;
        }

        int correctAnswers = 0;
        int totalPoints = 0;
        int maxPoints = 0;

        for (Question q : questions) {
            String paramName = "answer_" + q.getId();
            Object userAnswer = null;

            // Multi-select / multi-answer inputs send multiple values
            if (request.getParameterValues(paramName) != null && request.getParameterValues(paramName).length > 1) {
                userAnswer = Arrays.asList(request.getParameterValues(paramName));
            } else {
                String single = request.getParameter(paramName);
                if (single != null) {
                    // For multi-answer where we use textarea, split by newlines/commas
                    if (q.getQuestionType().equals(models.Question.MULTI_ANSWER)) {
                        String[] parts = single.split("[\\n,;]+");
                        userAnswer = Arrays.asList(parts);
                    } else {
                        userAnswer = single;
                    }
                }
            }

            boolean isCorrect = q.isCorrect(userAnswer);
            if (isCorrect) {
                correctAnswers++;
                totalPoints += q.getPoints();
            }
            maxPoints += q.getPoints();
        }

        // Prepare a QuizResult object and persist
        User currentUser = (User) session.getAttribute("user");
        Long userId = currentUser != null ? currentUser.getId() : 0L;

        QuizResult quizResult = new QuizResult(userId, quiz.getId(), questions.size(), maxPoints);
        quizResult.setScore(correctAnswers);
        quizResult.setTotalPoints(totalPoints);
        quizResult.setPracticeMode(quiz.isPracticeMode());

        try {
            if (quizResultDAO != null) {
                quizResultDAO.addQuizResult(quizResult);
            }

            // Save to quiz history
            if (quizHistoryDAO != null) {
                // Calculate time taken using the stored start time
                long startTime = session.getAttribute("quizStartTime") != null ? 
                    (Long) session.getAttribute("quizStartTime") : 
                    session.getCreationTime();
                int timeTaken = (int)(System.currentTimeMillis() - startTime) / 1000; // Time taken in seconds

                QuizHistory quizHistory = new QuizHistory(
                    userId,
                    quiz.getId(),
                    (int)((double)totalPoints / maxPoints * 100), // Convert to percentage
                    timeTaken
                );
                quizHistoryDAO.addQuizHistory(quizHistory);
            }

            // Increment quiz times taken
            if (quizDAO != null) {
                quizDAO.incrementQuizCompletions(quiz.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Clean up session attributes
        session.removeAttribute("currentQuiz");
        session.removeAttribute("currentQuizQuestions");
        session.removeAttribute("quizStartTime");

        // Redirect to quiz result page
        response.sendRedirect(request.getContextPath() + "/quiz-result?resultId=" + quizResult.getId());
    }
} 