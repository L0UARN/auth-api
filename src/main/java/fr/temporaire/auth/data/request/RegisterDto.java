package fr.temporaire.auth.data.request;

import org.springframework.lang.NonNull;

/**
 * Data transfer object (DTO) for user registration.
 * This class is used to capture the required data for registering a new user.
 */
public class RegisterDto {
    @NonNull
    private final String username;

    @NonNull
    private final String password;

    @NonNull
    private final String student;

    /**
     * Constructs a RegisterDto with the given username, password, and student identifier.
     *
     * @param username The username to be used for registration.
     * @param password The password to be used for registration.
     * @param student  The student identifier to be associated with the user.
     */
    public RegisterDto(@NonNull String username, @NonNull String password, @NonNull String student) {
        this.username = username;
        this.password = password;
        this.student = student;
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

    /**
     * Gets the student identifier.
     *
     * @return The student identifier.
     */
    @NonNull
    public String getStudent() {
        return student;
    }
}
