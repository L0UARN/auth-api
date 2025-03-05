package fr.temporaire.auth.data.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a user account in the authentication system.
 */
@Document("Account")
public class Account {
    @Id
    private final String username;
    private final String password;
    private final String student;

    /**
     * Constructs an Account instance.
     *
     * @param username The unique username of the account.
     * @param password The password associated with the account.
     * @param student  The student identifier linked to the account.
     */
    public Account(String username, String password, String student) {
        this.username = username;
        this.password = password;
        this.student = student;
    }

    /**
     * Gets the username.
     *
     * @return The username of the account.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password.
     *
     * @return The password of the account.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the student identifier.
     *
     * @return The student identifier linked to the account.
     */
    public String getStudent() {
        return student;
    }
}
