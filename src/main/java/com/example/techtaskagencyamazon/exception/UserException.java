package com.example.techtaskagencyamazon.exception;

/**
 * Custom exception class for handling user errors.
 */
public class UserException extends IllegalArgumentException {
    public UserException(String message) {
        super(message);
    }
}
