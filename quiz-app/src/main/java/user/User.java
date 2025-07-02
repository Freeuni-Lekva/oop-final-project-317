package user;

public class User {
    private String name;
    private String passHash;
    private int passedQuizzes;

    public User(String userName, String passwordHash) {
        name = userName;
        passHash = passwordHash;
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
}
