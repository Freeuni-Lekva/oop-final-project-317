package DAO;

import models.QuizResult;

import java.sql.SQLException;
import java.util.ArrayList;

public interface QuizResultDAO {

    // Adds a new quiz result to the database
    public void addQuizResult(QuizResult quizResult) throws SQLException;

    // Removes a quiz result from the database
    void removeQuizResult(long quizResultId) throws SQLException;

    // Gets all quiz results for a specific user
    public ArrayList<QuizResult> getUserQuizResults(long userId) throws SQLException;

    // Gets a specific quiz result by ID
    public QuizResult getQuizResult(long quizResultId) throws SQLException;

    // Gets all quiz results for a specific quiz
    public ArrayList<QuizResult> getQuizResults(long quizId) throws SQLException;

    // Gets all quiz results for a specific user on a specific quiz
    public ArrayList<QuizResult> getUserQuizResults(long userId, long quizId) throws SQLException;

    // Gets the top scoring results for a specific quiz
    public ArrayList<QuizResult> getTopScores(long quizId, int limit) throws SQLException;

    // Gets all practice mode results for a specific user
    public ArrayList<QuizResult> getPracticeResults(long userId) throws SQLException;

    // Gets all non-practice mode results for a specific user
    public ArrayList<QuizResult> getOfficialResults(long userId) throws SQLException;
}