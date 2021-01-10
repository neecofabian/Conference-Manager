package com.conference.backend.conference_and_rooms.managers;


import com.conference.backend.conference_and_rooms.entities.Amenity;
import com.conference.backend.data.utils.Role;
import com.conference.backend.conference_and_rooms.entities.Room;
import com.conference.backend.data.utils.base.CrudManager;
import com.conference.backend.users.User;
import com.conference.backend.conference_and_rooms.entities.ConferenceEvent;
import org.apache.commons.io.input.ObservableInputStream;

import java.io.Serializable;
import java.util.*;

/**
 * Backend service to store and retrieve {@link Room} instances.
 * Checks if Room exists
 */

public class RoomManager extends Observable implements Serializable, CrudManager<Room> {
    private static final long serialVersionUID = 2673458726345L;

    private Map<String, Room> rooms;

    public RoomManager() {
        this.rooms = new HashMap<String, Room>();
    }

    /**
     * Adds a {@link Room} to the map of {@code rooms} if there does not exist a Room already stored with this room name
     *
     * @param roles the list of {@link Role} the {@link User} adding the {@link Room}
     * @param room the {@link Room} to be added to the map {@code rooms}
     * @return a {@code true} if the {@link User} successfully added the {@link Room}
     */
    public boolean addRoom(List<Role> roles, Room room) {
        if (!roles.contains(Role.ORGANIZER)) {
            return false;
        }

        if (this.getRoomsList().stream().anyMatch(r -> r.getRoomName().equals(room.getRoomName()))) {
            return false;
        }

        save(room);
        return true;
    }

    /**
     * Constructs a {@link Room} with the given room name and capacity
     *
     * @param roomName the name of the {@link Room} to be constructed
     * @param capacity the capacity of the {@link Room} to be constructed
     * @return a {@link Room} with the given room name and capacity
     */
    public Room createRoom(String roomName, int capacity) {
        return new Room(roomName, capacity);
    }

    /**
     * Fetches the map of Room names to {@link Room}s
     *
     * @return a map of String to Room representing {@code rooms}
     */
    public Map<String, Room> getRoomsMap() {
        return this.rooms;
    }

    /**
     * Fetches a list of all {@link Room}s stored
     *
     * @return a list of {@link Room}s
     */
    public List<Room> getRoomsList() {
        return new ArrayList<Room>(this.rooms.values());
    }

    /**
     * Fetches a list of all ids (names) of all {@link Room}s stored
     *
     * @return a list of Strings of Room Names
     */
    public List<String> getRoomsNameList() { return new ArrayList<String>(this.rooms.keySet()); }

    /**
     * Returns the complete list of room names
     *
     * @return list of Strings that represent room names
     */
    @Override
    public List<String> getNames() {
        return getRoomsNameList();
    }

    /**
     * Returns the correct formatted room name that matches up with string word. Otherwise, return the empty string.
     *
     * @param word an inputted room name
     * @return correct formatted room name, or the empty string
     */
    public String getFormattedName(String word) {
        List<String> strings = getRoomsNameList();
        for (String s : strings) {
            if (word.equalsIgnoreCase(s)) {
                return s;
            }
        }
        return "";
    }

    /**
     * Fetches the capacity of a {@link Room} in {@code rooms} by the room's name
     *
     * @param roomName the String of the potential {@link Room}'s name
     * @return an int representing the room's capacity
     */
    public int getRoomCapacityByName(String roomName) { return rooms.get(roomName).getCapacity(); }

    /**
     * Checks if a {@link Room} with this Room name is already stored
     *
     * @param roomName the String of the potential {@link Room}'s name
     * @return a {@code true} if there already exists a Room stored with the name roomName
     */
    public boolean hasRoom(String roomName) {
        return this.rooms.containsKey(roomName);
    }

    /**
     * Returns a list of all toStrings of all {@link Room}s in the map of {@code rooms}
     *
     * @return a list of {@code String}s of the toString's of every {@link Room} in this manager
     */
    public List<String> getRoomToStrings() {
        List<String> result = new ArrayList<>();
        getRoomsList().stream()
                .forEach(r -> result.add(r.toString()));
        return result;
    }

