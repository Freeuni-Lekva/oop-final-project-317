package DAO;

import models.Quiz;

import java.sql.*;
import java.util.ArrayList;

public class QuizSQLDao implements QuizDAO {
    private Connection connection;

    public QuizSQLDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addQuiz(Quiz quiz) {
        String query = "INSERT INTO quizzes (title, description, created_by, created_date, last_modified, " +
                "randomize_questions, one_page, immediate_correction, practice_mode, time_limit) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, quiz.getTitle());
            stmt.setString(2, quiz.getDescription());
            stmt.setLong(3, quiz.getCreatedBy());
            stmt.setTimestamp(4, Timestamp.valueOf(quiz.getCreatedDate()));
            stmt.setTimestamp(5, Timestamp.valueOf(quiz.getLastModified()));
            stmt.setBoolean(6, quiz.isRandomizeQuestions());
            stmt.setBoolean(7, quiz.isOnePage());
            stmt.setBoolean(8, quiz.isImmediateCorrection());
            stmt.setBoolean(9, quiz.isPracticeMode());
            stmt.setInt(10, quiz.getTimeLimit());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    quiz.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeQuiz(Quiz quiz) {
        String query = "DELETE FROM quizzes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, quiz.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Quiz> getUserQuizzes(long userId) {
        return getUserQuizzes(Long.valueOf(userId));
    }

    @Override
    public ArrayList<Quiz> getUserQuizzes(Long userId) {
        ArrayList<Quiz> quizzes = new ArrayList<>();
        String query = "SELECT * FROM quizzes WHERE created_by = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Quiz quiz = extractQuizFromResultSet(rs);
                quizzes.add(quiz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    @Override
    public Quiz getQuiz(long quizId) {
        String query = "SELECT * FROM quizzes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, quizId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractQuizFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateQuiz(Quiz quiz) {
        String query = "UPDATE quizzes SET title = ?, description = ?, last_modified = ?, " +
                "randomize_questions = ?, one_page = ?, immediate_correction = ?, practice_mode = ?, time_limit = ? " +
                "WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, quiz.getTitle());
            stmt.setString(2, quiz.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(quiz.getLastModified()));
            stmt.setBoolean(4, quiz.isRandomizeQuestions());
            stmt.setBoolean(5, quiz.isOnePage());
            stmt.setBoolean(6, quiz.isImmediateCorrection());
            stmt.setBoolean(7, quiz.isPracticeMode());
            stmt.setInt(8, quiz.getTimeLimit());
            stmt.setLong(9, quiz.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to convert a ResultSet row into a Quiz object
    private Quiz extractQuizFromResultSet(ResultSet rs) throws SQLException {
        Quiz quiz = new Quiz(
                rs.getString("title"),
                rs.getString("description"),
                rs.getLong("created_by")
        );

        quiz.setId(rs.getLong("id"));
        quiz.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
        quiz.setLastModified(rs.getTimestamp("last_modified").toLocalDateTime());
        quiz.setRandomizeQuestions(rs.getBoolean("randomize_questions"));
        quiz.setOnePage(rs.getBoolean("one_page"));
        quiz.setImmediateCorrection(rs.getBoolean("immediate_correction"));
        quiz.setPracticeMode(rs.getBoolean("practice_mode"));
        try {
            quiz.setTimeLimit(rs.getInt("time_limit"));
        } catch (SQLException ignore) {}
        return quiz;
    }

    @Override
    public ArrayList<Quiz> getRecentQuizzes(int limit) {
        ArrayList<Quiz> quizzes = new ArrayList<>();
        String query = "SELECT * FROM quizzes ORDER BY created_date DESC LIMIT ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Quiz quiz = extractQuizFromResultSet(rs);
                quizzes.add(quiz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }
}