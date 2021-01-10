package backend.data.manager;

import com.conference.backend.conference_and_rooms.entities.ConferenceEvent;
import com.conference.backend.conference_and_rooms.entities.DateInterval;
import com.conference.backend.conference_and_rooms.entities.Room;
import com.conference.backend.conference_and_rooms.entities.Talk;
import com.conference.backend.data.utils.*;
import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;

import java.util.*;

import static org.junit.Assert.*;

import com.conference.backend.conference_and_rooms.managers.DateTimeConferenceEventSorter;
import com.conference.backend.users.User;
import org.junit.Test;

/**
 * Tests for {@link ConferenceEventManager}
 *
 */


public class ConferenceEventManagerIT {

    @Test
    public void AddRoomToConferenceEventManagerTrueWithOrganizerFalseWithAttendeeFalseWhenAlreadyAddedTest() {
        // Setting attributes for instance of ConferenceEventManager
        User u1 = new User("e1", "p1", "f1", "l1", Role.ORGANIZER);
        User u2 = new User("e2", "p2", "f2", "l2", Role.ATTENDEE);
        List<Role> u1Roles = u1.getRoles();
        List<Role> u2Roles = u2.getRoles();

        ConferenceEventManager conf1 = new ConferenceEventManager();
        // addRoom
        assertFalse(conf1.getRoomRepository().addRoom(u2Roles, conf1.getRoomRepository().createRoom("r1", 5)));
        assertTrue(conf1.getRoomRepository().addRoom(u1Roles, conf1.getRoomRepository().createRoom("r1", 5)));
        assertFalse(conf1.getRoomRepository().addRoom(u2Roles, conf1.getRoomRepository().createRoom("r2", 5)));
        assertFalse(conf1.getRoomRepository().addRoom(u1Roles, conf1.getRoomRepository().createRoom("r1", 5)));
        assertTrue(conf1.getRoomRepository().addRoom(u1Roles, conf1.getRoomRepository().createRoom("r2", 5)));
        assertFalse(conf1.getRoomRepository().addRoom(u1Roles, conf1.getRoomRepository().createRoom("r2", 5)));
        for (Room room : conf1.getRoomRepository().getRoomsList()) {
            assertTrue(room.getRoomName().equals("r1") || room.getRoomName().equals("r2"));
        }

    }

    @Test
    public void AddConferenceEventToCEMSuccessWithOrganizerFailureWithAttendeeFailureWhenRepeatedHasEventTrueTest() {
        User u1 = new User("e1", "p1", "f1", "l1", Role.ORGANIZER);
        User u2 = new User("e2", "p2", "f2", "l2", Role.ATTENDEE);
        List<Role> u1Roles = u1.getRoles();
        List<Role> u2Roles = u2.getRoles();

        ConferenceEventManager conf1 = new ConferenceEventManager();
        conf1.getRoomRepository().addRoom(u1Roles, conf1.getRoomRepository().createRoom("r1", 5));
        conf1.getRoomRepository().addRoom(u1Roles, conf1.getRoomRepository().createRoom("r2", 5));

        ConferenceEvent e1 = new Talk("Ted", 2);
        Date start1 = new Date(2020, 11, 3, 17, 0);
        Date end1 = new Date(2020, 11, 3, 20, 0);

        assertFalse(conf1.addEvent(u2Roles, new DateInterval(start1, end1), "r1", e1));
        assertTrue(conf1.addEvent(u1Roles, new DateInterval(start1, end1), "r1", e1));
        assertTrue(conf1.hasEvent("Ted"));
        assertTrue(conf1.getRoomRepository().getRoomsMap().get("r1").getAllEventNames().contains("Ted"));

    }

