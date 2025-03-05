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

    @Value("${AUTH_API_JWT_SECRET}")
    private String secret;

    @Autowired
    public AuthService(AccountRepository accountRepository, ExpiredTokenRepository expiredTokenRepository) {
        this.accountRepository = accountRepository;
        this.expiredTokenRepository = expiredTokenRepository;
    }

    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public boolean usernameIsRegistered(String username) {
        return findByUsername(username).isPresent();
    }

    public Optional<Account> findByStudent(String student) {
        return accountRepository.findByStudent(student);
    }

    public boolean studentIsRegistered(String student) {
        return findByStudent(student).isPresent();
    }

    public void register(Account account) {
        accountRepository.save(account);
    }

    public void unregister(Account account) {
        accountRepository.delete(account);
    }

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

    public String generateToken(Account account) {
        return JWT.create()
            .withClaim("student", account.getStudent())
            .withExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS))
            .sign(Algorithm.HMAC256(secret));
    }

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

    public void expireToken(String token) {
        expiredTokenRepository.save(new ExpiredToken(token));
    }
}
