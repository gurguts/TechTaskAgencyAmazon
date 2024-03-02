package com.example.techtaskagencyamazon.interfaces;

import java.util.List;

/**
 * Interface for ASIN validation.
 */
public interface AsinValidatorInterface {
    void validate(String asin);

    void validate(List<String> asinList);
}
