package com.quizmaster.servlet;

import DAO.QuizDAO;
import DAO.UserDAO;
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

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String searchTerm = request.getParameter("q");

        // If no search term, redirect to home
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        searchTerm = searchTerm.trim();

        // Get DAOs from servlet context
        QuizDAO quizDAO = (QuizDAO) getServletContext().getAttribute("quizDAO");
        UserDAO userDAO = (UserDAO) getServletContext().getAttribute("userSqlDao");

        if (quizDAO == null || userDAO == null) {
            request.setAttribute("error", "Search service is temporarily unavailable. Please try again later.");
            request.getRequestDispatcher("/search-results.jsp").forward(request, response);
            return;
        }

        try {
            // Search for quizzes (limit to 20 results)
            ArrayList<Quiz> quizResults = quizDAO.searchQuizzes(searchTerm, 20);

            // Search for users (limit to 10 results)
            ArrayList<User> userResults = userDAO.searchUsers(searchTerm, 10);

            // Remove current user from search results
            User currentUser = (User) session.getAttribute("user");
            if (userResults != null && currentUser != null) {
                userResults.removeIf(user -> user.getId() == currentUser.getId());
            }

            // Set attributes for JSP
            request.setAttribute("searchTerm", searchTerm);
            request.setAttribute("quizResults", quizResults);
            request.setAttribute("userResults", userResults);
            request.setAttribute("totalResults",
                    (quizResults != null ? quizResults.size() : 0) +
                            (userResults != null ? userResults.size() : 0));

            // Forward to search results page
            request.getRequestDispatcher("/search-results.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while searching. Please try again.");
            request.setAttribute("searchTerm", searchTerm);
            request.getRequestDispatcher("/search-results.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle POST requests the same way as GET
        doGet(request, response);
    }
}