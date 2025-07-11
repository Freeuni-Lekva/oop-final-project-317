package DAO;

import models.Quiz;

import java.util.ArrayList;

public interface QuizDAO {
    void addQuiz(Quiz quiz);

    void removeQuiz(Quiz quiz);

    void updateQuiz(Quiz quiz);

    ArrayList<Quiz> getUserQuizzes(long userId);

    Quiz getQuiz(long quizId);

    ArrayList<Quiz> getRecentQuizzes(int limit);

    void incrementQuizCompletions(long quizId);

    ArrayList<Quiz> getAllQuizzes();

    ArrayList<Quiz> searchQuizzes(String searchTerm, int limit);

    ArrayList<Quiz> getAllQuizzes(int limit);
}
