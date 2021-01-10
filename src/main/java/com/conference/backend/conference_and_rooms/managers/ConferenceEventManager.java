package com.conference.backend.conference_and_rooms.managers;

import com.conference.backend.conference_and_rooms.entities.*;
import com.conference.backend.conference_and_rooms.managers.ConferenceEventFactory;
import com.conference.backend.conference_and_rooms.managers.RoomManager;
import com.conference.backend.data.utils.*;
import com.conference.backend.data.utils.base.CrudManager;
import com.conference.backend.users.User;

import java.io.*;
import java.util.*;

/**
 * Backend service to store and manipulate {@link ConferenceEvent} and {@link Room}instances
 */
public class ConferenceEventManager extends Observable implements Serializable, CrudManager<ConferenceEvent> {
    private static final long serialVersionUID = 48352876358762L;

    private RoomManager roomRepository;
    private Map<String, ConferenceEvent> conferenceEvents;

    /**
     * Constructs an instance with no given data
     */
    public ConferenceEventManager() {
        this.roomRepository = new RoomManager();
        this.conferenceEvents = new HashMap<>();
    }

    /**
     * Saves a {@link ConferenceEvent} to the map of {@link ConferenceEvent}s
     * Precondition: the event is not in the map of {@link ConferenceEvent}
     *
     * @param event a {@link ConferenceEvent} instance to save
     */
    @Override
    public void save(ConferenceEvent event) {
        this.conferenceEvents.put(event.getEventName(), event);
        setChanged();
        notifyObservers(event.getEventName());
    }

    /**
     * Deletes a {@link ConferenceEvent} from the map of {@link ConferenceEvent}s
     * Precondition(s): the event is in the map of {@link ConferenceEvent}
     *
     * @param event a {@link ConferenceEvent} instance to delete
     */
    @Override
    public void delete(ConferenceEvent event) { conferenceEvents.remove(event.getEventName()); }

    /**
     * Returns the repository of {@link Room} objects (the schedule)
     *
     * @return the repository of {@link Room} objects (the schedule)
     */
    public RoomManager getRoomRepository(){
        return this.roomRepository;
    }

    /**
     * Checks if a {@link ConferenceEvent} with the given name is in the schedule.
     * Precondition(s): eventName is scheduled in a {@link Room} iff eventName is in {@code conferenceEvents}
     *
     * @param eventName name of the {@link ConferenceEvent}
     * @return true if the event is in the schedule, false otherwise
     */
    public boolean hasEvent(String eventName) {
        return this.conferenceEvents.get(eventName) != null;
    }

    /**
     * Returns a list of all {@link ConferenceEvent}s
     *
     * @return a list of all {@link ConferenceEvent}s
     */
    public List<ConferenceEvent> getConferenceEventsList() {
        return new ArrayList<ConferenceEvent> (this.conferenceEvents.values());
    }

    @Override
    public List<String> getNames() {
        return getConferenceEventNamesList();
    }

    /**
     * Returns the formatted name of the {@link ConferenceEvent} with the given name
     * Precondition(s): a {@link ConferenceEvent} with an unformatted name equivalent to the unformatted word exists in
     *      the map {@code conferenceEvents}
     *
     * @param word the word to find
     * @return the {@code String} of the properly formatted name of the {@link ConferenceEvent} desired that corresponds
     *      to word
     */
    public String getFormattedName(String word) {
        List<String> strings = getConferenceEventNamesList();
        for (String s : strings) {
            if (word.equalsIgnoreCase(s)) {
                return s;
            }
        }
        return "";
    }

    /**
     * Returns the {@link ConferenceEvent} with given eventName
     * Precondition(s): a {@link ConferenceEvent} with eventName exists in the map {@code conferenceEvents}
     *
     * @param eventName name of the {@link ConferenceEvent}
     * @return the {@link ConferenceEvent} with given eventName
     */
    public ConferenceEvent getConferenceEvent(String eventName) {
        return conferenceEvents.get(eventName);
    }

