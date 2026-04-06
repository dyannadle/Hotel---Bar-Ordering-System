package com.hotel.ordering.payload.request; // Package for incoming API request objects.

import jakarta.validation.constraints.NotBlank; // Constraint to ensure a field isn't empty.
import lombok.Getter; // Lombok: Generates Getters automatically!
import lombok.Setter; // Lombok: Generates Setters automatically!

/**
 * LoginRequest: The data we expect when a staff member tries to sign in.
 * This object is used to capture the username/password from the login screen.
 */
@Getter // Tells Lombok to generate getUsername() and getPassword().
@Setter // Tells Lombok to generate setUsername() and setPassword().
public class LoginRequest { // The request object class.

    @NotBlank // Validation: Ensures the username field is not empty or just whitespace.
    private String username;

    @NotBlank // Validation: Ensures the password field is not empty before authentication.
    private String password;
}
