package DAO;

import models.QuizHistory;

import java.util.ArrayList;

public interface QuizHistoryDAO {

    public void addQuizHistory(QuizHistory quizHistory);

    public ArrayList<QuizHistory> getUserQuizHistory(long userId);

    public ArrayList<QuizHistory> getUserQuizAttempts(long userId, long quizId);

    public QuizHistory getBestResult(long userId, long quizId);

    public QuizHistory getLatestAttempt(long userId, long quizId);
}
