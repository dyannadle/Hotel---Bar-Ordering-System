package com.hotel.ordering.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The User entity represents our staff members (Admin, Waiter, Kitchen).
 * @Entity: Tells JPA that this class should be mapped to a database table.
 * @Table: Specifies the name of the table in the database.
 * @Data: A Lombok annotation that automatically generates Getters, Setters, toString, etc.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // We'll define the Role enum next!
    public enum Role {
        ADMIN, WAITER, KITCHEN_STAFF
    }
}
