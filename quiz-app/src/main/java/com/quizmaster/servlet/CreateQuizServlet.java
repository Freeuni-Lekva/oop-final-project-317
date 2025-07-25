package com.quizmaster.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import models.Quiz;
import models.User;
import DAO.QuizSQLDao;
import DAO.UserSQLDao;

@WebServlet("/create-quiz")
public class CreateQuizServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect to create-quiz.jsp if accessed directly
        request.getRequestDispatcher("/create-quiz.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set character encoding
        request.setCharacterEncoding("UTF-8");

        // Get session and check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        // Get form parameters
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String numQuestionsStr = request.getParameter("numQuestions");
        String timeLimitStr = request.getParameter("timeLimit");

        // Get checkbox values (they will be "true" if checked, null if not checked)
        boolean randomizeQuestions = "true".equals(request.getParameter("randomizeQuestions"));
        boolean onePage = "true".equals(request.getParameter("onePage"));
        boolean immediateCorrection = "true".equals(request.getParameter("immediateCorrection"));
        boolean practiceMode = "true".equals(request.getParameter("practiceMode"));

        // Comprehensive validation
        StringBuilder errorMessage = new StringBuilder();

        // Validate title
        if (title == null || title.trim().isEmpty()) {
            errorMessage.append("Quiz title is required. ");
        } else if (title.trim().length() > 100) {
            errorMessage.append("Quiz title must be less than 100 characters. ");
        }

        // Validate description
        if (description != null && description.trim().length() > 500) {
            errorMessage.append("Description must be less than 500 characters. ");
        }

        // Validate number of questions
        if (numQuestionsStr == null || numQuestionsStr.trim().isEmpty()) {
            errorMessage.append("Number of questions is required. ");
        } else {
            try {
                int numQuestions = Integer.parseInt(numQuestionsStr);
                if (numQuestions < 1) {
                    errorMessage.append("Number of questions must be at least 1. ");
                } else if (numQuestions > 100) {
                    errorMessage.append("Number of questions cannot exceed 100. ");
                }
            } catch (NumberFormatException e) {
                errorMessage.append("Number of questions must be a valid number. ");
            }
        }

        int quizTimeLimitMinutes = 0;
        if (timeLimitStr != null && !timeLimitStr.trim().isEmpty()) {
            try {
                quizTimeLimitMinutes = Integer.parseInt(timeLimitStr.trim());
                if (quizTimeLimitMinutes < 0) {
                    errorMessage.append("Time limit cannot be negative. ");
                }
            } catch (NumberFormatException e) {
                errorMessage.append("Time limit must be a valid number. ");
            }
        }
        int quizTimeLimitSeconds = quizTimeLimitMinutes * 60;

        // If there are validation errors, forward back to the form
        if (errorMessage.length() > 0) {
            request.setAttribute("error", errorMessage.toString().trim());
            request.getRequestDispatcher("create-quiz.jsp").forward(request, response);
            return;
        }

        // Parse the number of questions (we know it's valid now)
        int numQuestions = Integer.parseInt(numQuestionsStr);

        // Get DAO objects from context
        QuizSQLDao quizDAO = (QuizSQLDao) getServletContext().getAttribute("quizDAO");

        // Current user object is already stored in session
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        // Create Quiz object
        Quiz quiz = new Quiz(title.trim(),
                description != null ? description.trim() : "",
                currentUser.getId());

        // Set quiz settings
        quiz.setRandomizeQuestions(randomizeQuestions);
        quiz.setOnePage(onePage);
        quiz.setImmediateCorrection(immediateCorrection);
        quiz.setPracticeMode(practiceMode);
        quiz.setTimeLimit(quizTimeLimitSeconds);

        // Save quiz to database
        quizDAO.addQuiz(quiz);

        // Store quiz ID in session for the questions creation step
        session.setAttribute("currentQuizId", quiz.getId());
        session.setAttribute("numQuestions", numQuestions);
        // reset any old progress
        session.removeAttribute("questionTypes");

        // Redirect to the first question-type selection page
        response.sendRedirect(request.getContextPath() + "/question-type?index=1");
    }
}