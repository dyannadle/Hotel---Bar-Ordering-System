package com.hotel.ordering.payload.request; // Package for incoming API request objects.

import jakarta.validation.constraints.*; // Import for validation constraints.
import lombok.Getter; // Lombok: Generates Getters automatically!
import lombok.Setter; // Lombok: Generates Setters automatically!

/**
 * SignupRequest: Use to create a new staff account (Admin/Waiter/Kitchen).
 * This object is used in the AuthController during manual registration.
 */
@Getter // Tells Lombok to generate gets for username, name, password, and role.
@Setter // Tells Lombok to generate sets for our registration data.
public class SignupRequest { // The request object class.

    @NotBlank // Validation: Username cannot be blank.
    @Size(min = 3, max = 20) // Validation: Username must be between 3 and 20 characters.
    private String username;

    @NotBlank // Validation: The staff member's real name cannot be blank.
    private String name;

    @NotBlank // Validation: The password cannot be blank.
    @Size(min = 6, max = 40) // Validation: Password must be at least 6 characters long for security.
    private String password;

    private String role; // The staff member's role (e.g. "waiter", "admin", "kitchen").
}
