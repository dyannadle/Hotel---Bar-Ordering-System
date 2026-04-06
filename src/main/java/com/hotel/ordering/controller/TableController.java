package com.hotel.ordering.controller;

import com.hotel.ordering.entity.RestaurantTable;
import com.hotel.ordering.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The TableController provides REST API endpoints for our restaurant tables.
 */
@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    /**
     * Get all tables.
     */
    @GetMapping
    public ResponseEntity<List<RestaurantTable>> getAllTables() {
        return ResponseEntity.ok(tableService.getAllTables());
    }

    /**
     * Get only available tables.
     */
    @GetMapping("/available")
    public ResponseEntity<List<RestaurantTable>> getAvailableTables() {
        return ResponseEntity.ok(tableService.getAvailableTables());
    }

    /**
     * Get a table by number.
     */
    @GetMapping("/number/{number}")
    public ResponseEntity<RestaurantTable> getTableByNumber(@PathVariable Integer number) {
        return tableService.getTableByNumber(number)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Add a new table.
     */
    @PostMapping
    public ResponseEntity<RestaurantTable> addTable(@RequestBody RestaurantTable table) {
        return ResponseEntity.ok(tableService.addTable(table));
    }

    /**
     * Update table status (e.g. set to OCCUPIED).
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<RestaurantTable> updateTableStatus(
            @PathVariable Long id, 
            @RequestParam RestaurantTable.TableStatus status) {
        return ResponseEntity.ok(tableService.updateTableStatus(id, status));
    }

    /**
     * Delete a table by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }
}
