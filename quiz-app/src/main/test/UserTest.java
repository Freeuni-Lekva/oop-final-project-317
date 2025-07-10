import models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "John Doe", "john@example.com", "hashedPassword", 5, false, false, null);
    }

    @Test
    void testConstructor_AllParameters() {
        long userId = 123L;
        String userName = "Jane Smith";
        String userEmail = "jane@example.com";
        String passwordHash = "abc123hash";
        int passedQuizzes = 10;
        boolean isAdmin = true;
        boolean isBanned = false;

        User newUser = new User(userId, userName, userEmail, passwordHash, passedQuizzes, isAdmin, isBanned, null);

        assertEquals(userId, newUser.getId());
        assertEquals(userName, newUser.getName());
        assertEquals(userEmail, newUser.getEmail());
        assertEquals(passwordHash, newUser.getPassHash());
        assertEquals(passedQuizzes, newUser.getPassedQuizzes());
        assertEquals(isAdmin, newUser.getIfAdmin());
        assertEquals(isBanned, newUser.getIfBanned());
    }

    @Test
    void testConstructor_WithBannedAdmin() {
        User bannedAdmin = new User(2L, "Banned Admin", "banned@example.com", "hash", 0, true, true, null);

        assertEquals(2L, bannedAdmin.getId());
        assertEquals("Banned Admin", bannedAdmin.getName());
        assertEquals("banned@example.com", bannedAdmin.getEmail());
        assertEquals("hash", bannedAdmin.getPassHash());
        assertEquals(0, bannedAdmin.getPassedQuizzes());
        assertTrue(bannedAdmin.getIfAdmin());
        assertTrue(bannedAdmin.getIfBanned());
    }

    @Test
    void testGetId() {
        assertEquals(1L, user.getId());
    }

    @Test
    void testGetName() {
        assertEquals("John Doe", user.getName());
    }

    @Test
    void testSetName() {
        String newName = "Jane Doe";

        user.setName(newName);

        assertEquals(newName, user.getName());
    }

    @Test
    void testSetName_EmptyString() {
        String emptyName = "";

        user.setName(emptyName);

        assertEquals(emptyName, user.getName());
    }

    @Test
    void testSetName_Null() {
        user.setName(null);

        assertNull(user.getName());
    }

    @Test
    void testSetName_SpecialCharacters() {
        String specialName = "José María O'Connor-Smith";

        user.setName(specialName);

        assertEquals(specialName, user.getName());
    }

    @Test
    void testGetEmail() {
        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    void testSetEmail() {
        String newEmail = "newemail@example.com";

        user.setEmail(newEmail);

        assertEquals(newEmail, user.getEmail());
    }

    @Test
    void testSetEmail_Null() {
        user.setEmail(null);

        assertNull(user.getEmail());
    }

    @Test
    void testSetEmail_ComplexFormat() {
        String complexEmail = "user+tag@sub.domain.co.uk";

        user.setEmail(complexEmail);

        assertEquals(complexEmail, user.getEmail());
    }

    @Test
    void testGetPassHash() {
        assertEquals("hashedPassword", user.getPassHash());
    }

    @Test
    void testSetPassHash() {
        String newHash = "newHashedPassword";

        user.setPassHash(newHash);

        assertEquals(newHash, user.getPassHash());
    }

    @Test
    void testSetPassHash_Null() {
        user.setPassHash(null);

        assertNull(user.getPassHash());
    }

    @Test
    void testSetPassHash_LongHash() {
        String longHash = "";
        for (int i = 0; i < 100; i++){
            longHash += "a";
        }

        user.setPassHash(longHash);

        assertEquals(longHash, user.getPassHash());
        assertEquals(100, user.getPassHash().length());
    }

    @Test
    void testGetPassedQuizzes() {
        assertEquals(5, user.getPassedQuizzes());
    }

    @Test
    void testSetPassedQuizzes() {
        int newQuizCount = 15;

        user.setPassedQuizzes(newQuizCount);

        assertEquals(newQuizCount, user.getPassedQuizzes());
    }

    @Test
    void testSetPassedQuizzes_Zero() {
        user.setPassedQuizzes(0);

        assertEquals(0, user.getPassedQuizzes());
    }

    @Test
    void testSetPassedQuizzes_NegativeValue() {
        int negativeValue = -5;

        user.setPassedQuizzes(negativeValue);

        assertEquals(negativeValue, user.getPassedQuizzes());
    }

    @Test
    void testSetPassedQuizzes_LargeValue() {
        int largeValue = Integer.MAX_VALUE;

        user.setPassedQuizzes(largeValue);

        assertEquals(largeValue, user.getPassedQuizzes());
    }

    @Test
    void testGetIfAdmin() {
        assertFalse(user.getIfAdmin());
    }

    @Test
    void testSetIfAdmin_True() {
        user.setIfAdmin(true);

        assertTrue(user.getIfAdmin());
    }

    @Test
    void testSetIfAdmin_False() {
        User adminUser = new User(2L, "Admin", "admin@example.com", "hash", 0, true, false, null);

        adminUser.setIfAdmin(false);

        assertFalse(adminUser.getIfAdmin());
    }

    @Test
    void testGetIfBanned() {
        assertFalse(user.getIfBanned());
    }

    @Test
    void testSetIfBanned_True() {
        user.setIfBanned(true);

        assertTrue(user.getIfBanned());
    }

    @Test
    void testSetIfBanned_False() {
        User bannedUser = new User(3L, "Banned User", "banned@example.com", "hash", 0, false, true, null);

        bannedUser.setIfBanned(false);

        assertFalse(bannedUser.getIfBanned());
    }

    @Test
    void testUserStateTransitions() {
        // Test admin promotion
        assertFalse(user.getIfAdmin());
        user.setIfAdmin(true);
        assertTrue(user.getIfAdmin());

        // Test banning
        assertFalse(user.getIfBanned());
        user.setIfBanned(true);
        assertTrue(user.getIfBanned());

        // Test unbanning
        user.setIfBanned(false);
        assertFalse(user.getIfBanned());

        // Test admin demotion
        user.setIfAdmin(false);
        assertFalse(user.getIfAdmin());
    }

    @Test
    void testMultiplePropertyChanges() {
        String newName = "Updated Name";
        String newEmail = "updated@example.com";
        String newHash = "updatedHash";
        int newQuizCount = 20;

        user.setName(newName);
        user.setEmail(newEmail);
        user.setPassHash(newHash);
        user.setPassedQuizzes(newQuizCount);
        user.setIfAdmin(true);
        user.setIfBanned(true);

        assertEquals(newName, user.getName());
        assertEquals(newEmail, user.getEmail());
        assertEquals(newHash, user.getPassHash());
        assertEquals(newQuizCount, user.getPassedQuizzes());
        assertTrue(user.getIfAdmin());
        assertTrue(user.getIfBanned());

        assertEquals(1L, user.getId());
    }

    @Test
    void testUserIdImmutable() {
        long originalId = user.getId();

        user.setName("New Name");
        user.setEmail("new@example.com");
        user.setPassHash("newHash");
        user.setPassedQuizzes(100);
        user.setIfAdmin(true);
        user.setIfBanned(true);

        assertEquals(originalId, user.getId());
    }

    @Test
    void testEdgeCaseValues() {
        // Test with minimum long value for ID
        User userWithMinId = new User(Long.MIN_VALUE, "Min User", "min@example.com", "hash", 0, false, false, null);
        assertEquals(Long.MIN_VALUE, userWithMinId.getId());

        // Test with maximum long value for ID
        User userWithMaxId = new User(Long.MAX_VALUE, "Max User", "max@example.com", "hash", 0, false, false, null);
        assertEquals(Long.MAX_VALUE, userWithMaxId.getId());

        // Test with zero ID
        User userWithZeroId = new User(0L, "Zero User", "zero@example.com", "hash", 0, false, false, null);
        assertEquals(0L, userWithZeroId.getId());
    }

    @Test
    void testQuizProgressTracking() {
        assertEquals(5, user.getPassedQuizzes());

        user.setPassedQuizzes(6);
        assertEquals(6, user.getPassedQuizzes());

        user.setPassedQuizzes(10);
        assertEquals(10, user.getPassedQuizzes());

        user.setPassedQuizzes(0);
        assertEquals(0, user.getPassedQuizzes());
    }
}