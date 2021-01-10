package com.conference.backend.conference_and_rooms.controllers;

import com.conference.backend.data.utils.base.Launcher;
import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.conference_and_rooms.controllers.sublauncher.ConferenceEventScheduleLauncher;
import com.conference.backend.conference_and_rooms.controllers.sublauncher.ConferenceEventViewingLauncher;
import com.conference.backend.conference_and_rooms.entities.*;
import com.conference.backend.data.utils.*;
import com.conference.backend.messenger.managers.MessengerManager;
import com.conference.backend.users.AppTrafficManager;
import com.conference.backend.users.UserLoginManager;
import com.conference.backend.users.UserManager;
import com.conference.frontend.ConferenceUtils;
import com.conference.frontend.conference.ConferenceView;

import java.io.IOException;
import java.util.*;

/**
 * Controller class for accessing {@link ConferenceEventManager}
 *
 */
public class ConferenceEventLauncher extends Launcher implements Startable {
    private final String HOME = "0";
    private final String SEE_SCHEDULE = "1";
    private final String SEE_SPEAKER_EVENTS = "2";
    private final String SEE_ALL_CONFERENCE = "3";

    /*
     *  Hashmap used to map an integer to a user friendly string along with the roles required.
     */
    private final Map<String, Value> MENU_CHOICE_TO_NAME_AND_ROLES = new HashMap<String, Value>() {{
        put(HOME, new Value("Go home", Role.values()));
        put(SEE_SCHEDULE, new Value("See a Sorted Event Schedule", Role.values()));
        put(SEE_SPEAKER_EVENTS, new Value("See Events You're Speaking in", Role.SPEAKER));
        put(SEE_ALL_CONFERENCE, new Value("See All Conference Events and Edit", Role.ORGANIZER));
    }};

    private final ConferenceEventScheduleLauncher conferenceEventScheduleLauncher;
    private final ConferenceEventViewingLauncher conferenceEventViewingLauncher;

    private final ConferenceEventManager conferenceEventManager;
    private final UserManager userManager;
    private final UserLoginManager userLoginManager;
    private final AppTrafficManager appTrafficManager;
    private final MessengerManager messengerManager;

    private final ConferenceView conferenceView;
    private final ConferenceUtils conferenceUtils;

    public ConferenceEventLauncher(UserLoginManager userLoginManager,
                                       AppTrafficManager appTrafficManager,
                                       ConferenceEventManager conferenceEventManager,
                                       MessengerManager messengerManager) {

        this.conferenceEventManager = conferenceEventManager;
        this.userLoginManager = userLoginManager;
        this.userManager = userLoginManager.getUserRepository();
        this.appTrafficManager = appTrafficManager;
        this.messengerManager = messengerManager;

        conferenceEventViewingLauncher = new ConferenceEventViewingLauncher(userLoginManager,
                appTrafficManager, conferenceEventManager, messengerManager);
        conferenceEventScheduleLauncher = new ConferenceEventScheduleLauncher(userLoginManager,
                appTrafficManager, conferenceEventManager, messengerManager);

        conferenceView = new ConferenceView();
        conferenceUtils = new ConferenceUtils();
    }

    /**
     * Prompts Organizers to see a schedule of {@link ConferenceEvent} objects with relevant info (ie. Speaker assigned
     * to the event) or go to the ConferenceEvent editor to create, delete, edit ConferenceEvents. Also give speakers
     * the option to see events they are speaking in.
     *
     */
    @Override
    public void start() {
        List<String> validOptions = conferenceUtils
                .getValidOptionsForUser(userManager.getRolesByUserId(userLoginManager.getCurrentUserId()),
                        MENU_CHOICE_TO_NAME_AND_ROLES);

        conferenceView.displayTitleConferenceEventMenu();
        conferenceView.displayOptions(validOptions, MENU_CHOICE_TO_NAME_AND_ROLES);

        try {
            String choice = this.requestInput(validOptions, MENU_CHOICE_TO_NAME_AND_ROLES, conferenceView);
            process(choice);
        } catch (IOException e) {
            conferenceView.displaySomethingWentWrong();
        }
    }

    /**
     * Checks the user's selected option and call the appropriate system controller.
     * @param choice the menu choice
     */
    private void process(String choice) {
        switch (choice) {
            case HOME:
                break;
            case SEE_SCHEDULE:
                conferenceEventScheduleLauncher.start();
                break;
            case SEE_SPEAKER_EVENTS:
                conferenceEventScheduleLauncher.seeSpeakerEvents(userLoginManager.getCurrentUserId());
                break;
            case SEE_ALL_CONFERENCE:
                conferenceEventViewingLauncher.start();
                break;
        }
    }
}
