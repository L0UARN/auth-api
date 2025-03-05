package fr.temporaire.auth.controller;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.temporaire.auth.data.model.Account;
import fr.temporaire.auth.data.request.CredentialsDto;
import fr.temporaire.auth.data.request.RegisterDto;
import fr.temporaire.auth.data.response.Error;
import fr.temporaire.auth.data.response.Message;
import fr.temporaire.auth.data.response.Token;
import fr.temporaire.auth.data.response.Verification;
import fr.temporaire.auth.data.response.utils.Result;
import fr.temporaire.auth.service.AuthService;

/**
 * Controller for authentication and authorization.
 */
@CrossOrigin(
        origins = {"*"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE},
        exposedHeaders = {HttpHeaders.SET_COOKIE, HttpHeaders.AUTHORIZATION},
        allowedHeaders = {HttpHeaders.SET_COOKIE, HttpHeaders.AUTHORIZATION}
)
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    /**
     * Constructs an AuthController with the specified AuthService.
     *
     * @param authService The authentication service.
     */
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registers a new user.
     *
     * @param registerRequest The registration request containing user details.
     * @return A response entity with a token if successful, or an error message if registration fails.
     */
    @PostMapping("/register")
    public ResponseEntity<Result<Token, Error>> register(@Validated @RequestBody RegisterDto registerRequest) {
        if (authService.usernameIsRegistered(registerRequest.getUsername())) {
            Result<Token, Error> result = Result.error(new Error("This username is already in use."));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        if (authService.studentIsRegistered(registerRequest.getStudent())) {
            Result<Token, Error> result = Result.error(new Error("This student is already registered."));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        Account account = new Account(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getStudent());
        authService.register(account);

        String token = authService.generateToken(account);
        Result<Token, Error> result = Result.success(new Token(token));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Unregisters a user.
     *
     * @param credentials The user's credentials.
     * @return A response entity indicating whether the unregistration was successful or not.
     */
    @DeleteMapping("/unregister")
    public ResponseEntity<Result<Message, Error>> unregister(@Validated @ModelAttribute CredentialsDto credentials) {
        Optional<Account> account = authService.validateCredentials(credentials);
        if (account.isEmpty()) {
            Result<Message, Error> result = Result.error(new Error("Invalid credentials."));
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        }

        authService.unregister(account.get());

        Result<Message, Error> result = Result.success(new Message("Account unregistered."));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Logs in a user and sends back a token.
     *
     * @param credentials The user's credentials.
     * @return A response entity with a token if successful, or an error message if login fails.
     */
    @PostMapping("/login")
    public ResponseEntity<Result<Token, Error>> dummyToken(@Validated @RequestBody CredentialsDto credentials) {
        Optional<Account> account = authService.validateCredentials(credentials);
        if (account.isEmpty()) {
            Result<Token, Error> result = Result.error(new Error("Invalid credentials."));
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        }

        String token = authService.generateToken(account.get());

        HttpCookie cookie = ResponseCookie.from("token", token).maxAge(Duration.ofDays(1)).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        Result<Token, Error> result = Result.success(new Token(token));
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(result);
    }

    /**
     * Retrieves the user's student id based on the provided token.
     *
     * @param token The authentication token.
     * @return A response entity with user's student id or an error message.
     */
    @GetMapping("/me")
    public ResponseEntity<Result<Verification, Error>> me(@RequestHeader(HttpHeaders.AUTHORIZATION) @Validated @NonNull String token) {
        Optional<Account> account = authService.verifyToken(token);
        if (account.isEmpty()) {
            Result<Verification, Error> result = Result.error(new Error("Invalid token."));
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        }

        Result<Verification, Error> result = Result.success(new Verification(account.get().getStudent()));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Logs out a user by expiring their token.
     *
     * @param token The authentication token (nullable).
     * @return A response entity confirming logout.
     */
    @DeleteMapping("/logout")
    public ResponseEntity<Message> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) @Nullable String token) {
        if (token == null) {
            return ResponseEntity.ok(new Message("Not logged in, but that's fine!"));
        }

        authService.expireToken(token);

        HttpCookie cookie = ResponseCookie.from("token", "").maxAge(0).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().headers(headers).body(null);
    }
}
