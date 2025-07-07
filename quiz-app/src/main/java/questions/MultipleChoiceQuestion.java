package questions;

import models.Question;
import java.util.Arrays;
import java.util.List;

/**
 * Multiple Choice question type
 * User selects from provided answer options using radio buttons
 * Example: "What's the capital of France? A) London B) Paris C) Berlin"
 */
public class MultipleChoiceQuestion extends Question {
    
    //To store the available options (A, B, C, D, etc.)
    private List<String> options;
    

    //Constructor for Multiple Choice questions
    public MultipleChoiceQuestion(String questionText, List<String> options, String correctAnswer) {
        super(questionText, Question.MULTIPLE_CHOICE, Arrays.asList(correctAnswer));
        this.options = options;
    }

    //Check if the user's answer is correct
    @Override
    public boolean isCorrect(Object userAnswer) {
        if (!(userAnswer instanceof String)) {
            return false;
        }
        String userAnswerStr = (String) userAnswer;
        String correctAnswer = getCorrectAnswers().get(0);
        return isAnswerMatch(userAnswerStr, correctAnswer);
    }

    //Get the list of available options for this multiple choice question
    public List<String> getOptions() {
        return options;
    }

    //Set the list of available options for this multiple choice question
    public void setOptions(List<String> options) {
        this.options = options;
    }
} 