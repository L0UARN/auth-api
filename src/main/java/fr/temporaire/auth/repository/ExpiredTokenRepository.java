package fr.temporaire.auth.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.temporaire.auth.data.model.ExpiredToken;

/**
 * Repository for managing {@link ExpiredToken} entities.
 * This interface allows querying and managing expired tokens in the repository.
 */
@Repository
public interface ExpiredTokenRepository extends CrudRepository<ExpiredToken, String> {

    /**
     * Finds an expired token by its value.
     *
     * @param token The token string to search for.
     * @return An {@link Optional} containing the found expired token, or empty if no expired token exists with the given value.
     */
    Optional<ExpiredToken> findByToken(String token);
}