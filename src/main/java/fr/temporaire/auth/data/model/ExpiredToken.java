package fr.temporaire.auth.data.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * Represents an expired authentication token stored in Redis.
 */
@RedisHash("expired_token")
public class ExpiredToken implements Serializable {
    @Id
    private String token;
    private boolean expired;

    /**
     * Constructs an ExpiredToken instance and marks it as expired.
     *
     * @param token The authentication token to be marked as expired.
     */
    public ExpiredToken(String token) {
        this.token = token;
        this.expired = true;
    }

    /**
     * Gets the token.
     *
     * @return The expired token.
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token.
     *
     * @param token The token to set.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Checks if the token is expired.
     *
     * @return {@code true} if the token is expired, {@code false} otherwise.
     */
    public boolean isExpired() {
        return expired;
    }

    /**
     * Sets the expiration status of the token.
     *
     * @param expired {@code true} to mark the token as expired, {@code false} otherwise.
     */
    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