    @Test
    public void AddEventSameNameSameRoomDiffTimeFailureAddSameConferenceEventFailureDifferentRoomFailureHasEventTest() {
        User u1 = new User("e1", "p1", "f1", "l1", Role.ORGANIZER);
        List<Role> u1Roles = u1.getRoles();

        ConferenceEventManager conf1 = new ConferenceEventManager();
        conf1.getRoomRepository().addRoom(u1Roles, conf1.getRoomRepository().createRoom("r1", 5));
        conf1.getRoomRepository().addRoom(u1Roles, conf1.getRoomRepository().createRoom("r2", 5));

        ConferenceEvent e1 = new Talk("Ted", 2);
        ConferenceEvent e2 = new Talk("Ted", 2);
        Date start1 = new Date(2020, 11, 3, 17, 0);
        Date end1 = new Date(2020, 11, 3, 18, 0);
        Date start2 = new Date(2020, 11, 3, 18, 0);
        Date end2 = new Date(2020, 11, 3, 19, 0);

        assertTrue(conf1.addEvent(u1Roles, new DateInterval(start1, end1), "r1", e1));
        assertFalse(conf1.addEvent(u1Roles, new DateInterval(start2, end2), "r1", e2));
        assertFalse(conf1.addEvent(u1Roles, new DateInterval(start2, end2), "r1", e1));
        assertFalse(conf1.addEvent(u1Roles, new DateInterval(start2, end2), "r2", e1));
        assertTrue(conf1.hasEvent("Ted"));
        assertTrue(conf1.getRoomRepository().getRoomsMap().get("r1").getAllEventNames().contains("Ted"));

    }
    @Test
    public void InterferingOverlappedSameIntervalSameRoomAddEventFailureDiffRoomNonInterferingIntervalSuccessHasEventRoomContainsTest() {
        User u1 = new User("e1", "p1", "f1", "l1", Role.ORGANIZER);
        List<Role> u1Roles = u1.getRoles();

        ConferenceEventManager conf1 = new ConferenceEventManager();
        conf1.getRoomRepository().addRoom(u1Roles, conf1.getRoomRepository().createRoom("r1", 5));
        conf1.getRoomRepository().addRoom(u1Roles, conf1.getRoomRepository().createRoom("r2", 5));

        ConferenceEvent e1 = new Talk("Ted", 2);
        ConferenceEvent e2 = new Talk("TedTalk", 2);
        ConferenceEvent e3 = new Talk("Ted1", 2);
        ConferenceEvent e4 = new Talk("Ted2", 2);
        Date start1 = new Date(2020, 11, 3, 17, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);

        assertTrue(conf1.addEvent(u1Roles, new DateInterval(start1, end1), "r1", e1));

        Date start2 = new Date(2020, 11, 3, 18, 0);
        Date end2 = new Date(2020, 11, 3, 20, 0);

        Date start3 = new Date(2020, 11, 3, 16, 0);
        Date end3 = new Date(2020, 11, 3, 20, 0);
        Date start4 = new Date(2020, 11, 3, 20, 0);
        Date end4 = new Date(2020, 11, 3, 21, 0);

        // e2 interferes with interval of e1
        assertFalse(conf1.addEvent(u1Roles, new DateInterval(start2, end2), "r1", e2));
        assertTrue(conf1.addEvent(u1Roles, new DateInterval(start1, end1), "r2", e2));
        assertTrue(conf1.addEvent(u1Roles, new DateInterval(start4, end4), "r2", e3));
        assertFalse(conf1.addEvent(u1Roles, new DateInterval(start3, end3), "r1", e2));
        assertTrue(conf1.addEvent(u1Roles, new DateInterval(start4, end4), "r1", e4));
        assertTrue(conf1.hasEvent("Ted"));
        assertTrue(conf1.hasEvent("TedTalk"));
        assertTrue(conf1.hasEvent("Ted1"));
        assertTrue(conf1.hasEvent("Ted2"));
        assertTrue(conf1.getRoomRepository().getRoomsMap().get("r1").getAllEventNames().contains("Ted"));
        assertTrue(conf1.getRoomRepository().getRoomsMap().get("r2").getAllEventNames().contains("TedTalk"));
        assertTrue(conf1.getRoomRepository().getRoomsMap().get("r2").getAllEventNames().contains("Ted1"));
        assertTrue(conf1.getRoomRepository().getRoomsMap().get("r1").getAllEventNames().contains("Ted2"));

    }

