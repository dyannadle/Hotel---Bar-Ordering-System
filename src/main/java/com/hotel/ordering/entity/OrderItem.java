package com.hotel.ordering.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * OrderItem: Represents a single item in a guest's order (e.g. 2 Pizzas).
 */
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id // The unique ID for this item.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many order items point to ONE main order.
    @ManyToOne 
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Many order items can point to ONE menu item (e.g., Pizza).
    @ManyToOne
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @Column(nullable = false) // How many of this item were ordered?
    private Integer quantity;

    /**
     * @Column pricePerUnit: Always store the price at the time of order!
     * If the menu price changes later, the old bills will stay the same.
     */
    @Column(nullable = false)
    private BigDecimal pricePerUnit;

    /**
     * getSubtotal: A helper method to calculate (Quantity * Price) for this item.
     */
    public BigDecimal getSubtotal() {
        return pricePerUnit.multiply(BigDecimal.valueOf(quantity));
    }
}
