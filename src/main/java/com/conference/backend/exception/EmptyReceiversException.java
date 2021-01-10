package com.conference.backend.exception;

import com.conference.backend.messenger.controllers.systems.SendMessageSystem;

/**
 * Thrown when sending messages to an empty list of receivers.
 * {@link SendMessageSystem} for usage.
 */
public class EmptyReceiversException extends Exception {
    private static final long serialVersionUID = 732648726349872L;

    /**
     * {@inheritDoc}
     */
    public EmptyReceiversException(final String message) {
        super(message);
    }
}
