package com.conference.backend.conference_and_rooms.managers;

import com.conference.backend.conference_and_rooms.entities.*;

/**
 * Concrete factory responsible for creating a Conference Event of a certain {@link EventType}
 */
public class ConferenceEventFactory {

    /**
     * Return a {@link ConferenceEvent} object with the specified {@link EventType}
     *
     * @param type the desired EventType of the new ConferenceEvent
     * @return a {@link ConferenceEvent} object with the specified {@link EventType}
     */
    public ConferenceEvent createEvent(EventType type) {
        if (type == EventType.PANEL) {
            return new Panel();
        } else if (type == EventType.PARTY) {
            return new Party();
        } else if (type == EventType.TALK) {
            return new Talk();
        } else if (type == EventType.VIP_SOCIAL) {
            return new VIPSocial();
        }

        return null;
    }
}
