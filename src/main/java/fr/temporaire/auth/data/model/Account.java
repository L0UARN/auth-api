package fr.temporaire.auth.data.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Account")
public class Account {
    @Id
    private final String username;
    private final String password;
    private final String student;

    public Account(String username, String password, String student) {
        this.username = username;
        this.password = password;
        this.student = student;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getStudent() {
        return student;
    }
}
