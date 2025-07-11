package DAO;

import models.User;

import java.util.ArrayList;
import java.sql.*;

public class UserSQLDao implements UserDAO{
    Connection connection;
    public UserSQLDao(Connection connection) {
        this.connection = connection;
    }

    public void addUser(User user) {
        String name = user.getName();
        String email = user.getEmail();
        String passHash = user.getPassHash();
        int passedQuizzes = user.getPassedQuizzes();
        boolean isAdmin = user.getIfAdmin();
        boolean isBanned = user.getIfBanned();
        int quizCreatedCount = user.getQuizCreatedCount();
        int quizTakenCount = user.getQuizTakenCount();
        String addUser = "INSERT INTO users (name, email, pass_hash, passed_quizzes, is_admin, is_banned, quiz_created_count, quiz_taken_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(addUser)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, passHash);
            stmt.setInt(4, passedQuizzes);
            stmt.setBoolean(5, isAdmin);
            stmt.setBoolean(6, isBanned);
            stmt.setInt(7, quizCreatedCount);
            stmt.setInt(8, quizTakenCount);
            stmt.executeUpdate();
            System.out.println("User added successfully");
        } catch (SQLException e) {
            System.out.println("Error adding user");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public User getUser(long userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getLong("id"), rs.getString("name"), rs.getString("email"),
                        rs.getString("pass_hash"), rs.getInt("passed_quizzes"),
                        rs.getBoolean("is_admin"), rs.getBoolean("is_banned"), rs.getTimestamp("created_at"),
                        rs.getInt("quiz_created_count"), rs.getInt("quiz_taken_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUser(String username) {
        String sql = "SELECT * FROM users WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getLong("id"), rs.getString("name"), rs.getString("email"),
                        rs.getString("pass_hash"), rs.getInt("passed_quizzes"),
                        rs.getBoolean("is_admin"), rs.getBoolean("is_banned"), rs.getTimestamp("created_at"),
                        rs.getInt("quiz_created_count"), rs.getInt("quiz_taken_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE LOWER(email) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email.toLowerCase().trim());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getLong("id"), rs.getString("name"), rs.getString("email"),
                        rs.getString("pass_hash"), rs.getInt("passed_quizzes"),
                        rs.getBoolean("is_admin"), rs.getBoolean("is_banned"), rs.getTimestamp("created_at"),
                        rs.getInt("quiz_created_count"), rs.getInt("quiz_taken_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<User> getFriends(long userId) {
        ArrayList<User> friends = new ArrayList<>();
        String sql = "SELECT * FROM users u INNER JOIN friendships f ON (f.user_id1 = u.id OR f.user_id2 = u.id) WHERE (f.user_id1 = ? OR f.user_id2 = ?) AND u.id != ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, userId);
            stmt.setLong(3, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                friends.add(new User(rs.getLong("id"), rs.getString("name"), rs.getString("email"),
                        rs.getString("pass_hash"), rs.getInt("passed_quizzes"),
                        rs.getBoolean("is_admin"), rs.getBoolean("is_banned"),  rs.getTimestamp("created_at"),
                        rs.getInt("quiz_created_count"), rs.getInt("quiz_taken_count")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    @Override
    public void addFriendship(long userId1, long userId2) {
        String sql = "INSERT INTO friendships (user_id1, user_id2) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId1);
            stmt.setLong(2, userId2);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}