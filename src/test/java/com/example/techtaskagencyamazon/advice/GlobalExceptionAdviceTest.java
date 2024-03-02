package com.example.techtaskagencyamazon.advice;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionAdviceTest {

    @Test
    void handleCustomerException() {
        GlobalExceptionAdvice globalExceptionAdvice = new GlobalExceptionAdvice();

        Exception exception = mock(Exception.class);

        when(exception.getMessage()).thenReturn("Test exception message");

        ResponseEntity<String> responseEntity = globalExceptionAdvice.handleCustomerException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Test exception message", responseEntity.getBody());
    }
}