    /**
     * Fetches the size of the list of {@link Room}s
     *
     * @return the {@code int} of the size of the list of {@link Room}s (number of Rooms in this manager)
     */
    public int size() {
        return getRoomsList().size();
    }

    /**
     * Fetches the name of the {@link Room} in which the {@link ConferenceEvent} with the given event name takes place at
     * Precondition(s): that a {@link Room} that has a {@link ConferenceEvent} with the name eventName take place in it,
     *      and exists in the map {@code rooms}
     *
     * @param eventName the name of the {@link ConferenceEvent} that takes place in the desired {@link Room}
     * @return the {@code String} of {@link Room} that has the {@link ConferenceEvent} with the name eventName
     */
    public String getRoomNameByEventName(String eventName) {
        List<String> roomNames = getRoomsNameList();
        for (String roomName : roomNames) {
            if (rooms.get(roomName).getAllEventNames().contains(eventName)) {
                return roomName;
            }
        }
        return "";
    }

    /**
     * Fetches a list of all ids (names) of all {@link ConferenceEvent}s of the {@code Room} given
     *
     * @param roomName the String of the potential {@link Room}'s name
     * @return a list of Strings of {@link ConferenceEvent} ids (names)
     */
    public List<String> getConferenceEventsListByRoomName(String roomName) {
        return new ArrayList<String>(rooms.get(roomName).getAllEventNames()); }

    /**
     * Adds the {@link Amenity} to the {@link Room} with the given room name
     * Precondition(s): that a {@link Room} that has the name roomName exists in the map {@code rooms}
     *
     * @param roomName the name of the {@link Room} that the {@link Amenity} is to be added to
     * @param amenity the {@link Amenity} to be added to the {@link Room} with the given room name
     * @return {@code true} if and only if the amenity is not already in the {@link Room} with the given room name
     */
    public boolean addAmenityByRoomName(String roomName, Amenity amenity) {
        return this.rooms.get(roomName).addAmenity(amenity);
    }

    /**
     * Fetches the list of names of the {@link Room}s each with the list of {@link Amenity}s
     *
     * @param amenities the list of {@link Amenity}s that every {@link Room} which name returned must have
     * @return the list of {@code String}s of room names of the {@link Room}s in the map {@code rooms} that each have
     *      every {@link Amenity} in the list amenities
     */
    public List<String> getRoomNamesHasAllOfAmenities(List<Amenity> amenities) {
        List<String> roomNames = this.getRoomsNameList();
        List<String> roomNamesWithAllAmenities = new ArrayList<>();
        for (String roomName : roomNames) {
            if (this.rooms.get(roomName).hasAllAmenities(amenities)) {
                roomNamesWithAllAmenities.add(roomName);
            }
        }
        return roomNamesWithAllAmenities;
    }

    /**
     * Fetches the list of toStrings of all {@link Amenity}s that the {@link Room} with the given room name contains
     * Precondition(s): that a {@link Room} that has the name roomName exists in the map {@code rooms}
     *
     * @param roomName the name of the {@link Room} of which the list of amenity toStrings is to be returned
     * @return the list of {@code String}s of the toStrings of all {@link Amenity}s that the {@link Room} with the given
     *      room name contains
     */
    public List<String> getAmenityStringListByRoomName(String roomName) {
        List<Amenity> amenities = this.rooms.get(roomName).getAmenityList();
        List<String> amenityStrings = new ArrayList<>();
        for (Amenity amenity : amenities) {
            amenityStrings.add(amenity.toString());
        }
        return amenityStrings;
    }

    /**
     * Deletes the Room room from map of rooms.
     *
     * @param room selected room to be deleted
     */
    @Override
    public void delete(Room room) {
        if (rooms.get(room.getId()) != null) {
            rooms.remove(room);
        }
    }

    /**
     * Saves the Room room to map of rooms
     *
     * @param room selected room to be saved
     */
    @Override
    public void save(Room room) {
        this.rooms.put(room.getRoomName(), room);
        setChanged();
        notifyObservers(room.getRoomName());
    }
}
