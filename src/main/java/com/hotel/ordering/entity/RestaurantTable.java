package com.hotel.ordering.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RestaurantTable: Represents the physical tables in our hospitality business.
 * @Entity: Maps this class to the database table 'restaurant_tables'.
 */
@Entity
@Table(name = "restaurant_tables")
@Data // Lombok: No more manual Getters and Setters!
@NoArgsConstructor // JPA requirement
@AllArgsConstructor // For easy table creation
public class RestaurantTable {

    @Id // The ID column (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing column
    private Long id;

    @Column(name = "table_number", nullable = false, unique = true) // Column name and requirement
    private Integer tableNumber;

    @Enumerated(EnumType.STRING) // Saves status as 'AVAILABLE' in database
    @Column(nullable = false)
    private TableStatus status = TableStatus.AVAILABLE;

    // The two possible states for a restaurant table.
    public enum TableStatus {
        AVAILABLE, OCCUPIED
    }
}
