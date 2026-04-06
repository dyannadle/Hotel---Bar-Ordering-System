package com.hotel.ordering.repository;

import com.hotel.ordering.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * RestaurantTableRepository: Interface for managing physical tables.
 */
@Repository // Marks this as a repository component.
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    /**
     * findByTableNumber: Custom query to find a specific table by its unique number.
     * Returns an Optional to avoid NullPointerException!
     */
    Optional<RestaurantTable> findByTableNumber(Integer tableNumber);

    /**
     * findByStatus: Find all available or all occupied tables.
     */
    List<RestaurantTable> findByStatus(RestaurantTable.TableStatus status);
}
