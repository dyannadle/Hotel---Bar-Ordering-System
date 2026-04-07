package com.hotel.ordering.service;

import com.hotel.ordering.entity.RestaurantTable;
import com.hotel.ordering.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * TableService: Handles the logic for our restaurant's physical tables.
 * @Service: Tells Spring that this is a service component.
 * @RequiredArgsConstructor: Injects our table repository automatically.
 */
@Service
@RequiredArgsConstructor
public class TableService {

    // Final field being injected by Lombok's @RequiredArgsConstructor.
    private final RestaurantTableRepository tableRepository;

    /**
     * getAllTables: Returns every table listed in our database.
     */
    public List<RestaurantTable> getAllTables() {
        return tableRepository.findAll();
    }

    /**
     * getAvailableTables: Filters the list to show only empty (AVAILABLE) tables.
     */
    public List<RestaurantTable> getAvailableTables() {
        return tableRepository.findByStatus(RestaurantTable.TableStatus.AVAILABLE);
    }

    /**
     * getTableByNumber: Searches for a table by its unique number (e.g. Table 5).
     */
    public Optional<RestaurantTable> getTableByNumber(Integer tableNumber) {
        return tableRepository.findByTableNumber(tableNumber);
    }

    /**
     * addTable: Adds a new physical table to our system.
     */
    @Transactional
    public RestaurantTable addTable(RestaurantTable table) {
        // Validate that the table number is unique to avoid 500 Data Integrity Errors
        if (tableRepository.findByTableNumber(table.getTableNumber()).isPresent()) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, 
                    "Table number " + table.getTableNumber() + " already exists!");
        }
        
        // Ensure a new record is created even if the client mistakenly provides an ID
        table.setId(null); 
        
        return tableRepository.save(table);
    }

    /**
     * updateTableStatus: Changes a table's status (e.g. from AVAILABLE to OCCUPIED).
     */
    @Transactional
    public RestaurantTable updateTableStatus(Long id, RestaurantTable.TableStatus status) {
        // Find the table by ID, or throw an error if it doesn't exist.
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));
        
        // Update the status and save it back to MySQL.
        table.setStatus(status);
        return tableRepository.save(table);
    }

    /**
     * deleteTable: Removes a table from the system by its ID.
     */
    @Transactional
    public void deleteTable(Long id) {
        tableRepository.deleteById(id);
    }
}