    /**
     * Schedules (add to a {@link Room} a {@link ConferenceEvent} during a {@link DateInterval}, and save it to
     * {@code conferenceEvents}
     *
     * @param roles the roles of the user attempting to save the {@link ConferenceEvent}
     * @param dateInterval the {@link DateInterval} the event takes place
     * @param roomName the name of the room where the event takes place
     * @param event the {@link ConferenceEvent} that is to be added
     * @return true iff the {@link ConferenceEvent} is scheduled and added
     */
    public boolean addEvent(List<Role> roles, DateInterval dateInterval, String roomName, ConferenceEvent event) {
        if (!roles.contains(Role.ORGANIZER)) {
            return false;
        }

        if (!this.roomRepository.hasRoom(roomName)) {
            return false;
        }

        if (this.roomRepository.getRoomsMap().get(roomName).getCapacity() < event.getCapacity()) {
            return false;
        }

        if (this.conferenceEvents.containsKey(event.getEventName())) {
            return false;
        }

        if (!roomRepository.getRoomsMap().get(roomName).addEvent(dateInterval, event.getEventName())) {
            return false;
        }

        this.save(event);
        return true;
    }

    /**
     * Returns and constructs a {@link ConferenceEvent} with given eventName, capacity, and EventType type
     *
     * @param eventName name of the {@link ConferenceEvent}
     * @param capacity capacity of the {@link ConferenceEvent}
     * @param type {@link EventType} of the {@link ConferenceEvent}
     * @return the {@link ConferenceEvent} with given eventName, capacity, and type
     */
    public ConferenceEvent createConferenceEvent(String eventName, int capacity, EventType type) {
        ConferenceEventFactory conferenceEventFactory = new ConferenceEventFactory();

        ConferenceEvent ce = conferenceEventFactory.createEvent(type);
        ce.setEventName(eventName);
        ce.setCapacity(capacity);
        ce.setId(ce.hashCode() + "");

        return ce;
    }

    /**
     * Returns and constructs a {@link DateInterval} with given start and end time
     *
     * @param start start {@code Date} of the {@link DateInterval}
     * @param end end {@code Date} of the {@link DateInterval}
     * @return the {@link DateInterval} with given start and end time
     */
    public DateInterval createDateInterval(Date start, Date end) {
        return new DateInterval(start, end);
    }

    /**
     * Removes a {@link ConferenceEvent} from the schedule (remove it from its {@link Room}, and delete the event
     * from {@code conferenceEvents}
     *
     * @param roles the roles of the user attempting to remove the {@link ConferenceEvent}
     * @param eventName the name of the {@link ConferenceEvent} to be removed
     * @return true iff the {@link ConferenceEvent} is removed from the schedule and {@code conferenceEvents}
     */
    public boolean removeEvent(List<Role> roles, String eventName) {
        if (!roles.contains(Role.ORGANIZER)) {
            return false;
        }

        if (!this.hasEvent(eventName)) {
            return false;
        }

        boolean eventRemovedFromRoom = false;

        List<Room> allRooms = this.roomRepository.getRoomsList();
        for (Room r : allRooms) {
            List allEvents = r.getAllEventNames();
            if (allEvents.contains(eventName)) {
                eventRemovedFromRoom = r.removeEvent(eventName);
            }
        }

        if (!eventRemovedFromRoom) {
            return false;
        }

        this.delete(this.conferenceEvents.get(eventName));
        return true;
    }

    /**
     * Returns a list of eventNames of all {@link ConferenceEvent}s that overlap with the time interval
     *
     * @param interval2 the {@link DateInterval} to find conflicting events for
     * @return a list of eventNames of all {@link ConferenceEvent}s that overlap with the time interval
     */
    public List<String> getEventsAtInterval(DateInterval interval2) {
        List<Room> allRooms = this.roomRepository.getRoomsList();
        List<String> results = new ArrayList<>();
        for (Room room : allRooms) {
            Set<String> allEventNames = room.getEventToDate().keySet();
            for (String eventName : allEventNames) {
                Date start1 = room.getEventToDate().get(eventName).getStart();
                Date end1 = room.getEventToDate().get(eventName).getEnd();
                Date start2 = interval2.getStart();
                Date end2 = interval2.getEnd();
                if (start1.before(end2) && start2.before(end1)) {
                    results.add(eventName);
                }
            }
        }
        return results;
    }

    /**
     * Fetches a list of eventNames of all {@link ConferenceEvent}s that overlap with the time interval
     *
     * @param id the id (name) of the {@link ConferenceEvent} to find the type of
     * @return the {@link EventType} of the  {@link ConferenceEvent} with the given id
     */
    public EventType getEventTypeById(String id) {
        return getConferenceEvent(id).getEventType();
    }

