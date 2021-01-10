package com.conference.backend.users;

import com.conference.backend.conference_and_rooms.entities.ConferenceEvent;
import com.conference.backend.data.utils.base.AbstractEntity;
import com.conference.backend.data.utils.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * {@link AbstractEntity} plus common user details representing a User.
 *
 */
public class User extends AbstractEntity {
    private static final long serialVersionUID = 283746827364324L;

    private String email;

    private String passwordHash;

    private String firstName;

    private String lastName;

    private List<String> conferenceEvents;

    private List<String> contactsList;

    // the roles of this User
    private List<Role> roles;

    /**
     * Constructs a new instance with the given data.
     *
     * @param email the email of this user
     * @param passwordHash
     *              hashes password using jBCrypt
     * @param firstName the first name of this user
     * @param lastName the last name of this user
     * @param roles the {@link Role}s of this user
     */
    public User(String email, String passwordHash, String firstName, String lastName, Role... roles) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.conferenceEvents = new ArrayList<>();
        this.contactsList = new ArrayList<>();
        this.roles = Arrays.asList(roles);
    }

    /**
     * Copy constructor.
     *
     * @param other
     *            The instance to copy
     */
    public User(User other) {
        this(other.getEmail(), other.getPasswordHash(), other.getFirstName(),
                other.getLastName());
        this.setId(other.getId());
        this.conferenceEvents = other.getConferenceEvents();
        this.contactsList = other.getContactsList();
        this.roles = other.getRoles();
    }

    /**
     * Default constructor.
     */
    public User() {
        // default constructor
        this.conferenceEvents = new ArrayList<>();
        this.contactsList = new ArrayList<>();
        this.roles = new ArrayList<>();
    }

    /**
     * Gets the hashed password of the {@link User}
     *
     * @return the hashed password
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets a new hashed password for the {@link User}
     *
     * @param passwordHash the new hashed password
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Gets the first name of the {@link User}
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets a new first name of the {@link User}
     *
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get last name of the {@link User}
     *
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets a new last name for the {@link User}
     *
     * @param lastName the new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get the email of the {@link User}
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set a new email of the {@link User}
     *
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get a list of all the {@link ConferenceEvent}s this {@link User} is attending
     *
     * @return a list of {@link ConferenceEvent}s
     */
    public ArrayList<String> getConferenceEvents() {
        return new ArrayList<>(conferenceEvents);
    }

    /**
     * Gets the Contacts list of this {@link User}
     *
     * @return the contacts list
     */
    public ArrayList<String> getContactsList() {
        return new ArrayList<>(contactsList);
    }

    /**
     * Adds the id of {@link ConferenceEvent} to this user's conference events.
     *
     * @param conferenceEventId The conferenceEventId to add
     * @return {@code true} if {@code role} was added successfully
     */
    public boolean addConferenceEventToConferenceEvents(String conferenceEventId) {
        if (this.conferenceEvents.contains(conferenceEventId)) {
            return false;
        } else {
            this.conferenceEvents.add(conferenceEventId);
        }
        return true;
    }

    /**
     * Adds the id of {@link User} to this user's contacts list.
     *
     * @param userId the id of the user
     * @return {@code true} if {@code userId} was added successfully
     */
    public boolean addContactToContacts(String userId) {
        if (this.contactsList.contains(userId)) {
            return false;
        } else {
            this.contactsList.add(userId);
        }
        return true;
    }

    /**
     * Removes the id of {@link ConferenceEvent} from this user's conference events list.
     *
     * @param conferenceId the id of the {@link ConferenceEvent}
     * @return {@code true} if {@code userId} was removed successfully
     */
    public boolean removeConferenceEventFromConferenceEvents(String conferenceId) {
        return this.conferenceEvents.remove(conferenceId);
    }

    /**
     * Removes the id of {@code User} from this user's contacts list.
     *
     * @param userId the id of the {@code User}
     * @return {@code true} if {@code userId} was removed successfully
     */
    public boolean removeContactFromContacts(String userId) {
        return this.contactsList.remove(userId);
    }

    public void setContactsList(ArrayList<String> contactsList) {
        this.contactsList = contactsList;
    }

    /**
     * @return roles of this user
     * {@link com.conference.backend.data.utils.Role} for enums
     */
    public ArrayList<Role> getRoles() {
        return new ArrayList<>(roles);
    }

    /**
     * Adds a {@link Role} to this user.
     *
     * @param role the {@link Role} of this user
     * @return {@code true} if {@code role} was added successfully
     */
    public boolean addRole(Role role) {
        if (this.getRoles().contains(role)) {
            return false;
        }
        this.roles.add(role);
        return true;
    }

    /**
     * Compares this {@link User} to another.
     *
     * @param o the other User to be compared
     * @return {@code true} if the the two {@link User}s are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        User other = (User) o;
        return email.equals(other.email)
                && firstName.equals(other.firstName)
                && lastName.equals(other.lastName);
    }

    /**
     * Should be used to check if a User has permissions to use a method.
     *
     * @param roles The roles this User should have
     * @return {@code true} if {@link User} has one of the roles passed.
     */
    public boolean hasAnyOneOfRoles(Role... roles) {
        List<Role> currentUserRoles = this.getRoles();

        for (Role r : roles) {
            if (currentUserRoles.contains(r)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Hashes the {@code User} based on its attributes
     * @return an {@code int} based on email, firstName, lastName
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, firstName, lastName);
    }

    /**
     * Returns a formatted string representation of the {@link User}
     *
     * @return a formatted string of {@link User} and their email and name
     */
    @Override
    public String toString() {
        return "User [" + "email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }
}
