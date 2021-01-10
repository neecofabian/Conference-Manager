package backend.data.manager;

import com.conference.backend.users.UserLoginManager;
import com.conference.backend.data.utils.Role;
import com.conference.backend.users.User;
import com.conference.backend.exception.UserNotFoundException;
import com.conference.backend.users.UserManager;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link UserManager} and {@link UserLoginManager}
 *
 */
public class UserManagerAndUserLoginManagerIT {

    private UserManager userRepository;
    private UserLoginManager userLoginManager;

    @Before
    public void setUp() {
        userRepository = new UserManager();
        userLoginManager = new UserLoginManager(userRepository);
    }

    @Test
    public void saveAndDeleteUserDoesNotThrowExceptionIfUserManagerSavedAndDeleted() {
        User u1 = new User();
        u1.setId("1");
        u1.setPasswordHash("hashmonkey");
        u1.setEmail("abc@gmail.com");
        u1.setFirstName("first");
        u1.setLastName("last");
        u1.addRole(Role.ATTENDEE);

        // Add this line to check that UserNotFoundException is thrown
        // um.deleteUser(u1);
        userRepository.save(u1);
        userRepository.delete(u1);
    }


    @Test
    public void getUserByIdOrThrowWithIdDoesNotThrowExceptionIfExistingInUserManager()
            throws UserNotFoundException {
        userLoginManager.signUp("abc@gmail.com", "hashmonkey", "first", "last",
                Role.ATTENDEE);

        userRepository.getUserByIdOrThrow(userRepository.getUsers().get(0).getId());
    }

    @Test
    public void getUserByEmailOrThrowWithEmailDoesNotThrowExceptionIfExistingInUserManager()
            throws UserNotFoundException {
        userLoginManager.signUp("abc@gmail.com", "hashmonkey", "first", "last",
                Role.ATTENDEE);
        userRepository.getUsers().get(0).setId("1");

        userRepository.getUserByEmailOrThrow("abc@gmail.com");
    }

    @Test
    public void getUsersWithAttendeeRoleIfAllUsersAreAttendeesExceptOneAndIncludeMultipleRoles()
            throws UserNotFoundException {
        User organizer = new User();
        organizer.addRole(Role.ORGANIZER);

        assertTrue(userLoginManager.saveUserIfOrganizer(organizer,
                "abc@gmail.com", "hashmonkey", "first", "last",
                Role.ORGANIZER));
        String a = userRepository.getUserByEmailOrThrow("abc@gmail.com").getId();

        assertTrue(userLoginManager.saveUserIfOrganizer(organizer,
                "abcd@gmail.com", "hashmonkey", "first", "last",
                Role.ATTENDEE, Role.ORGANIZER));
        String b = userRepository.getUserByEmailOrThrow("abcd@gmail.com").getId();

        assertTrue(userLoginManager.signUp(
                "abcde@gmail.com", "hashmonkey", "first", "last",
                Role.ATTENDEE));
        String c = userRepository.getUserByEmailOrThrow("abcde@gmail.com").getId();

        List<String> usersWithRole = userRepository.getUsersWithRole(Role.ORGANIZER).stream()
                .map(User::getId)
                .collect(Collectors.toList());

        List<String> result = Arrays.asList(a, b);

        Collections.sort(result);
        Collections.sort(usersWithRole);

        assertEquals(result, usersWithRole);
    }


    @Test
    public void getUsersWithOrganizerRoleButNotAttendeeIfAllOrganizersButOne() throws
            UserNotFoundException {

        User organizer = new User();
        organizer.addRole(Role.ORGANIZER);

        userLoginManager.saveUserIfOrganizer(organizer, "abc@gmail.com", "hashmonkey", "first", "last",
                Role.ORGANIZER);
        String a = userRepository.getUserByEmailOrThrow("abc@gmail.com").getId();

        userLoginManager.saveUserIfOrganizer(organizer, "abcd@gmail.com", "hashmonkey", "first", "last",
                Role.ATTENDEE, Role.ORGANIZER);
        String b = userRepository.getUserByEmailOrThrow("abcd@gmail.com").getId();


        userLoginManager.saveUserIfOrganizer(userRepository.getUsers().get(1),
                "abcde@gmail.com", "hashmonkey", "first", "last", Role.SPEAKER,
                Role.ORGANIZER, Role.ATTENDEE);
        String c = userRepository.getUserByEmailOrThrow("abcde@gmail.com").getId();


        userLoginManager.saveUserIfOrganizer(organizer, "abcdef@gmail.com", "hashmonkey", "first", "last",
                Role.ORGANIZER);
        String d = userRepository.getUserByEmailOrThrow("abcdef@gmail.com").getId();

        List<String> usersWithOnlyOrganizerRole = userRepository
                .getUsersWithRoleAndFilterRoles(Role.ORGANIZER, Role.ATTENDEE, Role.SPEAKER).stream()
                .map(User::getId)
                .collect(Collectors.toList());

        List<String> result = Arrays.asList(a, d);

        Collections.sort(result);
        Collections.sort(usersWithOnlyOrganizerRole);

        assertEquals(result, usersWithOnlyOrganizerRole);
    }
}
