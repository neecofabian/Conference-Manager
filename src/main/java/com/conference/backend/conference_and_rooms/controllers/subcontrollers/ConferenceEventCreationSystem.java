package com.conference.backend.conference_and_rooms.controllers.subcontrollers;

import com.conference.backend.data.utils.DistanceSpellChecker;
import com.conference.backend.data.utils.base.InputProcessor;
import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.conference_and_rooms.entities.ConferenceEvent;
import com.conference.backend.conference_and_rooms.entities.DateInterval;
import com.conference.backend.conference_and_rooms.entities.EventType;
import com.conference.backend.conference_and_rooms.entities.Room;
import com.conference.backend.conference_and_rooms.entities.Amenity;
import com.conference.backend.data.utils.Role;
import com.conference.backend.data.utils.base.SpellChecker;
import com.conference.backend.security.PropertiesIterator;
import com.conference.backend.users.AppTrafficManager;
import com.conference.backend.users.UserLoginManager;
import com.conference.backend.users.UserManager;
import com.conference.frontend.conference.ConferenceSubView;
import com.conference.frontend.conference.RoomView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 *  Controller for creating a {@link ConferenceEvent}.
 *
 */
public class ConferenceEventCreationSystem extends InputProcessor implements Startable {
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private final Map<String, EventType> EVENT_NAME_TO_TYPE = new HashMap<String, EventType>() {{
        put(EventType.TALK.toString(), EventType.TALK);
        put(EventType.PANEL.toString(), EventType.PANEL);
        put(EventType.PARTY.toString(), EventType.PARTY);
        put(EventType.VIP_SOCIAL.toString(), EventType.VIP_SOCIAL);
    }};

    private final Map<String, Amenity> AMENITY_NAME_TO_TYPE = new HashMap<String, Amenity>() {{
        put(Amenity.AV_EQUIPMENT.toString(), Amenity.AV_EQUIPMENT);
        put(Amenity.PODIUM.toString(), Amenity.PODIUM);
        put(Amenity.TABLES.toString(), Amenity.TABLES);
        put(Amenity.CHAIRS.toString(), Amenity.CHAIRS);
        put(Amenity.WIFI.toString(), Amenity.WIFI);
        put(Amenity.RESTROOMS.toString(), Amenity.RESTROOMS);
    }};

    private final ConferenceEventManager conferenceEventManager;
    private final UserManager userManager;
    private final UserLoginManager userLoginManager;
    private final AppTrafficManager appTrafficManager;

    private final ConferenceSubView conferenceSubView;
    private final RoomView roomView;

    private final SpellChecker distanceSpellChecker;

    public ConferenceEventCreationSystem(ConferenceEventManager conferenceEventManager,
                                         UserLoginManager userLoginManager, AppTrafficManager appTrafficManager) {
        this.userLoginManager = userLoginManager;
        this.userManager = userLoginManager.getUserRepository();
        this.appTrafficManager = appTrafficManager;
        this.conferenceEventManager = conferenceEventManager;

        this.conferenceSubView = new ConferenceSubView();
        this.roomView = new RoomView();

        distanceSpellChecker = new DistanceSpellChecker<>(conferenceEventManager.getRoomRepository());
        conferenceEventManager.getRoomRepository().addObserver((Observer) distanceSpellChecker);
    }

    /**
     * Allows Organizers to add a {@link ConferenceEvent} to a {@link Room} if there is no existing event with the same
     * name, the user inputs a valid {@link DateInterval}, the room exists and has enough capacity for the event's
     * capacity. Adding the ConferenceEvent will take in user input of the conference name, start time and end time,
     * room name, and capacity of the event. Before being asked for a room name, the Organizer has the option to see a
     * list of rooms with desired Amenities.
     *
     */
    @Override
    public void start() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<Role> roles = userManager.getRolesByUserId(userLoginManager.getCurrentUserId());

        PropertiesIterator prompts = new PropertiesIterator("phase2/src/dao/event_properties.txt");
        ArrayList<String> temp = new ArrayList<>();

        String tempEventType = "";

        boolean hasEvent;
        boolean roomExists;
        boolean hasCapacity = false;

        String input = ""; String inputYesNo = "";
        Date start = new Date(), end = new Date();