    @Test
    public void removeEventAttendeeFailureOrganizerSuccessNotAddedEventNonExistingEventFailureTest() {
        User u1 = new User("e1", "p1", "f1", "l1", Role.ORGANIZER);
        User u2 = new User("e2", "p2", "f2", "l2", Role.ATTENDEE);
        List<Role> u1Roles = u1.getRoles();
        List<Role> u2Roles = u2.getRoles();

        ConferenceEventManager conf1 = new ConferenceEventManager();
        conf1.getRoomRepository().addRoom(u1Roles, conf1.getRoomRepository().createRoom("r1", 5));

        ConferenceEvent e1 = new Talk("Ted", 2);
        ConferenceEvent e2 = new Talk("Teddy", 2);

        Date start1 = new Date(2020, 11, 3, 17, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);

        assertTrue(conf1.addEvent(u1Roles, new DateInterval(start1, end1), "r1", e1));
        assertFalse(conf1.removeEvent(u2Roles, "Ted"));
        assertTrue(conf1.hasEvent("Ted"));
        assertTrue(conf1.removeEvent(u1Roles, "Ted"));
        assertTrue(conf1.getConferenceEventsList().isEmpty());
        assertTrue(conf1.getRoomRepository().getRoomsMap().get("r1").getAllEventNames().isEmpty());
        assertFalse(conf1.removeEvent(u1Roles, "Teddy"));
        assertFalse(conf1.removeEvent(u1Roles, "Dog"));

    }

