package com.conference.backend.exception;

import com.conference.backend.users.controllers.event_signup_and_viewing.UserConferenceLauncher;

/**
 * Thrown when a User could not be found.
 * {@link UserConferenceLauncher} for usage
 */
public class UserNotFoundException extends Exception {

    private static final long serialVersionUID = 4786593287456L;

    /**
     * {@inheritDoc}
     */
    public UserNotFoundException(final String message) {
        super(message);
    }
}
