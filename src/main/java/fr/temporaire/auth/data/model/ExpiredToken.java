package fr.temporaire.auth.data.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("expired_token")
public class ExpiredToken implements Serializable {
    @Id
    private String token;
    private boolean expired;

    public ExpiredToken(String token) {
        this.token = token;
        this.expired = true;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
