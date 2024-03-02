package com.example.techtaskagencyamazon.repositories;

import com.example.techtaskagencyamazon.models.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * These are interface methods for interacting with user data in a MongoDB database.
 */
public interface UserRepository extends MongoRepository<User, String> {
    /**
     * This method returns the user
     *
     * @param login login of the requested user
     * @return Optional<User> that contains the user
     */
    Optional<User> findByLogin(String login);

    /**
     * This method returns a boolean indicating whether a user with the given login exists in the database.
     *
     * @param login login of the requested user
     * @return Does the user exist
     */
    boolean existsByLogin(String login);
}
