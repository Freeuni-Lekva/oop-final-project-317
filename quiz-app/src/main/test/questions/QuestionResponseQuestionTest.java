package questions;

import java.util.*;
import junit.framework.TestCase;

public class QuestionResponseQuestionTest extends TestCase {
    
    public void testConstructorAndBasicFunctionality() {
        List<String> answers = Arrays.asList("7", "8", "9", "10");
        QuestionResponseQuestion question = new QuestionResponseQuestion("Name a number between 7 and 10", answers);
        
        assertEquals("Name a number between 7 and 10", question.getQuestionText());
        assertEquals("QUESTION_RESPONSE", question.getQuestionType());
        assertEquals(answers, question.getCorrectAnswers());
    }
    
    public void testCorrectAnswers() {
        List<String> answers = Arrays.asList("Washington", "George Washington");
        QuestionResponseQuestion question = new QuestionResponseQuestion("First President?", answers);
        
        assertTrue(question.isCorrect("Washington"));
        assertTrue(question.isCorrect("George Washington"));
        assertTrue(question.isCorrect("washington")); // Case insensitive
        assertTrue(question.isCorrect(" Washington ")); // Whitespace handling
    }
    
    public void testIncorrectAnswers() {
        List<String> answers = Arrays.asList("Paris", "France");
        QuestionResponseQuestion question = new QuestionResponseQuestion("Capital of France?", answers);
        
        assertFalse(question.isCorrect("London"));
        assertFalse(question.isCorrect("Berlin"));
        assertFalse(question.isCorrect(""));
    }
    
    public void testNonStringInputs() {
        List<String> answers = Arrays.asList("4");
        QuestionResponseQuestion question = new QuestionResponseQuestion("What's 2+2?", answers);
        
        assertFalse(question.isCorrect(4)); // Integer
        assertFalse(question.isCorrect(4.0)); // Double
        assertFalse(question.isCorrect(true)); // Boolean
        assertFalse(question.isCorrect(null)); // Null
        assertFalse(question.isCorrect(new ArrayList<>())); // List
    }
    
    public void testSingleAnswer() {
        List<String> answers = Arrays.asList("Blue");
        QuestionResponseQuestion question = new QuestionResponseQuestion("What color is the sky?", answers);
        
        assertTrue(question.isCorrect("Blue"));
        assertTrue(question.isCorrect("blue"));
        assertFalse(question.isCorrect("Red"));
    }
} 