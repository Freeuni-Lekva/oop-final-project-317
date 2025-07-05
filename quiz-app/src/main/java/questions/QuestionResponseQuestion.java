package questions;

import models.Question;
import java.util.List;

/**
 * Question-Response question type
 * Simple text question with text response
 * Example: "Who was President during the Bay of Pigs fiasco?"
 */
public class QuestionResponseQuestion extends Question {

    //Constructor for Question-Response questions
    public QuestionResponseQuestion(String questionText, List<String> acceptableAnswers) {
        super(questionText, Question.QUESTION_RESPONSE, acceptableAnswers);
    }
    
    /**
     * Check if the user's answer is correct
     * @param userAnswer The answer provided by the user
     * @return true if the answer matches the correct answer
     */
    @Override
    public boolean isCorrect(Object userAnswer) {
        if (!(userAnswer instanceof String)) {
            return false;
        }
        String userAnswerStr = (String) userAnswer;
        for (String acceptableAnswer : getCorrectAnswers()) {
            if (isAnswerMatch(userAnswerStr, acceptableAnswer)) {
                return true;
            }
        }
        return false;
    }
} 