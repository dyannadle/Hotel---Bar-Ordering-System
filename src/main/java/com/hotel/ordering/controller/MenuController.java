package com.hotel.ordering.controller;

import com.hotel.ordering.entity.MenuItem;
import com.hotel.ordering.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MenuController: Defines the API endpoints for our hospitality menu.
 * @RestController: Tells Spring to convert our Java objects into JSON for the web.
 * @RequestMapping: Every endpoint in this class starts with /api/menu.
 */
@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuItemService menuItemService;

    /**
     * getAllMenuItems: Called when someone visits [GET] /api/menu.
     * Returns the full list of food and drinks.
     */
    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }

    /**
     * getAvailableMenuItems: Called when someone visits [GET] /api/menu/available.
     * Returns only items that are currently in stock.
     */
    @GetMapping("/available")
    public ResponseEntity<List<MenuItem>> getAvailableMenuItems() {
        return ResponseEntity.ok(menuItemService.getAvailableMenuItems());
    }

    /**
     * getMenuItemsByCategory: Returns items of a specific type (e.g. BEVERAGE).
     * @PathVariable: Extracts the category name directly from the URL.
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByCategory(@PathVariable MenuItem.Category category) {
        return ResponseEntity.ok(menuItemService.getMenuItemsByCategory(category));
    }

    /**
     * addMenuItem: Called when an Admin adds a new dish [POST] /api/menu.
     * @RequestBody: Converts the incoming JSON data into a Java MenuItem object.
     */
    @PostMapping
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem item) {
        return ResponseEntity.ok(menuItemService.addMenuItem(item));
    }

    /**
     * deleteMenuItem: Removes a dish from the system [DELETE] /api/menu/{id}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
}
