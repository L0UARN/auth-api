package fr.temporaire.auth.data.response;

/**
 * Represents a token.
 * This class is used to encapsulate a token that can be returned in an api response.
 */
public class Token {
    private final String token;

    /**
     * Constructs a Token object with the specified token string.
     *
     * @param token The token string.
     */
    public Token(String token) {
        this.token = token;
    }

    /**
     * Gets the token.
     *
     * @return The token string.
     */
    public String getToken() {
        return token;
    }
}