package com.conference.backend.conference_and_rooms.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ConferenceEvent} plus a single id representing a speaker
 *
 */
public class Talk extends ConferenceEvent {
    private static final long serialVersionUID = 8347563495324L;

    private String speakerId;

    /**
     * Constructs a new instance with the given data.
     *
     * @param eventName the name of the {@link ConferenceEvent}
     * @param capacity the capacity of the Talk
     */
    public Talk(String eventName, int capacity) {
        super(eventName, capacity);
        this.speakerId = "";
    }

    /**
     * Constructs a new instance with no given data.
     */
    public Talk() {
        super();
        this.speakerId = "";
    }

    /**
     * Returns a list of String objects that represent speakers at this event.
     * Talks should return a maximum of one speaker.
     *
     * @return list of String objects that represent speakers at this event.
     */
    @Override
    public List<String> getSpeakerIds() {
        List<String> ids = new ArrayList<>();
        ids.add(this.speakerId);
        return ids;
    }

    /**
     * Adds a potential speaker's id to this Talk event. If there is already a speaker id assigned to this Talk,
     * return false. Otherwise, return true once the new speaker id is assigned to the Talk.
     * Precondition(s): the user with {@code id} exists
     *
     * @param id the id of the speaker
     * @return true if new speaker id is assigned to the Talk, otherwise, return false.
     */
    @Override
    public boolean addSpeakerId(String id){
        if (this.speakerId.equals("")) {
            this.speakerId = id;
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Return the EventType of this event
     *
     * @return the EventType of this event
     */
    @Override
    public EventType getEventType() {
        return EventType.TALK;
    }
}
