package notifications;

import models.Notification;

public class ChallengeNotification extends Notification {
    private long quizId;

    public long getQuizId() {
        return quizId;
    }

    /**
     * Sets the quiz (by its ID) that this challenge refers to.
     * @param quizId the ID of the quiz being challenged
     */
    public void setQuiz(long quizId) {
        this.quizId = quizId;
    }

    /**
     * Convenience alias required by project specification – same as {@link #setQuiz(long)}.
     */
    public void setQuizId(long quizId) {
        setQuiz(quizId);
    }

    public ChallengeNotification() {
        super();
        this.setQuestionType(Notification.CHALLENGE_NOTIFICATION);
    }

    /**
     * Constructor to directly build a challenge with a target quiz.
     */
    public ChallengeNotification(long fromUserId, long toUserId, long quizId, String title, String message) {
        super(fromUserId, toUserId, title, message);
        this.quizId = quizId;
        this.setQuestionType(Notification.CHALLENGE_NOTIFICATION);
    }

    /**
     * Backward-compatible constructor without quizId.
     */
    public ChallengeNotification(long fromUserId, long toUserId, String title, String message) {
        super(fromUserId, toUserId, title, message);
        this.quizId = 0;
        this.setQuestionType(Notification.CHALLENGE_NOTIFICATION);
    }
}
