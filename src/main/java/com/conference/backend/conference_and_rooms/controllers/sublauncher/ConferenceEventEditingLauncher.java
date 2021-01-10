package com.conference.backend.conference_and_rooms.controllers.sublauncher;

import com.conference.backend.data.utils.base.Launcher;
import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.conference_and_rooms.controllers.subcontrollers.ConferenceEventAddSpeakerSystem;
import com.conference.backend.conference_and_rooms.controllers.subcontrollers.ConferenceEventCapacitySystem;
import com.conference.backend.data.utils.Role;
import com.conference.backend.data.utils.Value;
import com.conference.backend.exception.UserNotFoundException;
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

public class ConferenceEventEditingLauncher extends Launcher implements Startable {
    private final String HOME = "0";
    private final String ADD_SPEAKER = "1";
    private final String CHANGE_CAPACITY = "2";

    /*
     *  Hashmap used to map an integer to a user friendly string along with the roles required.
     */
    private final Map<String, Value> MENU_CHOICE_TO_NAME_AND_ROLES = new HashMap<String, Value>() {{
        put(HOME, new Value("Go home", Role.values()));
        put(ADD_SPEAKER, new Value("Add a speaker to an Event", Role.ORGANIZER));
        put(CHANGE_CAPACITY, new Value("Change the Capacity of an Event", Role.ORGANIZER));
    }};

    private final ConferenceEventAddSpeakerSystem conferenceEventAddSpeakerSystem;
    private final ConferenceEventCapacitySystem conferenceEventCapacitySystem;

    private final ConferenceEventManager conferenceEventManager;
    private final UserLoginManager userLoginManager;
    private final AppTrafficManager appTrafficManager;
    private final MessengerManager messengerManager;

    ConferenceView conferenceView;
    ConferenceUtils conferenceUtils;

    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public ConferenceEventEditingLauncher(UserLoginManager userLoginManager,
                                          AppTrafficManager appTrafficManager,
                                          ConferenceEventManager conferenceEventManager,
                                          MessengerManager messengerManager) {

        this.userLoginManager = userLoginManager;
        this.appTrafficManager = appTrafficManager;
        this.conferenceEventManager = conferenceEventManager;
        this.messengerManager = messengerManager;

        // INITIALIZE CONTROLLERS
        conferenceEventAddSpeakerSystem = new ConferenceEventAddSpeakerSystem(conferenceEventManager,
                userLoginManager, appTrafficManager);

        conferenceEventCapacitySystem = new ConferenceEventCapacitySystem(conferenceEventManager,
                userLoginManager, appTrafficManager);

        conferenceView = new ConferenceView();
        conferenceUtils = new ConferenceUtils();
    }

    @Override
    public void start() {
        UserManager userManager = userLoginManager.getUserRepository();
        List<String> validOptions = conferenceUtils
                .getValidOptionsForUser(userManager.getRolesByUserId(userLoginManager.getCurrentUserId()),
                        MENU_CHOICE_TO_NAME_AND_ROLES);

        conferenceView.displayTitleMenuOptionEditing();
        conferenceView.displayOptions(validOptions, MENU_CHOICE_TO_NAME_AND_ROLES);

        try {
            String choice = this.requestInput(validOptions, MENU_CHOICE_TO_NAME_AND_ROLES, conferenceView);
            process(choice);
        } catch (IOException | UserNotFoundException exception) {
            conferenceView.displaySomethingWentWrong();
        }
    }

    private void process(String choice) throws IOException, UserNotFoundException {
        switch (choice) {
            case HOME:
                break;
            case ADD_SPEAKER:
                conferenceEventAddSpeakerSystem.start();
                break;
            case CHANGE_CAPACITY:
                conferenceEventCapacitySystem.start();
                break;
        }
    }


}
