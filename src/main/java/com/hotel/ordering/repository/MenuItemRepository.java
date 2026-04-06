package com.hotel.ordering.repository;

import com.hotel.ordering.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The MenuItemRepository interface handles database interaction for the menu_items table.
 * By extending JpaRepository, we get methods like save(), findById(), delete(), and more for free!
 */
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    /**
     * Find all menu items by their category.
     * Spring Data JPA is smart enough to generate the query automatically based on the method name!
     */
    List<MenuItem> findByCategory(MenuItem.Category category);

    /**
     * Find all menu items that are currently available.
     */
    List<MenuItem> findByAvailableTrue();
}