        try {
            while (prompts.hasNext()) {
                String s;
                conferenceSubView.displayString(s = prompts.next());

                if (s.contains("Event Name")) {
                    do {
                        input = br.readLine().trim();
                        hasEvent = conferenceEventManager.hasEvent(input);
                        if (hasEvent) {
                            conferenceSubView.displayNameExists();
                            conferenceSubView.displayString(s);
                        }
                        if (input.equalsIgnoreCase(".home")) {
                            conferenceSubView.displayContainsHome();
                            conferenceSubView.displayString(s);
                        }

                    } while (hasEvent || input.equalsIgnoreCase(".home"));
                } else if (s.contains("Event Type")) {
                    conferenceSubView.displayValidEventTypes(new ArrayList<>(EVENT_NAME_TO_TYPE.keySet()));
                    tempEventType = requestInput(new ArrayList<>(EVENT_NAME_TO_TYPE.keySet()), s);
                } else if (s.contains("Start time")) {
                    start = requestDate(s, new Date(System.currentTimeMillis()));
                } else if (s.contains("End time")) {
                    end = requestDate(s, start);
                } else if (s.contains("View Amenities")) {
                    inputYesNo = requestBoolean();

                    if (inputYesNo.equalsIgnoreCase("y")) {
                        promptAmenity();
                    } else {
                        roomView.displayAllRoomsTitle();
                        roomView.displayRooms(conferenceEventManager.getRoomRepository().getRoomsNameList(),
                                conferenceEventManager.getRoomRepository());
                    }
                    do {
                        roomView.displayPromptSelectRoom();
                        input = br.readLine().trim();
                        roomExists = conferenceEventManager.getRoomRepository().hasRoom(input);

                        String spellCheckAttempt = distanceSpellChecker.corrections(input);

                        if (!roomExists) {
                            roomView.displayRoomDoesNotExist();
                            if (!spellCheckAttempt.equalsIgnoreCase(input)) {
                                conferenceSubView.displayWeThinkYouMeant(conferenceEventManager.getRoomRepository()
                                        .getFormattedName(spellCheckAttempt));
                            }
                        }
                    } while (!roomExists);
                } else if (s.contains("Capacity")) {
                    do {
                        input = requestPositiveInteger();
                        hasCapacity = Integer.parseInt(input) <= conferenceEventManager
                                .getRoomRepository().getRoomsMap().get(temp.get(4)).getCapacity();
                        if (!hasCapacity) {
                            roomView.displayRoomNotEnoughCapacity();
                        }
                    } while (!hasCapacity);
                } else {
                    input = br.readLine().trim();
                }

                if (!input.equalsIgnoreCase("")) {
                    temp.add(input);
                }
            }
        } catch (IOException e) {
            conferenceSubView.displaySomethingWentWrong();
        }
        try {
            ConferenceEvent ce = conferenceEventManager.createConferenceEvent(temp.get(0),
                    Integer.parseInt(temp.get(5)), EVENT_NAME_TO_TYPE.get(tempEventType));

            boolean eventCreated = conferenceEventManager.addEvent(roles,
                    conferenceEventManager.createDateInterval(start, end),
                    temp.get(4),
                    ce);

            if (eventCreated) {
                conferenceSubView.displaySuccessAddedEvent(ce.getEventName());
                appTrafficManager.updateNumOfConferencesCreatedAllTime();
            } else {
                conferenceSubView.displayEventNotAdded();
            }
        }  catch (IndexOutOfBoundsException e) {
            conferenceSubView.displayEmptyEvent();
        }
    }

    /**
     * Helper method used in {@link ConferenceEventCreationSystem} that allows an Organizer user to eo choose whether
     * or not to view rooms that accommodate certain amenities while creating a conference event to have the conference
     * event take place in a suitable room. If desired, prompts a series of yes/no questions of desired amenities that
     * the displayed rooms will have. If otherwise or there are no rooms that fit such criteria, displays all rooms.
     */
    private void promptAmenity() {
        PropertiesIterator promptsAmenity =
                new PropertiesIterator("phase2/src/dao/amenity_properties.txt");
        List<Amenity> tempAmenity = new ArrayList<Amenity>();

        conferenceSubView.displayAmenityDesiredTitle();

        while (promptsAmenity.hasNext()) {
            String sAmenity;
            conferenceSubView.displayString(sAmenity = promptsAmenity.next());
            String inputAmenity = requestBoolean();

            if (inputAmenity.equalsIgnoreCase("y")) {
                tempAmenity.add(AMENITY_NAME_TO_TYPE.get(sAmenity.substring(0, sAmenity.length() -2)));
            }
        }

        if (conferenceEventManager.getRoomRepository()
                .getRoomNamesHasAllOfAmenities(tempAmenity).size() == 0) {
            roomView.displayNoRooms();
            roomView.displayRooms(conferenceEventManager.getRoomRepository().getRoomsNameList(),
                    conferenceEventManager.getRoomRepository());
        } else {

            roomView.displayRoomsMatchingCriteria();
            roomView.displayRooms(conferenceEventManager.getRoomRepository()
                            .getRoomNamesHasAllOfAmenities(tempAmenity),
                    conferenceEventManager.getRoomRepository());
        }
    }


}