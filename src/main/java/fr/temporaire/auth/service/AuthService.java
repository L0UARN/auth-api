package fr.temporaire.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import fr.temporaire.auth.data.model.Account;
import fr.temporaire.auth.data.model.ExpiredToken;
import fr.temporaire.auth.data.request.CredentialsDto;
import fr.temporaire.auth.repository.AccountRepository;
import fr.temporaire.auth.repository.ExpiredTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class AuthService {
    private final AccountRepository accountRepository;
    private final ExpiredTokenRepository expiredTokenRepository;

    /**
     * The secret string used to sign the JWT token that this API will send to its clients
     */
    @Value("${AUTH_API_JWT_SECRET}")
    private String secret;

    /**
     * Constructs an AuthService with the specified AccountRepository and ExpiredTokenRepository.
     *
     * @param accountRepository the repository in which the user accounts will be stored
     * @param expiredTokenRepository the repository in which to mark the tokens as expired
     */
    @Autowired
    public AuthService(AccountRepository accountRepository, ExpiredTokenRepository expiredTokenRepository) {
        this.accountRepository = accountRepository;
        this.expiredTokenRepository = expiredTokenRepository;
    }

    /**
     * Find an account based on its username.
     *
     * @param username the username of the account to find.
     * @return an optional containing the account with the matching username, or an empty optional if it's not found.
     */
    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    /**
     * Finds whether an account with the specified username exists.
     *
     * @param username the username of the account.
     * @return whether an account is registered with the specified username.
     */
    public boolean usernameIsRegistered(String username) {
        return findByUsername(username).isPresent();
    }

    /**
     * Find an account based on its student id.
     *
     * @param student the student id of the account to find.
     * @return an optional containing the account with the matching username, or an empty optional if it's not found.
     */
    public Optional<Account> findByStudent(String student) {
        return accountRepository.findByStudent(student);
    }

    /**
     * Finds whether an account with the specified student id exists.
     *
     * @param student the student id of the account.
     * @return an optional containing the account with the matching student id, or an empty optional if it's not found.
     */
    public boolean studentIsRegistered(String student) {
        return findByStudent(student).isPresent();
    }

    /**
     * Save a new account to the database.
     *
     * @param account the account to save.
     */
    public void register(Account account) {
        accountRepository.save(account);
    }

    /**
     * Deletes an account from the database.
     *
     * @param account the account to delete.
     */
    public void unregister(Account account) {
        accountRepository.delete(account);
    }

    /**
     * Check if there exists an account that matches the specified credentials.
     *
     * @param credentials the credentials to validate.
     * @return an optional containing the account matching the credentials if they are valid, or an empty optional.
     */
    public Optional<Account> validateCredentials(CredentialsDto credentials) {
        Optional<Account> account = accountRepository.findByUsername(credentials.getUsername());
        if (account.isEmpty()) {
            System.out.println("No account with that username");
            return Optional.empty();
        }

        if (!account.get().getPassword().equals(credentials.getPassword())) {
            System.out.println("Wrong password");
            return Optional.empty();
        }

        return account;
    }

    /**
     * Generate a token for the specified account.
     *
     * @param account the account for which to generate the token.
     * @return the generated token.
     */
    public String generateToken(Account account) {
        return JWT.create()
            .withClaim("student", account.getStudent())
            .withExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS))
            .sign(Algorithm.HMAC256(secret));
    }

    /**
     * Verifies if a token is valid and corresponds to an account.
     *
     * @param token the token to check.
     * @return an optional containing the account matching the verified token, or an empty optional if it doesn't check out.
     */
    public Optional<Account> verifyToken(String token) {
        Optional<ExpiredToken> expiredToken = expiredTokenRepository.findByToken(token);
        if (expiredToken.isPresent()) {
            return Optional.empty();
        }

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).withClaimPresence("student").build();

        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            String student = decodedJWT.getClaim("student").asString();
            return accountRepository.findByStudent(student);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Marks a token as expired so it can't be verified anymore.
     *
     * @param token the token to expire.
     */
    public void expireToken(String token) {
        expiredTokenRepository.save(new ExpiredToken(token));
    }
}
