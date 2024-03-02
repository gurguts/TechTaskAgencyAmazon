package com.example.techtaskagencyamazon.services;

import com.example.techtaskagencyamazon.exception.UserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class UserValidatorTest {
    private final UserValidator userValidator = new UserValidator();

    @Test
    public void testValidateRegistrationData() {
        String validEmail = "test@example.com";
        String validPassword = "Test@1234";
        String validLogin = "testUser";

        Assertions.assertDoesNotThrow(() -> userValidator.validateRegistrationData(validEmail, validPassword, validLogin));

        String invalidEmail = "test";
        String invalidPassword = "test";
        String invalidLogin = "t";

        Assertions.assertThrows(UserException.class, () -> userValidator.validateRegistrationData(invalidEmail, validPassword, validLogin));
        Assertions.assertThrows(UserException.class, () -> userValidator.validateRegistrationData(validEmail, invalidPassword, validLogin));
        Assertions.assertThrows(UserException.class, () -> userValidator.validateRegistrationData(validEmail, validPassword, invalidLogin));
    }
}

