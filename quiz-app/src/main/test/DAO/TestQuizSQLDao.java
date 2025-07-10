package DAO;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import models.Quiz;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestQuizSQLDao {
    private static Connection connection;
    private static QuizSQLDao quizDao;

    @BeforeAll
    static void setUpClass() throws Exception {
        // Initialize database connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbUrl = "jdbc:mysql://localhost:3306/quizmastertest_db";
            String dbUser = "root";
            String dbPassword = "KoMSHi!!17"; // change with your database password

            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            System.out.println("Test database connection established successfully");

            quizDao = new QuizSQLDao(connection);

            initializeDatabaseTables(connection);

        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            throw new Exception("Failed to load MySQL driver", e);
        } catch (SQLException e) {
            System.err.println("Test database connection failed: " + e.getMessage());
            throw new Exception("Failed to connect to test database", e);
        }
    }

    private static void initializeDatabaseTables(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();

        // Drop table if it exists to ensure clean state
        String dropTableSQL = "DROP TABLE IF EXISTS quizzes";
        stmt.execute(dropTableSQL);
        System.out.println("Dropped existing quizzes table (if it existed)");

        // Create fresh table
        String createTableSQL = "CREATE TABLE quizzes ("
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "title VARCHAR(255) NOT NULL, "
                + "description TEXT, "
                + "created_by BIGINT, "
                + "created_date DATETIME, "
                + "last_modified DATETIME, "
                + "randomize_questions BOOLEAN, "
                + "one_page BOOLEAN, "
                + "immediate_correction BOOLEAN, "
                + "practice_mode BOOLEAN"
                + ")";

        stmt.execute(createTableSQL);
        System.out.println("Created fresh quizzes table");
        stmt.close();
    }

    @BeforeEach
    void setUp() throws Exception {
        // Clean up any existing test data before each test
        cleanupTestData();
    }

    private void cleanupTestData() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            String deleteSQL = "DELETE FROM quizzes WHERE title LIKE '%Test%' OR title LIKE '%JUnit%'";
            Statement stmt = connection.createStatement();
            stmt.execute(deleteSQL);
            stmt.close();
        }
    }

    @AfterAll
    static void tearDownClass() throws Exception {
        if (connection != null && !connection.isClosed()) {
            // Clean up test data before closing
            cleanupTestDataStatic();
            connection.close();
            System.out.println("Test database connection closed");
        }
    }

    private static void cleanupTestDataStatic() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            String deleteSQL = "DELETE FROM quizzes WHERE title LIKE '%Test%' OR title LIKE '%JUnit%'";
            Statement stmt = connection.createStatement();
            stmt.execute(deleteSQL);
            stmt.close();
        }
    }

    @Test
    @Order(1)
    @DisplayName("Test adding and getting a quiz")
    void testAddAndGetQuiz() {
        Quiz quiz = new Quiz("JUnit Test Quiz", "A test quiz for JUnit", 1L);
        quizDao.addQuiz(quiz);

        assertNotNull(quiz.getId());

        Quiz fetchedQuiz = quizDao.getQuiz(quiz.getId());
        assertNotNull(fetchedQuiz);
        assertEquals("JUnit Test Quiz", fetchedQuiz.getTitle());
        assertEquals("A test quiz for JUnit", fetchedQuiz.getDescription());
    }

    @Test
    @Order(2)
    @DisplayName("Test updating a quiz")
    void testUpdateQuiz() {
        Quiz quiz = new Quiz("Old Title", "Old Description", 1L);
        quizDao.addQuiz(quiz);

        quiz.setTitle("Updated Title");
        quiz.setDescription("Updated Description");
        quiz.setPracticeMode(true);
        quizDao.updateQuiz(quiz);

        Quiz updatedQuiz = quizDao.getQuiz(quiz.getId());
        assertEquals("Updated Title", updatedQuiz.getTitle());
        assertEquals("Updated Description", updatedQuiz.getDescription());
        assertTrue(updatedQuiz.isPracticeMode());
    }

    @Test
    @Order(3)
    @DisplayName("Test removing a quiz")
    void testRemoveQuiz() {
        Quiz quiz = new Quiz("Removable Quiz", "This will be deleted", 1L);
        quizDao.addQuiz(quiz);
        Long id = quiz.getId();

        quizDao.removeQuiz(quiz);
        Quiz deletedQuiz = quizDao.getQuiz(id);

        assertNull(deletedQuiz);
    }

    @Test
    @Order(4)
    @DisplayName("Test getting user quizzes")
    void testGetUserQuizzes() {
        Quiz quiz1 = new Quiz("User Quiz 1", "First", 42L);
        Quiz quiz2 = new Quiz("User Quiz 2", "Second", 42L);
        Quiz quiz3 = new Quiz("Other User's Quiz", "Should not appear", 100L);

        quizDao.addQuiz(quiz1);
        quizDao.addQuiz(quiz2);
        quizDao.addQuiz(quiz3);

        ArrayList<Quiz> userQuizzes = quizDao.getUserQuizzes(42L);
        assertEquals(2, userQuizzes.size());
    }
}