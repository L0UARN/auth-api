package fr.temporaire.auth.repository;

import fr.temporaire.auth.data.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

//@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    @Query("{username: '?0'}")
    Optional<Account> findByUsername(String username);
    @Query("{student: '?0'}")
    Optional<Account> findByStudent(String student);
}
