package com.conference.backend.messenger.controllers.launchers;

import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.data.utils.Role;
import com.conference.backend.data.utils.Value;
import com.conference.backend.data.utils.base.Launcher;
import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.messenger.managers.MessengerManager;
import com.conference.backend.users.UserLoginManager;
import com.conference.backend.users.UserManager;
import com.conference.frontend.ConferenceUtils;
import com.conference.frontend.messenger.MessengerView;

import java.io.IOException;
import java.util.*;

public class MessengerLauncher extends Launcher implements Startable {
    private final String HOME = "0";
    private final String SEND_MESSAGE = "1";
    private final String VIEW_CONVERSATIONS = "2";

    /*
     *  Hashmap used to map an integer to a user friendly string along with the roles required.
     */
    private final Map<String, Value> MENU_CHOICE_TO_NAME_AND_ROLES = new HashMap<String, Value>() {{
        put(HOME, new Value("Go home", Role.values()));
        put(SEND_MESSAGE, new Value("Compose a new message", Role.values()));
        put(VIEW_CONVERSATIONS, new Value("Manage your conversations and reply to messages", Role.values()));
    }};

    private final SendMessageLauncher sendMessageLauncher;
    private final ViewConversationsLauncher viewConversationsLauncher;
    private final ConferenceEventManager conferenceEventManager;
    private final UserManager userManager;
    private final UserLoginManager userLoginManager;

    private final MessengerView messengerView;
    private final ConferenceUtils conferenceUtils;
    private MessengerManager messengerManager;

    /**
     * Constructs a new instance of MessengerLauncher with the given data.
     *
     * @param conferenceEventManager The ConferenceEventManager associated with this instance
     * @param userManager The UserManager associated with this instance
     * @param userLoginManager The UserLoginManager associated with this instance
     * @param messengerManager he MessengerManager associated with this instance
     */
    public MessengerLauncher(ConferenceEventManager conferenceEventManager,
                             UserManager userManager, UserLoginManager userLoginManager,
                             MessengerManager messengerManager) {
        this.conferenceEventManager = conferenceEventManager;
        this.userManager = userManager;
        this.userLoginManager = userLoginManager;
        this.messengerManager = messengerManager;

        this.messengerView = new MessengerView();
        this.conferenceUtils = new ConferenceUtils();

        this.sendMessageLauncher = new SendMessageLauncher(conferenceEventManager,
                userLoginManager, messengerManager, userManager);
        this.viewConversationsLauncher = new ViewConversationsLauncher(userLoginManager,
                conferenceEventManager, messengerManager, userManager);
    }

    /**
     * Reads input from the client and processes input to check if it is valid.
     * If the input is valid, start a new thread.
     */
    @Override
    public void start() {
        this.messengerManager.update();
        List<String> validOptions = conferenceUtils
                .getValidOptionsForUser(userManager.getRolesByUserId(userLoginManager.getCurrentUserId()),
                        MENU_CHOICE_TO_NAME_AND_ROLES);
        this.messengerView.displayGreeting(this.userManager.getRolesByUserId(this.userLoginManager.getCurrentUserId()));
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
        switch(choice){
            case HOME:
                break;
            case SEND_MESSAGE:
                this.sendMessageLauncher.start();
                break;
            case VIEW_CONVERSATIONS:
                this.viewConversationsLauncher.start();
                break;
        }
    }
}
