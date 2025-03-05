package fr.temporaire.auth.data.response;

/**
 * Represents a message.
 * This class is used to encapsulate a message that can be returned in the response.
 */
public class Message {
    private final String message;

    /**
     * Constructs a Message object with the specified message content.
     *
     * @param message The message content.
     */
    public Message(String message) {
        this.message = message;
    }

    /**
     * Gets the message content.
     *
     * @return The message content.
     */
    public String getMessage() {
        return message;
    }
}
