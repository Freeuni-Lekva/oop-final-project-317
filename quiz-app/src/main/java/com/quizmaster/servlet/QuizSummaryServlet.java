package com.quizmaster.servlet;

import DAO.QuizDAO;
import DAO.QuizResultDAO;
import DAO.UserDAO;
import models.Quiz;
import models.QuizResult;
import models.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@WebServlet("/QuizSummary")
public class QuizSummaryServlet extends HttpServlet {
    private QuizDAO quizDAO;
    private QuizResultDAO quizResultDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Attempt initial lookup; if not yet available, will retry in doGet
        this.quizDAO = (QuizDAO) getServletContext().getAttribute("quizDAO");
        this.quizResultDAO = (QuizResultDAO) getServletContext().getAttribute("quizResultDAO");
        this.userDAO = (UserDAO) getServletContext().getAttribute("userDAO");
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

        HttpSession session = request.getSession();
        // Lazy fetch DAOs if init didn't have them yet
        if (quizDAO == null) quizDAO = (QuizDAO) getServletContext().getAttribute("quizDAO");
        if (quizResultDAO == null) quizResultDAO = (QuizResultDAO) getServletContext().getAttribute("quizResultDAO");
        if (userDAO == null) userDAO = (UserDAO) getServletContext().getAttribute("userDAO");

        if (quizDAO == null || quizResultDAO == null || userDAO == null) {
            java.sql.Connection conn = (java.sql.Connection) getServletContext().getAttribute("dbConnection");
            if (conn != null) {
                if (quizDAO == null) quizDAO = new DAO.QuizSQLDao(conn);
                if (quizResultDAO == null) quizResultDAO = new DAO.QuizResultSQLDAO(conn);
                if (userDAO == null) userDAO = new DAO.UserSQLDao(conn);
            }
        }

        if (quizDAO == null || quizResultDAO == null || userDAO == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DAO initialization problem after fallback");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        Long currentUserId = currentUser != null ? currentUser.getId() : null;

        String sortBy = request.getParameter("sortBy");
        if (sortBy == null) {
            sortBy = "date";
        }

        try {
            Quiz quiz = quizDAO.getQuiz(quizId);
            if (quiz == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Quiz not found");
                return;
            }

            User creator = userDAO.getUser(quiz.getCreatedBy());

            List<QuizResult> allResults = quizResultDAO.getQuizResults(quizId);
            // Filter out practice mode
            List<QuizResult> officialResults = new ArrayList<>();
            for (QuizResult result : allResults) {
                if (!result.isPracticeMode()) {
                    officialResults.add(result);
                }
            }

            List<QuizResult> userResults = new ArrayList<>();
            if (currentUserId != null) {
                userResults = quizResultDAO.getUserQuizResults(currentUserId, quizId);
                sortUserResults(userResults, sortBy);
            }

            List<QuizResult> topPerformersAllTime = getTopPerformers(officialResults, 10);
            List<QuizResult> topPerformersLastDay = getTopPerformersLastDay(officialResults, 10);
            List<QuizResult> recentPerformers = getRecentPerformers(officialResults, 20);
            QuizStats quizStats = calculateQuizStatistics(officialResults);

            boolean isOwner = currentUserId != null && currentUserId.equals(quiz.getCreatedBy());

            request.setAttribute("quiz", quiz);
            request.setAttribute("creator", creator);
            request.setAttribute("userResults", userResults);
            request.setAttribute("topPerformersAllTime", topPerformersAllTime);
            request.setAttribute("topPerformersLastDay", topPerformersLastDay);
            request.setAttribute("recentPerformers", recentPerformers);
            request.setAttribute("quizStats", quizStats);
            request.setAttribute("isOwner", isOwner);
            request.setAttribute("currentUser", currentUser);
            request.setAttribute("sortBy", sortBy);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/quizSummary.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Database error occurred while loading quiz summary");
        }
    }

    private void sortUserResults(List<QuizResult> userResults, String sortBy) {
        switch (sortBy.toLowerCase()) {
            case "percentage":
                Collections.sort(userResults, new Comparator<QuizResult>() {
                    @Override
                    public int compare(QuizResult a, QuizResult b) {
                        return Double.compare(b.getPercentage(), a.getPercentage());
                    }
                });
                break;
            case "score":
                Collections.sort(userResults, new Comparator<QuizResult>() {
                    @Override
                    public int compare(QuizResult a, QuizResult b) {
                        return Integer.compare(b.getScore(), a.getScore());
                    }
                });
                break;
            case "date":
            default:
                Collections.reverse(userResults);
                break;
        }
    }

    private List<QuizResult> getTopPerformers(List<QuizResult> results, int limit) {
        List<QuizResult> sorted = new ArrayList<>(results);
        Collections.sort(sorted, new Comparator<QuizResult>() {
            @Override
            public int compare(QuizResult a, QuizResult b) {
                int cmp = Double.compare(b.getPercentage(), a.getPercentage());
                if (cmp != 0) return cmp;
                return Integer.compare(b.getScore(), a.getScore());
            }
        });
        return sorted.subList(0, Math.min(limit, sorted.size()));
    }

    private List<QuizResult> getTopPerformersLastDay(List<QuizResult> results, int limit) {
        // Adjust this method if you need to filter by date
        return getTopPerformers(results, limit);
    }

    private List<QuizResult> getRecentPerformers(List<QuizResult> results, int limit) {
        List<QuizResult> recent = new ArrayList<>();
        for (int i = 0; i < results.size() && i < limit; i++) {
            recent.add(results.get(i));
        }
        return recent;
    }

    private QuizStats calculateQuizStatistics(List<QuizResult> results) {
        if (results.isEmpty()) {
            return new QuizStats(0, 0.0, 0.0, 0.0, 0.0);
        }

        int totalAttempts = results.size();
        double sumPercentage = 0.0;
        double maxPercentage = Double.MIN_VALUE;
        double minPercentage = Double.MAX_VALUE;
        List<Double> percentages = new ArrayList<>();

        for (QuizResult result : results) {
            double pct = result.getPercentage();
            sumPercentage += pct;
            if (pct > maxPercentage) maxPercentage = pct;
            if (pct < minPercentage) minPercentage = pct;
            percentages.add(pct);
        }

        double averagePercentage = sumPercentage / totalAttempts;
        Collections.sort(percentages);

        double medianPercentage;
        int size = percentages.size();
        if (size % 2 == 0) {
            medianPercentage = (percentages.get(size / 2 - 1) + percentages.get(size / 2)) / 2.0;
        } else {
            medianPercentage = percentages.get(size / 2);
        }

        return new QuizStats(totalAttempts, averagePercentage, maxPercentage, minPercentage, medianPercentage);
    }

    public static class QuizStats {
        private int totalAttempts;
        private double averagePercentage;
        private double maxPercentage;
        private double minPercentage;
        private double medianPercentage;

        public QuizStats(int totalAttempts, double averagePercentage, double maxPercentage,
                         double minPercentage, double medianPercentage) {
            this.totalAttempts = totalAttempts;
            this.averagePercentage = averagePercentage;
            this.maxPercentage = maxPercentage;
            this.minPercentage = minPercentage;
            this.medianPercentage = medianPercentage;
        }

        public int getTotalAttempts() { return totalAttempts; }
        public double getAveragePercentage() { return averagePercentage; }
        public double getMaxPercentage() { return maxPercentage; }
        public double getMinPercentage() { return minPercentage; }
        public double getMedianPercentage() { return medianPercentage; }
    }
}
