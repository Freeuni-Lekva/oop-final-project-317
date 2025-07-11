package DAO;

import DAO.QuestionSQLDao;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import models.Question;
import questions.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuestionSQLDaoTest {
    private static Connection connection;
    private static QuestionSQLDao questionDao;

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

            questionDao = new QuestionSQLDao(connection);

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
        String dropTableSQL = "DROP TABLE IF EXISTS questions";
        stmt.execute(dropTableSQL);
        System.out.println("Dropped existing questions table (if it existed)");

        // Create fresh table
        String createTableSQL = "CREATE TABLE questions ("
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "question_text TEXT NOT NULL, "
                + "question_type VARCHAR(100) NOT NULL, "
                + "quiz_id BIGINT NOT NULL, "
                + "correct_answers TEXT, "
                + "image_url VARCHAR(500), "
                + "points INT DEFAULT 1, "
                + "time_limit INT DEFAULT 60"
                + ")";

        stmt.execute(createTableSQL);
        System.out.println("Created fresh questions table");
        stmt.close();
    }

    @BeforeEach
    void setUp() throws Exception {
        // Clean up any existing test data before each test
        cleanupTestData();
    }

    private void cleanupTestData() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            String deleteSQL = "DELETE FROM questions WHERE question_text LIKE '%Test%' OR question_text LIKE '%JUnit%'";
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
            String deleteSQL = "DELETE FROM questions WHERE question_text LIKE '%Test%' OR question_text LIKE '%JUnit%'";
            Statement stmt = connection.createStatement();
            stmt.execute(deleteSQL);
            stmt.close();
        }
    }

    @Test
    @Order(1)
    @DisplayName("Test adding and getting a QuestionResponse question")
    void testAddAndGetQuestionResponse() {
        QuestionResponseQuestion question = new QuestionResponseQuestion(
                "What is the capital of France?", 
                Arrays.asList("Paris", "paris")
        );
        question.setQuizId(1L);
        question.setPoints(5);
        question.setTimeLimit(30);

        questionDao.addQuestion(question);
        assertNotNull(question.getId());

        Question fetchedQuestion = questionDao.getQuestion(question.getId());
        assertNotNull(fetchedQuestion);
        assertEquals("What is the capital of France?", fetchedQuestion.getQuestionText());
        assertEquals(Question.QUESTION_RESPONSE, fetchedQuestion.getQuestionType());
        assertEquals(Long.valueOf(1L), fetchedQuestion.getQuizId());
        assertEquals(5, fetchedQuestion.getPoints());
        assertEquals(30, fetchedQuestion.getTimeLimit());
        assertTrue(fetchedQuestion instanceof QuestionResponseQuestion);
    }

    @Test
    @Order(2)
    @DisplayName("Test adding and getting a MultipleChoice question")
    void testAddAndGetMultipleChoice() {
        MultipleChoiceQuestion question = new MultipleChoiceQuestion(
                "Which planet is closest to the sun?",
                Arrays.asList("Mercury", "Venus", "Earth", "Mars"),
                "Mercury"
        );
        question.setQuizId(2L);
        question.setPoints(3);

        questionDao.addQuestion(question);
        assertNotNull(question.getId());

        Question fetchedQuestion = questionDao.getQuestion(question.getId());
        assertNotNull(fetchedQuestion);
        assertEquals(Question.MULTIPLE_CHOICE, fetchedQuestion.getQuestionType());
        assertTrue(fetchedQuestion instanceof MultipleChoiceQuestion);
    }

    @Test
    @Order(3)
    @DisplayName("Test adding and getting a FillInBlank question")
    void testAddAndGetFillInBlank() {
        FillInBlankQuestion question = new FillInBlankQuestion(
                "The largest ocean is the _____ Ocean.",
                Arrays.asList("Pacific", "pacific")
        );
        question.setQuizId(3L);

        questionDao.addQuestion(question);
        Question fetchedQuestion = questionDao.getQuestion(question.getId());
        
        assertEquals(Question.FILL_IN_BLANK, fetchedQuestion.getQuestionType());
        assertTrue(fetchedQuestion instanceof FillInBlankQuestion);
    }

    @Test
    @Order(4)
    @DisplayName("Test adding and getting a PictureResponse question")
    void testAddAndGetPictureResponse() {
        PictureResponseQuestion question = new PictureResponseQuestion(
                "What animal is shown in this picture?",
                "http://example.com/dog.jpg",
                Arrays.asList("Dog", "dog", "Golden Retriever")
        );
        question.setQuizId(4L);

        questionDao.addQuestion(question);
        Question fetchedQuestion = questionDao.getQuestion(question.getId());
        
        assertEquals(Question.PICTURE_RESPONSE, fetchedQuestion.getQuestionType());
        assertEquals("http://example.com/dog.jpg", fetchedQuestion.getImageUrl());
        assertTrue(fetchedQuestion instanceof PictureResponseQuestion);
    }

    @Test
    @Order(5)
    @DisplayName("Test adding and getting a MultiAnswer question")
    void testAddAndGetMultiAnswer() {
        MultiAnswerQuestion question = new MultiAnswerQuestion(
                "Name three primary colors:",
                Arrays.asList("Red", "Blue", "Yellow"),
                false
        );
        question.setQuizId(5L);

        questionDao.addQuestion(question);
        Question fetchedQuestion = questionDao.getQuestion(question.getId());
        
        assertEquals(Question.MULTI_ANSWER, fetchedQuestion.getQuestionType());
        assertTrue(fetchedQuestion instanceof MultiAnswerQuestion);
    }

    @Test
    @Order(6)
    @DisplayName("Test adding and getting a MultipleChoiceMultipleAnswers question")
    void testAddAndGetMultipleChoiceMultipleAnswers() {
        MultipleChoiceMultipleAnswersQuestion question = new MultipleChoiceMultipleAnswersQuestion(
                "Which of the following are programming languages?",
                Arrays.asList("Java", "Python", "C++", "HTML", "CSS"),
                Arrays.asList("Java", "Python", "C++")
        );
        question.setQuizId(6L);

        questionDao.addQuestion(question);
        Question fetchedQuestion = questionDao.getQuestion(question.getId());
        
        assertEquals(Question.MULTIPLE_CHOICE_MULTIPLE_ANSWERS, fetchedQuestion.getQuestionType());
        assertTrue(fetchedQuestion instanceof MultipleChoiceMultipleAnswersQuestion);
    }

    @Test
    @Order(7)
    @DisplayName("Test removing a question")
    void testRemoveQuestion() {
        QuestionResponseQuestion question = new QuestionResponseQuestion(
                "JUnit Test Question to Remove",
                Arrays.asList("answer")
        );
        question.setQuizId(7L);

        questionDao.addQuestion(question);
        Long id = question.getId();
        assertNotNull(questionDao.getQuestion(id));

        questionDao.removeQuestion(question);
        Question deletedQuestion = questionDao.getQuestion(id);
        assertNull(deletedQuestion);
    }

    @Test
    @Order(8)
    @DisplayName("Test getting questions for a specific quiz")
    void testGetQuizQuestions() {
        QuestionResponseQuestion question1 = new QuestionResponseQuestion(
                "JUnit Test Question 1", Arrays.asList("answer1")
        );
        question1.setQuizId(100L);

        MultipleChoiceQuestion question2 = new MultipleChoiceQuestion(
                "JUnit Test Question 2", 
                Arrays.asList("answer2", "option2", "option3"),
                "answer2"
        );
        question2.setQuizId(100L);

        FillInBlankQuestion question3 = new FillInBlankQuestion(
                "JUnit Test Question 3", Arrays.asList("answer3")
        );
        question3.setQuizId(200L); // Different quiz

        questionDao.addQuestion(question1);
        questionDao.addQuestion(question2);
        questionDao.addQuestion(question3);

        ArrayList<Question> quiz100Questions = questionDao.getQuizQuestions(100L);
        assertEquals(2, quiz100Questions.size());

        ArrayList<Question> quiz200Questions = questionDao.getQuizQuestions(200L);
        assertEquals(1, quiz200Questions.size());
    }

    @Test
    @Order(9)
    @DisplayName("Test getting a non-existent question")
    void testGetNonexistentQuestion() {
        Question question = questionDao.getQuestion(999999L);
        assertNull(question);
    }

    @Test
    @Order(10)
    @DisplayName("Test getting questions for a non-existent quiz")
    void testGetEmptyQuizQuestions() {
        ArrayList<Question> questions = questionDao.getQuizQuestions(999999L);
        assertNotNull(questions);
        assertEquals(0, questions.size());
    }

    @Test
    @Order(11)
    @DisplayName("Test question with null image URL")
    void testQuestionWithNullImageUrl() {
        QuestionResponseQuestion question = new QuestionResponseQuestion(
                "Test question with no image",
                Arrays.asList("test answer")
        );
        question.setQuizId(8L);
        question.setImageUrl(null);

        questionDao.addQuestion(question);
        Question fetchedQuestion = questionDao.getQuestion(question.getId());
        
        assertNull(fetchedQuestion.getImageUrl());
    }

    @Test
    @Order(12)
    @DisplayName("Test question with empty answers")
    void testQuestionWithEmptyAnswers() {
        QuestionResponseQuestion question = new QuestionResponseQuestion(
                "Test question with empty answers",
                new ArrayList<>()
        );
        question.setQuizId(9L);

        questionDao.addQuestion(question);
        Question fetchedQuestion = questionDao.getQuestion(question.getId());
        
        assertNotNull(fetchedQuestion.getCorrectAnswers());
        assertEquals(0, fetchedQuestion.getCorrectAnswers().size());
    }
} 