package com.hotel.ordering.repository;

import com.hotel.ordering.entity.Order;
import com.hotel.ordering.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * OrderRepository: The data layer for orders. 
 * Spring Boot will automatically generate the SQL queries for these methods.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * findByStatus: Returns all orders with a specific status (e.g., all PREPARING orders).
     */
    List<Order> findByStatus(Order.OrderStatus status);

    /**
     * findByTable: Returns every order that has ever been placed at a specific table.
     */
    List<Order> findByTable(RestaurantTable table);

    /**
     * findByTableAndStatusNot: This is crucial! It finds "active" orders for a table 
     * by excluding 'COMPLETED' or 'CANCELLED' status.
     */
    List<Order> findByTableAndStatusNot(RestaurantTable table, Order.OrderStatus status);
}
