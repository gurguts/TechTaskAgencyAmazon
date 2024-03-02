package com.example.techtaskagencyamazon.restControllers;

import com.example.techtaskagencyamazon.dto.RegistrationDTO;
import com.example.techtaskagencyamazon.interfaces.UserServiceInterface;
import com.example.techtaskagencyamazon.interfaces.UserValidatorInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for processing new user registration requests.
 */
@RestController
@RequestMapping("api/v1/register")
public class RegistrationRestController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationRestController.class);
    private final UserServiceInterface userService;

    private final UserValidatorInterface userValidator;

    public RegistrationRestController(UserServiceInterface userService, UserValidatorInterface userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    /**
     * Method for registering a new user.
     *
     * @param registrationDTO DTO with registration data
     * @return ResponseEntity with a message about the registration result
     */
    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody RegistrationDTO registrationDTO) {
        logger.info("Received registration request for user: {}", registrationDTO.getLogin());
        userValidator.validateRegistrationData(registrationDTO.getEmail(),
                registrationDTO.getPassword(),
                registrationDTO.getLogin());
        String response = userService.registerUser(registrationDTO.getEmail(),
                registrationDTO.getLogin(),
                registrationDTO.getPassword());
        logger.info("User registered successfully.");
        return ResponseEntity.ok(response);
    }
}