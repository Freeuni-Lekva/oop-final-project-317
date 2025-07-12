package DAO;

import models.User;

import java.sql.*;
import java.util.ArrayList;

public class UserSQLDao implements UserDAO {
    private Connection connection;

    public UserSQLDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addUser(User user) {
        String sql = "INSERT INTO users (name, email, pass_hash, passed_quizzes, is_admin, is_banned, created_at, quiz_created_count, quiz_taken_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassHash());
            stmt.setInt(4, user.getPassedQuizzes());
            stmt.setBoolean(5, user.getIfAdmin());
            stmt.setBoolean(6, user.getIfBanned());
            stmt.setTimestamp(7, user.getCreatedAt());
            stmt.setInt(8, user.getQuizCreatedCount());
            stmt.setInt(9, user.getQuizTakenCount());
            stmt.executeUpdate();
            System.out.println("User added successfully");
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public User getUser(long userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
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
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
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
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<User> getFriends(long userId) {
        ArrayList<User> friends = new ArrayList<>();
        String sql = "SELECT DISTINCT u.* FROM users u " +
                     "JOIN friendships f ON (f.user_id1 = u.id OR f.user_id2 = u.id) " +
                     "WHERE (f.user_id1 = ? OR f.user_id2 = ?) AND u.id != ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, userId);
            stmt.setLong(3, userId);
            System.out.println("UserSQLDao.getFriends: Executing query for userId=" + userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User friend = mapUser(rs);
                    friends.add(friend);
                    System.out.println("UserSQLDao.getFriends: Found friend: " + friend.getName() + " (ID: " + friend.getId() + ")");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("UserSQLDao.getFriends: Total friends found: " + friends.size());
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

    @Override
    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void removeUser(User user) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<User> searchUsers(String searchTerm, int limit) {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE LOWER(name) LIKE ? OR LOWER(email) LIKE ? ORDER BY name LIMIT ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String pattern = "%" + searchTerm.toLowerCase().trim() + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setInt(3, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapUser(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean checkIfFriends(long userId1, long userId2) {
        String sql = "SELECT COUNT(*) AS cnt FROM friendships WHERE (user_id1 = ? AND user_id2 = ?) OR (user_id1 = ? AND user_id2 = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId1);
            stmt.setLong(2, userId2);
            stmt.setLong(3, userId2);
            stmt.setLong(4, userId1);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void removeFriendship(long userId1, long userId2) {
        String sql = "DELETE FROM friendships WHERE (user_id1 = ? AND user_id2 = ?) OR (user_id1 = ? AND user_id2 = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId1);
            stmt.setLong(2, userId2);
            stmt.setLong(3, userId2);
            stmt.setLong(4, userId1);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("pass_hash"),
                rs.getInt("passed_quizzes"),
                rs.getBoolean("is_admin"),
                rs.getBoolean("is_banned"),
                rs.getTimestamp("created_at"),
                rs.getInt("quiz_created_count"),
                rs.getInt("quiz_taken_count")
        );
    }
}
