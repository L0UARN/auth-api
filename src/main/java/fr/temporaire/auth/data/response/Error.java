package fr.temporaire.auth.data.response;

/**
 * Represents an error message.
 * This class is used to encapsulate an error description that can be returned in an api response.
 */
public class Error {
    private final String error;

    /**
     * Constructs an Error object with the specified error message.
     *
     * @param error The error message.
     */
    public Error(String error) {
        this.error = error;
    }

    /**
     * Gets the error message.
     *
     * @return The error message.
     */
    public String getError() {
        return error;
    }
}
