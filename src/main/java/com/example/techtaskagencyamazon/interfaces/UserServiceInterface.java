package com.example.techtaskagencyamazon.interfaces;

import com.example.techtaskagencyamazon.models.user.User;

import java.util.Optional;

/**
 * The interface provides methods for searching for a user by login and registering a new user.
 */
public interface UserServiceInterface {
    Optional<User> findByLogin(String login);

    String registerUser(String email, String login, String password);
}
