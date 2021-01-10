package backend.data.entity;

import com.conference.backend.conference_and_rooms.entities.ConferenceEvent;
import com.conference.backend.data.utils.Role;
import com.conference.backend.conference_and_rooms.entities.Talk;
import com.conference.backend.users.User;
import static org.junit.Assert.*;
import org.junit.Test;

public class UserIT {

    @Test
    public void equalsCompareWithFirstNameNotEqualAndIdNotEqualThenMakeEqual() {
        User u1 = new User();
        u1.setId("1");
        u1.setPasswordHash("hashmonkey");
        u1.setEmail("abc@gmail.com");
        u1.setFirstName("first");
        u1.setLastName("last");
        u1.addRole(Role.ATTENDEE);

        User u2 = new User();
        u2.setId("2");
        u2.setPasswordHash("hashpotato");
        u2.setEmail("abc@gmail.com");
        u2.setFirstName("anotherName");
        u2.setLastName("last");
        u2.addRole(Role.ATTENDEE);

        assertFalse(u1.equals(u2));

        u2.setFirstName("first");
        u2.setId(u1.getId());
        assertTrue(u1.equals(u2));
    }

    @Test
    public void getFirstNameAndCompareTwoEqualFirstNames() {
        User u1 = new User();
        u1.setId("1");
        u1.setPasswordHash("hashmonkey");
        u1.setEmail("abc@gmail.com");
        u1.setFirstName("first");
        u1.setLastName("last");
        u1.addRole(Role.ATTENDEE);

        assertTrue(u1.getFirstName().equals("first"));
    }

    @Test
    public void getLastNameAndCompareTwoEqualLastNames() {
        User u1 = new User();
        u1.setId("1");
        u1.setPasswordHash("hashmonkey");
        u1.setEmail("abc@gmail.com");
        u1.setFirstName("first");
        u1.setLastName("last");
        u1.addRole(Role.ATTENDEE);

        assertTrue(u1.getLastName().equals("last"));
    }

    @Test
    public void getEmailAndCompareTwoEqualEmails() {
        User u1 = new User();
        u1.setId("1");
        u1.setPasswordHash("hashmonkey");
        u1.setEmail("abc@gmail.com");
        u1.setFirstName("first");
        u1.setLastName("last");
        u1.addRole(Role.ATTENDEE);

        assertTrue(u1.getEmail().equals("abc@gmail.com"));
    }

    @Test
    public void getPasswordHashAndCompareTwoEqualPasswords() {
        User u1 = new User();
        u1.setId("1");
        u1.setPasswordHash("hashmonkey");
        u1.setEmail("abc@gmail.com");
        u1.setFirstName("first");
        u1.setLastName("last");
        u1.addRole(Role.ATTENDEE);

        assertTrue(u1.getPasswordHash().equals("hashmonkey"));
    }

    @Test
    public void checkIfUserRolesContainsOrganizerRole() {
        User u1 = new User();
        u1.setId("1");
        u1.setPasswordHash("hashmonkey");
        u1.setEmail("abc@gmail.com");
        u1.setFirstName("first");
        u1.setLastName("last");
        u1.addRole(Role.ORGANIZER);

        assertTrue(u1.getRoles().contains(Role.ORGANIZER));
    }

    @Test
    public void getConferenceEventsOfUserIfUserHasConferenceEvent() {
        User u1 = new User();
        u1.setId("1");
        u1.setPasswordHash("hashmonkey");
        u1.setEmail("abc@gmail.com");
        u1.setFirstName("first");
        u1.setLastName("last");
        u1.addRole(Role.ORGANIZER);

        ConferenceEvent c = new Talk("Monkey Event", 5);
        u1.addConferenceEventToConferenceEvents(c.getId());

        assertTrue(u1.getConferenceEvents().contains(c.getId()));
    }

    @Test
    public void getContactsListOfUserIfUserHasContactsList() {
        User u1 = new User();
        u1.setId("1");
        u1.setPasswordHash("hashmonkey");
        u1.setEmail("abc@gmail.com");
        u1.setFirstName("first");
        u1.setLastName("last");
        u1.addRole(Role.ORGANIZER);

        User u2 = new User();
        u1.setId("2");

        u1.addContactToContacts(u2.getId());

        assertTrue(u1.getContactsList().contains(u2.getId()));
    }

    @Test
    public void addContactToContacts() {
        User u1 = new User();
        u1.setId("1");
        User u2 = new User();
        u2.setId("2");
        User u3 = new User();
        u3.setId("3");
        User u4 = new User();
        u4.setId("3");

        assertTrue(u1.addContactToContacts(u2.getId()));
        assertTrue(u1.addContactToContacts(u3.getId()));
        assertFalse(u1.addContactToContacts(u4.getId()));
        assertTrue(u1.getContactsList().size() == 2);
    }

    @Test
    public void addConferenceEventToConferenceEvents() {
        User u1 = new User();

        ConferenceEvent c1 = new Talk("Talk", 2);
        c1.setId("c1");
        ConferenceEvent c2 = new Talk("Talk", 2);
        c2.setId("c2");
        ConferenceEvent c3 = new Talk("Talk", 2);
        c3.setId("c2");

        assertTrue(u1.addConferenceEventToConferenceEvents(c1.getId()));
        assertTrue(u1.addConferenceEventToConferenceEvents(c2.getId()));
        assertFalse(u1.addConferenceEventToConferenceEvents(c3.getId()));
        assertTrue(u1.getConferenceEvents().size() == 2);
    }
}
