package models;


import java.time.LocalDateTime;
import java.time.Duration;

/**
 * Model class representing a completed quiz attempt by a user
 * Stores overall quiz performance metrics
 */
public class QuizResult {
    // Database fields
    private long id;
    private long userId;
    private long quizId;
    private int score;             // Number of questions answered correctly
    private int totalQuestions;
    private int totalPoints;       // Total points earned
    private int maxPoints;
    private long completionTime;
    private boolean isPracticeMode;      // Whether this was a practice attempt

    public QuizResult(Long userId, Long quizId, int totalQuestions, int maxPoints) {
        this.userId = userId;
        this.quizId = quizId;
        this.totalQuestions = totalQuestions;
        this.maxPoints = maxPoints;
        this.score = 0;
        this.totalPoints = 0;
        this.isPracticeMode = false;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public int getTotalPoints() { return totalPoints; }
    public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }

    public int getMaxPoints() { return maxPoints; }
    public void setMaxPoints(int maxPoints) { this.maxPoints = maxPoints; }

    public long getCompletionTimeSeconds() { return completionTime; }
    public void setCompletionTimeSeconds(long completionTimeSeconds) {
        this.completionTime = completionTimeSeconds;
    }

    public boolean isPracticeMode() { return isPracticeMode; }
    public void setPracticeMode(boolean practiceMode) { this.isPracticeMode = practiceMode; }


    /**
     * Calculates percentage score based on correct answers
     */
    public double getPercentage() {
        if (totalQuestions == 0) return 0.0;
        return (double) score / totalQuestions * 100.0;
    }

    /**
     * Calculates points percentage based on points earned
     */
    public double getPointsPercentage() {
        if (maxPoints == 0) return 0.0;
        return (double) totalPoints / maxPoints * 100.0;
    }

    /**
     * Formats completion time as MM:SS
     */
    public String getCompletionTime() {
        long minutes = completionTime / 60;
        long seconds = completionTime % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Increments score when a question is answered correctly
     */
    public void incrementScore() {
        this.score++;
    }

    /**
     * Adds points to the total when a question is answered correctly
     */
    public void addPoints(int points) {
        this.totalPoints += points;
    }

    /**
     * Checks if this result ranks higher than another result
     * First by score, then by completion time (faster is better)
     */
    public boolean ranksHigherThan(QuizResult other) {
        if (other == null) return true;

        if (this.score != other.score) {
            return this.score > other.score;
        }

        return this.completionTime < other.completionTime;
    }

    @Override
    public String toString() {
        return "QuizResult{" +
                "id=" + id +
                ", userId=" + userId +
                ", quizId=" + quizId +
                ", score=" + score + "/" + totalQuestions +
                ", points=" + totalPoints + "/" + maxPoints +
                ", percentage=" + String.format("%.1f", getPercentage()) + "%" +
                ", completionTime=" + getCompletionTime() +
                ", practiceMode=" + isPracticeMode +
                '}';
    }
}