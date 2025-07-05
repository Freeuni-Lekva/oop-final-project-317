package questions;

import java.util.*;
import junit.framework.TestCase;

public class MultipleChoiceQuestionTest extends TestCase {
    
    public void testConstructorAndBasicFunctionality() {
        List<String> options = Arrays.asList("London", "Paris", "Berlin");
        MultipleChoiceQuestion question = new MultipleChoiceQuestion("Capital of France?", options, "Paris");
        
        assertEquals("Capital of France?", question.getQuestionText());
        assertEquals("MULTIPLE_CHOICE", question.getQuestionType());
        assertEquals(options, question.getOptions());
        assertEquals(Arrays.asList("Paris"), question.getCorrectAnswers());
    }
    
    public void testCorrectAnswer() {
        List<String> options = Arrays.asList("A", "B", "C", "D");
        MultipleChoiceQuestion question = new MultipleChoiceQuestion("Which letter comes first?", options, "A");
        
        assertTrue(question.isCorrect("A"));
        assertTrue(question.isCorrect("a")); // Case insensitive
        assertTrue(question.isCorrect(" A ")); // Whitespace handling
    }
    
    public void testIncorrectAnswers() {
        List<String> options = Arrays.asList("Red", "Blue", "Green");
        MultipleChoiceQuestion question = new MultipleChoiceQuestion("Color of grass?", options, "Green");
        
        assertFalse(question.isCorrect("Red"));
        assertFalse(question.isCorrect("Blue"));
        assertFalse(question.isCorrect("Yellow"));
        assertFalse(question.isCorrect(""));
    }
    
    public void testNonStringInputs() {
        List<String> options = Arrays.asList("1", "2", "3");
        MultipleChoiceQuestion question = new MultipleChoiceQuestion("Pick a number", options, "2");
        
        assertFalse(question.isCorrect(2)); // Integer
        assertFalse(question.isCorrect(2.0)); // Double
        assertFalse(question.isCorrect(true)); // Boolean
        assertFalse(question.isCorrect(null)); // Null
        assertFalse(question.isCorrect(new ArrayList<>())); // List
    }
    
    public void testOptionsGetterSetter() {
        List<String> options = Arrays.asList("X", "Y", "Z");
        MultipleChoiceQuestion question = new MultipleChoiceQuestion("Test", options, "X");
        
        assertEquals(options, question.getOptions());
        
        List<String> newOptions = Arrays.asList("A", "B", "C");
        question.setOptions(newOptions);
        assertEquals(newOptions, question.getOptions());
    }
    
    public void testEmptyOptions() {
        List<String> options = new ArrayList<>();
        MultipleChoiceQuestion question = new MultipleChoiceQuestion("Empty test", options, "None");
        
        assertEquals(0, question.getOptions().size());
        assertTrue(question.isCorrect("None"));
    }
} 