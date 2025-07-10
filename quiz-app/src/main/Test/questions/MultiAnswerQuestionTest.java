package questions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MultiAnswerQuestionTest {
    
    @Test
    @Order(1)
    @DisplayName("Test constructor and basic functionality")
    void testConstructorAndBasicFunctionality() {
        List<String> answers = Arrays.asList("California", "Texas", "Florida");
        MultiAnswerQuestion question = new MultiAnswerQuestion("List 3 US states", answers, false);
        
        assertEquals("List 3 US states", question.getQuestionText());
        assertEquals("MULTI_ANSWER", question.getQuestionType());
        assertEquals(answers, question.getCorrectAnswers());
        assertFalse(question.isOrderMatters());
    }
    
    @Test
    @Order(2)
    @DisplayName("Test order does not matter")
    void testOrderDoesNotMatter() {
        List<String> answers = Arrays.asList("Apple", "Banana", "Cherry");
        MultiAnswerQuestion question = new MultiAnswerQuestion("Name 3 fruits", answers, false);
        
        List<String> userAnswers1 = Arrays.asList("Apple", "Banana", "Cherry");
        List<String> userAnswers2 = Arrays.asList("Cherry", "Apple", "Banana");
        List<String> userAnswers3 = Arrays.asList("banana", "APPLE", " cherry ");
        
        assertTrue(question.isCorrect(userAnswers1));
        assertTrue(question.isCorrect(userAnswers2)); // Different order
        assertTrue(question.isCorrect(userAnswers3)); // Case insensitive
    }
    
    @Test
    @Order(3)
    @DisplayName("Test order matters")
    void testOrderMatters() {
        List<String> answers = Arrays.asList("China", "India", "USA");
        MultiAnswerQuestion question = new MultiAnswerQuestion("Top 3 countries by population", answers, true);
        
        List<String> userAnswers1 = Arrays.asList("China", "India", "USA");
        List<String> userAnswers2 = Arrays.asList("USA", "China", "India");
        List<String> userAnswers3 = Arrays.asList("china", "india", "usa");
        
        assertTrue(question.isCorrect(userAnswers1)); // Correct order
        assertFalse(question.isCorrect(userAnswers2)); // Wrong order
        assertTrue(question.isCorrect(userAnswers3)); // Case insensitive but correct order
    }
    
    @Test
    @Order(4)
    @DisplayName("Test incorrect answers")
    void testIncorrectAnswers() {
        List<String> answers = Arrays.asList("Red", "Blue", "Green");
        MultiAnswerQuestion question = new MultiAnswerQuestion("Name 3 primary colors", answers, false);
        
        List<String> userAnswers1 = Arrays.asList("Red", "Blue", "Yellow");
        List<String> userAnswers2 = Arrays.asList("Red", "Blue");
        List<String> userAnswers3 = Arrays.asList("Red", "Blue", "Green", "Purple");
        
        assertFalse(question.isCorrect(userAnswers1)); // One wrong answer
        assertFalse(question.isCorrect(userAnswers2)); // Too few answers
        assertFalse(question.isCorrect(userAnswers3)); // Too many answers
    }
    
    @Test
    @Order(5)
    @DisplayName("Test non-list inputs")
    void testNonListInputs() {
        List<String> answers = Arrays.asList("Answer1", "Answer2");
        MultiAnswerQuestion question = new MultiAnswerQuestion("Test question", answers, false);
        
        assertFalse(question.isCorrect("Single String")); // String
        assertFalse(question.isCorrect(123)); // Integer
        assertFalse(question.isCorrect(45.6)); // Double
        assertFalse(question.isCorrect(true)); // Boolean
        assertFalse(question.isCorrect(null)); // Null
        assertFalse(question.isCorrect(new HashMap<>())); // Map
    }
    
    @Test
    @Order(6)
    @DisplayName("Test order matters getter and setter")
    void testOrderMattersGetterSetter() {
        List<String> answers = Arrays.asList("Test1", "Test2");
        MultiAnswerQuestion question = new MultiAnswerQuestion("Test", answers, true);
        
        assertTrue(question.isOrderMatters());
        
        question.setOrderMatters(false);
        assertFalse(question.isOrderMatters());
        
        question.setOrderMatters(true);
        assertTrue(question.isOrderMatters());
    }
    
    @Test
    @Order(7)
    @DisplayName("Test empty answer list")
    void testEmptyAnswerList() {
        List<String> answers = new ArrayList<>();
        MultiAnswerQuestion question = new MultiAnswerQuestion("Empty test", answers, false);
        
        List<String> emptyUserAnswers = new ArrayList<>();
        List<String> nonEmptyUserAnswers = Arrays.asList("Something");
        
        assertTrue(question.isCorrect(emptyUserAnswers)); // Both empty
        assertFalse(question.isCorrect(nonEmptyUserAnswers)); // Size mismatch
        assertEquals(0, question.getCorrectAnswers().size());
    }
    
    @Test
    @Order(8)
    @DisplayName("Test single answer")
    void testSingleAnswer() {
        List<String> answers = Arrays.asList("OnlyAnswer");
        MultiAnswerQuestion question = new MultiAnswerQuestion("Single answer test", answers, false);
        
        List<String> userAnswers = Arrays.asList("OnlyAnswer");
        List<String> wrongUserAnswers = Arrays.asList("WrongAnswer");
        
        assertTrue(question.isCorrect(userAnswers));
        assertFalse(question.isCorrect(wrongUserAnswers));
    }
} 