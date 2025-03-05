package fr.temporaire.auth.data.response;

public class Verification {
    private final boolean ok;
    private final String student;

    public Verification(String student) {
        this.ok = true;
        this.student = student;
    }

    public boolean isOk() {
        return ok;
    }

    public String getStudent() {
        return student;
    }
}
