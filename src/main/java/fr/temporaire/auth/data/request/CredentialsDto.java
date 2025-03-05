package fr.temporaire.auth.data.request;

import org.springframework.lang.NonNull;

public class CredentialsDto {
    @NonNull
    private final String username;
    @NonNull
    private final String password;

    public CredentialsDto(@NonNull String username, @NonNull String password) {
        this.username = username;
        this.password = password;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @NonNull
    public String getPassword() {
        return password;
    }
}
