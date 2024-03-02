package com.example.techtaskagencyamazon.dto;

import lombok.Data;

/**
 * DTO class for registration
 */
@Data
public class RegistrationDTO {
    private String login;

    private String email;

    private String password;
}
