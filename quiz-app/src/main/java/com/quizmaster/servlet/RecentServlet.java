package com.quizmaster.servlet;

import DAO.QuizDAO;
import models.Quiz;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/recents")
public class RecentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        QuizDAO quizDAO = (QuizDAO) getServletContext().getAttribute("quizDAO");
        ArrayList<Quiz> recentQuizzes = new ArrayList<>();

        if (quizDAO != null) {
            try {
                recentQuizzes = quizDAO.getRecentQuizzes(20);
            } catch (Exception e) {
                System.err.println("Error fetching recent quizzes: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("QuizDAO is not initialized in ServletContext");
        }

        request.setAttribute("recentQuizzes", recentQuizzes);
        request.getRequestDispatcher("/recents.jsp").forward(request, response);
    }
}