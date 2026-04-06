package com.hotel.ordering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @SpringBootApplication: The heart of our Spring Boot app. It tells Spring to:
 * 1. Enable auto-configuration (handle default settings).
 * 2. Start component scanning (find our Controllers, Services, etc.).
 * 3. Act as a configuration class.
 */
@SpringBootApplication
public class HotelOrderingApplication {

	// This main method is the first thing that runs when we start the app.
	public static void main(String[] args) {
		// This line starts the entire Spring framework and the internal server (Tomcat).
		SpringApplication.run(HotelOrderingApplication.class, args);
	}

}