    /**
     * Add a speaker to speak at a {@link ConferenceEvent}
     *
     * @param userRoles  the roles of the {@link User} adding a speaker to a {@link ConferenceEvent}
     * @param speakerRoles the roles of the {@link User} being added as a speaker
     * @param speakerId the id of the speaker
     * @param eventName the name of the {@link ConferenceEvent} in which the speaker is being added
     * @return true iff {@code speaker} is added as a speaker to eventName {@link ConferenceEvent} by {@code user}
     */
    public boolean addSpeakerToEvent(List<Role> userRoles, List<Role> speakerRoles, String speakerId,
                                     String eventName) {
        if (!userRoles.contains(Role.ORGANIZER) || !speakerRoles.contains(Role.SPEAKER)) {
            return false;
        }

        // Event is in the schedule
        if (!this.hasEvent(eventName)) {
            return false;
        }
        // No previous speaker
        // need to check if speaker is already speaking at event and if the event can handle more speakers
        if (this.conferenceEvents.get(eventName).getSpeakerIds().contains(speakerId)) {
            return false;
        }

        if (this.conferenceEvents.get(eventName).getEventType() == EventType.PARTY) {
            return false;
        }

        if (this.conferenceEvents.get(eventName).getEventType() == EventType.TALK &&
                !this.conferenceEvents.get(eventName).getSpeakerIds().get(0).equals("")) {
            return false;
        }

        // Check if Speaker is not speaking in another event that overlaps this event's time interval
        List<Room> allRooms = this.roomRepository.getRoomsList();

        // Find interval of eventName
        for (Room room: allRooms){
            if (room.getEventToDate().containsKey(eventName)) {
                // This if is only true once; Get eventNames that overlap with given event's interval
                List<String> overlappingEvents = this.getEventsAtInterval(room.getEventToDate().get(eventName));
                for (String overlappingEventName : overlappingEvents) {
                    if (this.conferenceEvents.get(overlappingEventName).getSpeakerIds().contains(speakerId)) {
                        return false;
                    }
                }
            }
        }

        // Finally, add speaker to this event as a speaker
        return this.conferenceEvents.get(eventName).addSpeakerId(speakerId);
    }

    /**
     * Fetches the list of the {@link ConferenceEvent}s that {@code user} is speaking at
     * Precondition(s): {@code user} must have a speaker role
     *
     * @param speakerId the id of the speaker whose {@link ConferenceEvent}s are desired
     * @return a list of the {@link ConferenceEvent}s that {@code user} is speaking at
     */
    public List<String> getSpeakerEventNames(String speakerId) {
        List<String> speakerEvents = new ArrayList<>();
        List<ConferenceEvent> allEvents = this.getConferenceEventsList();

        for (ConferenceEvent event : allEvents){
            if (event.getSpeakerIds().contains(speakerId)) {
                speakerEvents.add(event.getEventName());
            }
        }
        return speakerEvents;
    }

    /**
     * Fetches the list of the {@link ConferenceEvent}s that {@code user} is speaking at
     * Precondition(s): {@code user} must have a speaker role
     *
     * @param speakerId the id of the speaker whose {@link ConferenceEvent}s are desired
     * @return a list of the {@link ConferenceEvent}s that {@code user} is speaking at
     */
    public List<ConferenceEvent> getSpeakerEvents(String speakerId) {
        List<ConferenceEvent> speakerEvents = new ArrayList<>();
        List<ConferenceEvent> allEvents = this.getConferenceEventsList();

        for (ConferenceEvent event : allEvents){
            if (event.getSpeakerIds().contains(speakerId)) {
                speakerEvents.add(event);
            }
        }
        return speakerEvents;
    }

    /**
     * Fetches the {@link Room} where {@code eventName} {@link ConferenceEvent} is being held
     * Precondition(s): {@code eventName} is the name of a {@link ConferenceEvent} that exists
     *
     * @param eventName the name of the {@link ConferenceEvent}
     * @return the {@link Room} where {@code eventName} {@link ConferenceEvent} is being held
     */
    public Room getRoomByEventName(String eventName) {
        List<Room> allRooms = this.roomRepository.getRoomsList();

        for (Room room : allRooms) {
            if (room.getEventToDate().containsKey(eventName)) {
                return room;
            }
        }
        return new Room();
    }

    /**
     * Returns the {@link DateInterval} in which {@code eventName} {@link ConferenceEvent} occurs
     * Precondition(s): {@code eventName} is the name of a {@link ConferenceEvent} that exists
     *
     * @param eventName the name of the {@link ConferenceEvent}
     * @return the {@link DateInterval} in which {@code eventName} {@link ConferenceEvent} occurs
     */
    public DateInterval getDateByEventName(String eventName) {
        List<Room> allRooms = this.roomRepository.getRoomsList();

        for (Room room : allRooms) {
            if (room.getEventToDate().containsKey(eventName)) {
                return room.getEventToDate().get(eventName);
            }
        }
        return new DateInterval();
    }

