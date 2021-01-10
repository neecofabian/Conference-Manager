package com.conference.backend.exception;

/**
 * Thrown when a message is not found.
 */
public class MessageNotFoundException extends Exception{
    private static final long serialVersionUID = 74352085265390L;

    /**
     * {@inheritDoc}
     */
    public MessageNotFoundException(final String message) {
        super(message);
    }
}
