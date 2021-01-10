package com.conference.backend.data.utils.base;

/**
 * The {@code Startable} interface should be implemented
 * for classes whose instances are intended to be executed by a thread.
 */
public interface Startable {
    /**
     * Creates a thread.
     */
    void start();
}
