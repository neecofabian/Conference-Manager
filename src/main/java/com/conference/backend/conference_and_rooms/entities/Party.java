package com.conference.backend.conference_and_rooms.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ConferenceEvent} with no speakers
 *
 */
public class Party extends ConferenceEvent {
    private static final long serialVersionUID = 6243145423234L;

    /**
     * Constructs a new instance with the given data.
     *
     * @param eventName the name of the {@link ConferenceEvent}
     * @param capacity the capacity of the Party
     */
    public Party(String eventName, int capacity) {
        super(eventName, capacity);
    }

    /**
     * Constructs a new instance with no given data.
     */
    public Party() {
        super();
    }

    /**
     * Returns a list of String objects that represent speakers at this event.
     * Party should return no speakers.
     *
     * @return list of String objects that represent speakers at this event.
     */
    @Override
    public List<String> getSpeakerIds() {
        return new ArrayList<>();
    }

    /**
     * Adds a potential speaker's id to this Party event. Parties should never allow a speaker to be added to the event.
     *
     * @param id the id of the speaker
     * @return return false.
     */
    @Override
    public boolean addSpeakerId(String id){
        return false;
    }

    /**
     * Return the EventType of this event
     *
     * @return the EventType of this event
     */
    @Override
    public EventType getEventType() {
        return EventType.PARTY;
    }
}