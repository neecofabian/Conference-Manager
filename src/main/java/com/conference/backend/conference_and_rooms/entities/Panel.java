package com.conference.backend.conference_and_rooms.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ConferenceEvent} plus multiple user id representing speakers
 *
 */
public class Panel extends ConferenceEvent {
    private static final long serialVersionUID = 6337543491234L;

    private ArrayList<String> speakerIds;

    /**
     * Constructs a new instance with the given data.
     *
     * @param eventName the name of the {@link ConferenceEvent}
     * @param capacity the capacity of the Panel
     */
    public Panel(String eventName, int capacity) {
        super(eventName, capacity);
        this.speakerIds = new ArrayList<String>();
    }

    /**
     * Constructs a new instance with no given data.
     */
    public Panel() {
        super();
        this.speakerIds = new ArrayList<String>();
    }

    /**
     * Returns a list of String objects that represent speakers at this event.
     * Panels can return multiple speakers.
     *
     * @return list of String objects that represent speakers at this event.
     */
    @Override
    public List<String> getSpeakerIds() {
        return this.speakerIds;
    }

    /**
     * Adds a potential speaker's id to this Talk event. If there is already a speaker id assigned to this Panel,
     * return false. Otherwise, return true once the new speaker id is assigned to the Panel.
     * Precondition(s): the user with {@code id} exists
     *
     * @param id the id of the speaker
     * @return true if new speaker id is assigned to the Panel, otherwise, return false.
     */
    @Override
    public boolean addSpeakerId(String id){
        if (!this.speakerIds.contains(id)) {
            this.speakerIds.add(id);
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
        return EventType.PANEL;
    }
}
