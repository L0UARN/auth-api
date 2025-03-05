package fr.temporaire.auth.repository;

import fr.temporaire.auth.data.model.ExpiredToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpiredTokenRepository extends CrudRepository<ExpiredToken, String> {
    Optional<ExpiredToken> findByToken(String token);
}
