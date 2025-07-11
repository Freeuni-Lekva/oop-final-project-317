package Listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import DAO.UserSQLDao;
import com.quizmaster.util.PasswordUtil;
import models.User;
import DAO.QuizSQLDao;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbUrl = "jdbc:mysql://localhost:3306/quizmaster_db";
            String dbUser = "root";

            String dbPassword = "marikuna12"; // change with your database password

            Connection dbConnection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            context.setAttribute("dbConnection", dbConnection);

            initializeDatabaseTables(sce, dbConnection);

            System.out.println("Database connection established successfully");

        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }

        List<User> userList = new ArrayList<>();
        context.setAttribute("userList", userList);
        System.out.println("QuizMaster application initialized successfully");


    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        Connection dbConnection = (Connection) context.getAttribute("dbConnection");

        if (dbConnection != null) {
            try {
                dbConnection.close();
                System.out.println("Database connection closed successfully");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
        System.out.println("QuizMaster application shutting down");
    }

    private void initializeDatabaseTables(ServletContextEvent sce, Connection dbConnection) {
        ServletContext context = sce.getServletContext();

        // Create DAO objects
        QuizSQLDao quizSqlDao = new QuizSQLDao(dbConnection);
        UserSQLDao userSqlDao = new UserSQLDao(dbConnection);

        // Set DAO objects as context attributes
        context.setAttribute("quizDAO", quizSqlDao);
        context.setAttribute("userDAO", userSqlDao);

        try (Statement stmt = dbConnection.createStatement()) {
            context = sce.getServletContext();
            // Create users table
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                    + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(50) NOT NULL UNIQUE, "
                    + "email VARCHAR(255) NOT NULL UNIQUE, "
                    + "pass_hash VARCHAR(255) NOT NULL, "
                    + "passed_quizzes INT DEFAULT 0, "
                    + "nof_notifications INT DEFAULT 0, "
                    + "is_admin BOOLEAN DEFAULT FALSE, "
                    + "is_banned BOOLEAN DEFAULT FALSE, "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "quiz_created_count INT DEFAULT 0, "
                    + "quiz_taken_count INT DEFAULT 0"
                    + ")";

            stmt.executeUpdate(createUsersTable);
            System.out.println("Users table created/verified successfully");

            userSqlDao = new UserSQLDao(dbConnection);
            context.setAttribute("userSqlDao", userSqlDao);
            User admin = new User("admin", "admin@admin", PasswordUtil.hashPassword("12341234"), 0, true, false, 0, 0);
            userSqlDao.addUser(admin);

            // Create Friendsships table
            String createFriendshipsTable = "CREATE TABLE IF NOT EXISTS friendships ("
                    + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                    + "user_id1 BIGINT NOT NULL, "
                    + "user_id2 BIGINT NOT NULL, "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY (user_id1) REFERENCES users(id), "
                    + "FOREIGN KEY (user_id2) REFERENCES users(id), "
                    + "UNIQUE KEY unique_friendship (user_id1, user_id2)"
                    + ")";

            stmt.executeUpdate(createFriendshipsTable);
            System.out.println("Frienships table created/verified successfully");

            // Create quizzes table
            String createQuizTable = "CREATE TABLE IF NOT EXISTS quizzes ("
                    + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                    + "title VARCHAR(255) NOT NULL, "
                    + "description TEXT, "
                    + "created_by BIGINT, "
                    + "created_date DATETIME, "
                    + "last_modified DATETIME, "
                    + "randomize_questions BOOLEAN, "
                    + "one_page BOOLEAN, "
                    + "immediate_correction BOOLEAN, "
                    + "practice_mode BOOLEAN, "
                    + "time_limit INT DEFAULT 0, "
                    + "times_taken INT DEFAULT 0" + ")";
            stmt.executeUpdate(createQuizTable);
            System.out.println("Quiz table created/verified successfully");

            // Create questions table
            String createQuestionsTable = "CREATE TABLE IF NOT EXISTS questions ("
                    + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                    + "question_text TEXT NOT NULL, "
                    + "question_type VARCHAR(100) NOT NULL, "
                    + "quiz_id BIGINT NOT NULL, "
                    + "correct_answers TEXT, "
                    + "image_url VARCHAR(1024), "
                    + "points INT DEFAULT 1, "
                    + "time_limit INT DEFAULT 0, "
                    + "FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE"
                    + ")";
            stmt.executeUpdate(createQuestionsTable);
            System.out.println("Questions table created/verified successfully");


            // Create quiz result table
            String createQuizResultTable = "CREATE TABLE IF NOT EXISTS quiz_results ("
                    + "id BIGINT AUTO_INCREMENT PRIMARY KEY,"
                    + "user_id BIGINT NOT NULL,"
                    + "quiz_id BIGINT NOT NULL,"
                    + "score INT NOT NULL,"
                    + "total_questions INT NOT NULL,"
                    + "total_points INT NOT NULL,"
                    + "max_points INT NOT NULL,"
                    + "is_practice_mode BOOLEAN NOT NULL"
                    + ")";

            stmt.executeUpdate(createQuizResultTable);
            System.out.println("Quizz Result table created/verified successfully");

            // Create notifications table
            String createNotificationsTable = "CREATE TABLE IF NOT EXISTS notifications ("
                    + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                    + "from_id BIGINT NOT NULL, "
                    + "to_id BIGINT NOT NULL, "
                    + "title VARCHAR(200) NOT NULL, "
                    + "message TEXT, "
                    + "question_type VARCHAR(100), "
                    + "create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY (from_id) REFERENCES users(id) ON DELETE CASCADE, "
                    + "FOREIGN KEY (to_id) REFERENCES users(id) ON DELETE CASCADE"
                    + ")";

            stmt.executeUpdate(createNotificationsTable);
            System.out.println("Notifications table created/verified successfully");

            String createQuizHistoryTable = "CREATE TABLE IF NOT EXISTS quiz_history ("
                    + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                    + "user_id BIGINT NOT NULL, "
                    + "quiz_id BIGINT NOT NULL, "
                    + "score INT NOT NULL, "
                    + "time_taken INT NOT NULL, "
                    + "completed_date DATETIME NOT NULL, "
                    + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, "
                    + "FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE"
                    + ")";

            stmt.executeUpdate(createQuizHistoryTable);

            System.out.println("Quiz history table created/verified successfully");

            // After quizzes table – initialize DAO objects in context
            // make sure quizResultDAO is available in context
            DAO.QuizResultSQLDAO quizResultDAOObj = new DAO.QuizResultSQLDAO(dbConnection);
            context.setAttribute("quizResultDAO", quizResultDAOObj);

            // Make Question DAO available
            DAO.QuestionSQLDao questionDaoObj = new DAO.QuestionSQLDao(dbConnection);
            context.setAttribute("questionDAO", questionDaoObj);

            System.out.println("All database tables initialized successfully");

        } catch (SQLException e) {
            System.err.println("Error initializing database tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
}