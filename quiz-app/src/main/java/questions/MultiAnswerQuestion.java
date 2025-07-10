package questions;

import models.Question;
import java.util.List;

/**
 * Multi-Answer question type
 * Multiple text fields for responses with optional ordering requirement
 * Example: "List the 50 states in the US" (order not relevant) or "List the 10 most populous countries" (order matters)
 */
public class MultiAnswerQuestion extends Question {
    
    private boolean orderMatters;
    
    //Constructor for Multi-Answer questions
    public MultiAnswerQuestion(String questionText, List<String> correctAnswers, boolean orderMatters) {
        super(questionText, Question.MULTI_ANSWER, correctAnswers);
        this.orderMatters = orderMatters;
    }
    
    //Check if the user's answers are correct
    @Override
    public boolean isCorrect(Object userAnswer) {
        if (!(userAnswer instanceof List)) {
            return false;
        }
        
        @SuppressWarnings("unchecked")
        List<String> userAnswers = (List<String>) userAnswer;
        List<String> correctAnswers = getCorrectAnswers();
        
        if (userAnswers.size() != correctAnswers.size()) {
            return false;
        }
        
        if (orderMatters) {
            for (int i = 0; i < userAnswers.size(); i++) {
                if (!isAnswerMatch(userAnswers.get(i), correctAnswers.get(i))) {
                    return false;
                }
            }
            return true;
        } else {
            for (String userAns : userAnswers) {
                boolean found = false;
                for (String correctAns : correctAnswers) {
                    if (isAnswerMatch(userAns, correctAns)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            }
            return true;
        }
    }
    
    //Get whether order matters for this question
    public boolean isOrderMatters() {
        return orderMatters;
    }
    
    //Set whether order matters for this question
    public void setOrderMatters(boolean orderMatters) {
        this.orderMatters = orderMatters;
    }
} 