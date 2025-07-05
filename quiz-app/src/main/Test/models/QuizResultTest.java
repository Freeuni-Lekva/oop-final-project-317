package models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for QuizResult class
 */
public class QuizResultTest {

    private QuizResult quizResult;
    private static final Long USER_ID = 1L;
    private static final Long QUIZ_ID = 100L;
    private static final int TOTAL_QUESTIONS = 10;
    private static final int MAX_POINTS = 100;

    @BeforeEach
    void setUp() {
        quizResult = new QuizResult(USER_ID, QUIZ_ID, TOTAL_QUESTIONS, MAX_POINTS);
    }

    @Test
    @DisplayName("Constructor Test")
    void testConstructor() {
        assertEquals(USER_ID, quizResult.getUserId());
        assertEquals(QUIZ_ID, quizResult.getQuizId());
        assertEquals(TOTAL_QUESTIONS, quizResult.getTotalQuestions());
        assertEquals(MAX_POINTS, quizResult.getMaxPoints());
        assertEquals(0, quizResult.getScore());
        assertEquals(0, quizResult.getTotalPoints());
        assertFalse(quizResult.isPracticeMode());
    }

    @Test
    @DisplayName("getId and setId")
    void testIdGetterSetter() {
        long testId = 1111;
        quizResult.setId(testId);
        assertEquals(testId, quizResult.getId());
    }

    @Test
    @DisplayName("getUserId and setUserId")
    void testUserIdGetterSetter() {
        long newUserId = 2222;
        quizResult.setUserId(newUserId);
        assertEquals(newUserId, quizResult.getUserId());
    }

    @Test
    @DisplayName("getQuizId and setQuizId")
    void testQuizIdGetterSetter() {
        long newQuizId = 3333;
        quizResult.setQuizId(newQuizId);
        assertEquals(newQuizId, quizResult.getQuizId());
    }

    @Test
    @DisplayName("getScore and setScore")
    void testScoreGetterSetter() {
        int newScore = 50;
        quizResult.setScore(newScore);
        assertEquals(newScore, quizResult.getScore());
    }

    @Test
    @DisplayName("getTotalQuestions and setTotalQuestions")
    void testTotalQuestionsGetterSetter() {
        int newTotalQuestions = 20;
        quizResult.setTotalQuestions(newTotalQuestions);
        assertEquals(newTotalQuestions, quizResult.getTotalQuestions());
    }

    @Test
    @DisplayName("getTotalPoints and setTotalPoints")
    void testTotalPointsGetterSetter() {
        int newTotalPoints = 100;
        quizResult.setTotalPoints(newTotalPoints);
        assertEquals(newTotalPoints, quizResult.getTotalPoints());
    }

    @Test
    @DisplayName("getMaxPoints and setMaxPoints")
    void testMaxPointsGetterSetter() {
        int newMaxPoints = 100;
        quizResult.setMaxPoints(newMaxPoints);
        assertEquals(newMaxPoints, quizResult.getMaxPoints());
    }

    @Test
    @DisplayName("isPracticeMode and setPracticeMode")
    void testPracticeModeGetterSetter() {
        quizResult.setPracticeMode(true);
        assertTrue(quizResult.isPracticeMode());

        quizResult.setPracticeMode(false);
        assertFalse(quizResult.isPracticeMode());
    }

    @Test
    @DisplayName("getPercentage")
    void testGetPercentage() {
        quizResult.setTotalPoints(80);
        quizResult.setMaxPoints(100);
        assertEquals(80.0, quizResult.getPercentage());
    }

    @Test
    @DisplayName("incrementScore")
    void testIncrementScore() {
        assertEquals(0, quizResult.getScore());

        quizResult.incrementScore();
        assertEquals(1, quizResult.getScore());

        quizResult.incrementScore();
        assertEquals(2, quizResult.getScore());
    }

    @Test
    @DisplayName("addPoints")
    void testAddPoints() {
        assertEquals(0, quizResult.getTotalPoints());

        quizResult.addPoints(2);
        assertEquals(2, quizResult.getTotalPoints());

        quizResult.addPoints(5);
        assertEquals(7, quizResult.getTotalPoints());
    }

    @Test
    @DisplayName("ranksHigherThan higher")
    void testRanksHigherThanHigherScore() {
        QuizResult other = new QuizResult(222L, 200L, 10, 100);

        quizResult.setScore(100);
        other.setScore(50);

        assertTrue(quizResult.ranksHigherThan(other));
    }

    @Test
    @DisplayName("ranksHigherThan lower")
    void testRanksHigherThanLowerScore() {
        QuizResult other = new QuizResult(2L, 200L, 10, 100);

        quizResult.setScore(30);
        other.setScore(70);

        assertFalse(quizResult.ranksHigherThan(other));
    }

    @Test
    @DisplayName("ranksHigherThan equal")
    void testRanksHigherThanEqualScores() {
        QuizResult other = new QuizResult(2L, 200L, 10, 100);

        quizResult.setScore(50);
        other.setScore(50);

        assertFalse(quizResult.ranksHigherThan(other));
    }

    @Test
    @DisplayName("toString")
    void testToString() {
        quizResult.setId(123L);
        quizResult.setScore(7);
        quizResult.setTotalPoints(85);
        quizResult.setPracticeMode(true);

        String result = quizResult.toString();

        assertTrue(result.contains("id=123"));
        assertTrue(result.contains("userId=1"));
        assertTrue(result.contains("quizId=100"));
        assertTrue(result.contains("score=7/10"));
        assertTrue(result.contains("points=85/100"));
        assertTrue(result.contains("percentage=85.0%"));
        assertTrue(result.contains("practiceMode=true"));
    }


    @Test
    @DisplayName("merged test")
    void testComplete() {
        quizResult.setId(1L);
        quizResult.setPracticeMode(false);

        quizResult.incrementScore();
        quizResult.addPoints(10);

        quizResult.incrementScore();
        quizResult.addPoints(15);

        quizResult.incrementScore();
        quizResult.addPoints(20);

        assertEquals(3, quizResult.getScore());
        assertEquals(45, quizResult.getTotalPoints());
        assertEquals(45.0, quizResult.getPercentage());
        assertFalse(quizResult.isPracticeMode());
    }

}