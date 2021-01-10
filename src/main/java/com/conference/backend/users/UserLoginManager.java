package com.conference.backend.users;

import com.conference.backend.data.utils.Role;
import com.conference.backend.exception.UserNotFoundException;
import com.conference.backend.security.PBKDF2WithHmacSHA1Hasher;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.List;

/**
 * Stores {@link UserManager} to save/delete {@link User}s here
 *
 * <ul>
 * <li>Takes care of signup/deleting users
 * <li>Stores Logged-in user</li>
 * </ul>
 */
public class UserLoginManager {
    private UserManager userRepository;
    private User currentUser;
    private PBKDF2WithHmacSHA1Hasher pbkdf2WithHmacSHA1Hasher;

    /**
     * Initializes this UserLogin Manager.
     *
     * @param userRepository The {@code UserManager} that stores all the users.
     */
    public UserLoginManager(UserManager userRepository) {
        this.userRepository = userRepository;
        this.pbkdf2WithHmacSHA1Hasher = new PBKDF2WithHmacSHA1Hasher();
    }

    /**
     * Sets the logged-in {@link User}.
     *
     * @param user The current logged-in {@link User}.
     */
    public void setUser(User user) {
        this.currentUser = user;
    }

    /**
     * Fetches the id of the current logged-in {@link User}.
     *
     * @return The id of the current logged-in {@link User}.
     */
    public String getCurrentUserId() {
        return (currentUser == null) ? "" : currentUser.getId();
    }

    /**
     * Checks if there is a current logged-in {@link User}.
     *
     * @return {@code true} if there exists a current logged-in {@link User}.
     */
    public boolean hasCurrentUser() {
        return !this.getCurrentUserId().equals("");
    }

    /**
     * Fetches the user repository.
     *
     * @return The user repository.
     */
    public UserManager getUserRepository() {
        return userRepository;
    }

    /**
     * Generate a string representing an account password to be stored.
     *
     * <strong>Code from:</strong>
     * @see <a href="https://bit.ly/2VfAWIj">https://bit.ly/2VfAWIj</a>
     * @param plainText Plaintext password from account creation
     * @return a {@code String} PBKDF2WithHmacSHA1 hashed password
     */
    public String hashPassword(String plainText) {
        return pbkdf2WithHmacSHA1Hasher.generateStrongPasswordHash(plainText);
    }

    /**
     * Verifies computed hash from plaintext (during login request) with
     * {@code passwordHash} in {@link User}.
     *
     * <strong>Code from:</strong>
     * @see <a href="https://bit.ly/2VfAWIj">https://bit.ly/2VfAWIj</a>
     * @param plainText Plaintext password from login request
     * @param passwordHash The {@link User}'s stored {@code passwordHash}
     * @return {@code true} if the password matches using PBKDF2WithHmacSHA1
     */
    public boolean checkPassword(String plainText, String passwordHash) {
        return pbkdf2WithHmacSHA1Hasher.validatePassword(plainText, passwordHash);
    }


    /**
     * Deletes the given {@link User} from the repository.
     *
     * @param user
     *          the {@link User} to delete
     * @throws UserNotFoundException if the {@code User} was not found
     */
    public void deleteUser(User user) throws UserNotFoundException {
        if (userRepository.getUserById(user.getId()).orElse(null) == null) {
            throw new UserNotFoundException("User@" + user.getId() + " was not found!");
        }
        userRepository.delete(user);
    }


    /**
     * A helper to persist a {@link User} with the information given into the repository.
     *
     * @param email The email of this {@code User}
     * @param plainTextPassword The password (as plaintext) of this {@code User}
     * @param firstName The first name of this {@code User}
     * @param lastName The last name of this {@code User}
     * @param roles The roles of this {@code User}
     * @return {@code true} if {@code User} was saved successfully
     */
    private boolean saveUser(String email, String plainTextPassword,
                             String firstName, String lastName, Role... roles) {
        if (userRepository.getUserByEmail(email).orElse(null) != null) {
            return false;
        }

        userRepository.save(userRepository.createNew(email,
                hashPassword(plainTextPassword), firstName, lastName, roles));
        return true;
    }

    /**
     * Persists a {@link User} with the information given into the repository if the {@code User}
     * we wish to create does not have SPEAKER {@link Role}.
     *
     *
     * @param email The email of this {@code User}
     * @param plainTextPassword The password (as plaintext) of this {@code User}
     * @param firstName The first name of this {@code User}
     * @param lastName The last name of this {@code User}
     * @param rolesOfUser The roles of this {@code User}
     * @return {@code true} if {@code User} was saved successfully
     */
    public boolean signUp(String email, String plainTextPassword,
                          String firstName, String lastName, Role... rolesOfUser) {
        List<Role> roles = Arrays.asList(rolesOfUser);
        if (roles.contains(Role.SPEAKER) || roles.contains(Role.ORGANIZER)) {
            return false;
        }

        return saveUser(email, plainTextPassword, firstName, lastName, rolesOfUser);
    }

    /**
     * Persists the given {@link User} into repository only if the logged in user is an organizer.
     * This means they can save a {@link User} with any role in {@link Role}.
     *
     * @param currentUser The current {@code User} signed in
     * @param email The email of this {@code User}
     * @param plainTextPassword The password (as plaintext) of this {@code User}
     * @param firstName The first name of this {@code User}
     * @param lastName The last name of this {@code User}
     * @param roles The roles of this {@code User}
     * @return {@code true} if {@code User} was saved successfully by the logged in user
     */
    public boolean saveUserIfOrganizer(User currentUser, String email, String plainTextPassword,
                                       String firstName, String lastName, Role... roles)  {
        currentUser.hasAnyOneOfRoles(Role.ORGANIZER);

        return saveUser(email, plainTextPassword, firstName, lastName, roles);
    }

    /**
     * Logs in a {@link User} to the program.
     *
     * <p>
     *     Returns true if the {@link User} exists and they entered the correct password. If the {@link User} doesn't
     *     exist, throw an exception.
     * </p>
     * @param email the email {@code User} logged in with
     * @param plainTextPassword the password {@code User} logged in with
     * @return {@code true} if the information {@code User} logged in with is correct, if {@code User} exists
     * @throws UserNotFoundException
     *              if no {@link User} in the repository has this id
     */
    public boolean logInUser(String email, String plainTextPassword) throws UserNotFoundException {
        User user = userRepository.getUserByEmailOrThrow(email);
        return checkPassword(plainTextPassword, user.getPasswordHash());
    }
}
