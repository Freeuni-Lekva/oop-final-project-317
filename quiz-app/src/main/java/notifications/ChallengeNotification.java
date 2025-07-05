package notifications;

import models.Notification;

public class ChallengeNotification extends Notification {
    public ChallengeNotification() {
        super();
    }

    public ChallengeNotification(long fromUserId, long toUserId, String title, String message) {
        super(fromUserId, toUserId, title, message);
        this.setQuestionType(Notification.CHALLENGE_NOTIFICATION);
    }
}
