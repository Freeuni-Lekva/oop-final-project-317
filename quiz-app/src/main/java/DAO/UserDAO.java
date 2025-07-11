package DAO;

import models.User;

import java.util.ArrayList;

public interface UserDAO {
    public void addUser(User user);

    public User getUser(long userId);

    public User getUser(String username);

    public User getUserByEmail(String email);

    public ArrayList<User> getFriends(long userId);

    public void addFriendship(long userId1, long userId2);
    
    // Search methods
    public ArrayList<User> searchUsers(String searchTerm, int limit);
}
