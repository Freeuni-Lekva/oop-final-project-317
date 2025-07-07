package DAO;

import models.QuizHistory;

import java.sql.*;
import java.util.ArrayList;

public class QuizHistorySQLDao implements QuizHistoryDAO {

    private Connection connection;

    public QuizHistorySQLDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addQuizHistory(QuizHistory quizHistory) {
        String sql = "INSERT INTO quiz_history (user_id, quiz_id, score, time_taken, completed_date) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, quizHistory.getUserId());
            stmt.setLong(2, quizHistory.getQuizId());
            stmt.setInt(3, quizHistory.getScore());
            stmt.setInt(4, quizHistory.getTimeTaken());
            stmt.setTimestamp(5, Timestamp.valueOf(quizHistory.getCompletedDate()));

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                quizHistory.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<QuizHistory> getUserQuizHistory(long userId) {
        ArrayList<QuizHistory> historyList = new ArrayList<>();
        String sql = "SELECT * FROM quiz_history WHERE user_id = ? ORDER BY completed_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                historyList.add(mapRowToQuizHistory(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historyList;
    }

    @Override
    public ArrayList<QuizHistory> getUserQuizAttempts(long userId, long quizId) {
        ArrayList<QuizHistory> attempts = new ArrayList<>();
        String sql = "SELECT * FROM quiz_history WHERE user_id = ? AND quiz_id = ? ORDER BY completed_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, quizId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                attempts.add(mapRowToQuizHistory(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attempts;
    }

    @Override
    public QuizHistory getBestResult(long userId, long quizId) {
        String sql = "SELECT * FROM quiz_history WHERE user_id = ? AND quiz_id = ? ORDER BY score DESC, time_taken ASC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, quizId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToQuizHistory(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public QuizHistory getLatestAttempt(long userId, long quizId) {
        String sql = "SELECT * FROM quiz_history WHERE user_id = ? AND quiz_id = ? ORDER BY completed_date DESC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, quizId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToQuizHistory(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper method to map ResultSet row to QuizHistory object
    private QuizHistory mapRowToQuizHistory(ResultSet rs) throws SQLException {
        QuizHistory history = new QuizHistory(
                rs.getLong("user_id"),
                rs.getLong("quiz_id"),
                rs.getInt("score"),
                rs.getInt("time_taken")
        );
        history.setId(rs.getLong("id"));
        history.setCompletedDate(rs.getTimestamp("completed_date").toLocalDateTime());
        return history;
    }
}
