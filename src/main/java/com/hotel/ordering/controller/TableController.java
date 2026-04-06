package com.hotel.ordering.controller;

import com.hotel.ordering.entity.RestaurantTable;
import com.hotel.ordering.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TableController: Manages all table-related API endpoints.
 * All URLs in this class start with /api/tables.
 */
@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {

    // Injecting our TableService to handle the logic.
    private final TableService tableService;

    /**
     * getAllTables: Returns a list of all tables in the restaurant [GET] /api/tables.
     */
    @GetMapping
    public ResponseEntity<List<RestaurantTable>> getAllTables() {
        return ResponseEntity.ok(tableService.getAllTables());
    }

    /**
     * getAvailableTables: Returns only the free tables [GET] /api/tables/available.
     */
    @GetMapping("/available")
    public ResponseEntity<List<RestaurantTable>> getAvailableTables() {
        return ResponseEntity.ok(tableService.getAvailableTables());
    }

    /**
     * getTableByNumber: Finds a specific table using its number (e.g., Table 1).
     * Returns 404 NOT FOUND if the table doesn't exist.
     */
    @GetMapping("/number/{number}")
    public ResponseEntity<RestaurantTable> getTableByNumber(@PathVariable Integer number) {
        return tableService.getTableByNumber(number)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * addTable: Adds a new table to the database [POST] /api/tables.
     */
    @PostMapping
    public ResponseEntity<RestaurantTable> addTable(@RequestBody RestaurantTable table) {
        return ResponseEntity.ok(tableService.addTable(table));
    }

    /**
     * updateTableStatus: Updates whether a table is AVAILABLE or OCCUPIED.
     * @PatchMapping: Used for partial updates (we're only changing the status).
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<RestaurantTable> updateTableStatus(
            @PathVariable Long id, 
            @RequestParam RestaurantTable.TableStatus status) {
        return ResponseEntity.ok(tableService.updateTableStatus(id, status));
    }

    /**
     * deleteTable: Removes a table from the system [DELETE] /api/tables/{id}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }
}
