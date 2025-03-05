package fr.temporaire.auth.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import fr.temporaire.auth.data.model.Account;

/**
 * Repository for managing {@link Account} entities.
 * This interface allows querying and managing user accounts in the MongoDB database.
 */
public interface AccountRepository extends MongoRepository<Account, String> {

    /**
     * Finds an account by its username.
     *
     * @param username The username to search for.
     * @return An {@link Optional} containing the found account, or empty if no account exists with the given username.
     */
    @Query("{username: '?0'}")
    Optional<Account> findByUsername(String username);

    /**
     * Finds an account by its student identifier.
     *
     * @param student The student identifier to search for.
     * @return An {@link Optional} containing the found account, or empty if no account exists with the given student identifier.
     */
    @Query("{student: '?0'}")
    Optional<Account> findByStudent(String student);
}