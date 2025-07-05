package DAO;

import models.QuizResult;
import java.sql.*;
import java.util.ArrayList;

public class QuizResultSQLDAO implements QuizResultDAO {

    private static final String TABLE_NAME = "quiz_results";

    private Connection connection;

    public QuizResultSQLDAO(Connection connection) {
        this.connection = connection;
    }

    @Override

    public void addQuizResult(QuizResult quizResult) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " (user_id, quiz_id, score, total_questions, total_points, max_points, " +
                        "is_practice_mode) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, quizResult.getUserId());
            statement.setLong(2, quizResult.getQuizId());
            statement.setInt(3, quizResult.getScore());
            statement.setInt(4, quizResult.getTotalQuestions());
            statement.setInt(5, quizResult.getTotalPoints());
            statement.setInt(6, quizResult.getMaxPoints());
            statement.setBoolean(7, quizResult.isPracticeMode());

            statement.executeUpdate();

            // Get the generated ID
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    quizResult.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    @Override
    public void removeQuizResult(long quizResultId) throws SQLException {
        String sql =
                "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, quizResultId);
            statement.executeUpdate();
        }
    }

    @Override
    public ArrayList<QuizResult> getUserQuizResults(long userId) throws SQLException {
        ArrayList<QuizResult> results = new ArrayList<>();
        String sql =
                "SELECT * FROM " + TABLE_NAME + " WHERE user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    results.add(mapResultSetToQuizResult(rs));
                }
            }
        }
        return results;
    }

    @Override
    public QuizResult getQuizResult(long quizResultId) throws SQLException {
        String sql =
                "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, quizResultId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToQuizResult(rs);
                }
            }
        }
        return null;
    }

    @Override
    public ArrayList<QuizResult> getQuizResults(long quizId) throws SQLException {
        ArrayList<QuizResult> results = new ArrayList<>();
        String sql =
                "SELECT * FROM " + TABLE_NAME + " WHERE quiz_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, quizId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    results.add(mapResultSetToQuizResult(rs));
                }
            }
        }
        return results;
    }

    @Override
    public ArrayList<QuizResult> getUserQuizResults(long userId, long quizId) throws SQLException {
        ArrayList<QuizResult> results = new ArrayList<>();
        String sql =
                "SELECT * FROM " + TABLE_NAME + " WHERE user_id = ? AND quiz_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setLong(2, quizId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    results.add(mapResultSetToQuizResult(rs));
                }
            }
        }
        return results;
    }

    @Override
    public ArrayList<QuizResult> getTopScores(long quizId, int limit) throws SQLException {
        ArrayList<QuizResult> results = new ArrayList<>();
        String sql =
                "SELECT * FROM " + TABLE_NAME + " WHERE quiz_id = ? AND is_completed = true " +
                        "ORDER BY score DESC, completion_time_seconds ASC LIMIT ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, quizId);
            statement.setInt(2, limit);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    results.add(mapResultSetToQuizResult(rs));
                }
            }
        }
        return results;
    }

    @Override
    public ArrayList<QuizResult> getPracticeResults(long userId) throws SQLException {
        ArrayList<QuizResult> results = new ArrayList<>();
        String sql =
                "SELECT * FROM " + TABLE_NAME + " WHERE user_id = ? AND is_practice_mode = true";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    results.add(mapResultSetToQuizResult(rs));
                }
            }
        }
        return results;
    }

    @Override
    public ArrayList<QuizResult> getOfficialResults(long userId) throws SQLException {
        ArrayList<QuizResult> results = new ArrayList<>();
        String sql =
                "SELECT * FROM " + TABLE_NAME + " WHERE user_id = ? AND is_practice_mode = false";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    results.add(mapResultSetToQuizResult(rs));
                }
            }
        }
        return results;
    }

    /**
     * Maps a ResultSet row to a QuizResult object
     */
    private QuizResult mapResultSetToQuizResult(ResultSet rs) throws SQLException {
        QuizResult quizResult = new QuizResult(
                rs.getLong("user_id"),
                rs.getLong("quiz_id"),
                rs.getInt("total_questions"),
                rs.getInt("max_points")
        );

        quizResult.setId(rs.getLong("id"));
        quizResult.setScore(rs.getInt("score"));
        quizResult.setTotalPoints(rs.getInt("total_points"));

        quizResult.setPracticeMode(rs.getBoolean("is_practice_mode"));

        return quizResult;
    }
}