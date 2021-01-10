package com.conference.backend.users;


import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.conference_and_rooms.entities.ConferenceEvent;
import com.conference.backend.conference_and_rooms.entities.DateInterval;
import com.conference.backend.data.utils.Role;
import com.conference.backend.exception.UserNotFoundException;
import com.conference.backend.data.utils.base.CrudManager;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Backend service to store and retrieve {@link User} instances.
 */
public class UserManager extends Observable implements Serializable, CrudManager<User> {

    private static final long serialVersionUID = 3467528467235L;

    // Maps from user ids to Users
    private Map<String, User> users;

    /**
     * Initializes this {@link UserManager}
     */
    public UserManager() {
        users = new HashMap<>();
    }

    /**
     * Fetches the users.
     *
     * @return the list of users
     */
    public List<User> getUsers() {
        return new ArrayList<User>(users.values());
    }

    /**
     * Fetches the names of all users.
     *
     * @return the name of all users
     */
    @Override
    public List<String> getNames() {
        List<String> emails = new ArrayList<>();
        for (User user : users.values()) {
            emails.add(user.getEmail());
        }
        return emails;
    }

    /**
     * Fetches the {@link User} with this id.
     *
     * @param id the id to fetch a {@link User} by
     * @return an {@link Optional} containing the user if found, or
     *          {@link Optional#empty()}
     */
    Optional<User> getUserById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    /**
     * Fetches the {@link User}s with this role.
     *
     * @param role the {@link Role} to fetch {@link User}s by
     * @return a list of {@link User}s with this role
     */
    public List<User> getUsersWithRole(Role role) {
        return this.getUsers().stream()
                .filter(currentUser -> currentUser.getRoles().contains(role))
                .collect(Collectors.toList());
    }

    /**
     * Fetches the {@link User} with this email.
     *
     * @param email the email to fetch {@link User} by
     * @return an {@link Optional} containing the user if found, or
     *          {@link Optional#empty()}
     */
    public Optional<User> getUserByEmail(String email) {
        Optional<User> user = users.values().stream()
                .filter(currentUser -> email.equals(currentUser.getEmail()))
                .findFirst();

        return user;
    }

    /**
     * Fetches the {@link User} with the given id.
     *
     * <p>
     *     Returns {@link User} instead of {@link Optional}.
     * </p>
     * @param id the id to fetch a {@link User} by
     * @return the {@link User}, if found
     */
    public User getUserByIdOrNull(String id) {
        return getUserById(id).orElse(null);
    }

    /**
     * Fetches the ID by the email passed.
     *
     * @param email the email of the user
     * @return the Id of this {@link User}
     * @throws UserNotFoundException if the email doesn't exist
     */
    public String getUserIdByEmail(String email) throws UserNotFoundException {
        return getUserByEmailOrThrow(email).getId();
    }

    /**
     * Adds a role to a user by their ID.
     * @param id The ID of the user.
     * @param role The {@code Role} that should be added.
     */
    public void addRoleToUserById(String id, Role role) {
        getUserByIdOrNull(id).addRole(role);
    }

    /**
     * Fetches the Roles of the {@code User} by their ID.
     *
     * @param id The ID of the {@code User}.
     * @return a list of roles of this {@code User}
     */
    public List<Role> getRolesByUserId(String id) {
        return new ArrayList<>(getUserByIdOrNull(id).getRoles());
    }

    /**
     * Checks if a {@code User} has any of the passed roles by ID.
     * @param id the ID of the {@code User}
     * @param roles the roles of the {@code User}
     * @return true if the {@code User} has any one of the roles passed
     */
    public boolean hasAnyOneOfRolesById(String id, Role... roles) {
        return getUserByIdOrNull(id).hasAnyOneOfRoles(roles);
    }

    /**
     * Checks if there is a {@code User} with this email.
     *
     * @param email the email of the {@code User}
     * @return true if there is a {@code User} with this email
     */
    public boolean hasUserByEmail(String email) {
        return getUserByEmail(email).orElse(null) != null;
    }

    /**
     * Checks if there is a {@code User} with this email.
     *
     * @param id the ID of the {@code User}
     * @return true if there is a {@code User} with this ID
     */
    public boolean hasUserById(String id) {
        return getUserByIdOrNull(id) != null;
    }

    /**
     * Gets the name of the User by their ID.
     *
     * @param id the ID of the {@code User}
     * @return the {@code String} representing the name.
     */
    public String getNameById(String id) {
        User u = getUserByIdOrNull(id);
        return u.getFirstName() + " " + u.getLastName();
    }

    /**
     * Gets the first name of the User by their ID.
     *
     * @param id the ID of the {@code User}
     * @return the {@code String} representing the first name.
     */
    public String getFirstNameById(String id) {
        User u = getUserByIdOrNull(id);
        return u.getFirstName();
    }

    /**
     * Gets the last name of the User by their ID.
     *
     * @param id the ID of the {@code User}
     * @return the {@code String} representing the last name.
     */
    public String getLastNameById(String id) {
        User u = getUserByIdOrNull(id);
        return u.getLastName();
    }

    /**
     * Gets the email by the {@code User} ID.
     *
     * @param id the ID of the {@code User}
     * @return the {@code String} representing the email.
     */
    public String getEmailByUserId(String id) {
        return getUserByIdOrNull(id).getEmail();
    }

    /**
     * Gets the conference events of this {@code User} by the ID passed.
     *
     * @param id the ID of this {@code User}
     * @return a list of strings of conference events
     */
    public List<String> getConferenceEventsById(String id) {
        return new ArrayList<>(getUserByIdOrNull(id).getConferenceEvents());
    }

