package questions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MultipleChoiceQuestionTest {
    
    @Test
    @Order(1)
    @DisplayName("Test constructor and basic functionality")
    void testConstructorAndBasicFunctionality() {
        List<String> options = Arrays.asList("London", "Paris", "Berlin");
        MultipleChoiceQuestion question = new MultipleChoiceQuestion("Capital of France?", options, "Paris");
        
        assertEquals("Capital of France?", question.getQuestionText());
        assertEquals("MULTIPLE_CHOICE", question.getQuestionType());
        assertEquals(options, question.getOptions());
        assertEquals(Arrays.asList("Paris"), question.getCorrectAnswers());
    }
    
    @Test
    @Order(2)
    @DisplayName("Test correct answer")
    void testCorrectAnswer() {
        List<String> options = Arrays.asList("A", "B", "C", "D");
        MultipleChoiceQuestion question = new MultipleChoiceQuestion("Which letter comes first?", options, "A");
        
        assertTrue(question.isCorrect("A"));
        assertTrue(question.isCorrect("a")); // Case insensitive
        assertTrue(question.isCorrect(" A ")); // Whitespace handling
    }
    
    @Test
    @Order(3)
    @DisplayName("Test incorrect answers")
    void testIncorrectAnswers() {
        List<String> options = Arrays.asList("Red", "Blue", "Green");
        MultipleChoiceQuestion question = new MultipleChoiceQuestion("Color of grass?", options, "Green");
        
        assertFalse(question.isCorrect("Red"));
        assertFalse(question.isCorrect("Blue"));
        assertFalse(question.isCorrect("Yellow"));
        assertFalse(question.isCorrect(""));
    }
    
    @Test
    @Order(4)
    @DisplayName("Test non-string inputs")
    void testNonStringInputs() {
        List<String> options = Arrays.asList("1", "2", "3");
        MultipleChoiceQuestion question = new MultipleChoiceQuestion("Pick a number", options, "2");
        
        assertFalse(question.isCorrect(2)); // Integer
        assertFalse(question.isCorrect(2.0)); // Double
        assertFalse(question.isCorrect(true)); // Boolean
        assertFalse(question.isCorrect(null)); // Null
        assertFalse(question.isCorrect(new ArrayList<>())); // List
    }
    
    @Test
    @Order(5)
    @DisplayName("Test options getter and setter")
    void testOptionsGetterSetter() {
        List<String> options = Arrays.asList("X", "Y", "Z");
        MultipleChoiceQuestion question = new MultipleChoiceQuestion("Test", options, "X");
        
        assertEquals(options, question.getOptions());
        
        List<String> newOptions = Arrays.asList("A", "B", "C");
        question.setOptions(newOptions);
        assertEquals(newOptions, question.getOptions());
    }
    
    @Test
    @Order(6)
    @DisplayName("Test empty options")
    void testEmptyOptions() {
        List<String> options = new ArrayList<>();
        MultipleChoiceQuestion question = new MultipleChoiceQuestion("Empty test", options, "None");
        
        assertEquals(0, question.getOptions().size());
        assertTrue(question.isCorrect("None"));
    }
} 