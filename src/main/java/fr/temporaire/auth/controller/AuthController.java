package fr.temporaire.auth.controller;

import fr.temporaire.auth.data.model.Account;
import fr.temporaire.auth.data.request.CredentialsDto;
import fr.temporaire.auth.data.request.RegisterDto;
import fr.temporaire.auth.data.response.*;
import fr.temporaire.auth.data.response.Error;
import fr.temporaire.auth.data.response.utils.Result;
import fr.temporaire.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Optional;

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

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

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

    @DeleteMapping("/logout")
    public ResponseEntity<Message> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) @Nullable String token) {
        if (token == null) {
            return ResponseEntity.ok(new Message("Not logged in, but that's fine!"));
        }

        authService.expireToken(token);

        HttpCookie cookie = ResponseCookie.from("token", "").maxAge(0).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().headers(headers).body(new Message("Logged out!"));
    }
}
