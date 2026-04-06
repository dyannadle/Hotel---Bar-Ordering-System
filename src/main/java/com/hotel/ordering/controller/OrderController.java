package com.hotel.ordering.controller; // Package for our web controller classes.

import com.hotel.ordering.entity.Order; // Using our Order database model.
import com.hotel.ordering.payload.response.BillResponse; // Our final receipt DTO.
import com.hotel.ordering.service.OrderService; // Using our order business logic.
import lombok.RequiredArgsConstructor; // Lombok: Injects dependencies automatically!
import org.springframework.http.ResponseEntity; // Wrapper for HTTP responses.
import org.springframework.security.access.prepost.PreAuthorize; // Role-Based Access Control.
import org.springframework.web.bind.annotation.*; // Standard Spring Web annotations.

import java.util.List; // For returning lists of items.

/**
 * OrderController: Handles all web requests related to orders.
 * All URLs start with /api/orders.
 */
@RestController // Marks this as a REST API controller.
@RequestMapping("/api/orders") // Sets the base URL path to /api/orders.
@RequiredArgsConstructor // Automatically creates a constructor for our private final fields!
public class OrderController { // The main class for order endpoints.

    // The service that handles our ordering logic.
    private final OrderService orderService; 

    /**
     * getOrdersByStatus: [GET] /api/orders?status=PLACED
     * @PreAuthorize: Admin, Waiter, and Kitchen Staff can all see order lists.
     */
    @GetMapping // Maps HTTP GET requests to this method.
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER', 'KITCHEN_STAFF')") // SECURITY: Most staff can view order lists.
    public ResponseEntity<List<Order>> getOrdersByStatus(@RequestParam Order.OrderStatus status) { 
        return ResponseEntity.ok(orderService.getOrdersByStatus(status)); // Returns matching orders.
    }

    /**
     * getOrderById: [GET] /api/orders/{id}
     * Any authenticated staff member can see the details of a specific order.
     */
    @GetMapping("/{id}") // URL e.g.: /api/orders/101
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER', 'KITCHEN_STAFF')") // SECURITY: Any logged-in staff can view details.
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) { 
        return ResponseEntity.ok(orderService.getOrderById(id)); // Returns the order details.
    }

    /**
     * placeOrder: [POST] /api/orders
     * @PreAuthorize: Only WAITERS and ADMINs can place a new order.
     */
    @PostMapping // Maps HTTP POST requests to this method.
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')") // SECURITY: Kitchen staff cannot place new orders!
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) { // Accepts the order JSON.
        return ResponseEntity.ok(orderService.placeOrder(order)); // Saves and returns the new order.
    }

    /**
     * updateOrderStatus: [PATCH] /api/orders/{id}/status?status=SERVED
     * @PreAuthorize: Everyone can update status (Waiters for 'SERVED', Kitchen for 'PREPARING').
     */
    @PatchMapping("/{id}/status") // Maps HTTP PATCH requests for updating status.
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER', 'KITCHEN_STAFF')") // SECURITY: All staff can push the order lifecycle.
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id, // Order ID from URL.
            @RequestParam Order.OrderStatus status) { // New status from parameter.
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status)); // Updates and returns.
    }

    /**
     * checkoutOrder: [POST] /api/orders/{id}/checkout
     * @PreAuthorize: Only WAITERS and ADMINs can close an order and generate a bill.
     */
    @PostMapping("/{id}/checkout") // URL e.g.: /api/orders/101/checkout
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')") // SECURITY: Kitchen staff cannot checkout guests!
    public ResponseEntity<BillResponse> checkoutOrder(@PathVariable Long id) { // Takes ID from URL.
        // Calls the service to calculate GST, free the table, and generate the bill JSON.
        return ResponseEntity.ok(orderService.checkoutOrder(id)); 
    }
}
