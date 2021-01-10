package com.conference.backend.messenger.controllers.launchers;

import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.data.utils.Role;
import com.conference.backend.data.utils.Value;
import com.conference.backend.data.utils.base.Launcher;
import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.messenger.controllers.systems.SendMessageSystem;
import com.conference.backend.messenger.managers.MessengerManager;
import com.conference.backend.users.UserLoginManager;
import com.conference.backend.users.UserManager;
import com.conference.frontend.ConferenceUtils;
import com.conference.frontend.messenger.MessengerView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendMessageLauncher extends Launcher implements Startable {
    private final String HOME = "0";
    private final String ATTENDEES_OF_ALL_EVENT = "1";
    private final String ATTENDEES_OF_ONE_EVENT = "2";
    private final String ALL_ATTENDEES = "3";
    private final String ALL_SPEAKERS = "4";
    private final String ONE_ATTENDEE_OR_SPEAKER = "5";

    private final Map<String, Value> MENU_CHOICE_TO_NAME_AND_ROLES = new HashMap<String, Value>() {{
        put(HOME, new Value("Go home", Role.values()));
        put(ATTENDEES_OF_ALL_EVENT, new Value("Attendees of All My Event(s)", Role.SPEAKER));
        put(ATTENDEES_OF_ONE_EVENT, new Value("Attendees of One of My Events", Role.SPEAKER));
        put(ALL_ATTENDEES, new Value("All Attendees", Role.ORGANIZER));
        put(ALL_SPEAKERS, new Value("All Speakers", Role.ORGANIZER));
        put(ONE_ATTENDEE_OR_SPEAKER, new Value("Send to anyone", Role.values()));
    }};

    private final ConferenceEventManager conferenceEventManager;
    private final UserLoginManager userLoginManager;
    private final MessengerView messengerView;
    private final ConferenceUtils conferenceUtils;
    private final UserManager userManager;
    private final SendMessageSystem sendMessageSystem;

    /**
     * Constructs a new instance of SendMessageLauncher with the given data.
     *
     * @param conferenceEventManager The ConferenceEventManager associated with this instance
     * @param userManager The UserManager associated with this instance
     * @param userLoginManager The UserLoginManager associated with this instance
     * @param messengerManager he MessengerManager associated with this instance
     */
    public SendMessageLauncher(ConferenceEventManager conferenceEventManager,
                               UserLoginManager userLoginManager,
                               MessengerManager messengerManager, UserManager userManager) {
        this.conferenceEventManager = conferenceEventManager;
        this.userLoginManager = userLoginManager;
        this.userManager = userManager;
        this.messengerView = new MessengerView();
        this.conferenceUtils = new ConferenceUtils();
        this.sendMessageSystem = new SendMessageSystem(conferenceEventManager,
                messengerView, messengerManager, userLoginManager);
    }

    /**
     * Reads input from the client and processes input to check if it is valid.
     * If the input is valid, start a new thread.
     */
    @Override
    public void start() {
        List<String> validOptions = conferenceUtils
                .getValidOptionsForUser(userManager.getRolesByUserId(userLoginManager.getCurrentUserId()),
                        MENU_CHOICE_TO_NAME_AND_ROLES);
        this.messengerView.displaySendMessageGreeting();
        this.messengerView.displayOptions(validOptions, MENU_CHOICE_TO_NAME_AND_ROLES);
        try {
            String choice = this.requestInput(validOptions, MENU_CHOICE_TO_NAME_AND_ROLES, messengerView);
            process(choice);
        } catch (IOException e) {
            this.messengerView.displaySomethingWentWrong();
        }
    }

    /**
     * Processes the string input from the current user and starts the system/launcher associated with that choice.
     *
     * @param choice a sting input from the current user
     */
    private void process(String choice) {
        switch (choice) {
            case ATTENDEES_OF_ALL_EVENT:
                sendMessageSystem.sendToAttendeesOfEvent();
                break;
            case ALL_SPEAKERS:
                sendMessageSystem.sendToRole(Role.SPEAKER);
                break;
            case ALL_ATTENDEES:
                sendMessageSystem.sendToRole(Role.ATTENDEE);
                break;
            case ONE_ATTENDEE_OR_SPEAKER:
                sendMessageSystem.sendToAttendeeOrSpeaker();
                break;
            case ATTENDEES_OF_ONE_EVENT:
                List<String> eventNames = conferenceEventManager
                        .getSpeakerEventNames(userLoginManager.getCurrentUserId());
                if (eventNames.isEmpty()) {
                    messengerView.displayNoTalksError();
                } else {
                    sendMessageSystem.sendToAttendeeOfOneEvent();
                }
                break;
        }
    }
}
