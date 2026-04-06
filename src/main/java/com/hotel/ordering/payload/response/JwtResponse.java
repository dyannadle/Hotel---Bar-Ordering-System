package com.hotel.ordering.payload.response; // Package for outgoing API response objects.

import lombok.Getter; // Lombok: Generates Getters automatically!
import lombok.Setter; // Lombok: Generates Setters automatically!
import java.util.List; // Import for role lists.

/**
 * JwtResponse: The "Passport" we send back to the staff after a successful login.
 * This object contains the token and the staff's identity.
 */
@Getter // Tells Lombok to generate gets for token, etc.
@Setter // Tells Lombok to generate sets for our response data.
public class JwtResponse { // The response object class.

	private String token; // The 12-hour JWT passport string.
	private String type = "Bearer"; // The type of token (used in the header).
	private Long id; // The staff member's database ID.
	private String username; // The staff member's login name.
	private List<String> roles; // The list of roles (e.g. ROLE_WAITER).

	/**
	 * Constructor: Created to quickly build the final response.
	 */
	public JwtResponse(String accessToken, Long id, String username, List<String> roles) { 
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.roles = roles;
	}
}
