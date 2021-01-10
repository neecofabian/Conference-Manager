package com.conference.backend.conference_and_rooms.managers;

import com.conference.backend.conference_and_rooms.entities.ConferenceEvent;
import com.conference.backend.conference_and_rooms.entities.DateInterval;
import com.conference.backend.conference_and_rooms.entities.Room;

import java.util.*;

/**
 * Encapsulated class to sort ConferenceEvent objects in chronological order
 */
public class DateTimeConferenceEventSorter {

    public DateTimeConferenceEventSorter() { }

    /**
     * Return a list of event names in order by their start times (earlier to latest)
     *
     * @param conferenceEvents a list of ConferenceEvent objects to sort
     * @param eventRepository the ConferenceEventManager containing all ConferenceEvent objects
     * @return a list of eventNames in order by their start times
     */
    public List<String> sort(List<ConferenceEvent> conferenceEvents, ConferenceEventManager eventRepository) {
        Collections.sort(conferenceEvents, (c1, c2) -> {
            DateInterval d1 = eventRepository.getDateByEventName(c1.getEventName());
            DateInterval d2 = eventRepository.getDateByEventName(c2.getEventName());

            return d1.compareTo(d2);
        });

        List<String> eventNames = new ArrayList<>();

        for (ConferenceEvent event : conferenceEvents) {
            eventNames.add(event.getEventName());
        }

        return eventNames;
    }

    /**
     * Split a date-ordered list of event names into a mapping of [String representations of] Start date to a list of
     * event names with the start date, sorted by time
     *
     * @param eventNames a list of ConferenceEvent names sorted by date
     * @param eventRepository the ConferenceEventManager containing all ConferenceEvent objects
     * @return a map that maps from a start date to a list of events with the start date, sorted by time
     */
    public Map<String, List<String>> splitByDay(List<String> eventNames, ConferenceEventManager eventRepository) {
        HashMap<String, List<String>> millisToEventNames = new HashMap<>();

        for (String event : eventNames) {
            Date day = eventRepository.getDateByEventName(event).getStart();
            String[] dayToString = day.toString().split("\\s+");

            String key = dayToString[0] + ", " + dayToString[1] + " " + dayToString[2] + ", "
                    + new Integer(day.getYear() + 1900).toString();

            List<String> dayEvents = millisToEventNames.get(key);
            if (dayEvents == null) {
                dayEvents = new ArrayList<>();
                millisToEventNames.put(key, dayEvents);
            }
            dayEvents.add(event);
        }

        return millisToEventNames;
    }

}
