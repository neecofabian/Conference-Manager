package com.conference.backend.users.controllers.event_signup_and_viewing;

import com.conference.backend.conference_and_rooms.entities.ConferenceEvent;
import com.conference.backend.data.utils.Role;
import com.conference.backend.data.utils.Value;
import com.conference.backend.data.utils.base.Launcher;
import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.users.AppTrafficManager;
import com.conference.backend.users.UserLoginManager;
import com.conference.backend.users.UserManager;
import com.conference.backend.users.controllers.event_signup_and_viewing.subcontrollers.UserConferenceCancelSystem;
import com.conference.backend.users.controllers.event_signup_and_viewing.subcontrollers.UserConferenceSignUpSystem;
import com.conference.frontend.ConferenceUtils;
import com.conference.frontend.users.UserView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for setting up User controllers.
 *
 */
public class UserConferenceLauncher extends Launcher implements Startable {
    private final String HOME = "0";
    private final String SIGNUP_CONFERENCE_EVENTS = "1";
    private final String SEE_CONFERENCE_EVENTS = "2";

    /*
     *  Hashmap used to map an integer to a user friendly string along with the roles required.
     */
    private final Map<String, Value> MENU_CHOICE_TO_NAME_AND_ROLES = new HashMap<String, Value>() {{
        put(SIGNUP_CONFERENCE_EVENTS, new Value("Signup for Conference Events", Role.ATTENDEE));
        put(SEE_CONFERENCE_EVENTS, new Value("See your Conference Events", Role.ATTENDEE));
        put(HOME, new Value("Go home"));
    }};

    private final UserConferenceCancelSystem userConferenceCancelSystem;
    private final UserConferenceSignUpSystem userConferenceSignUpSystem;

    private final UserLoginManager userLoginManager;
    private final ConferenceUtils conferenceUtils;

    private final UserView userView;

    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /**
     *
     * Constructs an instance of this {@code Runnable} class with the given parameters.
     *
     * @param userLoginManager          The {@code UserLoginManager} storing the current user and repository of users
     * @param appTrafficManager         The {@code AppTrafficManager} to update statistics
     * @param conferenceEventManager    The {@code ConferenceEventManager}
     *                                  storing the repository of {@link ConferenceEvent}s
     */
    public UserConferenceLauncher(UserLoginManager userLoginManager, AppTrafficManager appTrafficManager,
                                  ConferenceEventManager conferenceEventManager) {
        this.userLoginManager = userLoginManager;

        this.userConferenceCancelSystem = new UserConferenceCancelSystem(userLoginManager.getUserRepository(),
                userLoginManager, conferenceEventManager);
        this.userConferenceSignUpSystem = new UserConferenceSignUpSystem(userLoginManager.getUserRepository(),
                userLoginManager, appTrafficManager, conferenceEventManager);

        conferenceUtils = new ConferenceUtils();
        userView = new UserView();
    }

    /**
     * Reads input from the client and processes input to check if it is valid.
     * If the input is valid, start a new thread.
     */
    @Override
    public void start() {
        UserManager userManager = userLoginManager.getUserRepository();
        List<String> validOptions = conferenceUtils
                .getValidOptionsForUser(userManager.getRolesByUserId(userLoginManager.getCurrentUserId()),
                MENU_CHOICE_TO_NAME_AND_ROLES);

        userView.displayTitle();
        userView.displayOptions(validOptions, MENU_CHOICE_TO_NAME_AND_ROLES);

        try {
            String choice = this.requestInput(validOptions, MENU_CHOICE_TO_NAME_AND_ROLES, userView);
            process(choice);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes in input from the client and either goes back to the main menu, calls {@code UserConferenceCancelSystem}
     * to see and cancel any conference events, or calls {@code UserConferenceSignUpSystem} to allow the user to
     * sign up for any event.
     *
     * @param choice The client's input.
     */
    private void process(String choice) {
        switch (choice) {
            case HOME:
                break;
            case SEE_CONFERENCE_EVENTS:
                userConferenceCancelSystem.start();
                break;
            case SIGNUP_CONFERENCE_EVENTS:
                userConferenceSignUpSystem.start();
                break;
        }
    }
}