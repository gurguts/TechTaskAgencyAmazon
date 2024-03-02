package com.example.techtaskagencyamazon.security;

import com.example.techtaskagencyamazon.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

/**
 * The component provides methods for creating, validating, and resolving tokens.
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final UserDetailsService userDetailsService;

    /**
     * Secret key for JWT.
     */
    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * JWT validity period in milliseconds.
     */
    @Value("${jwt.expiration}")
    private long validityMilliseconds;

    public JwtTokenProvider(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Method for initializing JwtTokenProvider. It generates a secret key for the JWT.
     *
     * @throws NoSuchAlgorithmException if no encryption algorithm was found
     */
    @PostConstruct
    protected void init() throws NoSuchAlgorithmException {
        logger.info("Initializing JwtTokenProvider");
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hash = sha256.digest(secretKey.getBytes());
        SecretKeySpec secretKeySpec = new SecretKeySpec(hash, "HmacSHA256");
        secretKey = Base64.getEncoder().encodeToString(secretKeySpec.getEncoded());
        logger.info("JwtTokenProvider initialized successfully");
    }

    /**
     * Method for creating JWT.
     *
     * @param login user login
     * @param role  user role
     * @return JWT
     */
    public String createToken(String login, String role) {
        logger.info("Creating token for user: {}", login);
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("role", role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityMilliseconds * 1000);

        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(originalKey, SignatureAlgorithm.HS256)
                .compact();
        logger.info("Token created successfully for user: {}", login);
        return token;
    }

    /**
     * Method for JWT validation.
     *
     * @param token JWT
     * @return true if the JWT is valid, false otherwise
     */
    public boolean validateToken(String token) {
        logger.info("Validating token");
        try {
            byte[] decodedKey = Base64.getDecoder().decode(secretKey);
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(originalKey)
                    .build()
                    .parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Error validating token: ", e);
            throw new JwtAuthenticationException("JWT token is expired or invalid", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Method for obtaining authentication via JWT.
     *
     * @param token JWT
     * @return the Authentication object
     */
    public Authentication getAuthentication(String token) {
        logger.info("Getting authentication for token");
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * Method to get username from JWT.
     *
     * @param token JWT
     * @return username
     */
    public String getUsername(String token) {
        logger.info("Getting username from token");
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        String username = Jwts.parserBuilder().
                setSigningKey(originalKey)
                .build().parseClaimsJws(token)
                .getBody()
                .getSubject();
        logger.info("Username retrieved from token: {}", username);
        return username;
    }

    /**
     * Method to resolve JWT from request.
     *
     * @param request HTTP request
     * @return JWT
     */
    public String resolveToken(HttpServletRequest request) {
        logger.info("Resolving token from request");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("authToken")) {
                    logger.info("Token resolved from request");
                    return cookie.getValue();
                }
            }
        }
        logger.warn("No token found in request");
        return null;
    }
}
