package com.example.techtaskagencyamazon.services;

import com.example.techtaskagencyamazon.exception.UserException;
import com.example.techtaskagencyamazon.interfaces.UserServiceInterface;
import com.example.techtaskagencyamazon.models.user.User;
import com.example.techtaskagencyamazon.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for working with users.
 */
@Service
public class UserService implements UserServiceInterface {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Method for searching for a user by login.
     *
     * @param login User login
     * @return An optional User object that can contain the user if found
     */
    public Optional<User> findByLogin(String login) {
        logger.info("Finding user by login: {}", login);
        return userRepository.findByLogin(login);
    }

    /**
     * Method for user registration.
     *
     * @param email    User email
     * @param login    User login
     * @param password User password
     * @return A string confirming successful registration
     */
    public String registerUser(String email, String login, String password) {
        logger.info("Registering user: {}", login);
        if (userRepository.existsByLogin(login)) {
            throw new UserException("Login is already in use");
        }

        User newUser = new User(email, login, passwordEncoder.encode(password));

        userRepository.save(newUser);

        logger.info("User registered successfully: {}", login);
        return "User registered successfully";
    }
}
