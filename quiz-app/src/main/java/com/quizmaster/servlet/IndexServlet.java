package com.quizmaster.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import DAO.QuizDAO;
import models.Quiz;

@WebServlet("/")
public class IndexServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        DAO.QuizDAO quizDAO = (DAO.QuizDAO) getServletContext().getAttribute("quizDAO");
        if (quizDAO == null) {
            java.sql.Connection conn = (java.sql.Connection) getServletContext().getAttribute("dbConnection");
            if (conn != null) {
                quizDAO = new DAO.QuizSQLDao(conn);
                getServletContext().setAttribute("quizDAO", quizDAO);
            }
        }
        if (quizDAO != null) {
            java.util.List<models.Quiz> quizList = quizDAO.getRecentQuizzes(1000);
            request.setAttribute("quizzes", quizList);
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
} 