package DAO;

import models.Question;
import questions.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionSQLDao implements QuestionDAO {
    private Connection connection;

    public QuestionSQLDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addQuestion(Question question) {
        String query = "INSERT INTO questions (question_text, question_type, quiz_id, correct_answers, " +
                "image_url, points, time_limit) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, question.getQuestionText());
            stmt.setString(2, question.getQuestionType());
            stmt.setLong(3, question.getQuizId());
            stmt.setString(4, convertAnswersToString(question.getCorrectAnswers()));
            stmt.setString(5, question.getImageUrl());
            stmt.setInt(6, question.getPoints());
            stmt.setInt(7, question.getTimeLimit());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    question.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeQuestion(Question question) {
        String query = "DELETE FROM questions WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, question.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Question getQuestion(long questionId) {
        String query = "SELECT * FROM questions WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, questionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractQuestionFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Question> getQuizQuestions(long quizId) {
        ArrayList<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM questions WHERE quiz_id = ? ORDER BY id";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, quizId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Question question = extractQuestionFromResultSet(rs);
                if (question != null) {
                    questions.add(question);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    // Helper method to convert a ResultSet row into a Question object
    private Question extractQuestionFromResultSet(ResultSet rs) throws SQLException {
        String questionType = rs.getString("question_type");
        String questionText = rs.getString("question_text");
        List<String> correctAnswers = convertStringToAnswers(rs.getString("correct_answers"));
        String imageUrl = rs.getString("image_url");

        Question question = createQuestionInstance(questionType, questionText, correctAnswers, imageUrl);
        if (question != null) {
            question.setId(rs.getLong("id"));
            question.setQuizId(rs.getLong("quiz_id"));
            question.setImageUrl(imageUrl);
            question.setPoints(rs.getInt("points"));
            question.setTimeLimit(rs.getInt("time_limit"));
        }
        return question;
    }

    // Helper method to create the correct question instance based on type
    private Question createQuestionInstance(String questionType, String questionText, List<String> correctAnswers, String imageUrl) {
        switch (questionType) {
            case Question.QUESTION_RESPONSE:
                return new QuestionResponseQuestion(questionText, correctAnswers);
            case Question.MULTIPLE_CHOICE:
                // For multiple choice, we assume the first answer is the correct one and create dummy options
                String correctAnswer = correctAnswers.isEmpty() ? "" : correctAnswers.get(0);
                return new MultipleChoiceQuestion(questionText, correctAnswers, correctAnswer);
            case Question.FILL_IN_BLANK:
                return new FillInBlankQuestion(questionText, correctAnswers);
            case Question.PICTURE_RESPONSE:
                return new PictureResponseQuestion(questionText, imageUrl != null ? imageUrl : "", correctAnswers);
            case Question.MULTI_ANSWER:
                return new MultiAnswerQuestion(questionText, correctAnswers, true);
            case Question.MULTIPLE_CHOICE_MULTIPLE_ANSWERS:
                // For this type, we treat correctAnswers as both options and correct answers
                return new MultipleChoiceMultipleAnswersQuestion(questionText, correctAnswers, correctAnswers);
            default:
                return null;
        }
    }

    // Helper method to convert answers list to string for storage
    private String convertAnswersToString(List<String> answers) {
        if (answers == null || answers.isEmpty()) {
            return "";
        }
        return String.join("||", answers);
    }

    // Helper method to convert stored string back to answers list
    private List<String> convertStringToAnswers(String answersString) {
        if (answersString == null || answersString.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(answersString.split("\\|\\|")));
    }
} 