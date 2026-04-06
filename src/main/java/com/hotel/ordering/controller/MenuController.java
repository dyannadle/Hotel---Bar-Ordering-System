package com.hotel.ordering.controller;

import com.hotel.ordering.entity.MenuItem;
import com.hotel.ordering.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The MenuController provides REST API endpoints for our menu.
 * @RestController: Marks this as a RESTful controller, so it returns JSON by default.
 * @RequestMapping: Defines the base URL for all endpoints in this class.
 */
@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuItemService menuItemService;

    /**
     * Get all menu items.
     * @GetMapping: Maps HTTP GET requests to this method.
     */
    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }

    /**
     * Get all available menu items.
     */
    @GetMapping("/available")
    public ResponseEntity<List<MenuItem>> getAvailableMenuItems() {
        return ResponseEntity.ok(menuItemService.getAvailableMenuItems());
    }

    /**
     * Get menu items by category.
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByCategory(@PathVariable MenuItem.Category category) {
        return ResponseEntity.ok(menuItemService.getMenuItemsByCategory(category));
    }

    /**
     * Add a new menu item.
     * @PostMapping: Maps HTTP POST requests to this method.
     */
    @PostMapping
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem item) {
        return ResponseEntity.ok(menuItemService.addMenuItem(item));
    }

    /**
     * Delete a menu item by ID.
     * @DeleteMapping: Maps HTTP DELETE requests to this method.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
}
