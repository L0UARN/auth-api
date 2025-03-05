package fr.temporaire.auth.data.request;

import org.springframework.lang.NonNull;

public class RegisterDto {
    @NonNull
    private final String username;
    @NonNull
    private final String password;
    @NonNull
    private final String student;

    public RegisterDto(@NonNull String username, @NonNull String password, @NonNull String student) {
        this.username = username;
        this.password = password;
        this.student = student;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    @NonNull
    public String getStudent() {
        return student;
    }
}
