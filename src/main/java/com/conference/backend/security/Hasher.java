package com.conference.backend.security;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * The <code>Hasher</code> interface should be implemented
 * for classes whose purpose is to hash passwords. Allows for client
 * to implement any type of hashing algorithm they need.
 */
public interface Hasher {
    /**
     * Gets a generated password hash. All hashing algorithms should implement this method.
     * @param password the password to hash
     * @return the hashed password
     */
    String generateStrongPasswordHash(String password);

    /**
     * Checks if the passwords are equal depending on the hash.
     * @param originalPassword The password passed in the login screen
     * @param storedPassword The hashed password stored in memory
     * @return true if the passwords are equal after unhashing
     */
    boolean validatePassword(String originalPassword, String storedPassword);
}
