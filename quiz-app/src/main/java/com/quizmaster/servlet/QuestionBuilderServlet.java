package com.quizmaster.servlet;

import DAO.QuestionSQLDao;
import models.Question;
import questions.*;
import javax.servlet.ServletContext;
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
            // finished all questions – persist them to DB then show confirmation page

            Long quizId = (Long) session.getAttribute("currentQuizId");
            if (quizId != null) {
                ServletContext ctx = getServletContext();
                QuestionSQLDao questionDAO = (QuestionSQLDao) ctx.getAttribute("questionDAO");
                if (questionDAO == null) {
                    java.sql.Connection conn = (java.sql.Connection) ctx.getAttribute("dbConnection");
                    if (conn != null) {
                        questionDAO = new QuestionSQLDao(conn);
                        ctx.setAttribute("questionDAO", questionDAO);
                    }
                }

                if (questionDAO != null) {
                    // iterate over collected data and save
                    @SuppressWarnings("unchecked")
                    List<Map<String,String[]>> allData = (List<Map<String,String[]>>) session.getAttribute("questionsData");
                    if (allData != null) {
                        for (Map<String,String[]> dataMap : allData) {
                            Question q = buildQuestionFromMap(dataMap, quizId);
                            if (q != null) {
                                questionDAO.addQuestion(q);
                            }
                        }
                    }
                }
            }

            // finished – show confirmation page
            response.sendRedirect(request.getContextPath() + "/quiz-created.jsp");
        }
    }

    /**
     * Converts the raw parameter map for one question into a concrete Question instance.
     */
    private Question buildQuestionFromMap(Map<String,String[]> map, Long quizId) {
        if (map == null) return null;
        String type = map.getOrDefault("type", new String[]{""})[0];
        String prompt = map.getOrDefault("prompt", new String[]{""})[0];
        int points = 1;
        int timeLimitMinutes = 0;
        try { points = Integer.parseInt(map.getOrDefault("points", new String[]{"1"})[0]); } catch (NumberFormatException ignored) {}
        try { timeLimitMinutes = Integer.parseInt(map.getOrDefault("timeLimit", new String[]{"0"})[0]); } catch (NumberFormatException ignored) {}
        int timeLimit = timeLimitMinutes * 60;

        switch (type) {
            case "MultipleChoice": {
                List<String> options = collectSequentialValues(map, "option");
                int correctIdx = 0;
                try { correctIdx = Integer.parseInt(map.getOrDefault("answer", new String[]{"1"})[0]) - 1; } catch (NumberFormatException ignored) {}
                if (correctIdx >=0 && correctIdx < options.size()) {
                    // move correct answer to first position so DAO can recognise it
                    java.util.Collections.swap(options, 0, correctIdx);
                }
                String correctAns = options.get(0);
                MultipleChoiceQuestion q = new MultipleChoiceQuestion(prompt, options, correctAns);
                q.setPoints(points);
                q.setTimeLimit(timeLimit);
                q.setQuizId(quizId);
                return q;
            }
            case "MultipleChoiceMultipleAnswers": {
                List<String> options = collectSequentialValues(map, "option");
                String[] ansIdxArr = map.get("answers");
                List<String> correct = new java.util.ArrayList<>();
                if (ansIdxArr != null) {
                    for (String idxStr : ansIdxArr) {
                        try {
                            int i = Integer.parseInt(idxStr) - 1;
                            if (i >=0 && i < options.size()) correct.add(options.get(i));
                        } catch (NumberFormatException ignored) {}
                    }
                }
                MultipleChoiceMultipleAnswersQuestion q = new MultipleChoiceMultipleAnswersQuestion(prompt, options, correct);
                q.setPoints(points);
                q.setTimeLimit(timeLimit);
                q.setQuizId(quizId);
                return q;
            }
            case "FillInBlank": {
                String answer = map.getOrDefault("answer", new String[]{""})[0];
                FillInBlankQuestion q = new FillInBlankQuestion(prompt, java.util.Collections.singletonList(answer));
                q.setPoints(points);
                q.setTimeLimit(timeLimit);
                q.setQuizId(quizId);
                return q;
            }
            case "QuestionResponse": {
                String answer = map.getOrDefault("answer", new String[]{""})[0];
                QuestionResponseQuestion q = new QuestionResponseQuestion(prompt, java.util.Collections.singletonList(answer));
                q.setPoints(points);
                q.setTimeLimit(timeLimit);
                q.setQuizId(quizId);
                return q;
            }
            case "PictureResponse": {
                String imgUrl = map.getOrDefault("imageUrl", new String[]{""})[0];
                String answer = map.getOrDefault("answer", new String[]{""})[0];
                PictureResponseQuestion q = new PictureResponseQuestion(prompt, imgUrl, java.util.Collections.singletonList(answer));
                q.setPoints(points);
                q.setTimeLimit(timeLimit);
                q.setQuizId(quizId);
                return q;
            }
            case "MultiAnswer": {
                String answersRaw = map.getOrDefault("answers", new String[]{""})[0];
                List<String> answers = java.util.Arrays.asList(answersRaw.split("\n"));
                MultiAnswerQuestion q = new MultiAnswerQuestion(prompt, answers, true);
                q.setPoints(points);
                q.setTimeLimit(timeLimit);
                q.setQuizId(quizId);
                return q;
            }
            default:
                return null;
        }
    }

    /**
     * Helper that collects sequential parameters like option1, option2 … into a list in order.
     */
    private List<String> collectSequentialValues(Map<String,String[]> map, String prefix) {
        List<String> list = new java.util.ArrayList<>();
        int i = 1;
        while (true) {
            String key = prefix + i;
            if (!map.containsKey(key)) break;
            String val = map.get(key)[0];
            if (val != null && !val.trim().isEmpty()) list.add(val.trim());
            i++;
        }
        return list;
    }
} 