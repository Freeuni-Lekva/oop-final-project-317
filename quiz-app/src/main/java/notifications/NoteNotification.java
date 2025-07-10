package notifications;

import models.Notification;

public class NoteNotification extends Notification {
    public NoteNotification() {
        super();
        this.setQuestionType(Notification.NOTE_NOTIFICATION);
    }

    public NoteNotification(long fromUserId, long toUserId, String title, String message) {
        super(fromUserId, toUserId, title, message);
        this.setQuestionType(Notification.NOTE_NOTIFICATION);
    }
}