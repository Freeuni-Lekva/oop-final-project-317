package DAO;

import DAO.QuizHistorySQLDao;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import models.QuizHistory;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuizHistorySQLDaoTest {

    private static Connection connection;
    private static QuizHistorySQLDao historyDao;

    @BeforeAll
    static void setUpClass() throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbUrl = "jdbc:mysql://localhost:3306/quizmastertest_db";
            String dbUser = "root";
            String dbPassword = "KoMSHi!!17"; // change with your database password

            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            historyDao = new QuizHistorySQLDao(connection);

            initializeDatabaseTables(connection);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to set up test database", e);
        }
    }

    private static void initializeDatabaseTables(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();

        String dropTableSQL = "DROP TABLE IF EXISTS quiz_history";
        stmt.execute(dropTableSQL);

        String createTableSQL = "CREATE TABLE quiz_history ("
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "user_id BIGINT NOT NULL, "
                + "quiz_id BIGINT NOT NULL, "
                + "score INT NOT NULL, "
                + "time_taken INT NOT NULL, "
                + "completed_date DATETIME NOT NULL"
                + ")";
        stmt.execute(createTableSQL);
        stmt.close();
    }

    @BeforeEach
    void setUp() throws Exception {
        // Clean up any existing test data before each test
        cleanupTestData();
    }

    private void cleanupTestData() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            Statement stmt = connection.createStatement();
            stmt.execute("DELETE FROM quiz_history");
            stmt.close();
        }
    }

    @AfterAll
    static void tearDownClass() throws Exception {
        if (connection != null && !connection.isClosed()) {
            // Clean up test data before closing
            cleanupTestDataStatic();
            connection.close();
        }
    }

    private static void cleanupTestDataStatic() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            Statement stmt = connection.createStatement();
            stmt.execute("DELETE FROM quiz_history");
            stmt.close();
        }
    }

    @Test
    @Order(1)
    @DisplayName("Test adding and retrieving quiz history")
    void testAddAndRetrieveQuizHistory() {
        QuizHistory history = new QuizHistory(1L, 101L, 90, 300);
        history.setCompletedDate(LocalDateTime.now());

        historyDao.addQuizHistory(history);
        assertNotNull(history.getId());

        ArrayList<QuizHistory> userHistory = historyDao.getUserQuizHistory(1L);
        assertEquals(1, userHistory.size());
        assertEquals(90, userHistory.get(0).getScore());
    }

    @Test
    @Order(2)
    @DisplayName("Test getting user quiz attempts")
    void testGetUserQuizAttempts() {
        LocalDateTime now = LocalDateTime.now();

        QuizHistory attempt1 = new QuizHistory(2L, 202L, 70, 400);
        attempt1.setCompletedDate(now.minusDays(1));
        historyDao.addQuizHistory(attempt1);

        QuizHistory attempt2 = new QuizHistory(2L, 202L, 85, 350);
        attempt2.setCompletedDate(now);
        historyDao.addQuizHistory(attempt2);

        ArrayList<QuizHistory> attempts = historyDao.getUserQuizAttempts(2L, 202L);
        assertEquals(2, attempts.size());
        assertTrue(attempts.get(0).getScore() >= attempts.get(1).getScore() ||
                attempts.get(0).getCompletedDate().isAfter(attempts.get(1).getCompletedDate()));
    }

    @Test
    @Order(3)
    @DisplayName("Test getting best result")
    void testGetBestResult() {
        LocalDateTime now = LocalDateTime.now();

        QuizHistory result1 = new QuizHistory(3L, 303L, 60, 500);
        result1.setCompletedDate(now.minusDays(2));
        historyDao.addQuizHistory(result1);

        QuizHistory result2 = new QuizHistory(3L, 303L, 90, 450);
        result2.setCompletedDate(now.minusDays(1));
        historyDao.addQuizHistory(result2);

        QuizHistory result3 = new QuizHistory(3L, 303L, 85, 400);
        result3.setCompletedDate(now);
        historyDao.addQuizHistory(result3);

        QuizHistory best = historyDao.getBestResult(3L, 303L);
        assertNotNull(best);
        assertEquals(90, best.getScore());
    }

    @Test
    @Order(4)
    @DisplayName("Test getting latest attempt")
    void testGetLatestAttempt() {
        // Use a fixed 'now' truncated to seconds to prevent tiny timing discrepancies
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        QuizHistory older = new QuizHistory(4L, 404L, 50, 600);
        older.setCompletedDate(now.minusDays(3));
        historyDao.addQuizHistory(older);

        QuizHistory latest = new QuizHistory(4L, 404L, 75, 500);
        latest.setCompletedDate(now);
        historyDao.addQuizHistory(latest);

        QuizHistory fetched = historyDao.getLatestAttempt(4L, 404L);
        assertNotNull(fetched);
        assertEquals(75, fetched.getScore());
        assertEquals(now, fetched.getCompletedDate().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    @Order(5)
    @DisplayName("Test getting latest attempt when none exist")
    void testGetLatestAttemptWhenNoneExist() {
        QuizHistory result = historyDao.getLatestAttempt(999L, 999L);
        assertNull(result);
    }

    @Test
    @Order(6)
    @DisplayName("Test getting best result when none exist")
    void testGetBestResultWhenNoneExist() {
        QuizHistory result = historyDao.getBestResult(999L, 999L);
        assertNull(result);
    }

    @Test
    @Order(7)
    @DisplayName("Test adding quiz history with closed connection")
    void testAddQuizHistoryWithClosedConnection() throws Exception {
        connection.close();
        QuizHistory history = new QuizHistory(5L, 505L, 80, 300);
        history.setCompletedDate(LocalDateTime.now());
        historyDao.addQuizHistory(history);
    }
}
