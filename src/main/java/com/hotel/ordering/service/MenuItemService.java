package com.hotel.ordering.service;

import com.hotel.ordering.entity.MenuItem;
import com.hotel.ordering.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The MenuItemService handles all business logic related to our menu.
 * @Service: Tells Spring that this is a service component.
 * @RequiredArgsConstructor: A Lombok annotation that creates a constructor for our repository automatically!
 */
@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    /**
     * Get all menu items from the database.
     */
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    /**
     * Get only available menu items.
     */
    public List<MenuItem> getAvailableMenuItems() {
        return menuItemRepository.findByAvailableTrue();
    }

    /**
     * Get items by their category (e.g. STARTERS).
     */
    public List<MenuItem> getMenuItemsByCategory(MenuItem.Category category) {
        return menuItemRepository.findByCategory(category);
    }

    /**
     * Add a new menu item.
     * @Transactional: Ensures that either the entire operation succeeds or it fails and rolls back.
     */
    @Transactional
    public MenuItem addMenuItem(MenuItem item) {
        return menuItemRepository.save(item);
    }

    /**
     * Delete a menu item by its ID.
     */
    @Transactional
    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }
}
