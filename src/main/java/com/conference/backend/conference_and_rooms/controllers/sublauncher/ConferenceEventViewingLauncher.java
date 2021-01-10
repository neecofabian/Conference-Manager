package com.conference.backend.conference_and_rooms.controllers.sublauncher;

import com.conference.backend.data.utils.base.Launcher;
import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.conference_and_rooms.controllers.subcontrollers.ConferenceEventCreationSystem;
import com.conference.backend.conference_and_rooms.controllers.subcontrollers.ConferenceEventDeletionSystem;
import com.conference.backend.conference_and_rooms.entities.ConferenceEvent;
import com.conference.backend.data.utils.Role;
import com.conference.backend.data.utils.Value;
import com.conference.backend.messenger.managers.MessengerManager;
import com.conference.backend.users.AppTrafficManager;
import com.conference.backend.users.UserLoginManager;
import com.conference.backend.users.UserManager;
import com.conference.frontend.ConferenceUtils;
import com.conference.frontend.conference.ConferenceView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConferenceEventViewingLauncher extends Launcher implements Startable {
    private final String HOME = "0";
    private final String CREATE_EVENT = "1";
    private final String REMOVE_EVENT = "2";
    private final String EDIT_CONFERENCE_EVENTS = "3";

    /*
     *  Hashmap used to map an integer to a user friendly string along with the roles required.
     */
    private final Map<String, Value> MENU_CHOICE_TO_NAME_AND_ROLES = new HashMap<String, Value>() {{
        put(HOME, new Value("Go home", Role.values()));
        put(CREATE_EVENT, new Value("Create a Conference Event", Role.ORGANIZER));
        put(REMOVE_EVENT, new Value("Remove a Conference Event", Role.ORGANIZER));
        put(EDIT_CONFERENCE_EVENTS, new Value("Edit a Conference Event", Role.ORGANIZER));
    }};

    private final ConferenceEventEditingLauncher conferenceEventEditingLauncher;
    private final ConferenceEventCreationSystem conferenceEventCreationSystem;
    private final ConferenceEventDeletionSystem conferenceEventDeletionSystem;

    private final ConferenceEventManager conferenceEventManager;
    private final UserLoginManager userLoginManager;
    private final AppTrafficManager appTrafficManager;
    private final MessengerManager messengerManager;

    // TODO: CHANGE TO PRIVATE
    ConferenceView conferenceView;
    ConferenceUtils conferenceUtils;

    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public ConferenceEventViewingLauncher(UserLoginManager userLoginManager,
                                          AppTrafficManager appTrafficManager,
                                          ConferenceEventManager conferenceEventManager,
                                          MessengerManager messengerManager) {

        this.userLoginManager = userLoginManager;
        this.appTrafficManager = appTrafficManager;
        this.conferenceEventManager = conferenceEventManager;
        this.messengerManager = messengerManager;

        // INITIALIZE CONTROLLERS

        conferenceEventEditingLauncher = new ConferenceEventEditingLauncher(userLoginManager, appTrafficManager,
                conferenceEventManager, messengerManager);
        conferenceEventCreationSystem = new ConferenceEventCreationSystem(conferenceEventManager, userLoginManager,
                appTrafficManager);
        conferenceEventDeletionSystem = new ConferenceEventDeletionSystem(conferenceEventManager, userLoginManager,
                appTrafficManager);

        conferenceView = new ConferenceView();
        conferenceUtils = new ConferenceUtils();
    }

    /**
     * Allows Organizers to see all {@link ConferenceEvent} objects that are registered in any given room throughout the
     * schedule, along with relevant info (ie. Speaker assigned to the event). Further allows the Organizers to delete
     * an existing ConferenceEvent from the schedule and assign a Speaker user to an existing ConferenceEvent through
     * calling helper methods.
     *
     */
    @Override
    public void start() {
//        while(true) {
            conferenceView.displayTitleAllConferenceEvents();

            List<String> allConferenceEventNames = conferenceEventManager.getConferenceEventNamesList();

            conferenceView.displayTitleMenuOptionsSeeConference();
            conferenceView.printConferenceEvents(allConferenceEventNames, conferenceEventManager,
                    userLoginManager.getUserRepository());

            UserManager userManager = userLoginManager.getUserRepository();
            List<String> validOptions = conferenceUtils
                    .getValidOptionsForUser(userManager.getRolesByUserId(userLoginManager.getCurrentUserId()),
                            MENU_CHOICE_TO_NAME_AND_ROLES);

            conferenceView.displayOptions(validOptions, MENU_CHOICE_TO_NAME_AND_ROLES);

            try {
                String choice = this.requestInput(validOptions, MENU_CHOICE_TO_NAME_AND_ROLES, conferenceView);
                process(choice);
            } catch (IOException e) {
                conferenceView.displaySomethingWentWrong();
            }
//        }
    }

    private void process(String choice) throws IOException {
        switch (choice) {
            case HOME:
                break;
            case CREATE_EVENT:
                conferenceEventCreationSystem.start();
                break;
            case REMOVE_EVENT:
                conferenceEventDeletionSystem.start();
                break;
            case EDIT_CONFERENCE_EVENTS:
                conferenceEventEditingLauncher.start();
                break;
        }
    }
}
