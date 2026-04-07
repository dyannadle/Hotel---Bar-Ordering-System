package com.hotel.ordering.config; // Package for our startup configurations.

import com.hotel.ordering.entity.MenuItem; // Using our MenuItem database model.
import com.hotel.ordering.entity.RestaurantTable; // Using our RestaurantTable database model.
import com.hotel.ordering.repository.MenuItemRepository; // To save menu items to MySQL.
import com.hotel.ordering.repository.RestaurantTableRepository; // To save tables to MySQL.
import lombok.RequiredArgsConstructor; // Lombok: Injects dependencies automatically!
import org.springframework.boot.CommandLineRunner; // A special Spring interface that runs code ON STARTUP.
import org.springframework.stereotype.Component; // Marks this class as a Spring Bean to be managed.

import java.math.BigDecimal; // For precise currency handling.

/**
 * DataInitializer: The "Hotel Setup Crew".
 * This class runs once every time the application starts and ensures 
 * we have a basic menu and some tables to work with.
 */
@Component // Tells Spring Boot to find and run this class.
@RequiredArgsConstructor // Automatically creates a constructor for our repositories.
public class DataInitializer implements CommandLineRunner { // Implements the startup interface.

    private final RestaurantTableRepository tableRepository; // Access to tables table.
    private final MenuItemRepository menuRepository; // Access to menu items table.

    /**
     * run: This method executes automatically after the application context is loaded.
     */
    @Override // Overriding the run method from CommandLineRunner.
    public void run(String... args) throws Exception {

        // --- 1. SETUP TABLES (Expansion to 15 Tables) ---
        long currentTableCount = tableRepository.count(); // Check how many we have.
        if (currentTableCount < 15) { // If there are fewer than 15...
            System.out.println("🏨 Expanding Restaurant Tables to 15..."); // Print status.
            for (int i = (int) currentTableCount + 1; i <= 15; i++) { // Add only the missing ones!
                RestaurantTable table = new RestaurantTable(); // New table instance.
                table.setTableNumber(i); // Assign number (e.g. 11, 12, 13...).
                table.setStatus(RestaurantTable.TableStatus.AVAILABLE); // All start as Available.
                tableRepository.save(table); // Save to the database.
            }
        }

        // --- 2. SETUP MENU (If the kitchen is empty) ---
        if (menuRepository.count() == 0) { // Check if we already have a menu.
            System.out.println("🍕 Initializing Starter Menu..."); // Print to console.

            // ADDING FOOD ITEMS (MAIN_COURSE)
            saveMenuItem("Margherita Pizza", new BigDecimal("450.00"), MenuItem.Category.MAIN_COURSE, true);
            saveMenuItem("Club Sandwich", new BigDecimal("350.00"), MenuItem.Category.STARTER, true);
            saveMenuItem("Chicken Pasta", new BigDecimal("550.00"), MenuItem.Category.MAIN_COURSE, true);

            // ADDING DRINKS (BEVERAGE)
            saveMenuItem("Kingfisher Strong", new BigDecimal("280.00"), MenuItem.Category.BEVERAGE, true);
            saveMenuItem("Old Monk Rum", new BigDecimal("150.00"), MenuItem.Category.BEVERAGE, true);
            saveMenuItem("Fresh Lime Soda", new BigDecimal("120.00"), MenuItem.Category.BEVERAGE, true);

            // ADDING DESSERTS (DESSERT)
            saveMenuItem("Gulab Jamun", new BigDecimal("180.00"), MenuItem.Category.DESSERT, true);
            saveMenuItem("Vanilla Ice Cream", new BigDecimal("150.00"), MenuItem.Category.DESSERT, true);
        }
    }

    /**
     * saveMenuItem: Helper method to keep our startup code clean and readable.
     */
    private void saveMenuItem(String name, BigDecimal price, MenuItem.Category category, boolean available) {
        MenuItem item = new MenuItem(); // Create new item.
        item.setName(name); // Set dish name.
        item.setPrice(price); // Set price.
        item.setCategory(category); // Set category (Enum).
        item.setAvailable(available); // Set availability.
        menuRepository.save(item); // Save to database.
    }
}
