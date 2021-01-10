package backend.data.entity;

import com.conference.backend.conference_and_rooms.entities.ConferenceEvent;
import com.conference.backend.conference_and_rooms.entities.DateInterval;
import com.conference.backend.conference_and_rooms.entities.Room;
import static org.junit.Assert.*;

import com.conference.backend.conference_and_rooms.entities.Talk;
import org.junit.Test;

import java.util.*;

/**
 * Tests for {@link Room}
 *
 */

public class RoomIT {

    @Test
    public void getRoomNameTest() {
        // Setting attributes for instance of Room
        Map<String, DateInterval> room1Events = new HashMap<String, DateInterval>();
        ConferenceEvent event1 = new Talk("E1", 2);
        Date start1 = new Date(2020, 11, 3, 18, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        DateInterval date1 = new DateInterval(start1, end1);
        room1Events.put("E1", date1);
        Room r1 = new Room("Room 1", room1Events, 5);
        assertTrue(r1.getRoomName().equals("Room 1"));

    }

    @Test
    public void getEventToDateTest() {
        // Setting attributes for instance of Room
        Map<String, DateInterval> room1Events = new HashMap<String, DateInterval>();
        ConferenceEvent event1 = new Talk("E1", 2);
        Date start1 = new Date(2020, 11, 3, 18, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        DateInterval date1 = new DateInterval(start1, end1);
        room1Events.put("E1", date1);
        Room r1 = new Room("Room 1", room1Events, 5);
        assertTrue(r1.getEventToDate().equals(room1Events));

    }

    @Test
    public void getAllEventIDsTest() {
        // Setting attributes for instance of Room
        Map<String, DateInterval> room1Events = new HashMap<String, DateInterval>();
        ConferenceEvent event1 = new Talk("E1", 2);
        Date start1 = new Date(2020, 11, 3, 18, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        DateInterval date1 = new DateInterval(start1, end1);
        room1Events.put("E1", date1);
        Room r1 = new Room("Room 1", room1Events, 5);
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("E1");
        assertTrue(r1.getAllEventNames().equals(list1));

    }

    @Test
    public void addEventSameTimeEvent() {
        // Setting attributes for instance of Room
        Map<String, DateInterval> room1Events = new HashMap<String, DateInterval>();
        ConferenceEvent event1 = new Talk("E1", 2);
        Date start1 = new Date(2020, 11, 3, 18, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        DateInterval date1 = new DateInterval(start1, end1);
        room1Events.put("E1", date1);
        Room r1 = new Room("Room 1", room1Events, 5);
        ConferenceEvent event2 = new Talk("P2", 2);
        assertFalse(r1.addEvent(new DateInterval(start1, end1), event2.getEventName()));

    }

    @Test
    public void addEventEventEncompassesExistingEventTest() {
        // Setting attributes for instance of Room
        Map<String, DateInterval> room1Events = new HashMap<String, DateInterval>();
        ConferenceEvent event1 = new Talk("E1", 2);
        Date start1 = new Date(2020, 11, 3, 18, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        DateInterval date1 = new DateInterval(start1, end1);
        room1Events.put("E1", date1);
        Room r1 = new Room("Room 1", room1Events, 5);
        ConferenceEvent event2 = new Talk("P2", 2);

        Date start2 = new Date(2020, 11, 3, 17, 0);
        Date end2 = new Date(2020, 11, 3, 20, 0);
        assertFalse(r1.addEvent(new DateInterval(start2, end2), event2.getEventName()));

    }

    @Test
    public void addEventNonConflictingEventTest() {
        // Setting attributes for instance of Room
        Map<String, DateInterval> room1Events = new HashMap<String, DateInterval>();
        ConferenceEvent event1 = new Talk("E1", 2);
        Date start1 = new Date(2020, 11, 3, 18, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        DateInterval date1 = new DateInterval(start1, end1);
        room1Events.put("E1", date1);
        Room r1 = new Room("Room 1", room1Events, 5);
        ConferenceEvent event2 = new Talk("P2", 2);
        Date start3 = new Date(2020, 11, 3, 19, 0);
        Date end3 = new Date(2020, 11, 3, 20, 0);
        assertTrue(r1.addEvent(new DateInterval(start3, end3), event2.getEventName()));

    }

    @Test
    public void addEventSecondEventStartsInFirstEventTest() {
        // Setting attributes for instance of Room
        Map<String, DateInterval> room1Events = new HashMap<String, DateInterval>();
        ConferenceEvent event1 = new Talk("E1", 2);
        Date start1 = new Date(2020, 11, 3, 18, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        DateInterval date1 = new DateInterval(start1, end1);
        Room r1 = new Room("Room 1", room1Events, 5);
        assertTrue(r1.addEvent(date1, "E1"));
        ConferenceEvent event2 = new Talk("P2", 2);
        Date start3 = new Date(2020, 11, 3, 18, 30);
        Date end3 = new Date(2020, 11, 3, 20, 0);
        assertFalse(r1.addEvent(new DateInterval(start3, end3), event2.getEventName()));

    }

    @Test
    public void addEventSecondEventEndsInFirstEventTest() {
        // Setting attributes for instance of Room
        Map<String, DateInterval> room1Events = new HashMap<String, DateInterval>();
        ConferenceEvent event1 = new Talk("E1", 2);
        Date start1 = new Date(2020, 11, 3, 18, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        DateInterval date1 = new DateInterval(start1, end1);
        room1Events.put("E1", date1);
        Room r1 = new Room("Room 1", room1Events, 5);
        ConferenceEvent event2 = new Talk("P2", 2);
        Date start3 = new Date(2020, 11, 3, 17, 0);
        Date end3 = new Date(2020, 11, 3, 18, 30);
        assertFalse(r1.addEvent(new DateInterval(start3, end3), event2.getEventName()));

    }

    @Test
    public void addEventSecondEventEntirelyDuringFirstEventTest() {
        // Setting attributes for instance of Room
        Map<String, DateInterval> room1Events = new HashMap<String, DateInterval>();
        ConferenceEvent event1 = new Talk("E1", 2);
        Date start1 = new Date(2020, 11, 3, 18, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        DateInterval date1 = new DateInterval(start1, end1);
        room1Events.put("E1", date1);
        Room r1 = new Room("Room 1", room1Events, 5);
        ConferenceEvent event2 = new Talk("P2", 2);
        Date start3 = new Date(2020, 11, 3, 18, 0);
        Date end3 = new Date(2020, 11, 3, 19, 0);
        assertFalse(r1.addEvent(new DateInterval(start3, end3), event2.getEventName()));

    }

    @Test
    public void addEventFirstSecondThirdConsecutiveTimesSuccess() {
        // Setting attributes for instance of Room
        Map<String, DateInterval> room1Events = new HashMap<String, DateInterval>();
        ConferenceEvent event1 = new Talk("E1", 2);
        Date start1 = new Date(2020, 11, 3, 18, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        Date start2 = new Date(2020, 11, 3, 19, 0);
        Date end2 = new Date(2020, 11, 3, 20, 0);
        DateInterval date1 = new DateInterval(start1, end1);
        Room r1 = new Room("Room 1", room1Events, 5);
        assertTrue(r1.addEvent(date1, event1.getEventName()));
        ConferenceEvent event2 = new Talk("P2", 2);
        ConferenceEvent event3 = new Talk("E3", 2);
        assertTrue(r1.addEvent(new DateInterval(start2, end2), event2.getEventName()));
        Date start3 = new Date(2020, 11, 3, 20, 0);
        Date end3 = new Date(2020, 11, 3, 21, 0);
        assertTrue(r1.addEvent(new DateInterval(start3, end3), event3.getEventName()));


    }

    @Test
    public void addEventThenGetAllEventIdsTest() {
        // Setting attributes for instance of Room
        Map<String, DateInterval> room1Events = new HashMap<String, DateInterval>();
        ConferenceEvent event1 = new Talk("E1", 2);
        Date start1 = new Date(2020, 11, 3, 18, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        DateInterval date1 = new DateInterval(start1, end1);
        room1Events.put("E1", date1);
        Room r1 = new Room("Room 1", room1Events, 5);
        ConferenceEvent event2 = new Talk("P2", 2);
        Date start3 = new Date(2020, 11, 3, 19, 0);
        Date end3 = new Date(2020, 11, 3, 20, 0);
        assertTrue(r1.addEvent(new DateInterval(start3, end3), event2.getEventName()));
        ArrayList<String> list2 = new ArrayList<>();
        list2.add("E1");
        list2.add("P2");
        Collections.sort(list2);
        assertTrue(r1.getAllEventNames().equals(list2));

    }

    @Test
    public void removeEventThenGetAllEventIdsTest() {
        // Setting attributes for instance of Room
        Map<String, DateInterval> room1Events = new HashMap<String, DateInterval>();
        ConferenceEvent event1 = new Talk("E1", 2);
        Date start1 = new Date(2020, 11, 3, 18, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        DateInterval date1 = new DateInterval(start1, end1);
        room1Events.put("E1", date1);
        Room r1 = new Room("Room 1", room1Events, 5);
        ConferenceEvent event2 = new Talk("P2", 2);
        Date start3 = new Date(2020, 11, 3, 19, 0);
        Date end3 = new Date(2020, 11, 3, 20, 0);
        assertTrue(r1.addEvent(new DateInterval(start3, end3), event2.getEventName()));
        r1.removeEvent("E1");
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("P2");
        assertTrue(r1.getAllEventNames().equals(list1));
        assertFalse(r1.removeEvent("E2"));
        assertFalse(r1.removeEvent("E1"));
        assertTrue(r1.removeEvent("P2"));
    }

    @Test
    public void toStringTest() {
        // Setting attributes for instance of Room
        Map<String, DateInterval> room1Events = new HashMap<String, DateInterval>();
        ConferenceEvent event1 = new Talk("E1", 2);
        Date start1 = new Date(2020, 11, 3, 18, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        DateInterval date1 = new DateInterval(start1, end1);
        room1Events.put("E1", date1);
        Room r1 = new Room("Room 1", room1Events, 5);

        assertTrue(r1.toString().contains(r1.getRoomName()));
        assertTrue(r1.toString().contains(r1.getCapacity() + ""));
        assertTrue(r1.toString().contains(r1.getEventToDate().toString()));
    }

    @Test
    public void getCapacityTest() {
        // Setting attributes for instance of Room
        Map<String, DateInterval> room1Events = new HashMap<String, DateInterval>();
        ConferenceEvent event1 = new Talk("E1", 2);
        Date start1 = new Date(2020, 11, 3, 18, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        DateInterval date1 = new DateInterval(start1, end1);
        room1Events.put("E1", date1);
        Room r1 = new Room("Room 1", room1Events, 5);

        assertEquals(r1.getCapacity(), 5);
    }

}
