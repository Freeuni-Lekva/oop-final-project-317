package DAO;

import junit.framework.TestCase;
import models.Quiz;

import java.sql.*;
import java.util.ArrayList;

public class TestQuizSQLDao extends TestCase {
    private Connection connection;
    private QuizSQLDao quizDao;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Initialize database connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbUrl = "jdbc:mysql://localhost:3306/quizmastertest_db";
            String dbUser = "root";
            String dbPassword = "marikuna12"; // change with your database password

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

    private void initializeDatabaseTables(Connection connection) throws SQLException {
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

    private void cleanupTestData() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            String deleteSQL = "DELETE FROM quizzes WHERE title LIKE '%Test%' OR title LIKE '%JUnit%'";
            Statement stmt = connection.createStatement();
            stmt.execute(deleteSQL);
            stmt.close();
        }
    }

    @Override
    protected void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            // Clean up test data before closing
            cleanupTestData();
            connection.close();
            System.out.println("Test database connection closed");
        }
        super.tearDown();
    }

    public void testAddAndGetQuiz() {
        Quiz quiz = new Quiz("JUnit Test Quiz", "A test quiz for JUnit", 1L);
        quizDao.addQuiz(quiz);

        assertNotNull(quiz.getId());

        Quiz fetchedQuiz = quizDao.getQuiz(quiz.getId());
        assertNotNull(fetchedQuiz);
        assertEquals("JUnit Test Quiz", fetchedQuiz.getTitle());
        assertEquals("A test quiz for JUnit", fetchedQuiz.getDescription());
    }

    public void testUpdateQuiz() {
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

    public void testRemoveQuiz() {
        Quiz quiz = new Quiz("Removable Quiz", "This will be deleted", 1L);
        quizDao.addQuiz(quiz);
        Long id = quiz.getId();

        quizDao.removeQuiz(quiz);
        Quiz deletedQuiz = quizDao.getQuiz(id);

        assertNull(deletedQuiz);
    }

    public void testGetUserQuizzes() {
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