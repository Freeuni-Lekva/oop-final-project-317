package DAO;


import models.Quiz;

import java.util.ArrayList;

public interface QuizDAO {
    public void addQuiz(Quiz quiz);

    public void removeQuiz(Quiz quiz);

    public ArrayList<Quiz> getUserQuizzes(long userId);

    public Quiz getQuiz(long quizId);
}
