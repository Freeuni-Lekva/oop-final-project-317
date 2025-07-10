package DAO;

import models.Notification;
import notifications.NoteNotification;
import notifications.ChallengeNotification;
import notifications.FriendRequestNotification;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class NotificationsSQLDAOTest {
    private static Connection connection;
    private NotificationsSQLDAO dao;

    @BeforeAll
    static void setupDatabase() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/quizmastertest_db", "root", "your-password");
    }

    @AfterAll
    static void tearDownDatabase() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @BeforeEach
    void setUp() {
        dao = new NotificationsSQLDAO(connection);
        // Create notifications table if it does not exist
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS notifications (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "from_id BIGINT NOT NULL, " +
                    "to_id BIGINT NOT NULL, " +
                    "title VARCHAR(200) NOT NULL, " +
                    "message TEXT, " +
                    "question_type VARCHAR(100), " +
                    "create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (from_id) REFERENCES users(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (to_id) REFERENCES users(id) ON DELETE CASCADE" +
                    ")");
            // Clean up table before each test
            stmt.execute("DELETE FROM notifications");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSendAndGetNotification_Note() {
        NoteNotification note = new NoteNotification(1L, 2L, "Title1", "Message1");
        dao.sendNotification(note);
        List<Notification> notifications = dao.getNotifications(2L);
        assertEquals(1, notifications.size());
        Notification n = notifications.get(0);
        assertEquals("Title1", n.getTitle());
        assertEquals("Message1", n.getMessage());
        assertEquals(1L, n.getFromUserId());
        assertEquals(2L, n.getToUserId());
        assertEquals(Notification.NOTE_NOTIFICATION, n.getQuestionType());
    }

    @Test
    void testSendAndGetNotification_Challenge() {
        ChallengeNotification challenge = new ChallengeNotification(3L, 4L, "Challenge!", "You are challenged!");
        dao.sendNotification(challenge);
        List<Notification> notifications = dao.getNotifications(4L);
        assertEquals(1, notifications.size());
        Notification n = notifications.get(0);
        assertEquals("Challenge!", n.getTitle());
        assertEquals("You are challenged!", n.getMessage());
        assertEquals(3L, n.getFromUserId());
        assertEquals(4L, n.getToUserId());
        assertEquals(Notification.CHALLENGE_NOTIFICATION, n.getQuestionType());
    }

    @Test
    void testSendAndGetNotification_FriendRequest() {
        FriendRequestNotification fr = new FriendRequestNotification(5L, 6L, "Friend?", "Let's be friends!");
        dao.sendNotification(fr);
        List<Notification> notifications = dao.getNotifications(6L);
        assertEquals(1, notifications.size());
        Notification n = notifications.get(0);
        assertEquals("Friend?", n.getTitle());
        assertEquals("Let's be friends!", n.getMessage());
        assertEquals(5L, n.getFromUserId());
        assertEquals(6L, n.getToUserId());
        assertEquals(Notification.FRIEND_REQUEST_NOTIFICATION, n.getQuestionType());
    }

    @Test
    void testDeleteNotificationByObject() {
        NoteNotification note = new NoteNotification(1L, 2L, "Title", "Message");
        dao.sendNotification(note);
        List<Notification> notifications = dao.getNotifications(2L);
        assertEquals(1, notifications.size());
        Notification n = notifications.get(0);
        n.setId(getNotificationIdFromDb());
        dao.deleteNotification(n);
        List<Notification> afterDelete = dao.getNotifications(2L);
        assertTrue(afterDelete.isEmpty());
    }

    @Test
    void testDeleteNotificationById() {
        NoteNotification note = new NoteNotification(1L, 2L, "Title", "Message");
        dao.sendNotification(note);
        long id = getNotificationIdFromDb();
        dao.deleteNotification(id);
        List<Notification> afterDelete = dao.getNotifications(2L);
        assertTrue(afterDelete.isEmpty());
    }

    @Test
    void testDeleteNotificationThrowsForMissingId() {
        NoteNotification note = new NoteNotification(1L, 2L, "Title", "Message");
        // Not setting ID, should throw
        Exception ex = assertThrows(IllegalArgumentException.class, () -> dao.deleteNotification(note));
        assertTrue(ex.getMessage().contains("Notification ID cannot be 0"));
    }

    @Test
    void testDeleteNotificationThrowsForNonexistentId() {
        Exception ex = assertThrows(RuntimeException.class, () -> dao.deleteNotification(99999L));
        assertTrue(ex.getMessage().contains("No notification found with ID"));
    }

    @Test
    void testGetNotificationsReturnsEmptyForNoResults() {
        List<Notification> notifications = dao.getNotifications(12345L);
        assertTrue(notifications.isEmpty());
    }

    @Test
    void testMultipleNotificationsForUser() {
        dao.sendNotification(new NoteNotification(1L, 10L, "A", "A1"));
        dao.sendNotification(new ChallengeNotification(2L, 10L, "B", "B1"));
        dao.sendNotification(new FriendRequestNotification(3L, 10L, "C", "C1"));
        List<Notification> notifications = dao.getNotifications(10L);
        assertEquals(3, notifications.size());
        ArrayList<String> titles = new ArrayList<>();
        for (Notification n : notifications) {
            titles.add(n.getTitle());
        }
        assertTrue(titles.contains("A"));
        assertTrue(titles.contains("B"));
        assertTrue(titles.contains("C"));
    }

    // Helper to get the latest notification's ID from DB
    private long getNotificationIdFromDb() {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM notifications ORDER BY id DESC LIMIT 1")) {
            if (rs.next()) {
                return rs.getLong("id");
            }
            throw new RuntimeException("No notification in DB");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
} 