    @Test
    public void removeEventDoesNotRemoveOtherEventsInSameRoomAndDifferentRoomTest() {
        User u1 = new User("e1", "p1", "f1", "l1", Role.ORGANIZER);
        ConferenceEventManager conf1 = new ConferenceEventManager();
        conf1.getRoomRepository().addRoom(u1.getRoles(), conf1.getRoomRepository().createRoom("r1", 5));
        conf1.getRoomRepository().addRoom(u1.getRoles(), conf1.getRoomRepository().createRoom("r2", 5));

        ConferenceEvent e1 = new Talk("Ted", 2);
        ConferenceEvent e2 = new Talk("Teddy", 2);
        ConferenceEvent e3 = new Talk("DogShow", 2);

        Date start1 = new Date(2020, 11, 3, 17, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        Date start2 = new Date(2020, 11, 3, 19, 0);
        Date end2 = new Date(2020, 11, 3, 20, 0);

        assertTrue(conf1.addEvent(u1.getRoles(), new DateInterval(start1, end1), "r1", e1));
        assertTrue(conf1.addEvent(u1.getRoles(), new DateInterval(start1, end1), "r2", e2));
        assertTrue(conf1.addEvent(u1.getRoles(), new DateInterval(start2, end2), "r1", e3));
        assertTrue(conf1.removeEvent(u1.getRoles(), "Ted"));
        List<ConferenceEvent> l1 = conf1.getConferenceEventsList();
        assertTrue(l1.contains(e2));
        assertTrue(l1.contains(e3));
        assertFalse(l1.contains(e1));
        assertTrue(l1.remove(e2));
        assertTrue(l1.remove(e3));
        assertTrue(l1.isEmpty());
        List<String> l2 = conf1.getRoomRepository().getRoomsMap().get("r1").getAllEventNames();
        assertTrue(l2.contains("DogShow"));
        assertTrue(l2.remove("DogShow"));
        assertFalse(l2.contains("Ted"));
        assertTrue(l2.isEmpty());
        List<String> l3 = conf1.getRoomRepository().getRoomsMap().get("r2").getAllEventNames();
        assertTrue(l3.contains("Teddy"));
        assertTrue(l3.remove("Teddy"));
        assertTrue(l3.isEmpty());

    }

    @Test
    public void addSpeakerToEventSpeakerNotSpeakerOrUserNotOrganizerFailureSpeakerIsSpeakerUserOrganizerSuccessTest() {
        User u1 = new User("e1", "p1", "f1", "l1", Role.ATTENDEE);
        User u2 = new User("e2", "p2", "f2", "l2", Role.ORGANIZER);
        User s1 = new User("e1", "p1", "f1", "l1", Role.ATTENDEE);
        User s2 = new User("e1", "p1", "f1", "l1", Role.SPEAKER);
        s1.setId("1");
        s2.setId("2");
        ConferenceEventManager conf1 = new ConferenceEventManager();
        conf1.getRoomRepository().addRoom(u2.getRoles(), conf1.getRoomRepository().createRoom("r1", 5));
        ConferenceEvent e1 = new Talk("Ted", 2);
        ConferenceEvent e2 = new Talk("Ted", 2);

        Date start1 = new Date(2020, 11, 3, 17, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);

        assertTrue(conf1.addEvent(u2.getRoles(), new DateInterval(start1, end1), "r1", e1));
        assertTrue(conf1.addSpeakerToEvent(u2.getRoles(), s2.getRoles(), s2.getId(), "Ted"));

        List<String> l1 = e1.getSpeakerIds();
        assertTrue(l1.contains(s2.getId()));
        l1.remove(s2.getId());
        assertTrue(l1.isEmpty());
        assertTrue(conf1.removeEvent(u2.getRoles(), "Ted"));

        conf1.addEvent(u2.getRoles(), new DateInterval(start1, end1), "r1", e2);
        assertFalse(conf1.addSpeakerToEvent(u1.getRoles(), s2.getRoles(), s2.getId(),"Ted"));
        assertFalse(conf1.getConferenceEvent("Ted").getSpeakerIds().contains(s2.getId()));
        assertFalse(conf1.addSpeakerToEvent(u2.getRoles(), s1.getRoles(), s1.getId(),"Ted"));
        assertFalse(conf1.getConferenceEvent("Ted").getSpeakerIds().contains(s1.getId()));
        assertFalse(conf1.addSpeakerToEvent(u1.getRoles(), s1.getRoles(), s1.getId(),"Ted"));
        List<String> l2 = conf1.getConferenceEvent("Ted").getSpeakerIds();
        assertTrue(l2.contains(""));
        l2.remove("");
        assertTrue(l2.isEmpty());

    }

    @Test
    public void addSpeakerToEventSameSpeakerAndSpeakerAlreadyExistsFailure() {
        User u1 = new User("e2", "p2", "f2", "l2", Role.ORGANIZER);
        User s1 = new User("e1", "p1", "f1", "l1", Role.SPEAKER);
        User s2 = new User("e1", "p1", "f1", "l1", Role.SPEAKER);
        s1.setId("1");
        s2.setId("2");
        ConferenceEventManager conf1 = new ConferenceEventManager();
        conf1.getRoomRepository().addRoom(u1.getRoles(), conf1.getRoomRepository().createRoom("r1", 5));
        ConferenceEvent e1 = new Talk("Ted", 2);

        Date start1 = new Date(2020, 11, 3, 17, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);

        assertTrue(conf1.addEvent(u1.getRoles(), new DateInterval(start1, end1), "r1", e1));
        assertTrue(conf1.addSpeakerToEvent(u1.getRoles(), s1.getRoles(), s1.getId(),"Ted"));
        assertTrue(e1.getSpeakerIds().contains(s1.getId()));

        assertFalse(conf1.addSpeakerToEvent(u1.getRoles(), s1.getRoles(), s1.getId(),"Ted"));
        List<String> l1 = e1.getSpeakerIds();
        assertTrue(l1.remove(s1.getId()));
        assertTrue(l1.isEmpty());
        assertFalse(conf1.addSpeakerToEvent(u1.getRoles(), s2.getRoles(), s2.getId(),"Ted"));
        assertTrue(e1.getSpeakerIds().contains(s1.getId()));
        List<String> l2 = e1.getSpeakerIds();
        assertTrue(l2.remove(s1.getId()));
        assertTrue(l2.isEmpty());

    }

    @Test
    public void addSpeakerToEventsOverlappingFailureDeleteEventSuccessNonConflictingSuccessTest() {
        User u1 = new User("e2", "p2", "f2", "l2", Role.ORGANIZER);
        User s1 = new User("e1", "p1", "f1", "l1", Role.SPEAKER);
        s1.setId("1");
        ConferenceEventManager conf1 = new ConferenceEventManager();
        conf1.getRoomRepository().addRoom(u1.getRoles(), conf1.getRoomRepository().createRoom("r1", 5));
        conf1.getRoomRepository().addRoom(u1.getRoles(), conf1.getRoomRepository().createRoom("r2", 5));
        ConferenceEvent e1 = new Talk("Ted", 2);
        ConferenceEvent e2 = new Talk("Ted1", 2);
        ConferenceEvent e3 = new Talk("Ted2", 2);

        Date start1 = new Date(2020, 11, 3, 17, 0);
        Date end1 = new Date(2020, 11, 3, 18, 0);

        Date start2 = new Date(2020, 11, 3, 18, 0);
        Date end2 = new Date(2020, 11, 3, 19, 0);


        assertTrue(conf1.addEvent(u1.getRoles(), new DateInterval(start1, end1), "r1", e1));
        assertTrue(conf1.addEvent(u1.getRoles(), new DateInterval(start1, end1), "r2", e2));
        assertTrue(conf1.addEvent(u1.getRoles(), new DateInterval(start2, end2), "r2", e3));
        assertTrue(conf1.addSpeakerToEvent(u1.getRoles(), s1.getRoles(), s1.getId(),"Ted"));
        assertTrue(conf1.addSpeakerToEvent(u1.getRoles(), s1.getRoles(), s1.getId(),"Ted2"));
        assertTrue(e3.getSpeakerIds().contains(s1.getId()));
        assertFalse(conf1.addSpeakerToEvent(u1.getRoles(), s1.getRoles(), s1.getId(), "Ted1"));
        assertFalse(e2.getSpeakerIds().contains(s1.getId()));
        assertTrue(conf1.removeEvent(u1.getRoles(), "Ted"));
        assertTrue(conf1.addSpeakerToEvent(u1.getRoles(), s1.getRoles(), s1.getId(),"Ted1"));
        assertTrue(e2.getSpeakerIds().contains(s1.getId()));
    }

    @Test
    public void getEventsAtIntervalTest() {
        // Setting attributes for instance of ConferenceEventManager
        User u1 = new User("e1", "p1", "f1", "l1", Role.ORGANIZER);
        User u2 = new User("e2", "p2", "f2", "l2", Role.ATTENDEE);

        ConferenceEventManager conf1 = new ConferenceEventManager();
        // addRoom
        assertTrue(conf1.getRoomRepository().addRoom(u1.getRoles(), conf1.getRoomRepository().createRoom("r1", 5)));
        assertTrue(conf1.getRoomRepository().addRoom(u1.getRoles(), conf1.getRoomRepository().createRoom("r2", 5)));
        // Testing getEventsAtInterval with no events in interval
        Date start0 = new Date(2020, 11, 3, 17, 0);
        Date end0 = new Date(2020, 11, 3, 20, 0);
        DateInterval dateInterval0 = new DateInterval(start0, end0);
        assertTrue(conf1.getEventsAtInterval(dateInterval0).isEmpty());
        // Testing getEventsAtInterval with one event in one room in interval
        Date start1 = new Date(2020, 11, 3, 18, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        DateInterval dateInterval1 = new DateInterval(start1, end1);
        ConferenceEvent e1 = new Talk("e1", 2);
        assertTrue(conf1.addEvent(u1.getRoles(), dateInterval1, "r1", e1));
        assertEquals("e1", conf1.getEventsAtInterval(dateInterval0).get(0));
        // Testing getEventsAtInterval with one event in one room in interval, and events not in interval
        Date start2 = new Date(2020, 11, 3, 16, 0);
        Date end2 = new Date(2020, 11, 3, 17, 0);
        DateInterval dateInterval2 = new DateInterval(start2, end2);
        ConferenceEvent e2 = new Talk("e2", 2);
        assertTrue(conf1.addEvent(u1.getRoles(), dateInterval2, "r2", e2));

        Date start3 = new Date(2020, 11, 3, 20, 0);
        Date end3 = new Date(2020, 11, 3, 21, 0);
        DateInterval dateInterval3 = new DateInterval(start3, end3);
        ConferenceEvent e3 = new Talk("e3", 2);
        assertTrue(conf1.addEvent(u1.getRoles(), dateInterval3, "r1", e3));

        assertEquals("e1", conf1.getEventsAtInterval(dateInterval0).get(0));
        // Testing getEventsAtInterval with one event in each of two rooms in interval
        Date start4 = new Date(2020, 11, 3, 18, 0);
        Date end4 = new Date(2020, 11, 3, 19, 0);
        DateInterval dateInterval4 = new DateInterval(start4, end4);
        ConferenceEvent e4 = new Talk("e4", 2);
        assertTrue(conf1.addEvent(u1.getRoles(), dateInterval4, "r2", e4));

        assertTrue(conf1.getEventsAtInterval(dateInterval0).contains("e1"));
        assertTrue(conf1.getEventsAtInterval(dateInterval0).contains("e4"));
        // Testing getEventsAtInterval with part of event's DateInterval in interval
        Date start5 = new Date(2020, 11, 3, 16, 30);
        Date end5 = new Date(2020, 11, 3, 17, 30);
        DateInterval dateInterval5 = new DateInterval(start5, end5);
        ConferenceEvent e5 = new Talk("e5", 2);
        assertTrue(conf1.addEvent(u1.getRoles(), dateInterval5, "r1", e5));

        Date start6 = new Date(2020, 11, 3, 19, 30);
        Date end6 = new Date(2020, 11, 3, 20, 30);
        DateInterval dateInterval6 = new DateInterval(start6, end6);
        ConferenceEvent e6 = new Talk("e6", 2);
        assertTrue(conf1.addEvent(u1.getRoles(), dateInterval6, "r2", e6));

        assertTrue(conf1.getEventsAtInterval(dateInterval0).contains("e5"));
        assertTrue(conf1.getEventsAtInterval(dateInterval0).contains("e6"));
        // Testing getEventsAtInterval after an event in time interval is removed
        assertTrue(conf1.removeEvent(u1.getRoles(), "e6"));
        assertFalse(conf1.getEventsAtInterval(dateInterval0).contains("e6"));
    }

    @Test
    public void getRoomByEventNameTest() {
        User u1 = new User("e1", "p1", "f1", "l1", Role.ORGANIZER);

        ConferenceEventManager conf1 = new ConferenceEventManager();
        conf1.getRoomRepository().addRoom(u1.getRoles(), conf1.getRoomRepository().createRoom("r1", 5));
        conf1.getRoomRepository().addRoom(u1.getRoles(), conf1.getRoomRepository().createRoom("r2", 5));

        ConferenceEvent e1 = new Talk("Ted1", 2);
        ConferenceEvent e2 = new Talk("Ted2", 2);
        ConferenceEvent e3 = new Talk("Ted3", 2);

        Date start1 = new Date(2020, 11, 3, 17, 0);
        Date end1 = new Date(2020, 11, 3, 18, 0);
        Date start2 = new Date(2020, 11, 3, 18, 0);
        Date end2 = new Date(2020, 11, 3, 19, 0);

        assertTrue(conf1.addEvent(u1.getRoles(), new DateInterval(start1, end1), "r1", e1));
        assertTrue(conf1.addEvent(u1.getRoles(), new DateInterval(start2, end2), "r1", e2));
        assertTrue(conf1.addEvent(u1.getRoles(), new DateInterval(start2, end2), "r2", e3));

        assertEquals(conf1.getRoomByEventName("Ted1"), conf1.getRoomRepository().getRoomsMap().get("r1"));
        assertEquals(conf1.getRoomByEventName("Ted2"), conf1.getRoomRepository().getRoomsMap().get("r1"));
        assertEquals(conf1.getRoomByEventName("Ted3"), conf1.getRoomRepository().getRoomsMap().get("r2"));
    }

    @Test
    public void getDateByEventNameTest() {
        User u1 = new User("e1", "p1", "f1", "l1", Role.ORGANIZER);

        ConferenceEventManager conf1 = new ConferenceEventManager();
        conf1.getRoomRepository().addRoom(u1.getRoles(), conf1.getRoomRepository().createRoom("r1", 5));
        conf1.getRoomRepository().addRoom(u1.getRoles(), conf1.getRoomRepository().createRoom("r2", 5));

        ConferenceEvent e1 = new Talk("Ted1", 2);
        ConferenceEvent e2 = new Talk("Ted2", 2);
        ConferenceEvent e3 = new Talk("Ted3", 2);

        Date start1 = new Date(2020, 11, 3, 17, 0);
        Date end1 = new Date(2020, 11, 3, 18, 0);
        Date start2 = new Date(2020, 11, 3, 18, 0);
        Date end2 = new Date(2020, 11, 3, 19, 0);

        assertTrue(conf1.addEvent(u1.getRoles(), new DateInterval(start1, end1), "r1", e1));
        assertTrue(conf1.addEvent(u1.getRoles(), new DateInterval(start2, end2), "r1", e2));
        assertTrue(conf1.addEvent(u1.getRoles(), new DateInterval(start2, end2), "r2", e3));

        assertEquals(conf1.getDateByEventName("Ted1"), new DateInterval(start1, end1));
        assertEquals(conf1.getDateByEventName("Ted2"), new DateInterval(start2, end2));
        assertEquals(conf1.getDateByEventName("Ted3"), new DateInterval(start2, end2));
    }

    @Test
    public void getSpeakerEventsTest() {
        User u1 = new User("e1", "p1", "f1", "l1", Role.ORGANIZER);
        User u2 = new User("e1", "p1", "f1", "l1", Role.SPEAKER);
        u1.setId("id1");
        u2.setId("id2");

        ConferenceEventManager conf1 = new ConferenceEventManager();
        conf1.getRoomRepository().addRoom(u1.getRoles(), conf1.getRoomRepository().createRoom("r1", 5));
        conf1.getRoomRepository().addRoom(u1.getRoles(), conf1.getRoomRepository().createRoom("r2", 5));

        ConferenceEvent e1 = new Talk("Ted1", 2);
        ConferenceEvent e2 = new Talk("Ted2", 2);
        ConferenceEvent e3 = new Talk("Ted3", 2);

        Date start1 = new Date(2020, 11, 3, 17, 0);
        Date end1 = new Date(2020, 11, 3, 18, 0);
        Date start2 = new Date(2020, 11, 3, 18, 0);
        Date end2 = new Date(2020, 11, 3, 19, 0);

        assertTrue(conf1.addEvent(u1.getRoles(), new DateInterval(start1, end1), "r1", e1));
        assertTrue(conf1.addEvent(u1.getRoles(), new DateInterval(start2, end2), "r1", e2));
        assertTrue(conf1.addEvent(u1.getRoles(), new DateInterval(start2, end2), "r2", e3));

        // speaking at no events
        assertEquals(conf1.getSpeakerEvents(u2.getId()), new ArrayList<>());

        // speaking at 1 event
        assertTrue(conf1.addSpeakerToEvent(u1.getRoles(), u2.getRoles(), u2.getId(),e3.getEventName()));
        List<ConferenceEvent> speakerEventList1 = new ArrayList<>();
        speakerEventList1.add(e3);
        assertEquals(conf1.getSpeakerEvents(u2.getId()), speakerEventList1);

        // speaking at multiple events at diff times
        assertFalse(conf1.addSpeakerToEvent(u1.getRoles(), u2.getRoles(), u2.getId(), e2.getEventName()));
        assertTrue(conf1.addSpeakerToEvent(u1.getRoles(), u2.getRoles(), u2.getId(), e1.getEventName()));
        speakerEventList1.add(e1);
        assertEquals(conf1.getSpeakerEvents(u2.getId()), speakerEventList1);

        // removed an event, see if Speaker's event list updated
        assertTrue(conf1.removeEvent(u1.getRoles(), e3.getEventName()));
        speakerEventList1.remove(e3);
        assertEquals(conf1.getSpeakerEvents(u2.getId()), speakerEventList1);

        // speaker added to another event
        assertTrue(conf1.addSpeakerToEvent(u1.getRoles(), u2.getRoles(), u2.getId(), e2.getEventName()));
        assertTrue(conf1.getSpeakerEvents(u2.getId()).contains(e1));
        assertTrue(conf1.getSpeakerEvents(u2.getId()).contains(e2));
    }

    @Test
    public void test() {
        User u1 = new User("e1", "p1", "f1", "l1", Role.ORGANIZER);
        User u2 = new User("e1", "p1", "f1", "l1", Role.SPEAKER);
        u1.setId("id1");
        u2.setId("id2");

        ConferenceEventManager conf1 = new ConferenceEventManager();
        conf1.getRoomRepository().addRoom(u1.getRoles(), conf1.getRoomRepository().createRoom("r1", 5));
        conf1.getRoomRepository().addRoom(u1.getRoles(), conf1.getRoomRepository().createRoom("r2", 5));

        ConferenceEvent e1 = new Talk("Ted1", 2);
        ConferenceEvent e2 = new Talk("Ted2", 2);
        ConferenceEvent e3 = new Talk("Ted3", 2);

        Date start1 = new Date(2020, 0, 3, 17, 0);
        Date end1 = new Date(2020, 0, 3, 18, 0);
        Date start2 = new Date(2020, 0, 3, 18, 0);
        Date end2 = new Date(2020, 0, 3, 19, 0);

        conf1.addEvent(u1.getRoles(), new DateInterval(start2, end2), "r1", e1);
        conf1.addEvent(u1.getRoles(), new DateInterval(start1, end1), "r1", e2);
        conf1.addEvent(u1.getRoles(), new DateInterval(start2, end2), "r2", e3);

        DateTimeConferenceEventSorter dt = new DateTimeConferenceEventSorter();
        List<ConferenceEvent> ces = conf1.getConferenceEventsList();

        assertEquals(Arrays.asList(e2.getEventName(), e3.getEventName(), e1.getEventName()), dt.sort(ces, conf1));

        Map<String, List<String>> map = dt.splitByDay(Arrays.asList(e3.getEventName(), e1.getEventName(), e2.getEventName()), conf1);
        System.out.println(map);
//        System.out.println(new Date());
    }


}