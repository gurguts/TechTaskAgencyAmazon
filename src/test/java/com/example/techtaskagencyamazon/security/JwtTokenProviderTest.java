package com.example.techtaskagencyamazon.security;

import com.example.techtaskagencyamazon.exception.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() throws Exception {
        userDetailsService = mock(UserDetailsService.class);

        jwtTokenProvider = new JwtTokenProvider(userDetailsService);

        String secretKey = "testSecret";
        long validityMilliseconds = 3600 * 1000;
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityMilliseconds", validityMilliseconds);

        jwtTokenProvider.init();

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateToken() {
        String login = "testUser";
        String role = "USER";

        String token = jwtTokenProvider.createToken(login, role);

        byte[] decodedKey = Base64.getDecoder().decode((String) ReflectionTestUtils.getField(jwtTokenProvider,
                "secretKey"));
        Key originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(originalKey).build().parseClaimsJws(token);
        Claims claims = claimsJws.getBody();

        assertEquals(login, claims.getSubject());
        assertEquals(role, claims.get("role"));
        assertTrue(claims.getExpiration().after(new Date()));
    }

    @Test
    public void testTokenIssuedAt() {
        String login = "testUser";
        String role = "USER";

        String token = jwtTokenProvider.createToken(login, role);

        byte[] decodedKey = Base64.getDecoder().decode((String) ReflectionTestUtils.getField(jwtTokenProvider,
                "secretKey"));
        Key originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(originalKey).build().parseClaimsJws(token);
        Claims claims = claimsJws.getBody();

        long currentMillis = System.currentTimeMillis();
        Date current = new Date(currentMillis);

        assertTrue(claims.getIssuedAt().before(current) || claims.getIssuedAt().equals(current));
    }

    @Test
    public void testValidateValidToken() {
        String login = "testUser";
        String role = "USER";
        String validToken = jwtTokenProvider.createToken(login, role);

        assertTrue(jwtTokenProvider.validateToken(validToken));
    }

    @Test
    public void testValidateInvalidToken() {
        String invalidToken = "invalidToken";

        Exception exception = assertThrows(JwtAuthenticationException.class, () ->
                jwtTokenProvider.validateToken(invalidToken));

        String expectedMessage = "JWT token is expired or invalid";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetAuthentication() {
        String login = "testUser";
        String role = "USER";
        String token = jwtTokenProvider.createToken(login, role);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(login)).thenReturn(userDetails);

        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        assertNotNull(authentication);
        assertEquals(userDetails, authentication.getPrincipal());
        assertEquals("", authentication.getCredentials());
        assertEquals(userDetails.getAuthorities(), authentication.getAuthorities());
    }

    @Test
    public void testResolveTokenWithTokenPresent() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String expectedToken = "testToken";
        request.setCookies(new Cookie("authToken", expectedToken));

        String token = jwtTokenProvider.resolveToken(request);
        assertEquals(expectedToken, token);
    }

    @Test
    public void testResolveTokenWithTokenAbsent() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("otherCookie", "value"));

        String token = jwtTokenProvider.resolveToken(request);
        assertNull(token);
    }
}
