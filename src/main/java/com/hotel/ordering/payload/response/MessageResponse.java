package com.hotel.ordering.payload.response; // Package for our generic API response objects.

import lombok.Getter; // Lombok: Generates Getters automatically!
import lombok.Setter; // Lombok: Generates Setters automatically!

/**
 * MessageResponse: A simple object for generic API messages (e.g. success/error alerts).
 */
@Getter // Tells Lombok to generate getMessage().
@Setter // Tells Lombok to generate setMessage().
public class MessageResponse { // The response object class.

	private String message; // The actual message text we send to the staff.

	/**
	 * Constructor: Created to quickly build the final response message.
	 */
	public MessageResponse(String message) { 
		this.message = message;
	}
}
