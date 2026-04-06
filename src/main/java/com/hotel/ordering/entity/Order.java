package com.hotel.ordering.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order: Represents a group of food/drinks ordered by a guest at a specific table.
 * @Entity: Tells JPA to create a table named 'orders'.
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id // The unique ID for this order.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many orders can be linked to ONE table.
    @ManyToOne 
    @JoinColumn(name = "table_id", nullable = false)
    private RestaurantTable table;

    // Many orders can be linked to ONE waiter (User).
    @ManyToOne
    @JoinColumn(name = "waiter_id", nullable = false)
    private User waiter;

    @Enumerated(EnumType.STRING) // Saves the status as words like 'PLACED' or 'SERVED'.
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PLACED;

    @CreationTimestamp // Automatically records the exact time the order was created.
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(precision = 10, scale = 2) // Precision: 10 digits total, 2 decimal places.
    private java.math.BigDecimal totalAmount; // The final bill amount (Subtotal + Tax).

    /**
     * @OneToMany: One Order contains MANY OrderItems (e.g., Pizza + Coke).
     * mappedBy: Tells JPA that the 'order' field in OrderItem "owns" this link.
     * CascadeType.ALL: If we save/delete an Order, automatically save/delete all its items!
     * orphanRemoval: If an item is removed from this list, it's deleted from the database.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    // The possible states of an order in our system.
    public enum OrderStatus {
        PLACED, PREPARING, SERVED, COMPLETED, CANCELLED
    }
}
