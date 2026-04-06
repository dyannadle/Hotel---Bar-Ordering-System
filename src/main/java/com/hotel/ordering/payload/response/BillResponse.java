package com.hotel.ordering.payload.response; // Package for outgoing API response objects.

import lombok.Builder; // Lombok: Uses the Builder pattern to create objects easily.
import lombok.Getter; // Lombok: Generates Getters automatically!
import java.math.BigDecimal; // For financial precision (money).
import java.util.List; // For list-based storage of order items.

/**
 * BillResponse: The "Receipt" of our restaurant system.
 * This class provides a detailed breakdown of the meal, tax, and final amount.
 */
@Getter // Tells Lombok to generate gets for each field.
@Builder // Tells Lombok to enable the 'BillResponse.builder()' pattern.
public class BillResponse { // The response object class for checking out.

    private Long orderId; // The database ID of the finished order.

    private Integer tableNumber; // The table where the guests ate.

    private List<OrderItemResponse> items; // List of foods and drinks they ordered.

    private BigDecimal subtotal; // Total cost BEFORE tax.

    private BigDecimal gstAmount; // The 18.0% tax calculated from the subtotal.

    private BigDecimal grandTotal; // The final price the guest pays (Subtotal + Tax).

    /**
     * OrderItemResponse: A small helper class to show item names and prices on the bill.
     */
    @Getter // Tells Lombok to generate gets.
    @Builder // Enables the builder pattern for bill items.
    public static class OrderItemResponse { // Inner class for bill rows.
        private String itemName; // e.g. "Chicken Tikka"
        private Integer quantity; // e.g. 2
        private BigDecimal price; // The price at the time of order.
        private BigDecimal subtotal; // price * quantity.
    }
}
