package com.hotel.ordering.service;

import com.hotel.ordering.entity.RestaurantTable;
import com.hotel.ordering.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The TableService handles all business logic related to our restaurant tables.
 */
@Service
@RequiredArgsConstructor
public class TableService {

    private final RestaurantTableRepository tableRepository;

    /**
     * Get all tables from the database.
     */
    public List<RestaurantTable> getAllTables() {
        return tableRepository.findAll();
    }

    /**
     * Get only available tables.
     */
    public List<RestaurantTable> getAvailableTables() {
        return tableRepository.findByStatus(RestaurantTable.TableStatus.AVAILABLE);
    }

    /**
     * Find a table by its number.
     */
    public Optional<RestaurantTable> getTableByNumber(Integer tableNumber) {
        return tableRepository.findByTableNumber(tableNumber);
    }

    /**
     * Add a new table to the system.
     */
    @Transactional
    public RestaurantTable addTable(RestaurantTable table) {
        return tableRepository.save(table);
    }

    /**
     * Update the status of a table (e.g. set to OCCUPIED when a guest arrives).
     */
    @Transactional
    public RestaurantTable updateTableStatus(Long id, RestaurantTable.TableStatus status) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));
        table.setStatus(status);
        return tableRepository.save(table);
    }

    /**
     * Delete a table by its ID.
     */
    @Transactional
    public void deleteTable(Long id) {
        tableRepository.deleteById(id);
    }
}
