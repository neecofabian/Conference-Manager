package com.conference.backend.conference_and_rooms.entities;


import com.conference.backend.data.utils.base.AbstractEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Abstract ConferenceEvent class with event name, attendee list, and capacity plus {@link AbstractEntity} properties.
 * Serves as a parent class for different conference event types.
 *
 */
public abstract class ConferenceEvent extends AbstractEntity {
    private static final long serialVersionUID = 94638457630434L;

    private String eventName;
    private List<String> attendeeList;
    private int capacity;

    /**
     * Constructs a new instance with the given data.
     *
     * @param eventName name of the event
     * @param capacity maximum number of attendees
     */
    public ConferenceEvent(String eventName, int capacity) {
        this.eventName = eventName;
        this.capacity = capacity;
        this.attendeeList = new ArrayList<>();
        this.setId(this.hashCode() + "");
    }

    /**
     * Copy constructor.
     *
     * @param other the instance to copy
     */
    public ConferenceEvent(ConferenceEvent other) {
        this(other.getEventName(), other.getCapacity());
        this.setId(other.getId());
        this.attendeeList = other.getAttendeeList();
    }

    /**
     * Construct a ConferenceEvent with no Attendees
     */
    public ConferenceEvent() {
        this.attendeeList = new ArrayList<>();
    }

    /**
     * Returns the name of the event
     *
     * @return {@code eventName} name of the event
     */
    public String getEventName() {
        return this.eventName;
    }

    /**
     * Set the name of this event
     * Precondition: eventName must be unique
     *
     * @param eventName the name of this event
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Add an attendee to the list of this event's attendees
     *
     * @param attendeeId id of the attendee
     */
    public void addAttendee(String attendeeId) {
        this.attendeeList.add(attendeeId);
    }

    /**
     * Remove an attendee from this event's list of attendees
     *
     * @param attendeeId id of the attendee
     */
    public void removeAttendee(String attendeeId){
        this.attendeeList.remove(attendeeId);
    }

    /**
     * Get the maximum number of attendees that can attend this event
     *
     * @return the maximum number of attendees that can attend this event
     */
    public int getCapacity(){
        return this.capacity;
    }

    /**
     * Set the maximum number of attendees that can attend this event
     * Precondition: this event's capacity must be less than or equal to the capacity of the {@link Room}
     * where the event is being held
     *
     * @param capacity the maximum number of attendees that can attend this event
     */
    public void setCapacity(int capacity){
        this.capacity = capacity;
    }

    /**
     * Check if this event has more spots for attendees to sign up
     *
     * @return true if the event has space for one more attendee, false otherwise
     */
    public boolean hasCapacityForAttendees() {
        return capacity - attendeeList.size() > 0;
    }

    /**
     * Return this event's list of attendee ids
     *
     * @return a list of ids of event attendees
     */
    public List<String> getAttendeeList(){
        return this.attendeeList;
    }

    /**
     * Set the list of attendee ids
     *
     * @param attendeeList list of attendee ids
     */
    public void setAttendeeList(List<String> attendeeList) {
        this.attendeeList = attendeeList;
    }

    /**
     * Return a list of this event's Speaker ids
     *
     * @return a list of this event's Speaker ids
     */
    public abstract List<String> getSpeakerIds();

    /**
     * Add a Speaker to this event
     *
     * @param id the id of the speaker
     * @return true if the Speaker's id was added to this event's list of Speaker ids, false otherwise
     */
    public abstract boolean addSpeakerId(String id);

    /**
     * Return the hash code for this event
     *
     * @return a unique int based on the event's unique name
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), eventName);
    }

    /**
     * Return a string representation of the event
     *
     * @return a string representation of the event
     */
    @Override
    public String toString() {
        return this.eventName + " has" + this.attendeeList.size() +" attendees out of " + this.capacity + " spots.";
    }

    /**
     * Return the EventType of this event
     *
     * @return the EventType of this event
     */
    public abstract EventType getEventType();
}
