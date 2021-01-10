package com.conference.backend.conference_and_rooms.entities;


import com.conference.backend.data.utils.base.AbstractEntity;

import java.util.*;

/**
 * {@link AbstractEntity} plus details of a room. Includes the room name, a map of event names to their time, and the
 * room's capacity
 *
 */
public class Room extends AbstractEntity {
    private static final long serialVersionUID = 352402394879345L;

    private String roomName;
    private Map<String, DateInterval> eventToDate;
    private int capacity;
    private List<Amenity> amenityList;

    /**
     * Constructs a new instance with the given data.
     *
     * @param roomName name of the room
     * @param eventToDate scheduling map from {@link ConferenceEvent} event names held in this Room to
     *                    {@link DateInterval}s of when they occur
     * @param capacity maximum number of attendees that can attend a {@link ConferenceEvent} in this room
     */
    public Room(String roomName, Map<String, DateInterval> eventToDate, int capacity) {
        this.roomName = roomName;
        this.eventToDate = eventToDate;
        this.capacity = capacity;
        setId(this.hashCode() + "");
        this.amenityList = new ArrayList<Amenity>();
    }

    /**
     * Constructs a new instance with the given data.
     *
     * @param roomName name of the room
     * @param capacity maximum number of attendees that can attend a {@link ConferenceEvent} in this Room
     */
    public Room(String roomName, int capacity) {
        this.roomName = roomName;
        this.eventToDate = new HashMap<String, DateInterval>();
        this.capacity = capacity;
        setId(this.hashCode() + "");
        this.amenityList = new ArrayList<Amenity>();
    }

    /**
     * Copy constructor.
     *
     * @param other the instance to copy
     */
    public Room(Room other) {
        this(other.getRoomName(), other.getEventToDate(), other.getCapacity());
        this.setId(other.getId());
        this.amenityList = other.getAmenityList();
    }

    /**
     * Constructs a new instance with the no given data.
     */
    public Room() {}

    /**
     * Returns the unique name of this Room
     *
     * @return the unique name of this Room
     */
    public String getRoomName() {
        return this.roomName;
    }

    /**
     * Returns the eventToDate map
     *
     * @return the map from {@link ConferenceEvent} event names held in this Room to {@link DateInterval}s
     * of when they occur
     */
    public Map<String, DateInterval> getEventToDate() {
        return new HashMap<String, DateInterval>(eventToDate);
    }

    /**
     * Returns the list of event names of {@link ConferenceEvent}s held in this Room in alphabetical order
     *
     * @return the list of event names of {@link ConferenceEvent}s held in this Room in alphabetical order
     */
    public List<String> getAllEventNames() {
        List<String> result = new ArrayList<>(eventToDate.keySet());
        Collections.sort(result);
        return result;
    }

    /**
     * Schedules a {@link ConferenceEvent} in this Room during dateInterval if it does not overlap with another
     * {@link ConferenceEvent} held in this Room
     *
     * @param dateInterval {@link DateInterval} with the desired start date and end date of the {@link ConferenceEvent}
     * @param eventName event name of the {@link ConferenceEvent}
     *
     * @return true if {@link ConferenceEvent}'s dateInterval does not overlap with another scheduled event, and the
     * event name is added to the eventToDate scheduling map, false otherwise
     */
    public boolean addEvent(DateInterval dateInterval, String eventName) {
        Collection<DateInterval> allDates = this.eventToDate.values();
        for (DateInterval d : allDates) {
            Date start1 = d.getStart();
            Date end1 = d.getEnd();
            Date start2 = dateInterval.getStart();
            Date end2 = dateInterval.getEnd();
            if (start1.before(end2) && start2.before(end1)) {
                return false;
            }
        }

        this.eventToDate.put(eventName, dateInterval);
        return true;
    }

    /**
     * Removes a {@link ConferenceEvent}s being held in this Room, using its event name
     *
     * @param eventName name of the {@link ConferenceEvent} being removed
     * @return true if eventName is removed from the eventToDate scheduling map, false otherwise
     */
    public boolean removeEvent(String eventName) {
        return this.eventToDate.remove(eventName, this.eventToDate.get(eventName));
    }

    /**
     * Gets the maximum number of attendees that can attend a {@link ConferenceEvent} in this Room
     *
     * @return the maximum number of attendees that can attend a {@link ConferenceEvent} in this Room
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Adds the {@link Amenity} to this Room if the same Amenity is not already added to this Room
     *
     * @param amenity the amenity to add
     * @return {@code true} if and only if the Amenity is not already in the list of Amenities, amenityList
     */
    public boolean addAmenity(Amenity amenity) {
        if (this.amenityList.contains(amenity)) {
            return false;
        } else {
            this.amenityList.add(amenity);
            return true;
        }
    }

    /**
     * Checks if every {@link Amenity} in the list amenities is in this Room
     *
     * @param amenities the list of amenities this Room should have
     * @return {@code true} if and only if every Amenity in amenities is in amenityList
     */
    public boolean hasAllAmenities(List<Amenity> amenities) {
        for (Amenity amenity : amenities) {
            if (!amenityList.contains(amenity)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the list of every {@link Amenity} contained in this Room
     *
     * @return the list of Amenities, amenityList
     */
    public List<Amenity> getAmenityList() { return this.amenityList; }

    /**
     * Returns the hash code for this Room
     *
     * @return a unique int based on this Room's unique name
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), roomName);
    }

    /**
     * Returns a string representation of this Room
     *
     * @return a string representation of this Room
     */
    @Override
    public String toString() {
        return "Room " + roomName + " with max capacity " + this.capacity + " and schedule: " + eventToDate;
    }
}
