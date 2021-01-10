package com.conference.backend.data.utils;

// https://stackoverflow.com/questions/14677993/how-to-create-a-hashmap-with-two-keys-key-pair-value

import java.util.UUID;

/**
 * Used for creating menu options.
 */
public class Value {
    private final String s;
    private final Role[] roles;
    private static final long serialVersionUID = 873465837456534L;


    /**
     * Constructs a new instance with the given data.
     *
     * @param s the User friendly string
     * @param roles the roles this menu option required
     */
    public Value(String s, Role... roles) {
        this.s = s;
        this.roles = roles;
    }

    /**
     * @return an array of {@link Role}s
     */
    public Role[] getRoles() {
        return roles;
    }

    /**
     * @return the {@code String} for this menu option
     */
    public String getString() {
        return s;
    }

    /**
     * Compares two objects by role and string
     *
     * @param o the object to compare with
     * @return {@code true} if the object's roles and string are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Value)) return false;
        Value val = (Value) o;
        return s == val.s && roles == val.roles;
    }

    /**
     * To ensure unique values in hashmap
     * @return a random UUID as {@code int} for this {@code Value}
     */
    @Override
    public int hashCode() {
        return UUID.randomUUID().hashCode();
    }
}
