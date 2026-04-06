package com.hotel.ordering.service;

import com.hotel.ordering.entity.Order;
import com.hotel.ordering.entity.OrderItem;
import com.hotel.ordering.entity.RestaurantTable;
import com.hotel.ordering.repository.OrderRepository;
import com.hotel.ordering.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * OrderService: This is where we write the "rules" of our ordering system.
 * It coordinates between the database tables (Repositories).
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantTableRepository tableRepository;

    /**
     * placeOrder: Takes an order and saves it. 
     * @Transactional: Ensures that either the entire order is saved, or none of it!
     */
    @Transactional
    public Order placeOrder(Order order) {
        // 1. First, find the table in the database.
        RestaurantTable table = tableRepository.findById(order.getTable().getId())
                .orElseThrow(() -> new RuntimeException("Table not found!"));
        
        // 2. Mark the table as OCCUPIED so other waiters know it's busy.
        table.setStatus(RestaurantTable.TableStatus.OCCUPIED);
        tableRepository.save(table);

        // 3. We loop through each item added to the order.
        for (OrderItem item : order.getItems()) {
            // We must link each item to this parent order.
            item.setOrder(order);
            
            // Pro-tip: Set the price of the item from the menu's CURRENT price.
            if (item.getPricePerUnit() == null) {
                item.setPricePerUnit(item.getMenuItem().getPrice());
            }
        }

        // 4. Save the order. Because of 'cascade', all the OrderItems are saved too!
        return orderRepository.save(order);
    }

    /**
     * calculateSubtotal: Returns the total bill amount (without tax).
     */
    public BigDecimal calculateSubtotal(Long orderId) {
        // Find the order details from the database.
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));
        
        // Sum up every item (Quantity * Price).
        return order.getItems().stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * updateOrderStatus: Moves an order from PLACED -> PREPARING -> COMPLETED.
     */
    @Transactional
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        // Find the existing order.
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));
        
        // Change the status (e.g., from 'PLACED' to 'SERVED').
        order.setStatus(status);

        // If the order is COMPLETED or CANCELLED, we free up the table!
        if (status == Order.OrderStatus.COMPLETED || status == Order.OrderStatus.CANCELLED) {
            RestaurantTable table = order.getTable();
            table.setStatus(RestaurantTable.TableStatus.AVAILABLE);
            tableRepository.save(table);
        }

        // Save the updated order.
        return orderRepository.save(order);
    }

    // Fetches all orders with a specific status for the kitchen staff.
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    // Fetches full details for a specific order by its ID.
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found!"));
    }
}
