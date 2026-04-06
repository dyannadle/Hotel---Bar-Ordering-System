package com.hotel.ordering.controller; // Package for our web controller classes.

import com.hotel.ordering.entity.RestaurantTable; // Using our Table database model.
import com.hotel.ordering.service.TableService; // Using our table business logic.
import lombok.RequiredArgsConstructor; // Lombok: Injects dependencies automatically!
import org.springframework.http.ResponseEntity; // Wrapper for HTTP responses (200 OK, etc).
import org.springframework.security.access.prepost.PreAuthorize; // Used for Role-Based Access Control.
import org.springframework.web.bind.annotation.*; // Standard Spring Web annotations.

import java.util.List; // For returning lists of items.

/**
 * TableController: Manages all table-related API endpoints.
 * All URLs in this class start with /api/tables.
 */
@RestController // Marks this as a REST API controller.
@RequestMapping("/api/tables") // Sets the base URL path to /api/tables.
@RequiredArgsConstructor // Automatically creates a constructor for our private final fields!
public class TableController { // The main class for table management.

    // The service that handles our table database logic.
    private final TableService tableService; 

    /**
     * getAllTables: Returns a list of all tables in the restaurant [GET] /api/tables.
     */
    @GetMapping // Maps HTTP GET requests to this method.
    public ResponseEntity<List<RestaurantTable>> getAllTables() { // Returns a list of tables.
        return ResponseEntity.ok(tableService.getAllTables()); // Sends with 200 OK status.
    }

    /**
     * getAvailableTables: Returns only the free tables [GET] /api/tables/available.
     */
    @GetMapping("/available") // URL: /api/tables/available
    public ResponseEntity<List<RestaurantTable>> getAvailableTables() { // Fetches empty tables.
        return ResponseEntity.ok(tableService.getAvailableTables()); // Returns the list.
    }

    /**
     * getTableByNumber: Finds a specific table using its number (e.g., Table 1).
     * Returns 404 NOT FOUND if the table doesn't exist.
     */
    @GetMapping("/number/{number}") // URL e.g.: /api/tables/number/5
    public ResponseEntity<RestaurantTable> getTableByNumber(@PathVariable Integer number) { 
        return tableService.getTableByNumber(number) // Search for the table number.
                .map(ResponseEntity::ok) // If found, return 200 OK.
                .orElse(ResponseEntity.notFound().build()); // If missing, return 404 Not Found.
    }

    /**
     * addTable: Adds a new table to the database [POST] /api/tables.
     * @PreAuthorize: Only an ADMIN can add new tables to the hotel/bar.
     */
    @PostMapping // Maps HTTP POST requests to this method.
    @PreAuthorize("hasRole('ADMIN')") // SECURITY: Checks if user is Admin before adding a new table!
    public ResponseEntity<RestaurantTable> addTable(@RequestBody RestaurantTable table) { // Accepts table JSON.
        return ResponseEntity.ok(tableService.addTable(table)); // Saves and returns the new table.
    }

    /**
     * updateTableStatus: Updates whether a table is AVAILABLE or OCCUPIED.
     * @PreAuthorize: Both WAITERs and ADMINs can update a table's status.
     */
    @PatchMapping("/{id}/status") // Maps HTTP PATCH requests for partial updates.
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')") // SECURITY: Allow both Waiters and Admins to manage occupancy.
    public ResponseEntity<RestaurantTable> updateTableStatus(
            @PathVariable Long id, // Table ID from URL.
            @RequestParam RestaurantTable.TableStatus status) { // New status from parameter.
        return ResponseEntity.ok(tableService.updateTableStatus(id, status)); // Updates and returns.
    }

    /**
     * deleteTable: Removes a table from the system [DELETE] /api/tables/{id}.
     * @PreAuthorize: Only an ADMIN can permanently delete a table.
     */
    @DeleteMapping("/{id}") // Maps HTTP DELETE requests for a specific ID.
    @PreAuthorize("hasRole('ADMIN')") // SECURITY: Stops waiters or kitchen from deleting furniture!
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) { // Takes the ID from the URL.
        tableService.deleteTable(id); // Removes from MySQL.
        return ResponseEntity.noContent().build(); // Sends 204 No Content status.
    }
}
