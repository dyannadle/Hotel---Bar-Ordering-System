package com.hotel.ordering.controller; // Package for our web controller classes.

import com.hotel.ordering.entity.MenuItem; // Using our MenuItem database model.
import com.hotel.ordering.service.MenuItemService; // Using our menu business logic.
import lombok.RequiredArgsConstructor; // Lombok: Injects dependencies automatically!
import org.springframework.http.ResponseEntity; // Wrapper for HTTP responses (200 OK, etc).
import org.springframework.security.access.prepost.PreAuthorize; // Used for Role-Based Access Control.
import org.springframework.web.bind.annotation.*; // Standard Spring Web annotations.

import java.util.List; // For returning lists of items.

/**
 * MenuController: Defines the API endpoints for our hospitality menu.
 * @RestController: Tells Spring to convert our Java objects into JSON for the web.
 * @RequestMapping: Every endpoint in this class starts with /api/menu.
 */
@RestController // Marks this as a REST API controller.
@RequestMapping("/api/menu") // Sets the base URL path to /api/menu.
@RequiredArgsConstructor // Automatically creates a constructor for our private final fields!
public class MenuController { // The main class for menu endpoints.

    // The service that handles our menu database logic.
    private final MenuItemService menuItemService; 

    /**
     * getAllMenuItems: Called when someone visits [GET] /api/menu.
     * Returns the full list of food and drinks.
     */
    @GetMapping // Maps HTTP GET requests to this method.
    public ResponseEntity<List<MenuItem>> getAllMenuItems() { // Method returns a list inside a Response.
        return ResponseEntity.ok(menuItemService.getAllMenuItems()); // Sends the menu with a 200 OK status.
    }

    /**
     * getAvailableMenuItems: Called when someone visits [GET] /api/menu/available.
     * Returns only items that are currently in stock.
     */
    @GetMapping("/available") // URL: /api/menu/available
    public ResponseEntity<List<MenuItem>> getAvailableMenuItems() { // Fetching available food only.
        return ResponseEntity.ok(menuItemService.getAvailableMenuItems()); // Returns the list to the user.
    }

    /**
     * getMenuItemsByCategory: Returns items of a specific type (e.g. BEVERAGE).
     * @PathVariable: Extracts the category name directly from the URL.
     */
    @GetMapping("/category/{category}") // URL e.g.: /api/menu/category/APPETIZER
    public ResponseEntity<List<MenuItem>> getMenuItemsByCategory(@PathVariable MenuItem.Category category) { 
        return ResponseEntity.ok(menuItemService.getMenuItemsByCategory(category)); // Filters and returns.
    }

    /**
     * addMenuItem: Called when an Admin adds a new dish [POST] /api/menu.
     * @PreAuthorize: Only a staff member with 'ROLE_ADMIN' can call this!
     */
    @PostMapping // Maps HTTP POST requests to this method.
    @PreAuthorize("hasRole('ADMIN')") // SECURITY: Checks if the user is an Admin before running!
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem item) { // Accepts the dish in JSON format.
        return ResponseEntity.ok(menuItemService.addMenuItem(item)); // Saves and returns the new dish.
    }

    /**
     * deleteMenuItem: Removes a dish from the system [DELETE] /api/menu/{id}.
     * @PreAuthorize: Only a staff member with 'ROLE_ADMIN' can delete menu items.
     */
    @DeleteMapping("/{id}") // Maps HTTP DELETE requests for a specific ID.
    @PreAuthorize("hasRole('ADMIN')") // SECURITY: Stops waiters or kitchen from deleting dishes!
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) { // Takes the ID from the URL.
        menuItemService.deleteMenuItem(id); // Calls the service to remove it from MySQL.
        return ResponseEntity.noContent().build(); // Sends back a 204 No Content status.
    }
}
