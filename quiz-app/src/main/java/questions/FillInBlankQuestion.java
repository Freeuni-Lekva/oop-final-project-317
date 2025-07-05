package questions;

import models.Question;
import java.util.List;

/**
 * Fill in the Blank question type
 * Blank can go anywhere within the question text
 * Example: "One of President Lincoln's most famous speeches was the __________ Address."
 */
public class FillInBlankQuestion extends Question {

    //Constructor for Fill in the Blank questions
    public FillInBlankQuestion(String questionText, List<String> acceptableAnswers) {
        super(questionText, Question.FILL_IN_BLANK, acceptableAnswers);
    }

    // Check if the user's answer is correct
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