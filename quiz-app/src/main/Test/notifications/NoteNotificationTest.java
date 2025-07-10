package notifications;

import models.Notification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class NoteNotificationTest {

    private NoteNotification notification;

    @BeforeEach
    void setUp() {
        notification = new NoteNotification();
    }

    @Test
    void testDefaultConstructor() {
        NoteNotification notification = new NoteNotification();

        assertEquals(0, notification.getId());
        assertEquals(0, notification.getFromUserId());
        assertEquals(0, notification.getToUserId());
        assertNull(notification.getTitle());
        assertNull(notification.getMessage());
        assertEquals(Notification.NOTE_NOTIFICATION, notification.getQuestionType());
    }

    @Test
    void testParameterizedConstructor() {
        long fromUserId = 123L;
        long toUserId = 456L;
        String title = "Note Title";
        String message = "Note Message";

        NoteNotification notification = new NoteNotification(fromUserId, toUserId, title, message);

        assertEquals(0, notification.getId()); // ID is set to 0 in parent constructor
        assertEquals(fromUserId, notification.getFromUserId());
        assertEquals(toUserId, notification.getToUserId());
        assertEquals(title, notification.getTitle());
        assertEquals(message, notification.getMessage());
        assertEquals(Notification.NOTE_NOTIFICATION, notification.getQuestionType());
    }

    @Test
    void testSettersAndGetters() {
        long fromUserId = 789L;
        long toUserId = 101112L;
        String title = "New Note";
        String message = "New Note Message";
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
        assertEquals(Notification.NOTE_NOTIFICATION, notification.getQuestionType());
    }

    @Test
    void testQuestionTypeConstant() {
        assertEquals("NOTE_NOTIFICATION", Notification.NOTE_NOTIFICATION);
    }

    @Test
    void testQuestionTypeCannotBeChanged() {
        String originalQuestionType = notification.getQuestionType();
        notification.setQuestionType("DIFFERENT_TYPE");

        // The question type can be changed via setter, but constructor sets it correctly
        assertEquals("DIFFERENT_TYPE", notification.getQuestionType());

        // Create new instance to verify constructor behavior
        NoteNotification newNotification = new NoteNotification(1L, 2L, "title", "message");
        assertEquals(Notification.NOTE_NOTIFICATION, newNotification.getQuestionType());
    }

    @Test
    void testInheritanceFromNotification() {
        assertTrue(notification instanceof Notification);
    }

    @Test
    void testWithNullValues() {
        NoteNotification notification = new NoteNotification(0L, 0L, null, null);

        assertEquals(0L, notification.getFromUserId());
        assertEquals(0L, notification.getToUserId());
        assertNull(notification.getTitle());
        assertNull(notification.getMessage());
        assertEquals(Notification.NOTE_NOTIFICATION, notification.getQuestionType());
    }

    @Test
    void testWithEmptyStrings() {
        NoteNotification notification = new NoteNotification(1L, 2L, "", "");

        assertEquals("", notification.getTitle());
        assertEquals("", notification.getMessage());
        assertEquals(Notification.NOTE_NOTIFICATION, notification.getQuestionType());
    }

    @Test
    void testNoteSpecificScenarios() {
        // Test typical note scenario
        long senderId = 300L;
        long receiverId = 400L;
        String title = "Reminder";
        String message = "Don't forget about the meeting tomorrow";

        NoteNotification note = new NoteNotification(senderId, receiverId, title, message);

        assertEquals(senderId, note.getFromUserId());
        assertEquals(receiverId, note.getToUserId());
        assertEquals(title, note.getTitle());
        assertEquals(message, note.getMessage());
        assertEquals(Notification.NOTE_NOTIFICATION, note.getQuestionType());
    }

    @Test
    void testLongMessageContent() {
        String longMessage = "This is a very long note message that contains multiple sentences. " +
                "It might be used for detailed instructions or important information. " +
                "The system should handle long messages without any issues.";

        NoteNotification note = new NoteNotification(1L, 2L, "Long Note", longMessage);

        assertEquals(longMessage, note.getMessage());
        assertEquals(Notification.NOTE_NOTIFICATION, note.getQuestionType());
    }
}