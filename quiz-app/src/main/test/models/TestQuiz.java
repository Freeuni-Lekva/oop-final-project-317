package models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestQuiz {
    private Quiz quiz;
    private MockQuestion mockQuestion1;
    private MockQuestion mockQuestion2;
    private MockQuestion mockQuestion3;

    @BeforeEach
    void setUp() {
        quiz = new Quiz("Sample Quiz", "A test quiz", 42L);

        // Create mock questions for testing
        mockQuestion1 = new MockQuestion("Question 1", "MULTIPLE_CHOICE", Arrays.asList("A"));
        mockQuestion1.setPoints(5);
        mockQuestion1.setTimeLimit(60);

        mockQuestion2 = new MockQuestion("Question 2", "FILL_IN_BLANK", Arrays.asList("Answer"));
        mockQuestion2.setPoints(10);
        mockQuestion2.setTimeLimit(120);

        mockQuestion3 = new MockQuestion("Question 3", "QUESTION_RESPONSE", Arrays.asList("Response"));
        mockQuestion3.setPoints(15);
        mockQuestion3.setTimeLimit(180);
    }

    @Test
    @Order(1)
    @DisplayName("Test initial values of quiz")
    void testInitialValues() {
        assertEquals("Sample Quiz", quiz.getTitle());
        assertEquals("A test quiz", quiz.getDescription());
        assertEquals(Long.valueOf(42), quiz.getCreatedBy());
        assertNotNull(quiz.getCreatedDate());
        assertNotNull(quiz.getLastModified());
        assertNotNull(quiz.getQuestions());
        assertEquals(0, quiz.getQuestionCount());
        assertEquals(0, quiz.getTotalPoints());
        assertEquals(0, quiz.getTotalTimeLimit());
    }

    @Test
    @Order(2)
    @DisplayName("Test setter and getter methods")
    void testSettersAndGetters() {
        quiz.setId(99L);
        assertEquals(Long.valueOf(99), quiz.getId());

        quiz.setTitle("New Title");
        assertEquals("New Title", quiz.getTitle());

        quiz.setDescription("New Description");
        assertEquals("New Description", quiz.getDescription());

        quiz.setCreatedBy(77L);
        assertEquals(Long.valueOf(77), quiz.getCreatedBy());

        LocalDateTime date = LocalDateTime.of(2020, 1, 1, 12, 0);
        quiz.setCreatedDate(date);
        assertEquals(date, quiz.getCreatedDate());

        LocalDateTime modified = LocalDateTime.of(2021, 1, 1, 13, 0);
        quiz.setLastModified(modified);
        assertEquals(modified, quiz.getLastModified());

        quiz.setRandomizeQuestions(true);
        assertTrue(quiz.isRandomizeQuestions());

        quiz.setOnePage(true);
        assertTrue(quiz.isOnePage());

        quiz.setImmediateCorrection(true);
        assertTrue(quiz.isImmediateCorrection());

        quiz.setPracticeMode(true);
        assertTrue(quiz.isPracticeMode());
    }

    @Test
    @Order(3)
    @DisplayName("Test toString method")
    void testToString() {
        String result = quiz.toString();
        assertTrue(result.contains("Sample Quiz"));
        assertTrue(result.contains("A test quiz"));
        assertTrue(result.contains("questionCount=0"));
        assertTrue(result.contains("totalPoints=0"));
        assertTrue(result.contains("totalTimeLimit=0"));
    }

    @Test
    @Order(4)
    @DisplayName("Test null questions list")
    void testNullQuestionsList() {
        quiz.setQuestions(null);
        assertEquals(0, quiz.getQuestionCount());
        assertEquals(0, quiz.getTotalPoints());
        assertEquals(0, quiz.getTotalTimeLimit());
    }

    @Test
    @Order(5)
    @DisplayName("Test that lastModified timestamp updates when quiz setters are called")
    void testLastModifiedUpdatesOnSetters() {
        LocalDateTime originalModified = quiz.getLastModified();

        // Small delay to ensure time difference
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        quiz.setTitle("Updated Title");
        assertTrue(quiz.getLastModified().isAfter(originalModified));

        LocalDateTime afterTitleChange = quiz.getLastModified();
        quiz.setDescription("Updated Description");
        assertTrue(quiz.getLastModified().isAfter(afterTitleChange));

        LocalDateTime afterDescChange = quiz.getLastModified();
        quiz.setRandomizeQuestions(true);
        assertTrue(quiz.getLastModified().isAfter(afterDescChange));

        LocalDateTime afterRandomChange = quiz.getLastModified();
        quiz.setOnePage(true);
        assertTrue(quiz.getLastModified().isAfter(afterRandomChange));

        LocalDateTime afterOnePageChange = quiz.getLastModified();
        quiz.setImmediateCorrection(true);
        assertTrue(quiz.getLastModified().isAfter(afterOnePageChange));

        LocalDateTime afterImmediateChange = quiz.getLastModified();
        quiz.setPracticeMode(true);
        assertTrue(quiz.getLastModified().isAfter(afterImmediateChange));
    }

    @Test
    @Order(6)
    @DisplayName("Test single question addition")
    void testAddSingleQuestion() {
        quiz.setId(1L);

        assertEquals(0, quiz.getQuestionCount());
        assertEquals(0, quiz.getTotalPoints());
        assertEquals(0, quiz.getTotalTimeLimit());

        quiz.addQuestion(mockQuestion1);

        assertEquals(1, quiz.getQuestionCount());
        assertEquals(5, quiz.getTotalPoints());
        assertEquals(60, quiz.getTotalTimeLimit());
        assertEquals(Long.valueOf(1), mockQuestion1.getQuizId());
        assertTrue(quiz.getQuestions().contains(mockQuestion1));
    }

    @Test
    @Order(7)
    @DisplayName("Test multiple questions addition")
    void testAddMultipleQuestions() {
        quiz.setId(2L);

        quiz.addQuestion(mockQuestion1);
        quiz.addQuestion(mockQuestion2);
        quiz.addQuestion(mockQuestion3);

        assertEquals(3, quiz.getQuestionCount());
        assertEquals(30, quiz.getTotalPoints()); // 5 + 10 + 15
        assertEquals(360, quiz.getTotalTimeLimit()); // 60 + 120 + 180

        assertEquals(Long.valueOf(2), mockQuestion1.getQuizId());
        assertEquals(Long.valueOf(2), mockQuestion2.getQuizId());
        assertEquals(Long.valueOf(2), mockQuestion3.getQuizId());
    }

    @Test
    @Order(8)
    @DisplayName("Test addition with null question list")
    void testAddQuestionWithNullQuestionsList() {
        quiz.setQuestions(null);

        quiz.addQuestion(mockQuestion1);

        assertEquals(0, quiz.getQuestionCount());
        assertEquals(0, quiz.getTotalPoints());
        assertEquals(0, quiz.getTotalTimeLimit());
    }

    @Test
    @Order(9)
    @DisplayName("Test that adding a question updates the lastModified timestamp")
    void testAddQuestionUpdatesLastModified() {
        quiz.setId(3L);
        LocalDateTime originalModified = quiz.getLastModified();

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        quiz.addQuestion(mockQuestion1);
        assertTrue(quiz.getLastModified().isAfter(originalModified));
    }

    @Test
    @Order(10)
    @DisplayName("Test empty questions list")
    void testEmptyQuestionsList() {
        quiz.setQuestions(new ArrayList<>());

        assertEquals(0, quiz.getQuestionCount());
        assertEquals(0, quiz.getTotalPoints());
        assertEquals(0, quiz.getTotalTimeLimit());
    }

    @Test
    @Order(11)
    @DisplayName("Test questions with zero points and time")
    void testQuestionsWithZeroPointsAndTime() {
        quiz.setId(4L);

        MockQuestion zeroQuestion = new MockQuestion("Zero Question", "MULTIPLE_CHOICE", Arrays.asList("A"));
        zeroQuestion.setPoints(0);
        zeroQuestion.setTimeLimit(0);

        quiz.addQuestion(zeroQuestion);

        assertEquals(1, quiz.getQuestionCount());
        assertEquals(0, quiz.getTotalPoints());
        assertEquals(0, quiz.getTotalTimeLimit());
    }

    @Test
    @Order(12)
    @DisplayName("Test toString with questions")
    void testToStringWithQuestions() {
        quiz.setId(5L);
        quiz.addQuestion(mockQuestion1);
        quiz.addQuestion(mockQuestion2);

        String result = quiz.toString();
        assertTrue(result.contains("Sample Quiz"));
        assertTrue(result.contains("A test quiz"));
        assertTrue(result.contains("questionCount=2"));
        assertTrue(result.contains("totalPoints=15"));
        assertTrue(result.contains("totalTimeLimit=180"));
    }

    @Test
    @Order(13)
    @DisplayName("Test boolean defaults")
    void testBooleanDefaults() {
        // Test that boolean settings default to false
        assertFalse(quiz.isRandomizeQuestions());
        assertFalse(quiz.isOnePage());
        assertFalse(quiz.isImmediateCorrection());
        assertFalse(quiz.isPracticeMode());
    }

    @Test
    @Order(14)
    @DisplayName("Test id type consistency")
    void testIdTypeConsistency() {
        quiz.setId(100L);
        assertTrue(quiz.getId() instanceof Long);
        assertEquals(Long.valueOf(100), quiz.getId());
    }

    // Mock Question class for testing
    private static class MockQuestion extends Question {
        public MockQuestion(String questionText, String questionType, List<String> correctAnswers) {
            super(questionText, questionType, correctAnswers);
        }

        @Override
        public boolean isCorrect(Object userAnswer) {
            return false;
        }
    }
}