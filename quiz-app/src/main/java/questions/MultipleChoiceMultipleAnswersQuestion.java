package questions;

import models.Question;
import java.util.List;

/**
 * Multiple Choice with Multiple Answers question type
 * User can select multiple responses from provided options
 * Example: "Mark each true statement: (1) Stanford established 1891, (2) Stanford has best CS dept, (3) Stanford going to bowl game"
 */
public class MultipleChoiceMultipleAnswersQuestion extends Question {
    
    private List<String> options;
    
    //Constructor for Multiple Choice with Multiple Answers questions
    public MultipleChoiceMultipleAnswersQuestion(String questionText, List<String> options, List<String> correctAnswers) {
        super(questionText, Question.MULTIPLE_CHOICE_MULTIPLE_ANSWERS, correctAnswers);
        this.options = options;
    }
    
    //Check if the user's selections are correct
    @Override
    public boolean isCorrect(Object userAnswer) {
        if (!(userAnswer instanceof List)) {
            return false;
        }
        
        @SuppressWarnings("unchecked")
        List<String> userSelections = (List<String>) userAnswer;
        List<String> correctAnswers = getCorrectAnswers();
        
        if (userSelections.size() != correctAnswers.size()) {
            return false;
        }
        
        for (String userSelection : userSelections) {
            boolean found = false;
            for (String correctAnswer : correctAnswers) {
                if (isAnswerMatch(userSelection, correctAnswer)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        
        for (String correctAnswer : correctAnswers) {
            boolean found = false;
            for (String userSelection : userSelections) {
                if (isAnswerMatch(userSelection, correctAnswer)) {
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
    
    //Get the list of available options for this question
    public List<String> getOptions() {
        return options;
    }
    
    //Set the list of available options for this question
    public void setOptions(List<String> options) {
        this.options = options;
    }
} 