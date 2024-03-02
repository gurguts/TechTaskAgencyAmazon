package com.example.techtaskagencyamazon.interfaces;

/**
 * The interface provides methods for checking the correctness of the entered data during registration.
 */
public interface UserValidatorInterface {
    void validateRegistrationData(String email, String password, String login);
}
