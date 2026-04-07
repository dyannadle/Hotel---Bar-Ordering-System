package com.hotel.ordering.security.jwt; // Package for our JSON Web Token logic.

import com.hotel.ordering.service.UserDetailsImpl; // Using our custom UserDetails wrapper.
import io.jsonwebtoken.*; // JJWT library for creating and parsing tokens.
import io.jsonwebtoken.io.Decoders; // Used to decode our Base64 secret key.
import io.jsonwebtoken.security.Keys; // Used to create a cryptographic key for signing.
import org.slf4j.Logger; // Logger interface for printing errors to the console.
import org.slf4j.LoggerFactory; // Factory for creating the logger.
import org.springframework.beans.factory.annotation.Value; // Used to read from application.properties.
import org.springframework.security.core.Authentication; // Represents the logged-in user.
import org.springframework.stereotype.Component; // Marks this class as a Spring Bean.

import java.security.Key; // Java's standard interface for cryptographic keys.
import java.util.Date; // For setting the token's Issue and Expiration dates.

/**
 * JwtUtils: The "Passport Office" of our restaurant system.
 * This class creates, parses, and validates the 12-hour JWT tokens.
 */
@Component // Tells Spring to manage this class so we can @Autowired it later.
public class JwtUtils { // Utility class for JWT operations.

    // Creates a logger to help us debug token errors in the terminal.
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwt.secret}") // Reads the secret key from application.properties.
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}") // Reads the 12-hour limit (43200000ms).
    private int jwtExpirationMs;

    /**
     * generateJwtToken: Creates the "Passport" after a staff member logs in.
     */
    public String generateJwtToken(Authentication authentication) { // Takes the login session info.

        // 1. Get the current staff member's details.
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        // 2. Build the token with a Subject (Username), Issue Date, and Expiration Date.
        return Jwts.builder() // Start building the token...
                .setSubject((userPrincipal.getUsername())) // Set the "Owner" of this token.
                .setIssuedAt(new Date()) // Set the time the token was created (now).
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Set expiration to +12 hours.
                .signWith(key(), SignatureAlgorithm.HS256) // Sign it with our secret key so it can't be faked!
                .compact(); // Finalize and return the long token string.
    }

    /**
     * key: Converts our text-based secret from properties into a real Cryptographic Key.
     */
    private Key key() {
        // We use the raw bytes of our secret string to create the HMAC-SHA key.
        return Keys.hmacShaKeyFor(jwtSecret.getBytes()); 
    }

    /**
     * getUserNameFromJwtToken: Reads the "Passport" to see who it belongs to.
     */
    public String getUserNameFromJwtToken(String token) { // Takes the token string.
        return Jwts.parserBuilder() // Prepare the parser...
                .setSigningKey(key()) // Use our secret key to verify the signature.
                .build() // Build the parser.
                .parseClaimsJws(token) // Read the token data.
                .getBody() // Get the "Claims" (Body).
                .getSubject(); // Extract the Username we stored earlier.
    }

    /**
     * validateJwtToken: Checks if the "Passport" is real, expired, or tampered with.
     */
    public boolean validateJwtToken(String authToken) { // Takes the token string.
        try {
            // Try to parse the token using our secret key.
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true; // If no errors occur, the token is VALID!
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage()); // Token format is wrong.
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage()); // 12 hours have passed!
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage()); // Token type isn't JWT.
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage()); // Token is blank.
        }

        return false; // If any error caught above, the token is INVALID!
    }
}