    /**
     * Updates the {@code capacity} of the {@link ConferenceEvent} with the given eventName to the given capacity if the
     * user is able to do so and the new capacity is allowed
     *
     * @param capacity the capacity that the {@link ConferenceEvent} with name EventName is to be changed to
     * @param roles the list of {@link Role}s that the {@link User} has
     * @param eventName the name of the {@link ConferenceEvent} of which the capacity is desired to be changed
     * @return {@code true} if and only if roles contains an Organizer {@link Role}, a {@link ConferenceEvent} with the
     *      name eventName exists in the map {@code conferenceEvents}, the given capacity is not higher than the
     *      capacity of the {@link Room} the Conference Event is located in, and the attendee list of the
     *      {@link ConferenceEvent} does not exceed the new capacity
     */
    public boolean updateConferenceEventCapacity(int capacity, List<Role> roles, String eventName) {
        if (!roles.contains(Role.ORGANIZER)) {
            return false;
        }

        if (!this.hasEvent(eventName)) {
            return false;
        }

        if (this.getRoomByEventName(eventName).getCapacity() < capacity) {
            return false;
        }

        if (conferenceEvents.get(eventName).getAttendeeList().size() > capacity) {
            return false;
        }

        conferenceEvents.get(eventName).setCapacity(capacity);
        return true;
    }

    /**
     * Returns the list of attendees of the {@link ConferenceEvent} with the given event name
     * Precondition(s): a {@link ConferenceEvent} with the name eventName exists in the map {@code conferenceEvents}
     *
     * @param eventName the name of the {@link ConferenceEvent}
     * @return the list of {@code String} of attendees of the {@link ConferenceEvent} with name eventName
     */
    public List<String> getAttendeeListByConferenceEventName(String eventName) {
        return conferenceEvents.get(eventName).getAttendeeList();
    }

    /**
     * Returns the {@link EventType} of the {@link ConferenceEvent} with the given event name
     * Precondition(s): a {@link ConferenceEvent} with the name eventName exists in the map {@code conferenceEvents}
     *
     * @param eventName the name of the {@link ConferenceEvent}
     * @return the {@link EventType} of the {@link ConferenceEvent} with name eventName
     */
    public EventType getEventTypeByEventName(String eventName) {
        return conferenceEvents.get(eventName).getEventType();
    }

    /**
     * Fetches the list of ids of the speakers of the {@link ConferenceEvent} with the given event name
     * Precondition(s): a {@link ConferenceEvent} with the name eventName exists in the map {@code conferenceEvents}
     *
     * @param eventName the name of the {@link ConferenceEvent}
     * @return the list of {@code String} of ids of speakers of the {@link ConferenceEvent} with name eventName
     */
    public List<String> getSpeakerIdsByEventName(String eventName) {
        return conferenceEvents.get(eventName).getSpeakerIds();
    }

    /**
     * Returns the size of the list of attendees of the {@link ConferenceEvent} with the given event name
     * Precondition(s): a {@link ConferenceEvent} with the name eventName exists in the map {@code conferenceEvents}
     *
     * @param eventName the name of the {@link ConferenceEvent}
     * @return the {@code int} of the size of the attendee list (number of attendees) of the {@link ConferenceEvent}
     *      with name eventName
     */
    public int getAttendeeListSizeByEventName(String eventName) {
        return conferenceEvents.get(eventName).getAttendeeList().size();
    }

    /**
     * Fetches the capacity of the {@link ConferenceEvent} with the given event name
     * Precondition(s): a {@link ConferenceEvent} with the name eventName exists in the map {@code conferenceEvents}
     *
     * @param eventName the name of the {@link ConferenceEvent}
     * @return the {@code int} of the capacity of the {@link ConferenceEvent} with name eventName
     */
    public int getCapacityByEventName(String eventName) {
        return conferenceEvents.get(eventName).getCapacity();
    }

    /**
     * Fetches the list of names of the speakers of all the {@link ConferenceEvent}s in this manager
     *
     * @return the list of {@code String} of names of the {@link ConferenceEvent}s in the map {@code conferenceEvents}
     */
    public List<String> getConferenceEventNamesList() {
        List<String> eventNames = new ArrayList<>();
        for (String eventName : conferenceEvents.keySet()) {
            eventNames.add(eventName);
        }

        return eventNames;
    }

}
