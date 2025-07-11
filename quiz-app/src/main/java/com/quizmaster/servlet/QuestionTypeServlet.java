package com.quizmaster.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles displaying the question-type picker for each question and collects the user's choice.
 *
 * Expected session attributes set beforehand:
 *   - numQuestions  Integer   total number of questions in the quiz
 *   - questionTypes List<String>  (this servlet builds it incrementally)
 */
@WebServlet("/question-type")
public class QuestionTypeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("numQuestions") == null) {
            // If the flow is accessed directly, redirect to quiz creation page.
            response.sendRedirect(request.getContextPath() + "/create-quiz.jsp");
            return;
        }

        int totalQuestions = (Integer) session.getAttribute("numQuestions");
        int currentIndex = 1;
        try {
            currentIndex = Integer.parseInt(request.getParameter("index"));
        } catch (NumberFormatException | NullPointerException ignored) { }

        request.setAttribute("currentIndex", currentIndex);
        request.setAttribute("totalQuestions", totalQuestions);

        // forward to JSP
        request.getRequestDispatcher("/question-type.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String selectedType = request.getParameter("questionType");
        int currentIndex = Integer.parseInt(request.getParameter("currentIndex"));
        int totalQuestions = (Integer) session.getAttribute("numQuestions");

        // Store selected type list in session
        List<String> types = (List<String>) session.getAttribute("questionTypes");
        if (types == null) {
            types = new ArrayList<>();
            session.setAttribute("questionTypes", types);
        }
        // Ensure list size sync (in case of back navigation etc.)
        if (types.size() >= currentIndex) {
            // replace existing
            types.set(currentIndex - 1, selectedType);
        } else {
            types.add(selectedType);
        }

        if (currentIndex <= totalQuestions) {
            // redirect to question builder for this index
            response.sendRedirect(request.getContextPath() + "/question-builder?index=" + currentIndex + "&type=" + selectedType);
        }
    }
} 