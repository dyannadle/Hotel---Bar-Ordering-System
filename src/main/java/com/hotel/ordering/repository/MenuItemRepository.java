package com.hotel.ordering.repository;

import com.hotel.ordering.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JpaRepository: A Spring Data magic interface. By extending it, we get logic for 
 * Saving, Updating, Finding, and Deleting records for free!
 */
@Repository // Marks this interface as a data-access component.
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    /**
     * findByCategory: Automatically generated SQL query by Spring Data.
     * It finds all dishes belonging to a specific category (e.g. BEVERAGE).
     */
    List<MenuItem> findByCategory(MenuItem.Category category);

    /**
     * findByAvailableTrue: Automatically generates a query to fetch all 
     * dishes where 'available' = true.
     */
    List<MenuItem> findByAvailableTrue();
}
