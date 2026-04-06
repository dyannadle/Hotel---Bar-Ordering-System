package com.hotel.ordering.service; // Package for our user management services.

import com.hotel.ordering.entity.User; // Using our User database model.
import com.hotel.ordering.repository.UserRepository; // Using our user database repository.
import lombok.RequiredArgsConstructor; // Lombok: Injects dependencies automatically!
import org.springframework.security.core.userdetails.UserDetails; // Spring's main standard for user info.
import org.springframework.security.core.userdetails.UserDetailsService; // Interface we must implement for security.
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Error thrown if user doesn't exist.
import org.springframework.stereotype.Service; // Marks this as a Spring Service bean.
import org.springframework.transaction.annotation.Transactional; // Ensures database operations are safe.

/**
 * UserDetailsServiceImpl: The "Staff Database Loader".
 * This class tells Spring Security how to find our staff members in MySQL.
 */
@Service // Tells Spring to manage this class.
@RequiredArgsConstructor // Automatically creates a constructor for our repository.
public class UserDetailsServiceImpl implements UserDetailsService { // Implements the standard interface.

    // The repository used to talk to the 'users' table in MySQL.
    private final UserRepository userRepository;

    /**
     * loadUserByUsername: The search method used during login.
     */
    @Override // Overriding the method from UserDetailsService.
    @Transactional // Makes sure the whole process is one safe database transaction.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // Takes a username string.

        // 1. Search for the user in the database by their username.
        User user = userRepository.findByUsername(username)
                // 2. If not found, throw a clear "Username Not Found" error.
                .orElseThrow(() -> new UsernameNotFoundException("Staff member not found with username: " + username));

        // 2. "Build" the special SecurityUserDetails object from our database user.
        return UserDetailsImpl.build(user);
    }

}
