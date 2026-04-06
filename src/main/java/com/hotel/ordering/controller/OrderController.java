package com.hotel.ordering.controller;

import com.hotel.ordering.entity.Order;
import com.hotel.ordering.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OrderController: Handles all web requests related to orders.
 * All URLs start with /api/orders.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * getOrdersByStatus: [GET] /api/orders?status=PLACED
     * Fetches all orders with a specific status for the kitchen screen.
     */
    @GetMapping
    public ResponseEntity<List<Order>> getOrdersByStatus(@RequestParam Order.OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    /**
     * getOrderById: [GET] /api/orders/{id}
     * Returns the full details (including items) of one order.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    /**
     * placeOrder: [POST] /api/orders
     * Takes an order from a waiter and saves it.
     */
    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.placeOrder(order));
    }

    /**
     * updateOrderStatus: [PATCH] /api/orders/{id}/status?status=SERVED
     * Allows the kitchen or waiter to update the progress of an order.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id, 
            @RequestParam Order.OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }
}
