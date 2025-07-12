package DAO;

import models.User;

import java.util.ArrayList;

public interface UserDAO {
   
    void addUser(User user);

    User getUser(long userId);

    User getUser(String username);

    User getUserByEmail(String email);

    ArrayList<User> getFriends(long userId);

    void addFriendship(long userId1, long userId2);

    ArrayList<User> getAllUsers();

    void removeUser(User user);

    ArrayList<User> searchUsers(String searchTerm, int limit);

    boolean checkIfFriends(long userId1, long userId2);
    
    void removeFriendship(long userId1, long userId2);
}
