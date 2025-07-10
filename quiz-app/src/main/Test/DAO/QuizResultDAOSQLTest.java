package DAO;

import models.QuizResult;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * Unit tests for QuizResultSQLDAO class using real database
 * Uses quizmastertest_db database for testing
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuizResultDAOSQLTest {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/quizmastertest_db";
    private static final String DB_USER = "root"; // Change to your MySQL username
    private static final String DB_PASSWORD = "Gegaong20042222@"; // Change to your MySQL password

    private static Connection connection;
    private QuizResultSQLDAO dao;

    private static final Long USER_ID = 1L;
    private static final Long QUIZ_ID = 100L;
    private static final int TOTAL_QUESTIONS = 10;
    private static final int MAX_POINTS = 100;

    @BeforeAll
    static void setUpDatabase() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        String createQuizResultsTable =
                "CREATE TABLE IF NOT EXISTS quiz_results ("
                        + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                        + "user_id BIGINT NOT NULL, "
                        + "quiz_id BIGINT NOT NULL, "
                        + "score INT NOT NULL DEFAULT 0, "
                        + "total_questions INT NOT NULL, "
                        + "total_points INT NOT NULL DEFAULT 0, "
                        + "max_points INT NOT NULL, "
                        + "is_practice_mode BOOLEAN NOT NULL DEFAULT FALSE, "
                        + "is_completed BOOLEAN NOT NULL DEFAULT TRUE, "
                        + "completion_time_seconds INT DEFAULT 0"
                        + ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createQuizResultsTable);
        }
    }

    @BeforeEach
    void setUp() throws SQLException {
        dao = new QuizResultSQLDAO(connection);

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM quiz_results");
            stmt.execute("ALTER TABLE quiz_results AUTO_INCREMENT = 1");
        }
    }

    @AfterAll
    static void tearDownDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS quiz_results");
        }
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    @DisplayName("addQuizResult")
    void testAddQuizResultSuccess() throws SQLException {
        QuizResult quizResult = new QuizResult(USER_ID, QUIZ_ID, TOTAL_QUESTIONS, MAX_POINTS);
        quizResult.setScore(8);
        quizResult.setTotalPoints(85);
        quizResult.setPracticeMode(true);

        dao.addQuizResult(quizResult);

        assertNotNull(quizResult.getId());
        assertTrue(quizResult.getId() > 0);

        QuizResult retrieved = dao.getQuizResult(quizResult.getId());
        assertNotNull(retrieved);
        assertEquals(USER_ID, retrieved.getUserId());
        assertEquals(QUIZ_ID, retrieved.getQuizId());
        assertEquals(8, retrieved.getScore());
        assertEquals(85, retrieved.getTotalPoints());
        assertTrue(retrieved.isPracticeMode());
    }

    @Test
    @DisplayName("addQuizResult multiple")
    void testAddQuizResultMultiple() throws SQLException {
        QuizResult result1 = new QuizResult(USER_ID, QUIZ_ID, TOTAL_QUESTIONS, MAX_POINTS);
        result1.setScore(5);
        result1.setTotalPoints(50);

        QuizResult result2 = new QuizResult(USER_ID, QUIZ_ID + 1, TOTAL_QUESTIONS, MAX_POINTS);
        result2.setScore(7);
        result2.setTotalPoints(70);
        result2.setPracticeMode(true);

        dao.addQuizResult(result1);
        dao.addQuizResult(result2);

        assertNotNull(result1.getId());
        assertNotNull(result2.getId());
        assertNotEquals(result1.getId(), result2.getId());
    }

    @Test
    @DisplayName("getQuizResult success")
    void testGetQuizResultSuccess() throws SQLException {
        QuizResult original = new QuizResult(USER_ID, QUIZ_ID, TOTAL_QUESTIONS, MAX_POINTS);
        original.setScore(6);
        original.setTotalPoints(60);
        original.setPracticeMode(false);

        dao.addQuizResult(original);

        QuizResult retrieved = dao.getQuizResult(original.getId());

        assertNotNull(retrieved);
        assertEquals(original.getId(), retrieved.getId());
        assertEquals(USER_ID, retrieved.getUserId());
        assertEquals(QUIZ_ID, retrieved.getQuizId());
        assertEquals(6, retrieved.getScore());
        assertEquals(60, retrieved.getTotalPoints());
        assertFalse(retrieved.isPracticeMode());
    }

    @Test
    @DisplayName("getQuizResult not found")
    void testGetQuizResultNotFound() throws SQLException {
        QuizResult result = dao.getQuizResult(999L);
        assertNull(result);
    }

    @Test
    @DisplayName("removeQuizResult success")
    void testRemoveQuizResultSuccess() throws SQLException {
        QuizResult quizResult = new QuizResult(USER_ID, QUIZ_ID, TOTAL_QUESTIONS, MAX_POINTS);
        dao.addQuizResult(quizResult);

        Long id = quizResult.getId();
        assertNotNull(dao.getQuizResult(id));

        dao.removeQuizResult(id);

        assertNull(dao.getQuizResult(id));
    }

    @Test
    @DisplayName("getUserQuizResults success")
    void testGetUserQuizResultsSuccess() throws SQLException {
        QuizResult result1 = new QuizResult(USER_ID, QUIZ_ID, TOTAL_QUESTIONS, MAX_POINTS);
        result1.setScore(8);
        dao.addQuizResult(result1);

        QuizResult result2 = new QuizResult(USER_ID, QUIZ_ID + 1, TOTAL_QUESTIONS, MAX_POINTS);
        result2.setScore(6);
        dao.addQuizResult(result2);

        QuizResult result3 = new QuizResult(USER_ID + 1, QUIZ_ID, TOTAL_QUESTIONS, MAX_POINTS);
        result3.setScore(9);
        dao.addQuizResult(result3);

        ArrayList<QuizResult> userResults = dao.getUserQuizResults(USER_ID);

        assertEquals(2, userResults.size());
        assertTrue(userResults.stream().allMatch(r -> r.getUserId() == USER_ID));
    }

    @Test
    @DisplayName("getUserQuizResults empty")
    void testGetUserQuizResultsEmpty() throws SQLException {
        ArrayList<QuizResult> results = dao.getUserQuizResults(999L);
        assertEquals(0, results.size());
    }

    @Test
    @DisplayName("getQuizResults success")
    void testGetQuizResultsSuccess() throws SQLException {
        QuizResult result1 = new QuizResult(USER_ID, QUIZ_ID, TOTAL_QUESTIONS, MAX_POINTS);
        dao.addQuizResult(result1);

        QuizResult result2 = new QuizResult(USER_ID + 1, QUIZ_ID, TOTAL_QUESTIONS, MAX_POINTS);
        dao.addQuizResult(result2);

        QuizResult result3 = new QuizResult(USER_ID, QUIZ_ID + 1, TOTAL_QUESTIONS, MAX_POINTS);
        dao.addQuizResult(result3);

        ArrayList<QuizResult> quizResults = dao.getQuizResults(QUIZ_ID);

        assertEquals(2, quizResults.size());
        assertTrue(quizResults.stream().allMatch(r -> r.getQuizId() == QUIZ_ID));
    }

    @Test
    @DisplayName("getUserQuizResults with quizId")
    void testGetUserQuizResultsWithQuizId() throws SQLException {
        QuizResult result1 = new QuizResult(USER_ID, QUIZ_ID, TOTAL_QUESTIONS, MAX_POINTS);
        dao.addQuizResult(result1);

        QuizResult result2 = new QuizResult(USER_ID, QUIZ_ID + 1, TOTAL_QUESTIONS, MAX_POINTS);
        dao.addQuizResult(result2);

        QuizResult result3 = new QuizResult(USER_ID + 1, QUIZ_ID, TOTAL_QUESTIONS, MAX_POINTS);
        dao.addQuizResult(result3);

        ArrayList<QuizResult> results = dao.getUserQuizResults(USER_ID, QUIZ_ID);

        assertEquals(1, results.size());
        assertEquals(USER_ID, results.get(0).getUserId());
        assertEquals(QUIZ_ID, results.get(0).getQuizId());
    }

    @Test
    @DisplayName("getTopScores success")
    void testGetTopScoresSuccess() throws SQLException {
        QuizResult result1 = new QuizResult(USER_ID, QUIZ_ID, TOTAL_QUESTIONS, MAX_POINTS);
        result1.setScore(8);
        dao.addQuizResult(result1);

        QuizResult result2 = new QuizResult(USER_ID + 1, QUIZ_ID, TOTAL_QUESTIONS, MAX_POINTS);
        result2.setScore(10);
        dao.addQuizResult(result2);

        QuizResult result3 = new QuizResult(USER_ID + 2, QUIZ_ID, TOTAL_QUESTIONS, MAX_POINTS);
        result3.setScore(6);
        dao.addQuizResult(result3);

        ArrayList<QuizResult> topScores = dao.getTopScores(QUIZ_ID, 2);

        assertEquals(2, topScores.size());
        assertEquals(10, topScores.get(0).getScore()); // Highest score first
        assertEquals(8, topScores.get(1).getScore());
    }

    @Test
    @DisplayName("getPracticeResults success")
    void testGetPracticeResultsSuccess() throws SQLException {
        QuizResult practiceResult = new QuizResult(USER_ID, QUIZ_ID, TOTAL_QUESTIONS, MAX_POINTS);
        practiceResult.setPracticeMode(true);
        dao.addQuizResult(practiceResult);

        QuizResult officialResult = new QuizResult(USER_ID, QUIZ_ID + 1, TOTAL_QUESTIONS, MAX_POINTS);
        officialResult.setPracticeMode(false);
        dao.addQuizResult(officialResult);

        ArrayList<QuizResult> practiceResults = dao.getPracticeResults(USER_ID);

        assertEquals(1, practiceResults.size());
        assertTrue(practiceResults.get(0).isPracticeMode());
    }

    @Test
    @DisplayName("getOfficialResults success")
    void testGetOfficialResultsSuccess() throws SQLException {
        QuizResult practiceResult = new QuizResult(USER_ID, QUIZ_ID, TOTAL_QUESTIONS, MAX_POINTS);
        practiceResult.setPracticeMode(true);
        dao.addQuizResult(practiceResult);

        QuizResult officialResult = new QuizResult(USER_ID, QUIZ_ID + 1, TOTAL_QUESTIONS, MAX_POINTS);
        officialResult.setPracticeMode(false);
        dao.addQuizResult(officialResult);

        ArrayList<QuizResult> officialResults = dao.getOfficialResults(USER_ID);

        assertEquals(1, officialResults.size());
        assertFalse(officialResults.get(0).isPracticeMode());
    }

    @Test
    @DisplayName("complete test")
    void testComplete() throws SQLException {
        QuizResult quizResult = new QuizResult(USER_ID, QUIZ_ID, TOTAL_QUESTIONS, MAX_POINTS);
        quizResult.setScore(7);
        quizResult.setTotalPoints(75);
        quizResult.setPracticeMode(false);

        dao.addQuizResult(quizResult);
        Long id = quizResult.getId();

        QuizResult retrieved = dao.getQuizResult(id);
        assertNotNull(retrieved);
        assertEquals(7, retrieved.getScore());
        assertEquals(75, retrieved.getTotalPoints());
        assertFalse(retrieved.isPracticeMode());

        retrieved.incrementScore();
        retrieved.addPoints(10);

        assertEquals(8, retrieved.getScore());
        assertEquals(85, retrieved.getTotalPoints());

        dao.removeQuizResult(id);

        assertNull(dao.getQuizResult(id));
    }
}