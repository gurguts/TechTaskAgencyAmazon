package com.example.techtaskagencyamazon.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Class for global handling of exceptions that are thrown in controllers
 */
@ControllerAdvice
public class GlobalExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionAdvice.class);

    /**
     * The method handles all exceptions, writes to the logs,
     * returns a response with an error message and HttpStatus.BAD_REQUEST
     *
     * @param exception Accepts an exception to class Exception and lower in the hierarchy
     * @return returns error message and HttpStatus.BAD_REQUEST
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleCustomerException(Exception exception) {
        logger.error("Exception occurred: {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}