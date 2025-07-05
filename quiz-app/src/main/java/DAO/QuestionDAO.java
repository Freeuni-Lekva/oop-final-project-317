package DAO;

import models.Question;

import java.util.ArrayList;

public interface QuestionDAO {
    public void addQuestion(Question question);

    public void removeQuestion(Question question);

    public Question getQuestion(long questionId);

    public ArrayList<Question> getQuizQuestions(long quizId);
}
