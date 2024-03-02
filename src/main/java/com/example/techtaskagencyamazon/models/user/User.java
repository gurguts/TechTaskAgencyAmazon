package com.example.techtaskagencyamazon.models.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model for user
 */
@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String login;

    private String email;

    private String password;

    private Status status;

    private Role role;

    public User(String email, String login, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.status = Status.ACTIVE;
        this.role = Role.USER;
    }

    public User() {
    }
}
