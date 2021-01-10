package backend.data.manager;

import com.conference.backend.data.utils.*;
import com.conference.backend.conference_and_rooms.managers.RoomManager;
import com.conference.backend.conference_and_rooms.entities.Room;

import java.util.*;

import static org.junit.Assert.*;

import com.conference.backend.users.User;
import org.junit.Test;

public class RoomManagerIT {

    @Test
    public void addRoomEmptyRoomManagerTest() {
        RoomManager roomManager = new RoomManager();
        User organizer = new User();
        organizer.addRole(Role.ORGANIZER);
        List<Role> roles = organizer.getRoles();
        assertTrue(roomManager.addRoom(roles, roomManager.createRoom("ConferenceRoom", 5)));
    }

    @Test
    public void addRoomRoomNameExistsAssertFalseTest() {
        RoomManager roomManager = new RoomManager();
        User organizer = new User();
        organizer.addRole(Role.ORGANIZER);
        List<Role> roles = organizer.getRoles();
        assertTrue(roomManager.addRoom(roles, roomManager.createRoom("ConferenceRoom", 5)));
        assertFalse(roomManager.addRoom(roles, roomManager.createRoom("ConferenceRoom", 4)));
    }

    @Test
    public void addRoomNonEmptyRoomMapNotSameRoomNameAssertTrue() {
        RoomManager roomManager = new RoomManager();
        User organizer = new User();
        organizer.addRole(Role.ORGANIZER);
        List<Role> roles = organizer.getRoles();
        assertTrue(roomManager.addRoom(roles, roomManager.createRoom("ConferenceRoom", 5)));
        assertTrue(roomManager.addRoom(roles, roomManager.createRoom("ConferenceRoom1", 5)));
    }

    @Test
    public void getRoomsMapOneRoomEntryTest() {
        RoomManager roomManager = new RoomManager();
        User organizer = new User();
        organizer.addRole(Role.ORGANIZER);
        List<Role> roles = organizer.getRoles();
        assertTrue(roomManager.addRoom(roles, roomManager.createRoom("ConferenceRoom", 5)));
        Map<String, Room> roomsMap = roomManager.getRoomsMap();
        Collection<Room> rooms = roomsMap.values();
        for (Room room : rooms) {
            assertTrue(roomsMap.containsKey(room.getRoomName()));
            assertTrue(roomsMap.get(room.getRoomName()).equals(room));
        }
    }
    @Test
    public void getRoomsMapTwoRoomEntriesTest() {
        RoomManager roomManager = new RoomManager();
        User organizer = new User();
        organizer.addRole(Role.ORGANIZER);
        List<Role> roles = organizer.getRoles();
        assertTrue(roomManager.addRoom(roles, roomManager.createRoom("ConferenceRoom", 5)));
        Map<String, Room> roomsMap = roomManager.getRoomsMap();
        Collection<Room> rooms = roomsMap.values();
        for (Room room : rooms) {
            assertTrue(roomsMap.containsKey(room.getRoomName()));
            assertTrue(roomsMap.get(room.getRoomName()).equals(room));
        }
    }

    @Test
    public void hasRoomTwoRoomEntriesTest() {
        RoomManager roomManager = new RoomManager();
        User organizer = new User();
        organizer.addRole(Role.ORGANIZER);
        List<Role> roles = organizer.getRoles();
        assertTrue(roomManager.addRoom(roles, roomManager.createRoom("ConferenceRoom", 5)));
        Map<String, Room> roomsMap = roomManager.getRoomsMap();
        Collection<Room> rooms = roomsMap.values();
        for (Room room : rooms) {
            assertTrue(roomManager.hasRoom(room.getRoomName()));
        }
    }

    @Test
    public void getRoomsListOneRoomTest() {
        RoomManager roomManager = new RoomManager();
        User organizer = new User();
        organizer.addRole(Role.ORGANIZER);
        List<Role> roles = organizer.getRoles();
        assertTrue(roomManager.addRoom(roles, roomManager.createRoom("ConferenceRoom", 5)));
        List<Room> rooms = roomManager.getRoomsList();
        assertTrue(rooms.get(0).getRoomName().equals("ConferenceRoom"));
    }

    @Test
    public void getRoomsMapEmptyMapTest() {
        RoomManager roomManager = new RoomManager();
        Map<String, Room> roomsMap = roomManager.getRoomsMap();
        assertTrue(roomsMap.isEmpty());
    }

    @Test
    public void getRoomsListEmptyTest() {
        RoomManager roomManager = new RoomManager();
        List<Room> roomsList = roomManager.getRoomsList();
        assertTrue(roomsList.isEmpty());
    }

    @Test
    public void hasRoomEmptyMapTest() {
        RoomManager roomManager = new RoomManager();
        assertFalse(roomManager.hasRoom("1"));
        User organizer = new User();
        organizer.addRole(Role.ORGANIZER);
        List<Role> roles = organizer.getRoles();
        assertTrue(roomManager.addRoom(roles, roomManager.createRoom("ConferenceRoom", 5)));
        assertTrue(roomManager.hasRoom("ConferenceRoom"));
    }
}
