package com.conference.backend.data.utils.base;

/**
 * The {@code Initializer} interface should be implemented
 * for classes intended to initialize instances.
 */
public interface Initializer {
    /**
     * Should be implemented if this class initializes instances.
     * @throws Exception if some exception occurred.
     */
    void init() throws Exception;
}
