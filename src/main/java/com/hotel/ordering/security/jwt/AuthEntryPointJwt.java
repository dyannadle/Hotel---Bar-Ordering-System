package com.hotel.ordering.security.jwt; // Package for our filter logic.

import com.fasterxml.jackson.databind.ObjectMapper; // Jackson: Used to convert Java maps to JSON text.
import jakarta.servlet.ServletException; // Standard exception for servlet operations.
import jakarta.servlet.http.HttpServletRequest; // Represents the incoming HTTP request.
import jakarta.servlet.http.HttpServletResponse; // Represents our outgoing HTTP response.
import org.slf4j.Logger; // Logger interface for printing errors to the terminal.
import org.slf4j.LoggerFactory; // Factory for creating the logger.
import org.springframework.http.MediaType; // Constant for "application/json" content type.
import org.springframework.security.core.AuthenticationException; // Represents an authentication failure.
import org.springframework.security.web.AuthenticationEntryPoint; // Interface for handling 401 errors.
import org.springframework.stereotype.Component; // Marks this class as a Spring Bean.

import java.io.IOException; // Standard Exception for input/output errors.
import java.util.HashMap; // Using a Map to build our JSON error response.
import java.util.Map; // The interface for our error data map.

/**
 * AuthEntryPointJwt: The "Security Alarm".
 * This class catches unauthorized access attempts and returns a 401 Unauthorized error.
 */
@Component // Tells Spring to manage this class so we can use it in WebSecurityConfig.
public class AuthEntryPointJwt implements AuthenticationEntryPoint { // Implements the EntryPoint interface.

  // Logger to help us see unauthorized attempts in the server console.
  private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

  /**
   * commence: This method runs whenever an unauthenticated user tries to access a protected URL.
   */
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException { // Takes the request, response, and the specific error.

    // 1. Log the error message to the server's terminal for the developer to see.
    logger.error("Unauthorized error: {}", authException.getMessage());

    // 2. Set the response type to "application/json" so the frontend knows what's coming.
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    // 3. Set the HTTP status code to 401 (Unauthorized).
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    // 4. Build a Map containing the error details we want to send to the client.
    final Map<String, Object> body = new HashMap<>(); 
    body.put("status", HttpServletResponse.SC_UNAUTHORIZED); // The 401 status code.
    body.put("error", "Unauthorized"); // General error name.
    body.put("message", authException.getMessage()); // The specific reason (e.g. "Bad credentials").
    body.put("path", request.getServletPath()); // The URL the user was trying to access.

    // 5. Use the ObjectMapper (Jackson) to write the Map as a JSON string to the response output stream.
    final ObjectMapper mapper = new ObjectMapper(); 
    mapper.writeValue(response.getOutputStream(), body); // Converts the Map to JSON and sends it!
  }

}
