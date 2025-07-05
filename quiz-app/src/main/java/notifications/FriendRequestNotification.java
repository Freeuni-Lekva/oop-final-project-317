package notifications;

import models.Notification;

public class FriendRequestNotification extends Notification {
    public FriendRequestNotification() {
        super();
    }

    public FriendRequestNotification(long fromUserId, long toUserId, String title, String message) {
        super(fromUserId, toUserId, title, message);
        this.setQuestionType(Notification.FRIEND_REQUEST_NOTIFICATION);
    }
}
