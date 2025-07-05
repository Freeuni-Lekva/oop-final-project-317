package models;

import java.util.List;

/**
 * Abstract base class for all question types
 * Contains common fields and methods that all questions share
 */
public abstract class Question {
    // Question type constants
    public static final String QUESTION_RESPONSE = "QUESTION_RESPONSE";
    public static final String MULTIPLE_CHOICE = "MULTIPLE_CHOICE";
    public static final String FILL_IN_BLANK = "FILL_IN_BLANK";
    public static final String PICTURE_RESPONSE = "PICTURE_RESPONSE";

    // Database fields
    private Long id;
    private String questionText;
    private String questionType;
    private Long quizId;  // Which quiz this question belongs to
    private List<String> correctAnswers;// List of acceptable answers
    private String imageUrl; // Only for picture-response questions
    private int points;
    private int timeLimit;

    public Question(String questionText, String questionType, List<String> correctAnswers) {
        this.questionText = questionText;
        this.questionType = questionType;
        this.correctAnswers = correctAnswers;
    }

    public abstract boolean isCorrect(Object userAnswer);

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }

    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }

    public List<String> getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(List<String> correctAnswers) { this.correctAnswers = correctAnswers; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public int getTimeLimit() { return timeLimit; }
    public void setTimeLimit(int timeLimit) { this.timeLimit = timeLimit; }


    protected boolean isAnswerMatch(String userAnswer, String correctAnswer) {
        if (userAnswer == null || correctAnswer == null) return false;
        return userAnswer.trim().equalsIgnoreCase(correctAnswer.trim());
    }

    // Method to get display name for question type
    public String getQuestionTypeDisplayName() {
        switch (questionType) {
            case QUESTION_RESPONSE:
                return "Question-Response";
            case MULTIPLE_CHOICE:
                return "Multiple Choice";
            case FILL_IN_BLANK:
                return "Fill in the Blank";
            case PICTURE_RESPONSE:
                return "Picture-Response";
            default:
                return questionType;
        }
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", questionType='" + questionType + '\'' +
                ", quizId=" + quizId +
                ", points=" + points +
                ", timeLimit=" + timeLimit +
                '}';
    }
}

