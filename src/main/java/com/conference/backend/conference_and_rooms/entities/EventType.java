package com.conference.backend.conference_and_rooms.entities;

/**
 * A type of ConferenceEvent
 */
public enum EventType {
    PANEL("Panel"),
    TALK("Talk"),
    PARTY("Party"),
    VIP_SOCIAL("VIP Social");

    private String event;

    EventType(String event) {
        this.event = event;
    }

    /**
     * Return a String representation of the EventType
     *
     * @return a String representation of the EventType
     */
    @Override
    public String toString(){
        return event;
    }
}
