package com.hotel.ordering.service; // Package for our user management services.

import com.fasterxml.jackson.annotation.JsonIgnore; // Used to hide the password from JSON outputs.
import com.hotel.ordering.entity.User; // Using our User entity.
import org.springframework.security.core.GrantedAuthority; // Represents a single skill/role of the user.
import org.springframework.security.core.authority.SimpleGrantedAuthority; // A simple implementation of a role.
import org.springframework.security.core.userdetails.UserDetails; // Spring's main standard for user info.

import java.util.Collection; // The standard Java collection interface.
import java.util.Collections; // Utility class for working with collections.
import java.util.List; // For list-based storage of roles.
import java.util.Objects; // Utility for checking equality of objects.

/**
 * UserDetailsImpl: The "Passport Wrapper".
 * It takes our User entity and prepares it for Spring Security's authentication system.
 */
public class UserDetailsImpl implements UserDetails { // Implements the standard UserDetails interface.

    private static final long serialVersionUID = 1L; // Version ID for serializing this object.

    private Long id; // The staff member's unique database ID.
    private String username; // The staff member's login name.

    @JsonIgnore // SECURITY: Prevents the password from ever being sent over the web in JSON!
    private String password;

    // The collection of roles/permissions the staff member has (e.g. ROLE_ADMIN).
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructor: Build the wrapper using data from our User entity.
     */
    public UserDetailsImpl(Long id, String username, String password,
                           Collection<? extends GrantedAuthority> authorities) { // Takes ID, name, password, and roles.
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * build: A static helper to transform a User entity into this UserDetailsImpl wrapper.
     */
    public static UserDetailsImpl build(User user) { // Takes a database User object.
        // 1. Convert our Enum role (e.g. ADMIN) into a Spring Security Authority (ROLE_ADMIN).
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name()) // Prefixes with "ROLE_".
        );

        // 2. Return the new wrapper with all the mapped data.
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                authorities);
    }

    @Override // Returns the list of roles the user has.
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // Returns the staff member's ID.
    public Long getId() {
        return id;
    }

    @Override // Returns the hashed password.
    public String getPassword() {
        return password;
    }

    @Override // Returns the login username.
    public String getUsername() {
        return username;
    }

    @Override // Returns true to signify the account is "active" and not expired.
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override // Returns true to signify the account is not locked (e.g. too many failed logins).
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override // Returns true to signify the credentials (password) won't expire.
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override // Returns true to signify the staff member is allowed to work.
    public boolean isEnabled() {
        return true;
    }

    @Override // Custom equals method to compare staff members by their unique IDs.
    public boolean equals(Object o) {
        if (this == o) return true; // If they are the exact same object.
        if (o == null || getClass() != o.getClass()) return false; // If the other object is null or different class.
        UserDetailsImpl user = (UserDetailsImpl) o; // Cast to our type.
        return Objects.equals(id, user.id); // Compare their database IDs.
    }
}
