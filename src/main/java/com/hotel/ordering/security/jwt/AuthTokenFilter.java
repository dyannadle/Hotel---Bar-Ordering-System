package com.hotel.ordering.security.jwt; // Package for our filter logic.

import com.hotel.ordering.service.UserDetailsServiceImpl; // Using our database-based user loader.
import jakarta.servlet.FilterChain; // Represents the chain of filters for a request.
import jakarta.servlet.ServletException; // Standard exception for servlet operations.
import jakarta.servlet.http.HttpServletRequest; // Represents the incoming HTTP request.
import jakarta.servlet.http.HttpServletResponse; // Represents the outgoing HTTP response.
import org.slf4j.Logger; // Logger interface for terminal debugging.
import org.slf4j.LoggerFactory; // Factory for creating the logger.
import org.springframework.beans.factory.annotation.Autowired; // Used to manually inject beans.
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Session token.
import org.springframework.security.core.context.SecurityContextHolder; // The system's session storage.
import org.springframework.security.core.userdetails.UserDetails; // Spring's user details interface.
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // Source of auth details.
import org.springframework.util.StringUtils; // Utility for string checking.
import org.springframework.web.filter.OncePerRequestFilter; // Base class for filters that run once per request.

import java.io.IOException; // Standard Exception for input/output errors.

/**
 * AuthTokenFilter: The "Bouncer" of the system.
 * This filter runs once for every single request to check the staff's "Passport" (JWT).
 */
public class AuthTokenFilter extends OncePerRequestFilter { // Inherits from OncePerRequestFilter.

  @Autowired // Manually injects our JWT utility.
  private JwtUtils jwtUtils;

  @Autowired // Manually injects our User service.
  private UserDetailsServiceImpl userDetailsService;

  // Logger to help us see what the bouncer is doing in the console.
  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  /**
   * doFilterInternal: The main "Bouncer" logic.
   * Intercepts the request, checks for a token, and authorizes the user.
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException { // Takes the request, response, and the next filter.

    try {
      // 1. Try to extract the JWT (Passport) from the Authorization header.
      String jwt = parseJwt(request);

      // 2. If the token exists and is valid...
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        // 3. Get the username from the token.
        String username = jwtUtils.getUserNameFromJwtToken(jwt);

        // 4. Load the user's full details (Roles/Password) from the database.
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 5. Create a new authentication session for this user.
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());

        // 6. Add extra details from the specific web request (like IP address).
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 7. Store the authenticated user in the global Security Context!
        // This makes the user officially "logged in" for the rest of this request.
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      // If anything fails during token checking, log the error.
      logger.error("Cannot set staff member authentication: {}", e.getMessage());
    }

    // 8. Move to the next filter in the chain (e.g., the Controller).
    filterChain.doFilter(request, response);
  }

  /**
   * parseJwt: Extracts the "Bearer <token>" from the Authorization header.
   */
  private String parseJwt(HttpServletRequest request) { // Takes the request.
    // Get the value of the 'Authorization' header.
    String headerAuth = request.getHeader("Authorization");

    // Tokens usually start with "Bearer " followed by the long string.
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7); // Return only the token part (skip the first 7 characters).
    }

    return null; // Return null if no Bearer token was found.
  }
}
