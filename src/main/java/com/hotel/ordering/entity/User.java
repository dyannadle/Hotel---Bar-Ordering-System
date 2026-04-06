package com.hotel.ordering.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The User entity represents our staff members (Admin, Waiter, Kitchen).
 * @Entity: Marks this class as a database table. JPA will create a table named 'users'.
 */
@Entity
@Table(name = "users") // Explicitly names the table 'users' in MySQL.
@Data // Lombok: Generates Getters, Setters, Equals, HashCode, and toString automatically!
@NoArgsConstructor // Lombok: Generates a constructor with no arguments (needed by JPA).
@AllArgsConstructor // Lombok: Generates a constructor with all fields as arguments.
public class User {

    @Id // Marks this field as the Primary Key.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tells MySQL to auto-increment the ID (1, 2, 3...).
    private Long id;

    @Column(nullable = false, unique = true) // Column cannot be null and must be unique.
    private String username;

    @Column(nullable = false) // Staff must have a password.
    private String password;

    @Column(nullable = false) // Staff must have a display name.
    private String name;

    @Enumerated(EnumType.STRING) // Saves the Enum as a String (e.g., 'ADMIN') instead of a number.
    @Column(nullable = false)
    private Role role;

    // Define the possible roles for our staff.
    public enum Role {
        ADMIN, WAITER, KITCHEN_STAFF
    }
}
