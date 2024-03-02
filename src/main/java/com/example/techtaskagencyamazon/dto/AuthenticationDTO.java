package com.example.techtaskagencyamazon.dto;

import lombok.Data;

/**
 * DTO class for authorization
 */
@Data
public class AuthenticationDTO {
    private String login;
    private String password;
}