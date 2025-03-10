package fr.temporaire.auth.data.response;

/**
 * Represents the verification status when a user requests a verification of their authentication token.
 */
public class Verification {
    private final boolean ok;
    private final String student;

    /**
     * Constructs a Verification object with the given student identifier.
     * The verification is marked as successful.
     *
     * @param student The student identifier associated with the verification.
     */
    public Verification(String student) {
        this.ok = true;
        this.student = student;
    }

    /**
     * Checks if the verification was successful.
     *
     * @return {@code true} if the verification was successful, otherwise {@code false}.
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * Gets the student identifier associated with the verification.
     *
     * @return The student identifier.
     */
    public String getStudent() {
        return student;
    }
}