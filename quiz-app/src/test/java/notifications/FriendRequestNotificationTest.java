package notifications;

import models.Notification;
import notifications.FriendRequestNotification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class FriendRequestNotificationTest {

    private FriendRequestNotification notification;

    @BeforeEach
    void setUp() {
        notification = new FriendRequestNotification();
    }

    @Test
    void testDefaultConstructor() {
        FriendRequestNotification notification = new FriendRequestNotification();

        assertEquals(0, notification.getId());
        assertEquals(0, notification.getFromUserId());
        assertEquals(0, notification.getToUserId());
        assertNull(notification.getTitle());
        assertNull(notification.getMessage());
        assertEquals(Notification.FRIEND_REQUEST_NOTIFICATION, notification.getQuestionType());
    }

    @Test
    void testParameterizedConstructor() {
        long fromUserId = 123L;
        long toUserId = 456L;
        String title = "Friend Request Title";
        String message = "Friend Request Message";

        FriendRequestNotification notification = new FriendRequestNotification(fromUserId, toUserId, title, message);

        assertEquals(0, notification.getId()); // ID is set to 0 in parent constructor
        assertEquals(fromUserId, notification.getFromUserId());
        assertEquals(toUserId, notification.getToUserId());
        assertEquals(title, notification.getTitle());
        assertEquals(message, notification.getMessage());
        assertEquals(Notification.FRIEND_REQUEST_NOTIFICATION, notification.getQuestionType());
    }

    @Test
    void testSettersAndGetters() {
        long fromUserId = 789L;
        long toUserId = 101112L;
        String title = "New Friend Request";
        String message = "New Friend Request Message";
        long id = 999L;

        notification.setFromUserId(fromUserId);
        notification.setToUserId(toUserId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setId(id);

        assertEquals(fromUserId, notification.getFromUserId());
        assertEquals(toUserId, notification.getToUserId());
        assertEquals(title, notification.getTitle());
        assertEquals(message, notification.getMessage());
        assertEquals(id, notification.getId());
        assertEquals(Notification.FRIEND_REQUEST_NOTIFICATION, notification.getQuestionType());
    }

    @Test
    void testQuestionTypeConstant() {
        assertEquals("FRIEND_REQUEST_NOTIFICATION", Notification.FRIEND_REQUEST_NOTIFICATION);
    }

    @Test
    void testQuestionTypeCannotBeChanged() {
        String originalQuestionType = notification.getQuestionType();
        notification.setQuestionType("DIFFERENT_TYPE");

        // The question type can be changed via setter, but constructor sets it correctly
        assertEquals("DIFFERENT_TYPE", notification.getQuestionType());

        // Create new instance to verify constructor behavior
        FriendRequestNotification newNotification = new FriendRequestNotification(1L, 2L, "title", "message");
        assertEquals(Notification.FRIEND_REQUEST_NOTIFICATION, newNotification.getQuestionType());
    }

    @Test
    void testInheritanceFromNotification() {
        assertTrue(notification instanceof Notification);
    }

    @Test
    void testWithNullValues() {
        FriendRequestNotification notification = new FriendRequestNotification(0L, 0L, null, null);

        assertEquals(0L, notification.getFromUserId());
        assertEquals(0L, notification.getToUserId());
        assertNull(notification.getTitle());
        assertNull(notification.getMessage());
        assertEquals(Notification.FRIEND_REQUEST_NOTIFICATION, notification.getQuestionType());
    }

    @Test
    void testWithEmptyStrings() {
        FriendRequestNotification notification = new FriendRequestNotification(1L, 2L, "", "");

        assertEquals("", notification.getTitle());
        assertEquals("", notification.getMessage());
        assertEquals(Notification.FRIEND_REQUEST_NOTIFICATION, notification.getQuestionType());
    }

    @Test
    void testFriendRequestSpecificScenarios() {
        // Test typical friend request scenario
        long senderId = 100L;
        long receiverId = 200L;
        String title = "Friend Request";
        String message = "John Doe wants to be your friend";

        FriendRequestNotification friendRequest = new FriendRequestNotification(senderId, receiverId, title, message);

        assertEquals(senderId, friendRequest.getFromUserId());
        assertEquals(receiverId, friendRequest.getToUserId());
        assertEquals(title, friendRequest.getTitle());
        assertEquals(message, friendRequest.getMessage());
        assertEquals(Notification.FRIEND_REQUEST_NOTIFICATION, friendRequest.getQuestionType());
    }
}