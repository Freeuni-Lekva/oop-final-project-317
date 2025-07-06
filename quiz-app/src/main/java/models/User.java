package models;

public class User {
    private String name;
    private String passHash;
    private int passedQuizzes;
    private String email;
    private long userId;
    private boolean isAdmin;
    private boolean isBanned;


    public User(long userId, String userName, String userEmail, String passwordHash, int passedQuizzes, boolean isAdmin, boolean isBanned) {
        this.userId = userId;
        this.name = userName;
        this.email = userEmail;
        this.passHash = passwordHash;
        this.passedQuizzes = passedQuizzes;
        this.isAdmin = isAdmin;
        this.isBanned = isBanned;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }
    public String getPassHash() {
        return passHash;
    }

    public void setPassedQuizzes(int passedQuizzes) {
        this.passedQuizzes = passedQuizzes;
    }
    public int getPassedQuizzes() {
        return passedQuizzes;
    }

    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return email; }

    public void setIfAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }
    public boolean getIfAdmin() { return isAdmin; }

    public void setIfBanned(boolean isBanned) { this.isBanned = isBanned; }
    public boolean getIfBanned() { return isBanned; }
}
