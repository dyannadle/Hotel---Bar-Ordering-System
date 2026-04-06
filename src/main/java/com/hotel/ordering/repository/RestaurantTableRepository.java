package com.hotel.ordering.repository;

import com.hotel.ordering.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The RestaurantTableRepository interface handles database interaction for the restaurant_tables table.
 */
@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    /**
     * Find a table by its unique table number.
     */
    Optional<RestaurantTable> findByTableNumber(Integer tableNumber);

    /**
     * Find all tables with a specific status (e.g. AVAILABLE).
     */
    List<RestaurantTable> findByStatus(RestaurantTable.TableStatus status);
}