    /**
     * Creates a new {@link User} with this email, password, first name, last name, and role.
     *
     * @param email the email of this new {@link User}
     * @param password the password of this new {@link User}
     * @param firstName the first name of this new {@link User}
     * @param lastName the last name of this new {@link User}
     * @param roles the {@link Role} of this new {@link User}
     * @return the new {@link User} created
     */
    public User createNew(String email, String password, String firstName, String lastName, Role... roles) {
        return new User(email, password, firstName, lastName, roles);
    }

    /**
     * Deletes the given {@link User} from the users.
     *
     * @param user
     *          the {@link User} to delete
     */
    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    /**
     * Persists the given {@link User} into the users dictionary.
     *
     * @param user
     *          the user to save
     */
    @Override
    public void save(User user) {
        User entity = users.get(user.getId());

        if (entity == null) {
            entity = new User(user);

            if (user.getId() == null) {
                entity.setId(entity.hashCode() + "");
            }

            users.put(entity.getId(), entity);
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Fetches the {@link User} with the given email
     *
     * <p>
     *     Returns {@link User} instead of {@link Optional}. If the user can't be found,
     *     throw an exception.
     * </p>
     * @param email the email to fetch a {@link User} by
     * @return the {@link User}, if found
     * @throws UserNotFoundException
     *              if no user in the repository has this email
     */
    public User getUserByEmailOrThrow(String email) throws UserNotFoundException {
        return getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        "User with email: " + email + " was not found!"
                ));
    }

    /**
     * Fetches the {@link User} with the given id.
     *
     * <p>
     *     Returns {@link User} instead of {@link Optional}. If the user can't be found,
     *     throw an exception.
     * </p>
     * @param id the id to fetch a {@link User} by
     * @return the {@link User}, if found
     * @throws UserNotFoundException
     *              if no {@link User} in the repository has this id
     */
    public User getUserByIdOrThrow(String id) throws UserNotFoundException {
        return getUserById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        "User@" + id + " was not found!"
                ));
    }

    /**
     * Fetches the {@link User}s with {@code roleRequired} but not any of the filtered roles.
     *
     * @param roleRequired The role this {@link User} should have
     * @param filteredRoles The roles this {@link User} should not have
     * @return a list of {@link User} satisfying the conditions:
     *              (1) Has {@code roleRequired}
     *              (2) Does not contain any roles in {@code filteredRoles}
     */
    public List<User> getUsersWithRoleAndFilterRoles(Role roleRequired, Role... filteredRoles) {
        List<User> candidates = new ArrayList<>();
        for (User user : getUsersWithRole(roleRequired)) {
            // User should not have any of the filtered roles
            if (!user.getRoles().stream().anyMatch(r -> Arrays.asList(filteredRoles).contains(r))) {
                candidates.add(user);
            }
        }
        return candidates;
    }

    /**
     * Signs up a {@link User} for a conference event. Adds to the user's list of {@code conferenceEvents},
     * and adds to the {@code attendees} of {@link ConferenceEvent}.
     *
     * @param currentUserId The String id of the {@link User} to update
     * @param conferenceEvent The {@link ConferenceEvent} to update
     * @param eventRepository The {@link ConferenceEventManager} for operations required to signup
     * @return returns {@code true} if this operation was successful.
     */
    public boolean signUpUserForConferenceEvent(String currentUserId,
                                                ConferenceEventManager eventRepository,
                                                ConferenceEvent conferenceEvent) {
        User user = getUserByIdOrNull(currentUserId);

        if (!conferenceEvent.hasCapacityForAttendees()) {
            return false;
        }

        DateInterval a = eventRepository.getDateByEventName(conferenceEvent.getEventName());

        List<String> usersConferenceEvents = user.getConferenceEvents();

        for (String ce : usersConferenceEvents) {
            DateInterval b = eventRepository.getDateByEventName(ce);

            if (a.getStart().before(b.getEnd()) && b.getStart().before(a.getEnd()))  {
                return false;
            }
        }

        boolean eventAddedToUserEvents = user.addConferenceEventToConferenceEvents(conferenceEvent.getEventName());
        if (eventAddedToUserEvents) {
            conferenceEvent.addAttendee(user.getId());
            setChanged();
            notifyObservers(eventRepository);
            return true;
        }
        return false;
    }

    /**
     * Cancels a conference event from a {@link User}. Removes from the user's list of {@code conferenceEvents},
     * and removes from the {@code attendees} of {@link ConferenceEvent}.
     *
     * @param id The id for the {@link User} to update
     * @param conferenceEvent The {@link ConferenceEvent} to update
     * @return returns {@code true} if this operation was successful.
     */
    public boolean removeUserFromConferenceEvent(String id, ConferenceEvent conferenceEvent) {
        boolean result = getUserByIdOrNull(id).removeConferenceEventFromConferenceEvents(conferenceEvent.getEventName());
        if (result) {
            conferenceEvent.removeAttendee(id);
            return true;
        }
        return false;
    }

    /**
     * Gets a neatly typed string split by newlines of the users in this repository.
     *
     * @return a String of users
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        this.users.values().forEach(s -> result.append(s.toString() + "\n"));
        return result.toString();
    }

    /**
     * Removes a conference event from all the conference events list of this {@code User}.
     *
     * @param id The ID of the {@code User}
     * @param conferenceEventName The name of the conference
     * @return true if the {@code ConferenceEvent} was removed properly.
     */
    public boolean removeConferenceEventFromConferenceEvents(String id, String conferenceEventName) {
        return getUserByIdOrNull(id).removeConferenceEventFromConferenceEvents(conferenceEventName);
    }
}
