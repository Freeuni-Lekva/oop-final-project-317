package models;

import java.sql.Time;
import java.sql.Timestamp;

public class User {
    private String name;
    private String passHash;
    private int passedQuizzes;
    private String email;
    private long userId;
    private boolean isAdmin;
    private boolean isBanned;
    private Timestamp createdAt;
    private int quizCreatedCount;
    private int quizTakenCount;


    public User(long userId, String userName, String userEmail, String passwordHash, int passedQuizzes, boolean isAdmin, boolean isBanned, Timestamp createdAt,  int quizCreatedCount, int quizTakenCount) {
        this.userId = userId;
        this.name = userName;
        this.email = userEmail;
        this.passHash = passwordHash;
        this.passedQuizzes = passedQuizzes;
        this.isAdmin = isAdmin;
        this.isBanned = isBanned;
        this.quizCreatedCount = quizCreatedCount;
        this.quizTakenCount = quizTakenCount;
    }

    public User(String userName, String userEmail, String passwordHash, int passedQuizzes, boolean isAdmin, boolean isBanned, int quizCreatedCount, int quizTakenCount) {
        this.userId = 0;
        this.name = userName;
        this.email = userEmail;
        this.passHash = passwordHash;
        this.passedQuizzes = passedQuizzes;
        this.isAdmin = isAdmin;
        this.isBanned = isBanned;
        this.quizCreatedCount = 0;
        this.quizTakenCount = 0;
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

    public long getId() {return  userId;}

    public Timestamp getCreatedAt() { return createdAt; }

    public void setQuizCreatedCount(Timestamp createdAt) { this.createdAt = createdAt; }
    public void increaseQuizCreatedCount() {this.quizCreatedCount++;}
    public int getQuizCreatedCount() { return quizCreatedCount; }

    public void setQuizTakenCount(int quizTakenCount) {this.quizTakenCount = quizTakenCount;}
    public void increaseQuizTakenCount() {this.quizTakenCount++;}
    public int getQuizTakenCount() { return quizTakenCount; }
}
