package com.quizmaster.servlet;

import DAO.UserDAO;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/friends")
public class FriendsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        UserDAO userDAO = (UserDAO) getServletContext().getAttribute("userSqlDao");

        if (userDAO == null) {
            request.setAttribute("error", "Service temporarily unavailable");
            request.getRequestDispatcher("/friends.jsp").forward(request, response);
            return;
        }

        try {
            ArrayList<User> friends = userDAO.getFriends(currentUser.getId());
            request.setAttribute("friends", friends);
            request.setAttribute("hasFriends", friends != null && !friends.isEmpty());

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Unable to load friends list");
        }
        request.getRequestDispatcher("/friends.jsp").forward(request, response);
    }
}