package questions;

import java.util.*;
import junit.framework.TestCase;

public class MultipleChoiceMultipleAnswersQuestionTest extends TestCase {
    
    public void testConstructorAndBasicFunctionality() {
        List<String> options = Arrays.asList("Option A", "Option B", "Option C", "Option D");
        List<String> correct = Arrays.asList("Option A", "Option C");
        MultipleChoiceMultipleAnswersQuestion question = new MultipleChoiceMultipleAnswersQuestion("Select all correct", options, correct);
        
        assertEquals("Select all correct", question.getQuestionText());
        assertEquals("MULTIPLE_CHOICE_MULTIPLE_ANSWERS", question.getQuestionType());
        assertEquals(options, question.getOptions());
        assertEquals(correct, question.getCorrectAnswers());
    }
    
    public void testCorrectSelections() {
        List<String> options = Arrays.asList("Stanford established 1891", "Stanford has best CS dept", "Stanford going to bowl game");
        List<String> correct = Arrays.asList("Stanford established 1891", "Stanford has best CS dept");
        MultipleChoiceMultipleAnswersQuestion question = new MultipleChoiceMultipleAnswersQuestion("Mark true statements", options, correct);
        
        List<String> userSelections1 = Arrays.asList("Stanford established 1891", "Stanford has best CS dept");
        List<String> userSelections2 = Arrays.asList("Stanford has best CS dept", "Stanford established 1891");
        List<String> userSelections3 = Arrays.asList("stanford established 1891", "STANFORD HAS BEST CS DEPT");
        
        assertTrue(question.isCorrect(userSelections1)); // Exact match
        assertTrue(question.isCorrect(userSelections2)); // Different order
        assertTrue(question.isCorrect(userSelections3)); // Case insensitive
    }
    
    public void testIncorrectSelections() {
        List<String> options = Arrays.asList("True1", "False1", "True2", "False2");
        List<String> correct = Arrays.asList("True1", "True2");
        MultipleChoiceMultipleAnswersQuestion question = new MultipleChoiceMultipleAnswersQuestion("Select true statements", options, correct);
        
        List<String> userSelections1 = Arrays.asList("True1", "False1");
        List<String> userSelections2 = Arrays.asList("True1");
        List<String> userSelections3 = Arrays.asList("True1", "True2", "False1");
        List<String> userSelections4 = Arrays.asList("False1", "False2");
        
        assertFalse(question.isCorrect(userSelections1)); // One wrong
        assertFalse(question.isCorrect(userSelections2)); // Missing one correct
        assertFalse(question.isCorrect(userSelections3)); // Extra wrong selection
        assertFalse(question.isCorrect(userSelections4)); // All wrong
    }
    
    public void testNonListInputs() {
        List<String> options = Arrays.asList("A", "B", "C");
        List<String> correct = Arrays.asList("A", "B");
        MultipleChoiceMultipleAnswersQuestion question = new MultipleChoiceMultipleAnswersQuestion("Test", options, correct);
        
        assertFalse(question.isCorrect("A")); // String
        assertFalse(question.isCorrect(123)); // Integer
        assertFalse(question.isCorrect(45.6)); // Double
        assertFalse(question.isCorrect(true)); // Boolean
        assertFalse(question.isCorrect(null)); // Null
        assertFalse(question.isCorrect(new HashMap<>())); // Map
    }
    
    public void testOptionsGetterSetter() {
        List<String> options = Arrays.asList("X", "Y", "Z");
        List<String> correct = Arrays.asList("X");
        MultipleChoiceMultipleAnswersQuestion question = new MultipleChoiceMultipleAnswersQuestion("Test", options, correct);
        
        assertEquals(options, question.getOptions());
        
        List<String> newOptions = Arrays.asList("A", "B", "C", "D");
        question.setOptions(newOptions);
        assertEquals(newOptions, question.getOptions());
    }
    
    public void testEmptySelections() {
        List<String> options = Arrays.asList("Option1", "Option2");
        List<String> correct = new ArrayList<>();
        MultipleChoiceMultipleAnswersQuestion question = new MultipleChoiceMultipleAnswersQuestion("Select none", options, correct);
        
        List<String> emptySelections = new ArrayList<>();
        List<String> someSelections = Arrays.asList("Option1");
        
        assertTrue(question.isCorrect(emptySelections)); // Both empty
        assertFalse(question.isCorrect(someSelections)); // User selected when none correct
    }
    
    public void testSingleCorrectAnswer() {
        List<String> options = Arrays.asList("Wrong1", "Correct", "Wrong2");
        List<String> correct = Arrays.asList("Correct");
        MultipleChoiceMultipleAnswersQuestion question = new MultipleChoiceMultipleAnswersQuestion("Pick one", options, correct);
        
        List<String> userSelections1 = Arrays.asList("Correct");
        List<String> userSelections2 = Arrays.asList("Wrong1");
        List<String> userSelections3 = Arrays.asList("Correct", "Wrong1");
        
        assertTrue(question.isCorrect(userSelections1)); // Correct single selection
        assertFalse(question.isCorrect(userSelections2)); // Wrong selection
        assertFalse(question.isCorrect(userSelections3)); // Extra selection
    }
    
    public void testAllOptionsCorrect() {
        List<String> options = Arrays.asList("All True 1", "All True 2", "All True 3");
        List<String> correct = Arrays.asList("All True 1", "All True 2", "All True 3");
        MultipleChoiceMultipleAnswersQuestion question = new MultipleChoiceMultipleAnswersQuestion("All are correct", options, correct);
        
        List<String> userSelections1 = Arrays.asList("All True 1", "All True 2", "All True 3");
        List<String> userSelections2 = Arrays.asList("All True 1", "All True 2");
        
        assertTrue(question.isCorrect(userSelections1)); // All selected
        assertFalse(question.isCorrect(userSelections2)); // Missing one
    }
} 