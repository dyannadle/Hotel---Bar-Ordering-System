package com.hotel.ordering.service; // Package for our business logic services.

import com.hotel.ordering.entity.Order; // Using our Order database model.
import com.hotel.ordering.entity.OrderItem; // Using our Order item model.
import com.hotel.ordering.entity.RestaurantTable; // Using our table database model.
import com.hotel.ordering.payload.response.BillResponse; // Our final receipt DTO.
import com.hotel.ordering.repository.OrderRepository; // Accessing the orders table in MySQL.
import com.hotel.ordering.repository.RestaurantTableRepository; // Accessing the tables table.
import lombok.RequiredArgsConstructor; // Lombok: Injects dependencies (Repos) automatically!
import org.springframework.beans.factory.annotation.Value; // Reads data from application.properties.
import org.springframework.stereotype.Service; // Marks this as a Spring Service bean.
import org.springframework.transaction.annotation.Transactional; // Ensures all DB actions are safe.

import java.math.BigDecimal; // For high-precision money (no floating point errors!).
import java.math.RoundingMode; // To round money to 2 decimal places properly.
import java.util.List; // For returning lists of data.
import java.util.stream.Collectors; // For data transformation streams.

/**
 * OrderService: The project's brain! It manages subtotals, tax, and order flow.
 */
@Service // Tells Spring to manage the business logic in this class.
@RequiredArgsConstructor // Automatically creates a constructor for our private final fields!
public class OrderService { // The service class for all hotel orders.

    private final OrderRepository orderRepository; // Injected repository for Orders.
    private final RestaurantTableRepository tableRepository; // Injected repository for Tables.

    @Value("${app.tax.gst-rate}") // Injects our 18.0 percentage from properties.
    private Double gstRate; // Store the tax rate as a number.

    /**
     * placeOrder: Creates a new order and marks the table as OCCUPIED.
     * @Transactional: Makes sure the order and table update both succeed or both fail.
     */
    @Transactional // Database Transaction: Prevents data corruption.
    public Order placeOrder(Order order) { // Takes a new order object.
        
        // 1. Find the table in the database using the ID provided.
        RestaurantTable table = tableRepository.findById(order.getTable().getId())
                .orElseThrow(() -> new RuntimeException("Table not found!")); // Error if table is missing.
        
        // 2. Mark the table as OCCUPIED so the staff knows it's busy.
        table.setStatus(RestaurantTable.TableStatus.OCCUPIED); // Update status in Java.
        tableRepository.save(table); // Update status in MySQL.

        // 3. Loop through each item (Dish) added to this order.
        for (OrderItem item : order.getItems()) { 
            // We must link each item to this parent order (Foreign Key).
            item.setOrder(order); 
            
            // Pro-tip: Set the price of the item from the menu's CURRENT price.
            if (item.getPricePerUnit() == null) { // If price isn't set yet...
                item.setPricePerUnit(item.getMenuItem().getPrice()); // Get price from Menu database.
            }
        }

        // 4. Calculate initial subtotal (sum of price * qty).
        BigDecimal subtotal = order.getItems().stream() // Start a loop...
                .map(OrderItem::getSubtotal) // Get Price * Qty for each row.
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum them all up.
        
        order.setTotalAmount(subtotal); // Set the current total.
        order.setStatus(Order.OrderStatus.PLACED); // Set the starting status.

        // 5. Save the order. All items are saved too because of 'CascadeType.ALL'!
        return orderRepository.save(order); // Return the final saved order.
    }

    /**
     * calculateSubtotal: Returns the total bill amount (excluding tax).
     */
    public BigDecimal calculateSubtotal(Long orderId) { // Takes the order's primary key.
        Order order = getOrderById(orderId); // Fetch the order data.
        return order.getItems().stream() // Loop through items.
                .map(OrderItem::getSubtotal) // Get Price * Qty.
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Return the total sum.
    }

    /**
     * updateOrderStatus: Moves an order from PLACED to KITCHEN to SERVED.
     */
    @Transactional // Ensures the status update is saved correctly.
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) { // Takes ID and new status.
        Order order = getOrderById(orderId); // Fetch from DB.
        order.setStatus(status); // Set the new stage.

        // If 'CANCELLED', make the table available again!
        if (status == Order.OrderStatus.CANCELLED) {
            RestaurantTable table = order.getTable(); // Get table.
            table.setStatus(RestaurantTable.TableStatus.AVAILABLE); // Free it.
            tableRepository.save(table); // Save table update.
        }

        return orderRepository.save(order); // Save order update and return.
    }

    /**
     * checkoutOrder: The final billing logic! Tax + Finishing + Table Cleanup.
     */
    @Transactional // Critical: Must update both order and table status.
    public BillResponse checkoutOrder(Long id) { // Takes the order ID.
        
        // 1. Fetch the Order from the database.
        Order order = getOrderById(id); 

        // 2. Fetch current subtotal.
        BigDecimal subtotal = calculateSubtotal(id); 

        // 3. GST Calculation (Subtotal * 18 / 100).
        BigDecimal gstAmount = subtotal.multiply(new BigDecimal(gstRate)) // Multiply by rate.
                .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP); // Divide by 100 and round to 2 cents.

        // 4. Grand Total = Subtotal + Tax.
        BigDecimal grandTotal = subtotal.add(gstAmount); // The final guest price.

        // 5. Store the final grand total and mark as COMPLETED.
        order.setTotalAmount(grandTotal); 
        order.setStatus(Order.OrderStatus.COMPLETED); 
        orderRepository.save(order); // Save finished order.

        // 6. MAKE THE TABLE FREE (Available) for new customers!
        RestaurantTable table = order.getTable(); 
        table.setStatus(RestaurantTable.TableStatus.AVAILABLE); 
        tableRepository.save(table); // Save table status to MySQL.

        // 7. BUILD THE RECEIPT (BillResponse) to send back as JSON.
        return BillResponse.builder() // Using Lombok Builder pattern...
                .orderId(order.getId()) // ID of the order.
                .tableNumber(table.getTableNumber()) // Table number (matched to entity!).
                .subtotal(subtotal) // Pre-tax money.
                .gstAmount(gstAmount) // Tax money.
                .grandTotal(grandTotal) // Final amount guest pays.
                .items(order.getItems().stream() // Map each order item to a bill row.
                    .map(item -> BillResponse.OrderItemResponse.builder()
                        .itemName(item.getMenuItem().getName()) // Dish name from menu.
                        .quantity(item.getQuantity()) // How many ordered.
                        .price(item.getPricePerUnit()) // Price at order time.
                        .subtotal(item.getSubtotal()) // Line total (Qty * Price).
                        .build()) // Finish item row.
                    .collect(Collectors.toList())) // Collect to a list.
                .build(); // Return the complete bill!
    }

    /**
     * getOrdersByStatus: Fetches all orders with a specific status (e.g. for the kitchen screen).
     */
    public List<Order> getOrdersByStatus(Order.OrderStatus status) { // Takes status enum.
        return orderRepository.findByStatus(status); // Returns results from MySQL.
    }

    /**
     * getOrderById: Standard helper to find an order by its ID.
     */
    public Order getOrderById(Long id) { // Takes order primary key.
        return orderRepository.findById(id) // Simple find.
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id)); // Error if missing.
    }
}
