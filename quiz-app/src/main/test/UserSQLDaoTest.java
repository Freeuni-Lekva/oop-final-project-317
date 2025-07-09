import DAO.UserSQLDao;

import com.mysql.cj.jdbc.JdbcConnection;
import models.User;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserSQLDaoTest {

    private static UserSQLDao userSQLDao;
    private static Connection dbConnection;

    @BeforeAll
    static void setUp() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbUrl = "jdbc:mysql://localhost:3306/quizmastertest_db";
            String dbUser = "root";
            String dbPassword = "Wiwibura22."; // change with your database password
            dbConnection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            System.out.println("Database connection established successfully");

        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }

        String createUsersTable = " CREATE TABLE IF NOT EXISTS users( " +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(255) NOT NULL, " +
                "email VARCHAR(255) NOT NULL, " +
                "pass_hash VARCHAR(255) NOT NULL, " +
                "passed_quizzes INT DEFAULT 0, " +
                "is_admin BOOLEAN DEFAULT FALSE, " +
                "is_banned BOOLEAN DEFAULT FALSE)";

        String createFriendshipsTable = " CREATE TABLE IF NOT EXISTS friendships ( " +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "user_id1 BIGINT NOT NULL, " +
                "user_id2 BIGINT NOT NULL, " +
                "FOREIGN KEY (user_id1) REFERENCES users(id), " +
                "FOREIGN KEY (user_id2) REFERENCES users(id))";

        try (Statement stmt = dbConnection.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createFriendshipsTable);
        }

        userSQLDao = new UserSQLDao(dbConnection);
    }

    @BeforeEach
    void cleanDatabase() throws SQLException {
        try (Statement stmt = dbConnection.createStatement()) {
            stmt.execute("DELETE FROM friendships");
            stmt.execute("DELETE FROM users");
            stmt.execute("ALTER TABLE friendships AUTO_INCREMENT = 1");
            stmt.execute("ALTER TABLE users       AUTO_INCREMENT = 1");
        }
    }


    @AfterAll
    static void tearDown() throws SQLException {
        if (dbConnection != null && !dbConnection.isClosed()) {
            dbConnection.close();
        }
    }

    private void insertTestUser(long id, String name, String email, String passHash,
                                int passedQuizzes, boolean isAdmin, boolean isBanned) throws SQLException {
        String sql = "INSERT INTO users (id, name, email, pass_hash, passed_quizzes, is_admin, is_banned) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, passHash);
            stmt.setInt(5, passedQuizzes);
            stmt.setBoolean(6, isAdmin);
            stmt.setBoolean(7, isBanned);
            stmt.executeUpdate();
        }
    }

    private void insertTestFriendship(long userId1, long userId2) throws SQLException {
        String sql = "INSERT INTO friendships (user_id1, user_id2) VALUES (?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setLong(1, userId1);
            stmt.setLong(2, userId2);
            stmt.executeUpdate();
        }
    }

    @Test
    void testAddUser_Success() throws SQLException {
        User user = new User(0L, "John Doe", "john@example.com", "hashedPassword", 5, false, false);

        userSQLDao.addUser(user);

        String sql = "SELECT * FROM users WHERE name = ? AND email = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setString(1, "John Doe");
            stmt.setString(2, "john@example.com");
            ResultSet rs = stmt.executeQuery();

            assertTrue(rs.next());
            assertEquals("John Doe", rs.getString("name"));
            assertEquals("john@example.com", rs.getString("email"));
            assertEquals("hashedPassword", rs.getString("pass_hash"));
            assertEquals(5, rs.getInt("passed_quizzes"));
            assertEquals(false, rs.getBoolean("is_admin"));
            assertEquals(false, rs.getBoolean("is_banned"));
        }
    }

    @Test
    void testAddUser_WithAdminAndBannedFlags() throws SQLException {
        User user = new User(0L, "Admin User", "admin@example.com", "adminHash", 10, true, true);

        userSQLDao.addUser(user);

        String sql = "SELECT * FROM users WHERE name = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setString(1, "Admin User");
            ResultSet rs = stmt.executeQuery();

            assertTrue(rs.next());
            assertEquals("Admin User", rs.getString("name"));
            assertEquals("admin@example.com", rs.getString("email"));
            assertEquals("adminHash", rs.getString("pass_hash"));
            assertEquals(10, rs.getInt("passed_quizzes"));
            assertEquals(true, rs.getBoolean("is_admin"));
            assertEquals(true, rs.getBoolean("is_banned"));
        }
    }

    @Test
    void testGetUserById_UserExists() throws SQLException {
        insertTestUser(1L, "John Doe", "john@example.com", "hashedPassword", 5, false, false);

        User result = userSQLDao.getUser(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("hashedPassword", result.getPassHash());
        assertEquals(5, result.getPassedQuizzes());
        assertEquals(false, result.getIfAdmin());
        assertEquals(false, result.getIfBanned());
    }

    @Test
    void testGetUserById_UserNotExists() {
        User result = userSQLDao.getUser(999L);

        assertNull(result);
    }

    @Test
    void testGetUserByUsername_UserExists() throws SQLException {
        insertTestUser(1L, "John Doe", "john@example.com", "hashedPassword", 5, false, false);

        User result = userSQLDao.getUser("John Doe");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("hashedPassword", result.getPassHash());
        assertEquals(5, result.getPassedQuizzes());
        assertEquals(false, result.getIfAdmin());
        assertEquals(false, result.getIfBanned());
    }

    @Test
    void testGetUserByUsername_UserNotExists() {
        User result = userSQLDao.getUser("NonExistentUser");

        assertNull(result);
    }

    @Test
    void testGetUserByUsername_WithSpecialCharacters() throws SQLException {
        insertTestUser(1L, "User's Name", "user@example.com", "hash", 0, false, false);

        User result = userSQLDao.getUser("User's Name");

        assertNotNull(result);
        assertEquals("User's Name", result.getName());
    }

    @Test
    void testGetFriends_MultipleFriends() throws SQLException {
        insertTestUser(1L, "User One", "user1@example.com", "hash1", 3, false, false);
        insertTestUser(2L, "Friend One", "friend1@example.com", "hash2", 5, false, false);
        insertTestUser(3L, "Friend Two", "friend2@example.com", "hash3", 7, true, false);
        insertTestUser(4L, "Not Friend", "notfriend@example.com", "hash4", 2, false, true);

        insertTestFriendship(1L, 2L);
        insertTestFriendship(1L, 3L);

        ArrayList<User> friends = userSQLDao.getFriends(1L);

        assertNotNull(friends);
        assertEquals(2, friends.size());

        boolean foundFriend1 = false, foundFriend2 = false;
        for (User friend : friends) {
            if (friend.getId() == 2L) {
                foundFriend1 = true;
                assertEquals("Friend One", friend.getName());
                assertEquals("friend1@example.com", friend.getEmail());
                assertEquals(5, friend.getPassedQuizzes());
                assertEquals(false, friend.getIfAdmin());
            } else if (friend.getId() == 3L) {
                foundFriend2 = true;
                assertEquals("Friend Two", friend.getName());
                assertEquals("friend2@example.com", friend.getEmail());
                assertEquals(7, friend.getPassedQuizzes());
                assertEquals(true, friend.getIfAdmin());
            }
        }

        assertTrue(foundFriend1, "Friend One should be in the results");
        assertTrue(foundFriend2, "Friend Two should be in the results");
    }

    @Test
    void testGetFriends_BidirectionalFriendship() throws SQLException {
        insertTestUser(1L, "User One", "user1@example.com", "hash1", 3, false, false);
        insertTestUser(2L, "Friend One", "friend1@example.com", "hash2", 5, false, false);

        // Test friendship in both directions
        insertTestFriendship(2L, 1L); // Friend is user_id1, user is user_id2

        ArrayList<User> friends = userSQLDao.getFriends(1L);

        assertNotNull(friends);
        assertEquals(1, friends.size());
        assertEquals(2L, friends.get(0).getId());
        assertEquals("Friend One", friends.get(0).getName());
    }

    @Test
    void testGetFriends_NoFriends() throws SQLException {
        insertTestUser(1L, "Lonely User", "lonely@example.com", "hash", 0, false, false);

        ArrayList<User> friends = userSQLDao.getFriends(1L);

        assertNotNull(friends);
        assertEquals(0, friends.size());
        assertTrue(friends.isEmpty());
    }

    @Test
    void testGetFriends_NonExistentUser() {
        ArrayList<User> friends = userSQLDao.getFriends(999L);

        assertNotNull(friends);
        assertEquals(0, friends.size());
    }

    @Test
    void testAddFriendship_Success() throws SQLException {
        insertTestUser(1L, "User One", "user1@example.com", "hash1", 3, false, false);
        insertTestUser(2L, "User Two", "user2@example.com", "hash2", 5, false, false);

        userSQLDao.addFriendship(1L, 2L);

        String sql = "SELECT * FROM friendships WHERE user_id1 = ? AND user_id2 = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setLong(1, 1L);
            stmt.setLong(2, 2L);
            ResultSet rs = stmt.executeQuery();

            assertTrue(rs.next());
            assertEquals(1L, rs.getLong("user_id1"));
            assertEquals(2L, rs.getLong("user_id2"));
        }
    }

    @Test
    void testAddFriendship_MultipleFriendships() throws SQLException {
        insertTestUser(1L, "User One", "user1@example.com", "hash1", 3, false, false);
        insertTestUser(2L, "User Two", "user2@example.com", "hash2", 5, false, false);
        insertTestUser(3L, "User Three", "user3@example.com", "hash3", 7, false, false);

        userSQLDao.addFriendship(1L, 2L);
        userSQLDao.addFriendship(1L, 3L);

        String sql = "SELECT COUNT(*) as count FROM friendships WHERE user_id1 = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setLong(1, 1L);
            ResultSet rs = stmt.executeQuery();

            assertTrue(rs.next());
            assertEquals(2, rs.getInt("count"));
        }
    }

    @Test
    void testIntegration_AddUserAndCreateFriendship() throws SQLException {
        User user1 = new User(0L, "Alice", "alice@example.com", "hashA", 2, false, false);
        User user2 = new User(0L, "Bob", "bob@example.com", "hashB", 4, false, false);

        userSQLDao.addUser(user1);
        userSQLDao.addUser(user2);

        User addedUser1 = userSQLDao.getUser("Alice");
        User addedUser2 = userSQLDao.getUser("Bob");

        assertNotNull(addedUser1);
        assertNotNull(addedUser2);

        userSQLDao.addFriendship(addedUser1.getId(), addedUser2.getId());

        ArrayList<User> aliceFriends = userSQLDao.getFriends(addedUser1.getId());
        ArrayList<User> bobFriends = userSQLDao.getFriends(addedUser2.getId());

        assertEquals(1, aliceFriends.size());
        assertEquals(1, bobFriends.size());
        assertEquals("Bob", aliceFriends.get(0).getName());
        assertEquals("Alice", bobFriends.get(0).getName());
    }
}