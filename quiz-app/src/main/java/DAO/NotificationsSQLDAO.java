package DAO;

import models.Notification;
import notifications.ChallengeNotification;
import notifications.FriendRequestNotification;
import notifications.NoteNotification;

import java.sql.*;
import java.util.ArrayList;

public class NotificationsSQLDAO implements NotificationDAO {

    private Connection connection;

    public NotificationsSQLDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void sendNotification(Notification notification) {
        String sql = "INSERT INTO notifications (from_id, to_id, title, message, question_type) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, notification.getFromUserId());
            stmt.setLong(2, notification.getToUserId());
            stmt.setString(3, notification.getTitle());
            stmt.setString(4, notification.getMessage());
            stmt.setString(5, notification.getQuestionType());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error sending notification", e);
        }
    }

    @Override
    public void deleteNotification(Notification notification) {
        if (notification.getId() == 0) {
            throw new IllegalArgumentException("Notification ID cannot be 0");
        }
        deleteNotification(notification.getId());
    }

    @Override
    public void deleteNotification(long notificationId) {
        String sql = "DELETE FROM notifications WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, notificationId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("No notification found with ID: " + notificationId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting notification", e);
        }
    }

    @Override
    public ArrayList<Notification> getNotifications(long userId) {
        String sql = "SELECT id, from_id, to_id, title, message, question_type, create_date FROM notifications WHERE to_id = ? ORDER BY create_date DESC";
        ArrayList<Notification> notifications = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Notification notification = createNotificationInstance(rs.getString("question_type"));
                    assert notification != null;
                    notification.setId(rs.getLong("id"));
                    notification.setFromUserId(rs.getLong("from_id"));
                    notification.setToUserId(rs.getLong("to_id"));
                    notification.setTitle(rs.getString("title"));
                    notification.setMessage(rs.getString("message"));
                    notification.setQuestionType(rs.getString("question_type"));

                    notifications.add(notification);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving notifications for user: " + userId, e);
        }
        return notifications;
    }

    @Override
    public int getNotificationCount(long userId) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE to_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting notifications for user: " + userId, e);
        }
        return 0;
    }

    @Override
    public String getSenderUsername(long fromUserId) {
        String sql = "SELECT name FROM users WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, fromUserId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving sender username: " + fromUserId, e);
        }
        return "Unknown User";
    }

    private Notification createNotificationInstance(String questionType) {
        switch (questionType) {
            case Notification.NOTE_NOTIFICATION:
                return new NoteNotification();
            case Notification.CHALLENGE_NOTIFICATION:
                return new ChallengeNotification();
            case Notification.FRIEND_REQUEST_NOTIFICATION:
                return new FriendRequestNotification();
            default:
                return null;
        }
    }
}