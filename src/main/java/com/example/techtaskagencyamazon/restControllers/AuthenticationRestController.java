package com.example.techtaskagencyamazon.restControllers;

import com.example.techtaskagencyamazon.dto.AuthenticationDTO;
import com.example.techtaskagencyamazon.interfaces.UserServiceInterface;
import com.example.techtaskagencyamazon.models.user.User;
import com.example.techtaskagencyamazon.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for processing authentication requests. It provides methods for logging in and logging out.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationRestController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationRestController.class);
    private final AuthenticationManager authenticationManager;

    private final UserServiceInterface userService;

    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationRestController(AuthenticationManager authenticationManager,
                                        UserServiceInterface userService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Метод для аутентификации пользователя.
     *
     * @param requestDTO DTO с данными для аутентификации
     * @return ResponseEntity с данными об аутентифицированном пользователе
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationDTO requestDTO) {
        logger.info("Received login request for user: {}", requestDTO.getLogin());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDTO.getLogin(), requestDTO.getPassword()));
        User user = userService.findByLogin(requestDTO.getLogin())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User \"" + requestDTO.getLogin() + "\" doesn't exist"));
        String token = jwtTokenProvider.createToken(requestDTO.getLogin(), user.getRole().name());

        Map<Object, Object> response = new HashMap<>();
        response.put("login", requestDTO.getLogin());
        response.put("token", token);

        logger.info("User {} authenticated successfully", requestDTO.getLogin());
        return ResponseEntity.ok(response);

    }

    /**
     * Метод для выхода из системы.
     *
     * @param httpServletResponse HTTP-ответ
     */
    @GetMapping("/logout")
    public void logout(HttpServletResponse httpServletResponse) {
        logger.info("Received logout request");
        Cookie cookie = new Cookie("authToken", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        logger.info("User logged out successfully");
    }
}