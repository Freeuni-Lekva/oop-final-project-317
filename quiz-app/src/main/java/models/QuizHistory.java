package models;

import java.time.LocalDateTime;

public class QuizHistory {
    private long id;
    private long userId;
    private long quizId;
    private int score;
    private int timeTaken; // in seconds
    private LocalDateTime completedDate;

    public QuizHistory(long userId, long quizId, int score, int totalPoints, int timeTaken) {
        this.userId = userId;
        this.quizId = quizId;
        this.score = score;
        this.timeTaken = timeTaken;
        this.completedDate = LocalDateTime.now();
    }

    public QuizHistory(Long userId, Long quizId, int score, int timeTaken) {
        this.userId = userId;
        this.quizId = quizId;
        this.score = score;
        this.timeTaken = timeTaken;
        this.completedDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTimeTaken() { return timeTaken; }
    public void setTimeTaken(int timeTaken) { this.timeTaken = timeTaken; }

    public LocalDateTime getCompletedDate() { return completedDate; }
    public void setCompletedDate(LocalDateTime completedDate) { this.completedDate = completedDate; }

    @Override
    public String toString() {
        return "QuizHistory{" +
                "id=" + id +
                ", userId=" + userId +
                ", quizId=" + quizId +
                ", score=" + score +
                ", timeTaken=" + timeTaken + "s" +
                ", completedDate=" + completedDate +
                '}';
    }
}