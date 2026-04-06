package com.hotel.ordering.repository;

import com.hotel.ordering.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The UserRepository interface handles database interaction for the users table.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their unique username. 
     * Crucial for authentication during login.
     */
    Optional<User> findByUsername(String username);

    /**
     * Check if a username already exists.
     */
    Boolean existsByUsername(String username);
}
