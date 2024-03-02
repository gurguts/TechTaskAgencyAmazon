package com.example.techtaskagencyamazon.restController;

import com.example.techtaskagencyamazon.dto.RegistrationDTO;
import com.example.techtaskagencyamazon.interfaces.UserServiceInterface;
import com.example.techtaskagencyamazon.interfaces.UserValidatorInterface;
import com.example.techtaskagencyamazon.restControllers.RegistrationRestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RegistrationRestControllerTest {

    @Autowired
    private RegistrationRestController registrationRestController;

    @MockBean
    private UserServiceInterface userService;

    @MockBean
    private UserValidatorInterface userValidator;

    @Test
    public void testRegisterUser() {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setLogin("testUser");
        registrationDTO.setPassword("testPassword");
        registrationDTO.setEmail("testEmail");

        when(userService.registerUser(registrationDTO.getEmail(), registrationDTO.getLogin(), registrationDTO.getPassword())).thenReturn("User registered successfully");

        ResponseEntity<?> responseEntity = registrationRestController.registerUser(registrationDTO);

        verify(userValidator, times(1)).validateRegistrationData(registrationDTO.getEmail(), registrationDTO.getPassword(), registrationDTO.getLogin());
        assertEquals("User registered successfully", responseEntity.getBody());
    }
}
