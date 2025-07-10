package notifications;

import models.Notification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChallengeNotificationTest {

    private ChallengeNotification notification;

    @BeforeEach
    public void setUp() {
        notification = new ChallengeNotification();
    }

    @Test
    public void testDefaultConstructor() {
        ChallengeNotification notification = new ChallengeNotification();

        assertEquals(0, notification.getId());
        assertEquals(0, notification.getFromUserId());
        assertEquals(0, notification.getToUserId());
        Assertions.assertNull(notification.getTitle());
        Assertions.assertNull(notification.getMessage());
        assertEquals(Notification.CHALLENGE_NOTIFICATION, notification.getQuestionType());
    }

    @Test
    public void testParameterizedConstructor() {
        long fromUserId = 123L;
        long toUserId = 456L;
        String title = "Challenge Title";
        String message = "Challenge Message";

        ChallengeNotification notification = new ChallengeNotification(fromUserId, toUserId, title, message);

        assertEquals(0, notification.getId()); // ID is set to 0 in parent constructor
        assertEquals(fromUserId, notification.getFromUserId());
        assertEquals(toUserId, notification.getToUserId());
        assertEquals(title, notification.getTitle());
        assertEquals(message, notification.getMessage());
        assertEquals(Notification.CHALLENGE_NOTIFICATION, notification.getQuestionType());
    }

    @Test
    public void testSettersAndGetters() {
        long fromUserId = 789L;
        long toUserId = 101112L;
        String title = "New Challenge";
        String message = "New Challenge Message";
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
        assertEquals(Notification.CHALLENGE_NOTIFICATION, notification.getQuestionType());
    }

    @Test
    public void testQuestionTypeConstant() {
        assertEquals("CHALLENGE_NOTIFICATION", Notification.CHALLENGE_NOTIFICATION);
    }

    @Test
    public void testQuestionTypeSetAfterConstruction() {
        String originalQuestionType = notification.getQuestionType();
        notification.setQuestionType("DIFFERENT_TYPE");

        // The question type can be changed via setter, but constructor sets it correctly
        assertEquals("DIFFERENT_TYPE", notification.getQuestionType());

        // Create new instance to verify constructor behavior
        ChallengeNotification newNotification = new ChallengeNotification(1L, 2L, "title", "message");
        assertEquals(Notification.CHALLENGE_NOTIFICATION, newNotification.getQuestionType());
    }

    @Test
    public void testInheritanceFromNotification() {
        Assertions.assertTrue(notification instanceof Notification);
    }

    @Test
    public void testWithNullValues() {
        ChallengeNotification notification = new ChallengeNotification(0L, 0L, null, null);

        assertEquals(0L, notification.getFromUserId());
        assertEquals(0L, notification.getToUserId());
        Assertions.assertNull(notification.getTitle());
        Assertions.assertNull(notification.getMessage());
        assertEquals(Notification.CHALLENGE_NOTIFICATION, notification.getQuestionType());
    }

    @Test
    public void testWithEmptyStrings() {
        ChallengeNotification notification = new ChallengeNotification(1L, 2L, "", "");

        assertEquals("", notification.getTitle());
        assertEquals("", notification.getMessage());
        assertEquals(Notification.CHALLENGE_NOTIFICATION, notification.getQuestionType());
    }

    @Test
    public void testChallengeSpecificScenarios() {
        // Test typical challenge scenario
        long challengerId = 100L;
        long challengedId = 200L;
        String title = "Quiz Challenge";
        String message = "You have been challenged to a quiz!";

        ChallengeNotification challenge = new ChallengeNotification(challengerId, challengedId, title, message);

        assertEquals(challengerId, challenge.getFromUserId());
        assertEquals(challengedId, challenge.getToUserId());
        assertEquals(title, challenge.getTitle());
        assertEquals(message, challenge.getMessage());
        assertEquals(Notification.CHALLENGE_NOTIFICATION, challenge.getQuestionType());
    }
}