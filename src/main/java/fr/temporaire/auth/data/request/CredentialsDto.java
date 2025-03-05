package fr.temporaire.auth.data.request;

import org.springframework.lang.NonNull;

/**
 * Data transfer object (DTO) for user credentials.
 * This class is used for passing username and password for authentication.
 */
public class CredentialsDto {
    @NonNull
    private final String username;

    @NonNull
    private final String password;

    /**
     * Constructs a CredentialsDto with the given username and password.
     *
     * @param username The username for authentication.
     * @param password The password for authentication.
     */
    public CredentialsDto(@NonNull String username, @NonNull String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the username.
     *
     * @return The username.
     */
    @NonNull
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password.
     *
     * @return The password.
     */
    @NonNull
    public String getPassword() {
        return password;
    }
}
