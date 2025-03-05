package fr.temporaire.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import fr.temporaire.auth.data.model.Account;
import fr.temporaire.auth.data.request.CredentialsDto;
import fr.temporaire.auth.service.AuthService;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthServiceTests {
    @Autowired
    AuthService authService;

    static Account account;

    @Test
    @BeforeAll
    public void setUp() {
        account = new Account("testUsername", "testPassword", "testStudent");
        authService.register(account);

        assertTrue(authService.usernameIsRegistered(account.getUsername()));
        assertTrue(authService.studentIsRegistered(account.getStudent()));
        assertTrue(authService.findByUsername(account.getUsername()).isPresent());
        assertTrue(authService.findByStudent(account.getStudent()).isPresent());
    }

    @Test
    public void testValidateCredentials() {
        CredentialsDto credentials = new CredentialsDto(account.getUsername(), account.getPassword());
        Optional<Account> validated = authService.validateCredentials(credentials);

        assertTrue(validated.isPresent());
        assertEquals(account.getUsername(), validated.get().getUsername());
        assertEquals(account.getPassword(), validated.get().getPassword());
        assertEquals(account.getStudent(), validated.get().getStudent());
    }

    @Test
    public void testToken() {
        String token = authService.generateToken(account);
        Optional<Account> inToken = authService.verifyToken(token);

        assertTrue(inToken.isPresent());
        assertEquals(account.getUsername(), inToken.get().getUsername());
        assertEquals(account.getPassword(), inToken.get().getPassword());
        assertEquals(account.getStudent(), inToken.get().getStudent());
    }

    @Test
    public void testExpiredToken() {
        String token = authService.generateToken(account);
        authService.expireToken(token);

        Optional<Account> inToken = authService.verifyToken(token);
        assertTrue(inToken.isEmpty());
    }

    @Test
    public void testFakeToken() {
        String fakeToken = JWT.create()
            .withClaim("student", account.getStudent())
            .withExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS))
            .sign(Algorithm.HMAC256("fake secret"));

        Optional<Account> inToken = authService.verifyToken(fakeToken);
        assertTrue(inToken.isEmpty());
    }

    @Test
    @AfterAll
    public void testUnregister() {
        authService.unregister(account);

        assertFalse(authService.studentIsRegistered(account.getStudent()));
        assertFalse(authService.findByUsername(account.getUsername()).isPresent());
        assertFalse(authService.findByStudent(account.getStudent()).isPresent());
    }
}
