package backend.data.entity;

import com.conference.backend.data.utils.*;

import static org.junit.Assert.*;

import com.conference.backend.conference_and_rooms.entities.Talk;
import com.conference.backend.users.User;
import org.junit.Test;

import java.util.*;

/**
 * Tests for {@link Talk}
 *
 */

public class TalkIT {

    @Test
    public void getEventNameTest() {
        Talk e1 = new Talk("TedTalk", 2);
        assertTrue(e1.getEventName().equals("TedTalk"));

    }

    @Test
    public void attendeeListTests() {
        Talk e1 = new Talk("TedTalk", 2);
        User attendee1 = new User();
        attendee1.setId("1");
        User attendee2 = new User();
        attendee2.setId("2");
        e1.addAttendee(attendee1.getId());
        e1.addAttendee(attendee2.getId());
        assertTrue(e1.getAttendeeList().contains(attendee1.getId()));
        assertTrue(e1.getAttendeeList().contains(attendee2.getId()));
        e1.removeAttendee(attendee2.getId());
        assertTrue(e1.getAttendeeList().contains(attendee1.getId()));
        assertFalse(e1.getAttendeeList().contains(attendee2.getId()));
        // Setting a new list
        User attendee3 = new User();
        attendee3.setId("3");
        ArrayList newList = new ArrayList<String>();
        newList.add(attendee3.getId());
        e1.setAttendeeList(newList);
        assertTrue(e1.getAttendeeList().contains(attendee3.getId()));
        assertFalse(e1.getAttendeeList().contains(attendee1.getId()));
    }

    @Test
    public void capacityTests() {
        Talk e1 = new Talk("TedTalk", 2);
        assertEquals(e1.getCapacity(), 2);
        User attendee1 = new User();
        attendee1.setId("1");
        e1.addAttendee(attendee1.getId());
        assertTrue(e1.hasCapacityForAttendees());
        e1.setCapacity(1);
        assertFalse(e1.hasCapacityForAttendees());
    }

    @Test
    public void speakerTests() {
        Talk e1 = new Talk("TedTalk", 2);
        assertEquals("", e1.getSpeakerIds().get(0));
        User speaker1 = new User();
        speaker1.setId("1");
        assertTrue(speaker1.addRole(Role.SPEAKER));
        assertTrue(e1.addSpeakerId("1"));
        assertEquals("1", e1.getSpeakerIds().get(0));
        User speaker2 = new User();
        speaker2.setId("2");
        assertTrue(speaker2.addRole(Role.SPEAKER));
        assertFalse(e1.addSpeakerId("2"));
        assertEquals("1", e1.getSpeakerIds().get(0));

    }

}
