package models;

public abstract class Notification {
    private String title;
    private String message;
    private long fromUserId;
    private long toUserId;
    private long id;
    private String questionType;

    public static final String NOTE_NOTIFICATION = "NOTE_NOTIFICATION";
    public static final String FRIEND_REQUEST_NOTIFICATION = "FRIEND_REQUEST_NOTIFICATION";
    public static final String CHALLENGE_NOTIFICATION = "CHALLENGE_NOTIFICATION";

    public Notification() {
        this.id = 0;
    }

    public Notification(long fromUserId, long toUserId, String title, String message) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.title = title;
        this.message = message;
        this.id = 0;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public long getToUserId() {
        return toUserId;
    }

    public long getId() {
        return id;
    }

    public String getQuestionType() {
        return questionType;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
}
