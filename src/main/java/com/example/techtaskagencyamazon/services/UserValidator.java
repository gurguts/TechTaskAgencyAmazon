package com.example.techtaskagencyamazon.services;

import com.example.techtaskagencyamazon.exception.UserException;
import com.example.techtaskagencyamazon.interfaces.UserValidatorInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for validating user data.
 */
@Service
public class UserValidator implements UserValidatorInterface {
    private static final Logger logger = LoggerFactory.getLogger(UserValidator.class);

    /**
     * Method for validating user registration data.
     *
     * @param email    User email
     * @param password User password
     * @param login    User login
     */
    public void validateRegistrationData(String email, String password, String login) {
        logger.info("Validating registration data for user: {}", login);
        if (login.equals(password)) {
            throw new UserException("Password cannot be the same as login");
        }
        validateEmail(email);
        validatePassword(password);
        validateLogin(login);
        logger.info("All validations were successful for the user: {}", login);
    }

    /**
     * Method for validating user login.
     *
     * @param login User login
     */
    private void validateLogin(String login) {
        if (login == null) {
            throw new UserException("Login cannot be null");
        }
        if (login.length() < 2 || login.length() > 50) {
            throw new UserException("Login must be between 2 and 50 characters long");
        }
    }

    /**
     * Method for validating user password.
     *
     * @param password User password
     */
    private void validatePassword(String password) {
        if (password == null) {
            throw new UserException("Password cannot be null");
        }
        String passwordRegex = "^(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$";
        if (!password.matches(passwordRegex)) {
            throw new UserException(
                    "The password must have a minimum of 8 and a maximum of 20 characters, including one number, " +
                            "one special character, one lowercase letter, one uppercase letter, and no spaces");
        }
    }

    /**
     * Method for validating user email.
     *
     * @param email User email
     */
    private void validateEmail(String email) {
        if (email == null) {
            throw new UserException("Email cannot be null");
        }
        if (email.length() < 2 || email.length() > 100) {
            throw new UserException("Email must be between 2 and 100 characters long");
        }
        if (!email.contains("@")) {
            throw new UserException("Email must contain exactly one @");
        }
    }
}
