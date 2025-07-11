package questions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import questions.FillInBlankQuestion;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FillInBlankQuestionTest {
    
    @Test
    @Order(1)
    @DisplayName("Test constructor and basic functionality")
    void testConstructorAndBasicFunctionality() {
        List<String> answers = Arrays.asList("Lincoln", "Abraham Lincoln");
        FillInBlankQuestion question = new FillInBlankQuestion("The 16th President was ________", answers);
        
        assertEquals("The 16th President was ________", question.getQuestionText());
        assertEquals("FILL_IN_BLANK", question.getQuestionType());
        assertEquals(answers, question.getCorrectAnswers());
    }
    
    @Test
    @Order(2)
    @DisplayName("Test multiple correct answers")
    void testMultipleCorrectAnswers() {
        List<String> answers = Arrays.asList("USA", "United States", "America");
        FillInBlankQuestion question = new FillInBlankQuestion("I live in ________", answers);
        
        assertTrue(question.isCorrect("USA"));
        assertTrue(question.isCorrect("United States"));
        assertTrue(question.isCorrect("America"));
        assertTrue(question.isCorrect("usa")); // Case insensitive
        assertTrue(question.isCorrect(" America ")); // Whitespace handling
    }
    
    @Test
    @Order(3)
    @DisplayName("Test incorrect answers")
    void testIncorrectAnswers() {
        List<String> answers = Arrays.asList("cat", "feline");
        FillInBlankQuestion question = new FillInBlankQuestion("A ________ is a pet", answers);
        
        assertFalse(question.isCorrect("dog"));
        assertFalse(question.isCorrect("bird"));
        assertFalse(question.isCorrect(""));
        assertFalse(question.isCorrect("cats")); // Plural not accepted
    }
    
    @Test
    @Order(4)
    @DisplayName("Test non-string inputs")
    void testNonStringInputs() {
        List<String> answers = Arrays.asList("42");
        FillInBlankQuestion question = new FillInBlankQuestion("Answer is ________", answers);
        
        assertFalse(question.isCorrect(42)); // Integer
        assertFalse(question.isCorrect(42.0)); // Double
        assertFalse(question.isCorrect(true)); // Boolean
        assertFalse(question.isCorrect(null)); // Null
        assertFalse(question.isCorrect(new ArrayList<>())); // List
    }
    
    @Test
    @Order(5)
    @DisplayName("Test single answer")
    void testSingleAnswer() {
        List<String> answers = Arrays.asList("water");
        FillInBlankQuestion question = new FillInBlankQuestion("Fish live in ________", answers);
        
        assertTrue(question.isCorrect("water"));
        assertTrue(question.isCorrect("Water"));
        assertFalse(question.isCorrect("ocean"));
    }
    
    @Test
    @Order(6)
    @DisplayName("Test empty answer list")
    void testEmptyAnswerList() {
        List<String> answers = new ArrayList<>();
        FillInBlankQuestion question = new FillInBlankQuestion("No answers ________", answers);
        
        assertFalse(question.isCorrect("anything"));
        assertEquals(0, question.getCorrectAnswers().size());
    }
} 