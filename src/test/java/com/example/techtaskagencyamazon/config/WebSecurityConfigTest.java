package com.example.techtaskagencyamazon.config;

import com.example.techtaskagencyamazon.interfaces.UserServiceInterface;
import com.example.techtaskagencyamazon.interfaces.UserValidatorInterface;
import com.example.techtaskagencyamazon.restControllers.RegistrationRestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvcD;
    private final UserServiceInterface userService = mock(UserServiceInterface.class);
    private final UserValidatorInterface userValidator = mock(UserValidatorInterface.class);
    private final RegistrationRestController registrationRestController = new RegistrationRestController(userService, userValidator);
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(registrationRestController).build();

    @Test
    public void testAccessToRegisterForAllUsers() throws Exception {
        when(userService.registerUser(anyString(), anyString(), anyString())).thenReturn("User registered successfully.");

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\", \"login\":\"testUser\", \"password\":\"Test@1234\"}"))
                .andExpect(status().isOk());

        verify(userService, times(1)).registerUser(anyString(), anyString(), anyString());
        verify(userValidator, times(1)).validateRegistrationData(anyString(), anyString(), anyString());
    }

    @Test
    public void shouldDenyAccessForUnauthenticatedUsers() throws Exception {
        mockMvcD.perform(get("/api/v1/auth/reports/salesAndTrafficByAsin"))
                .andExpect(status().isForbidden());
    }
}