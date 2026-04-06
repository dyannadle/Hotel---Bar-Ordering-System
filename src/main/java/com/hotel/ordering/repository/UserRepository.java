package com.hotel.ordering.repository;

import com.hotel.ordering.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository: Interface for managing staff authentication data.
 */
@Repository // Repository component
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * findByUsername: Used for login. If the user exists, it returns them.
     */
    Optional<User> findByUsername(String username);

    /**
     * existsByUsername: Quick check to see if a username is already taken.
     */
    Boolean existsByUsername(String username);
}
