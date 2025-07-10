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
        request.getRequestDispatcher("/WEB-INF/jsp/create-quiz.jsp").forward(request, response);
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
        UserSQLDao userDAO = (UserSQLDao) getServletContext().getAttribute("userDAO");

        // Get current user
        String username = (String) session.getAttribute("user");
        User currentUser = userDAO.getUser(username);

        if (currentUser == null) {
            request.setAttribute("error", "User not found. Please log in again.");
            request.getRequestDispatcher("create-quiz.jsp").forward(request, response);
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

        // Save quiz to database
        quizDAO.addQuiz(quiz);

        // Store quiz ID in session for the questions creation step
        session.setAttribute("currentQuizId", quiz.getId());
        session.setAttribute("quizNumQuestions", numQuestions);

        // Redirect to create-questions.jsp
        response.sendRedirect("create-questions.jsp");
    }
}