package com.hotel.ordering.controller; // Package for our web controller classes.

import com.hotel.ordering.entity.User; // Using our User database model.
import com.hotel.ordering.payload.request.LoginRequest; // DTO for sign-in data.
import com.hotel.ordering.payload.request.SignupRequest; // DTO for sign-up data.
import com.hotel.ordering.payload.response.JwtResponse; // DTO for successful login response.
import com.hotel.ordering.payload.response.MessageResponse; // DTO for simple messages.
import com.hotel.ordering.repository.UserRepository; // Using our User database repository.
import com.hotel.ordering.security.jwt.JwtUtils; // Our utility for token generation.
import com.hotel.ordering.service.UserDetailsImpl; // Our custom UserDetails wrapper.
import jakarta.validation.Valid; // Annotation for validating incoming JSON data.
import lombok.RequiredArgsConstructor; // Lombok: Injects dependencies automatically!
import org.springframework.http.ResponseEntity; // Wrapper for HTTP responses.
import org.springframework.security.authentication.AuthenticationManager; // Spring's main login handler.
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Token for login data.
import org.springframework.security.core.Authentication; // Represents an authenticated user session.
import org.springframework.security.core.context.SecurityContextHolder; // Holds security info for the current thread.
import org.springframework.security.crypto.password.PasswordEncoder; // Encoder for hashing passwords.
import org.springframework.web.bind.annotation.*; // Standard Spring Web annotations.

import java.util.List; // For list operations.
import java.util.stream.Collectors; // For data transformation streams.

/**
 * AuthController: The entrance to our system for staff.
 * We'll use this to create the initial Admin and log in other waiters/kitchen.
 */
@CrossOrigin(origins = "*", maxAge = 3600) // Allows web browsers to call this API safely.
@RestController // Marks this as a REST API controller.
@RequestMapping("/api/auth") // Base path for all authentication: /api/auth
@RequiredArgsConstructor // Automatically creates a constructor for dependencies.
public class AuthController { // The controller for Sign-in and Sign-up.

    private final AuthenticationManager authenticationManager; // Handles the sign-in logic.
    private final UserRepository userRepository; // Accesses the 'users' table in MySQL.
    private final PasswordEncoder encoder; // Encrypts passwords (BCrypt).
    private final JwtUtils jwtUtils; // Generates the 12-hour JWT tokens.

    /**
     * authenticateUser: The login endpoint ([POST] /api/auth/signin).
     */
    @PostMapping("/signin") // URL: /api/auth/signin
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) { 

        // 1. Authenticate the staff member's username and password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // 2. Set the authenticated user in the security context (Global access for this request).
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generate the "Passport" (JWT) for the next 12 hours.
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        // 4. Get the staff details to send back to the client.
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
        List<String> roles = userDetails.getAuthorities().stream() // Get the roles (e.g. ROLE_WAITER).
                .map(item -> item.getAuthority()) // Convert to strings.
                .collect(Collectors.toList()); // Collect into a list.

        // 5. Send back the token and user info as a 200 OK response.
        return ResponseEntity.ok(new JwtResponse(jwt, 
                                                 userDetails.getId(), 
                                                 userDetails.getUsername(), 
                                                 roles));
    }

    /**
     * registerUser: The signup endpoint ([POST] /api/auth/signup).
     * Used manually to create staff members.
     */
    @PostMapping("/signup") // URL: /api/auth/signup
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        // 1. Check if the username is already taken in the database.
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity // Return an error if name already exists.
                    .badRequest() // Sends 400 Bad Request.
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // 2. Create the new staff member account object.
        User user = new User(); // New User instance.
        user.setUsername(signUpRequest.getUsername()); // Set username from JSON.
        user.setName(signUpRequest.getName()); // Set display name.
        // Hash the password using BCrypt so it's not stored in plain text!
        user.setPassword(encoder.encode(signUpRequest.getPassword()));

        // 3. Set the role based on the request (WAITER, ADMIN, or KITCHEN_STAFF).
        String strRole = signUpRequest.getRole(); // Get role string from JSON.
        if (strRole == null) { // If role is empty...
            user.setRole(User.Role.WAITER); // Default is WAITER.
        } else { // If role is provided...
            switch (strRole.toLowerCase()) { // Check the value.
                case "admin": // If it's admin...
                    user.setRole(User.Role.ADMIN); // Set as Admin.
                    break;
                case "kitchen": // If it's kitchen...
                    user.setRole(User.Role.KITCHEN_STAFF); // Set as Kitchen.
                    break;
                default: // Anything else...
                    user.setRole(User.Role.WAITER); // Default to Waiter.
            }
        }

        // 4. Save to MySQL database using JPA.
        userRepository.save(user); // Persist the new staff member.

        // 5. Return success message.
        return ResponseEntity.ok(new MessageResponse("Staff member registered successfully!"));
    }
}
