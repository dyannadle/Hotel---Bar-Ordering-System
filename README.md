# 🏨 Hotel & Bar Ordering System

Welcome to the **Hotel & Bar Ordering System**, a robust, fully-featured backend engine designed to manage restaurant tables, process orders, track customer details, and securely handle billing with automated tax calculations. 

Built with **Spring Boot 3**, **Spring Security (JWT)**, and **MySQL**, this system is designed for high-performance hospitality environments.

---

## ✨ Key Features

- **🏢 Table Management**: Auto-initializes 15 tables on startup. Tracks real-time occupancy (`AVAILABLE` vs `OCCUPIED`).
- **🔐 Role-Based Access Control**: Secure endpoints using JWT. Distinct roles for `ADMIN`, `WAITER`, and `KITCHEN_STAFF`.
- **🍽️ Order Processing**: Seamlessly link customers, tables, waiters, and menu items to track orders through their lifecycle (`PLACED` -> `PREPARING` -> `SERVED` -> `COMPLETED`).
- **📱 Customer Tracking**: Attach customer phone numbers directly to table orders for modern CRM and billing requirements.
- **🧾 Automated Billing**: Instantly calculates subtotals and auto-applies configured GST (18%) to generate a final `BillResponse` receipt.
- **👁️ Live Table Visibility**: Waiters can instantly query a specific table to see exactly what items are currently on it and what the active running bill is.
- **📚 OpenAPI / Swagger UI**: Fully documented, interactive API dashboard generated out-of-the-box.

---

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.2+** 
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Validation
- **MySQL Database**
- **JWT (JSON Web Tokens)** for secure, stateless authentication.
- **Springdoc OpenAPI (Swagger)** for API exploration.
- **Lombok** for clean, boilerplate-free code.

---

## 🚀 Getting Started

### Prerequisites
Make sure you have the following installed:
- Java Development Kit (JDK) 17 or higher
- Maven
- MySQL Server (running on default port `3306`)

### 1. Database Configuration
Open `src/main/resources/application.properties` and verify your MySQL credentials. By default, the system looks for:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hotel_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_password_here
```
*(No need to manually create the database; the system auto-generates `hotel_db` and all required tables if they don't exist).*

### 2. Running the Application
Open your terminal in the project root directory and run:
```bash
mvn spring-boot:run
```

### 3. Data Initialization
On its very first startup, the application's `DataInitializer` will automatically seed the database with:
- **15 Restaurant Tables**
- **Default Menu Items** (e.g., Margherita Pizza, Mojito)
- **3 Default Users** for testing:
  - `admin` / `adminpassword`
  - `waiter1` / `waiterpassword`
  - `kitchen1` / `kitchenpassword`

---

## 📖 API Documentation & Testing

Once the server is running on `localhost:8080`, navigate to your browser and open the interactive Swagger Dashboard:

👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

### How to Authenticate in Swagger
1. Scroll to the **`auth-controller`** and use `POST /api/auth/signin` with the admin credentials:
   ```json
   {
     "username": "admin",
     "password": "adminpassword"
   }
   ```
2. Copy the resulting `token` from the response body.
3. Scroll to the absolute top of the page and click the green **`[Authorize]`** button.
4. Type `Bearer ` (with a space) followed by your pasted token in the value box.
5. Click **Close**. You are now authorized to use all secured endpoints!

---

## 💡 Quick Examples

### Placing an Order for a Table
`POST /api/orders`
```json
{
  "customerPhone": "9876543210",
  "table": { "id": 5 },
  "waiter": { "id": 2 },
  "items": [
    { "menuItem": { "id": 1 }, "quantity": 2 }
  ]
}
```

### Checking What's on a Table
`GET /api/orders/active/table/5`
*Instantly returns the active order details, customer phone number, and a running total with tax included!*

### Generating the Final Bill & Freeing the Table
`POST /api/orders/{id}/checkout`
*Completes the order, calculates final GST, generates the receipt, and frees up the physical table for the next guests.*

---
*Built with ❤️ by Deepmind Antigravity*
