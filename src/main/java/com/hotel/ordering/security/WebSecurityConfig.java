package com.hotel.ordering.security; // Package for our security configuration.

import com.hotel.ordering.security.jwt.AuthEntryPointJwt; // Our custom unauthorized error handler.
import com.hotel.ordering.security.jwt.AuthTokenFilter; // Our custom JWT interceptor filter.
import com.hotel.ordering.service.UserDetailsServiceImpl; // Our service to load users from MySQL.
import lombok.RequiredArgsConstructor; // Lombok: Injects dependencies automatically!
import org.springframework.context.annotation.Bean; // Marks a method as a source of a Spring Bean.
import org.springframework.context.annotation.Configuration; // Marks this as a configuration class.
import org.springframework.security.authentication.AuthenticationManager; // The main login handler.
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // The provider for DB-based login.
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Helper for auth config.
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Enables @PreAuthorize on controllers.
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // used to configure the HTTP firewall.
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // Helper to disable default security features.
import org.springframework.security.config.http.SessionCreationPolicy; // used to set how sessions are managed.
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // The industry standard for password hashing.
import org.springframework.security.crypto.password.PasswordEncoder; // The interface for password hashing.
import org.springframework.security.web.SecurityFilterChain; // The main chain of security rules.
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // The default login filter.

/**
 * WebSecurityConfig: The "Command Center" for the system's security.
 * It defines who can access which part of the restaurant system.
 */
@Configuration // Tells Spring Boot that this is a configuration file.
@EnableMethodSecurity // Allows us to use @PreAuthorize("hasRole('ADMIN')") on our controller methods.
@RequiredArgsConstructor // Automatically creates a constructor for our private final fields!
public class WebSecurityConfig { // The main configuration class.

  // Service that tells Spring how to find our staff members in the database.
  private final UserDetailsServiceImpl userDetailsService; 

  // Handler that sends a 401 error if someone tries to access without a token.
  private final AuthEntryPointJwt unauthorizedHandler; 

  /**
   * authenticationJwtTokenFilter: Creates the "Passport Checkpoint".
   * This filter intercepts every request to see if the staff member has a valid JWT.
   */
  @Bean // Tells Spring to manage this object.
  public AuthTokenFilter authenticationJwtTokenFilter() { 
    return new AuthTokenFilter(); // Returns a new instance of our custom filter.
  }

  /**
   * authenticationProvider: Sets up the "Database Verifier".
   * This tells Spring Security to use our UserDetailsService and BCrypt encoder.
   */
  @Bean // Tells Spring to manage this object.
  public DaoAuthenticationProvider authenticationProvider() { 
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); // New provider instance.
     
    authProvider.setUserDetailsService(userDetailsService); // Link it to our staff database service.
    // Use BCrypt to safely hash passwords.
    authProvider.setPasswordEncoder(passwordEncoder()); // Link it to our BCrypt password hasher.
 
    return authProvider; // Returns the configured provider.
  }

  /**
   * authenticationManager: The "Login Boss".
   * This object is used in the AuthController to verify username/password.
   */
  @Bean // Tells Spring to manage this object.
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception { 
    return authConfig.getAuthenticationManager(); // Gets the manager from Spring's internal config.
  }

  /**
   * passwordEncoder: The "Secret Hashing Machine".
   * We use BCrypt to turn plain text passwords into unreadable hashes.
   */
  @Bean // Tells Spring to manage this object.
  public PasswordEncoder passwordEncoder() { 
    return new BCryptPasswordEncoder(); // Returns the BCrypt implementation.
  }

  /**
   * filterChain: The "Firewall Rules".
   * Defines which URLs are public (like Login) and which require a "Passport" (JWT).
   */
  @Bean // Tells Spring to manage this object.
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
    http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF (not needed for REST APIs using stateless JWT).
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler)) // Set our custom error handler.
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Tell Spring NOT to use sessions (Cookies).
        .authorizeHttpRequests(auth -> // Define URL rules...
          auth.requestMatchers("/api/auth/**").permitAll() // Allow everyone to Login/Signup.
              .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll() // Allow everyone to see API docs.
              .anyRequest().authenticated() // EVERY OTHER request requires a valid JWT!
        );
    
    // 1. Tell Spring how to verify identities using our database provider.
    http.authenticationProvider(authenticationProvider());

    // 2. Add our JWT "Bouncer" before the standard password filter.
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }
}
