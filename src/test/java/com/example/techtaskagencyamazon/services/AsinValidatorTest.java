package com.example.techtaskagencyamazon.services;

import com.example.techtaskagencyamazon.exception.ReportException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.List;

public class AsinValidatorTest {
    private final AsinValidator asinValidator = new AsinValidator();

    @Test
    public void testValidateSingleAsin() {
        String validAsin = "1234567890";
        Assertions.assertDoesNotThrow(() -> asinValidator.validate(validAsin));

        String invalidAsin = "12345";
        Assertions.assertThrows(ReportException.class, () -> asinValidator.validate(invalidAsin));
    }

    @Test
    public void testValidateAsinList() {
        List<String> validAsins = Arrays.asList("1234567890", "0987654321");
        Assertions.assertDoesNotThrow(() -> asinValidator.validate(validAsins));

        List<String> invalidAsins = Arrays.asList("1234567890", "12345");
        Assertions.assertThrows(ReportException.class, () -> asinValidator.validate(invalidAsins));
    }
}
