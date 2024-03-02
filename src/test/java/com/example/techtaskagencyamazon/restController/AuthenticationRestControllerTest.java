package com.example.techtaskagencyamazon.restController;

import com.example.techtaskagencyamazon.dto.AuthenticationDTO;
import com.example.techtaskagencyamazon.models.user.Role;
import com.example.techtaskagencyamazon.models.user.User;
import com.example.techtaskagencyamazon.restControllers.AuthenticationRestController;
import com.example.techtaskagencyamazon.security.JwtTokenProvider;
import com.example.techtaskagencyamazon.services.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.http.HttpStatus;


import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationRestControllerTest {

    @Autowired
    private AuthenticationRestController authenticationRestController;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    public void testLogout() {
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        authenticationRestController.logout(httpServletResponse);

        Cookie cookie = httpServletResponse.getCookie("authToken");
        assert cookie != null;
        assertEquals("", cookie.getValue());
        assertEquals(0, cookie.getMaxAge());
        assertEquals("/", cookie.getPath());
    }

    @Test
    public void testAuthenticate() {
        AuthenticationDTO requestDTO = new AuthenticationDTO();
        requestDTO.setLogin("testUser");
        requestDTO.setPassword("testPassword");

        User testUser = new User();
        testUser.setLogin("testUser");
        testUser.setRole(Role.USER);

        when(userService.findByLogin(requestDTO.getLogin())).thenReturn(java.util.Optional.of(testUser));
        when(jwtTokenProvider.createToken(requestDTO.getLogin(), testUser.getRole().name())).thenReturn("testToken");

        ResponseEntity<?> responseEntity = authenticationRestController.authenticate(requestDTO);

        verify(authenticationManager, times(1)).authenticate(new UsernamePasswordAuthenticationToken(requestDTO.getLogin(), requestDTO.getPassword()));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Object responseBodyObject = responseEntity.getBody();
        if (responseBodyObject instanceof Map<?, ?> responseBody) {
            assertEquals("testUser", responseBody.get("login"));
            assertEquals("testToken", responseBody.get("token"));
        }

    }
}
