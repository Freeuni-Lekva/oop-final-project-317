package questions;

import models.Question;
import java.util.List;

/**
 * Picture-Response question type
 * System displays an image and user provides a text response
 * Example: Show image of a bird, user responds with bird species name
 */
public class PictureResponseQuestion extends Question {

    //Constructor for Picture-Response questions
    public PictureResponseQuestion(String questionText, String imageUrl, List<String> acceptableAnswers) {
        super(questionText, Question.PICTURE_RESPONSE, acceptableAnswers);
        setImageUrl(imageUrl);
    }

    //Check if the user's answer is correct
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