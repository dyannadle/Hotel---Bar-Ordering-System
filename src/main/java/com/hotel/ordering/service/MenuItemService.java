package com.hotel.ordering.service;

import com.hotel.ordering.entity.MenuItem;
import com.hotel.ordering.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * MenuItemService: Where the "business logic" for our menu lives.
 * @Service: Marks this as a service class that Spring can manage.
 * @RequiredArgsConstructor: Lombok magic that injects our repository via a constructor.
 */
@Service
@RequiredArgsConstructor
public class MenuItemService {

    // Final field: injected automatically through the constructor.
    private final MenuItemRepository menuItemRepository;

    /**
     * getAllMenuItems: Fetches every single item from the database.
     */
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    /**
     * getAvailableMenuItems: Fetches only the items that are currently in stock.
     */
    public List<MenuItem> getAvailableMenuItems() {
        return menuItemRepository.findByAvailableTrue();
    }

    /**
     * getMenuItemsByCategory: Filters the menu by a specific category (e.g. STARTER).
     */
    public List<MenuItem> getMenuItemsByCategory(MenuItem.Category category) {
        return menuItemRepository.findByCategory(category);
    }

    /**
     * addMenuItem: Saves a new dish to our hotel's menu.
     * @Transactional: Ensures the database operation is "all or nothing".
     */
    @Transactional
    public MenuItem addMenuItem(MenuItem item) {
        return menuItemRepository.save(item);
    }

    /**
     * deleteMenuItem: Removes a dish from the menu by its ID.
     */
    @Transactional
    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }
}
