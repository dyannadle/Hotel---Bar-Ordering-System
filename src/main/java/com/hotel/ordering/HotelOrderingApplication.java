package com.hotel.ordering;

import io.swagger.v3.oas.annotations.OpenAPIDefinition; // For OpenAPI definitions.
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType; // For security scheme types.
import io.swagger.v3.oas.annotations.info.Info; // For info documentation.
import io.swagger.v3.oas.annotations.security.SecurityRequirement; // To require authentication.
import io.swagger.v3.oas.annotations.security.SecurityScheme; // For defining the scheme.
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @SpringBootApplication: The heart of our Spring Boot app.
 */
@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(title = "Hotel & Bar Ordering System API", version = "1.0"),
    security = @SecurityRequirement(name = "bearerAuth") // Global requirement.
)
@SecurityScheme(
    name = "bearerAuth", // Matching name for our scheme.
    type = SecuritySchemeType.HTTP, // HTTP scheme.
    scheme = "bearer", // Bearer type.
    bearerFormat = "JWT" // Format: JWT.
)
public class HotelOrderingApplication {

	// This main method is the first thing that runs when we start the app.
	public static void main(String[] args) {
		// This line starts the entire Spring framework and the internal server (Tomcat).
		SpringApplication.run(HotelOrderingApplication.class, args);
	}

}
