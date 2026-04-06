package com.hotel.ordering.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * MenuItem: Represents food and drinks on our menu.
 * @Entity: Maps this class to a database table ('menu_items').
 */
@Entity
@Table(name = "menu_items")
@Data // Lombok: Generates Getters, Setters, etc. automatically!
@NoArgsConstructor // Required by JPA to create the object from database data.
@AllArgsConstructor // Generates a constructor with all fields.
public class MenuItem {

    @Id // The unique ID (Primary Key) for every dish.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment (1, 2, 3...).
    private Long id;

    @Column(nullable = false) // The name of the dish (cannot be empty).
    private String name;

    @Column(nullable = false) // The price of the dish.
    private BigDecimal price; // BigDecimal is used for exact money calculations!

    @Enumerated(EnumType.STRING) // Saves the category as a string (e.g. 'BEVERAGE').
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false) // Boolean to show if item is currently available.
    private Boolean available = true; // Default is 'true'.

    // Our menu categories
    public enum Category {
        APPETIZER, MAIN_COURSE, DESSERT, BEVERAGE, STARTER
    }
}
