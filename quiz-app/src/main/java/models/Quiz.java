package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a quiz
 * Contains all quiz properties and settings
 */
public class Quiz {
    // Database fields
    private long id;
    private String title;
    private String description;
    private Long createdBy; // User ID who created the quiz
    private LocalDateTime createdDate;
    private LocalDateTime lastModified;

    // Quiz options/settings
    private boolean randomizeQuestions;
    private boolean onePage; // true = single page, false = multiple pages
    private boolean immediateCorrection;
    private boolean practiceMode;

    private int timeLimit; // total allowed time in seconds (0 = unlimited)

    // Not stored in database - populated when needed
    private List<Question> questions;

    public Quiz(String title, String description, Long createdBy) {
        this.id = 0;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.createdDate = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        this.timeLimit = 0;
        this.questions = new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        this.title = title;
        this.lastModified = LocalDateTime.now();
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
        this.lastModified = LocalDateTime.now();
    }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getLastModified() { return lastModified; }
    public void setLastModified(LocalDateTime lastModified) { this.lastModified = lastModified; }

    public boolean isRandomizeQuestions() { return randomizeQuestions; }
    public void setRandomizeQuestions(boolean randomizeQuestions) {
        this.randomizeQuestions = randomizeQuestions;
        this.lastModified = LocalDateTime.now();
    }

    public boolean isOnePage() { return onePage; }
    public void setOnePage(boolean onePage) {
        this.onePage = onePage;
        this.lastModified = LocalDateTime.now();
    }

    public boolean isImmediateCorrection() { return immediateCorrection; }
    public void setImmediateCorrection(boolean immediateCorrection) {
        this.immediateCorrection = immediateCorrection;
        this.lastModified = LocalDateTime.now();
    }

    public boolean isPracticeMode() { return practiceMode; }
    public void setPracticeMode(boolean practiceMode) {
        this.practiceMode = practiceMode;
        this.lastModified = LocalDateTime.now();
    }

    public int getTimeLimit() { return timeLimit; }
    public void setTimeLimit(int timeLimit) { this.timeLimit = timeLimit; }

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }

    // Returns the number of questions in the quiz
    public int getQuestionCount() {
        if (questions == null) {
            return 0;
        }
        return questions.size();
    }

    // Calculates the total points for all questions in the quiz
    public int getTotalPoints() {
        if (questions == null) {
            return 0;
        }
        int sum = 0;
        for (Question question : questions) {
            sum += question.getPoints();
        }
        return sum;
    }

    public int getTotalTimeLimit() {
        if (questions == null) {
            return 0;
        }
        int total = 0;
        for (Question question : questions) {
            total += question.getTimeLimit();
        }
        return total;
    }

    public void addQuestion(Question question) {
        if (questions != null) {
            questions.add(question);
            question.setQuizId(this.id);
            this.lastModified = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdBy=" + createdBy +
                ", createdDate=" + createdDate +
                ", questionCount=" + getQuestionCount() +
                ", totalPoints=" + getTotalPoints() +
                ", totalTimeLimit=" + getTotalTimeLimit() +
                ", timeLimit=" + timeLimit +
                '}';
    }
}