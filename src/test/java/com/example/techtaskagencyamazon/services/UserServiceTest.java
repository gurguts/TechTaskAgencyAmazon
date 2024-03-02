package com.example.techtaskagencyamazon.services;

import com.example.techtaskagencyamazon.exception.UserException;
import com.example.techtaskagencyamazon.models.user.User;
import com.example.techtaskagencyamazon.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class UserServiceTest {
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    private final UserService userService = new UserService(userRepository, passwordEncoder);

    @Test
    public void testFindByLogin() {
        String login = "testUser";
        User user = new User("test@example.com", login, "Test@1234");

        Mockito.when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByLogin(login);

        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals(user, foundUser.get());
    }

    @Test
    public void testRegisterUser() {
        String email = "test@example.com";
        String login = "testUser";
        String password = "Test@1234";

        Mockito.when(userRepository.existsByLogin(login)).thenReturn(false);
        Mockito.when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        User user = new User(email, login, "encodedPassword");

        Mockito.when(userRepository.save(user)).thenReturn(user);

        String result = userService.registerUser(email, login, password);

        Assertions.assertEquals("User registered successfully", result);
    }

    @Test
    public void testRegisterUserFailure() {
        String email = "test@example.com";
        String login = "testUser";
        String password = "Test@1234";

        Mockito.when(userRepository.existsByLogin(login)).thenReturn(true);

        Assertions.assertThrows(UserException.class, () -> userService.registerUser(email, login, password));
    }

    @Test
    public void testRegisterUserFailureOnSave() {
        String email = "test@example.com";
        String login = "testUser";
        String password = "Test@1234";

        Mockito.when(userRepository.existsByLogin(login)).thenReturn(false);
        Mockito.when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        User user = new User(email, login, "encodedPassword");

        Mockito.when(userRepository.save(user)).thenThrow(new DataAccessException("Test exception") {
        });

        Assertions.assertThrows(DataAccessException.class, () -> userService.registerUser(email, login, password));
    }
}

