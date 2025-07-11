package com.quizmaster.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/question-builder")
public class QuestionBuilderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("numQuestions") == null) {
            response.sendRedirect(request.getContextPath() + "/create-quiz.jsp");
            return;
        }
        int total = (Integer) session.getAttribute("numQuestions");
        int index = Integer.parseInt(request.getParameter("index"));
        String type = request.getParameter("type");
        if (type == null) {
            response.sendRedirect(request.getContextPath() + "/question-type?index=" + index);
            return;
        }
        request.setAttribute("currentIndex", index);
        request.setAttribute("totalQuestions", total);
        request.setAttribute("questionType", type);
        request.getRequestDispatcher("/question-builder.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int index = Integer.parseInt(request.getParameter("currentIndex"));
        int total = (Integer) session.getAttribute("numQuestions");
        String type = request.getParameter("questionType");

        // collect basic data (for demonstration only)
        Map<String, String[]> qData = new HashMap<>();
        qData.put("type", new String[]{type});
        qData.putAll(request.getParameterMap());

        List<Map<String, String[]>> questions = (List<Map<String, String[]>>) session.getAttribute("questionsData");
        if (questions == null) {
            questions = new ArrayList<>();
            session.setAttribute("questionsData", questions);
        }
        if (questions.size() >= index) {
            questions.set(index - 1, qData);
        } else {
            questions.add(qData);
        }

        if (index < total) {
            // proceed to next question-type selection page
            response.sendRedirect(request.getContextPath() + "/question-type?index=" + (index + 1));
        } else {
            // finished all questions – show confirmation page
            response.sendRedirect(request.getContextPath() + "/quiz-created.jsp");
        }
    }
} 