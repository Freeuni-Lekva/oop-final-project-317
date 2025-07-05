package questions;

import java.util.*;
import junit.framework.TestCase;

public class PictureResponseQuestionTest extends TestCase {
    
    public void testConstructorAndBasicFunctionality() {
        List<String> answers = Arrays.asList("Dog", "Canine", "Puppy");
        PictureResponseQuestion question = new PictureResponseQuestion("What animal is this?", "http://example.com/dog.jpg", answers);
        
        assertEquals("What animal is this?", question.getQuestionText());
        assertEquals("PICTURE_RESPONSE", question.getQuestionType());
        assertEquals("http://example.com/dog.jpg", question.getImageUrl());
        assertEquals(answers, question.getCorrectAnswers());
    }
    
    public void testMultipleCorrectAnswers() {
        List<String> answers = Arrays.asList("Bird", "Eagle", "Bald Eagle");
        PictureResponseQuestion question = new PictureResponseQuestion("Identify this bird", "http://example.com/eagle.jpg", answers);
        
        assertTrue(question.isCorrect("Bird"));
        assertTrue(question.isCorrect("Eagle"));
        assertTrue(question.isCorrect("Bald Eagle"));
        assertTrue(question.isCorrect("bird")); // Case insensitive
        assertTrue(question.isCorrect(" Eagle ")); // Whitespace handling
    }
    
    public void testIncorrectAnswers() {
        List<String> answers = Arrays.asList("Rose", "Flower");
        PictureResponseQuestion question = new PictureResponseQuestion("What is this?", "http://example.com/rose.jpg", answers);
        
        assertFalse(question.isCorrect("Tulip"));
        assertFalse(question.isCorrect("Tree"));
        assertFalse(question.isCorrect(""));
        assertFalse(question.isCorrect("Roses")); // Plural not accepted
    }
    
    public void testNonStringInputs() {
        List<String> answers = Arrays.asList("Car");
        PictureResponseQuestion question = new PictureResponseQuestion("Vehicle type?", "http://example.com/car.jpg", answers);
        
        assertFalse(question.isCorrect(123)); // Integer
        assertFalse(question.isCorrect(45.6)); // Double
        assertFalse(question.isCorrect(true)); // Boolean
        assertFalse(question.isCorrect(null)); // Null
        assertFalse(question.isCorrect(new ArrayList<>())); // List
    }
    
    public void testImageUrlHandling() {
        List<String> answers = Arrays.asList("Mountain");
        PictureResponseQuestion question = new PictureResponseQuestion("What landscape?", "https://photos.com/mountain.png", answers);
        
        assertEquals("https://photos.com/mountain.png", question.getImageUrl());
        assertTrue(question.isCorrect("Mountain"));
    }
    
    public void testNullImageUrl() {
        List<String> answers = Arrays.asList("Test");
        PictureResponseQuestion question = new PictureResponseQuestion("Test question", null, answers);
        
        assertNull(question.getImageUrl());
        assertTrue(question.isCorrect("Test"));
    }
    
    public void testEmptyAnswerList() {
        List<String> answers = new ArrayList<>();
        PictureResponseQuestion question = new PictureResponseQuestion("No answers", "http://example.com/image.jpg", answers);
        
        assertFalse(question.isCorrect("anything"));
        assertEquals(0, question.getCorrectAnswers().size());
    }
} 