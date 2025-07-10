package Listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.User;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbUrl = "jdbc:mysql://localhost:3306/quizmaster_db";
            String dbUser = "root";
            String dbPassword = "your-password"; // change with your database password
            Connection dbConnection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            context.setAttribute("dbConnection", dbConnection);
            initializeDatabaseTables(dbConnection);
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

    private void initializeDatabaseTables(Connection dbConnection) {
        try (Statement stmt = dbConnection.createStatement()) {

            // Create users table
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(50) NOT NULL, "
                    + "email VARCHAR(255) NOT NULL UNIQUE, "
                    + "pass_hash VARCHAR(255) NOT NULL, "
                    + "passed_quizzes INT DEFAULT 0"
                    + ")";
            stmt.executeUpdate(createUsersTable);
            System.out.println("Users table created/verified successfully");

            // Create quizzes table
            String createQuizzesTable = "CREATE TABLE IF NOT EXISTS quizzes ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "title VARCHAR(200) NOT NULL, "
                    + "description TEXT, "
                    + "max_minutes INT NOT NULL, "
                    + "question_quantity INT NOT NULL"
                    + ")";
            stmt.executeUpdate(createQuizzesTable);
            System.out.println("Quizzes table created/verified successfully");

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
            System.out.println("Notifications table created/verified successfully");

            System.out.println("All database tables initialized successfully");

        } catch (SQLException e) {
            System.err.println("Error initializing database tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
}