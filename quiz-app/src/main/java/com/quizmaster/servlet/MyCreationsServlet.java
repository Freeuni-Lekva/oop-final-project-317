package com.quizmaster.servlet;

import DAO.QuizDAO;
import models.Quiz;
import models.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Displays all quizzes created by the currently logged-in user.
 */
@WebServlet("/my-creations")
public class MyCreationsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // not logged in – redirect to login
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        QuizDAO quizDAO = (QuizDAO) getServletContext().getAttribute("quizDAO");
        if (quizDAO == null) {
            throw new IllegalStateException("QuizDAO not initialized in servlet context");
        }

        List<Quiz> quizzes = quizDAO.getUserQuizzes(currentUser.getId());
        request.setAttribute("quizzes", quizzes);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/my-creations.jsp");
        dispatcher.forward(request, response);
    }
} 