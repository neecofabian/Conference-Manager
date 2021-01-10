package com.conference.backend.data.utils;

/**
 * The Role of a User
 */
public enum Role {
    ORGANIZER("Organizer"),
    ATTENDEE("Attendee"),
    SPEAKER("Speaker"),
    VIP("VIP");

    private String role;

    /**
     * Constructs a Role
     * @param role the string associated with this role.
     */
    Role(String role) {
        this.role = role;
    }

    /**
     * returns the String representation of the given Role.
     *
     * @return String representation of Role.
     */
    @Override
    public String toString(){
        return role;
    }
}
