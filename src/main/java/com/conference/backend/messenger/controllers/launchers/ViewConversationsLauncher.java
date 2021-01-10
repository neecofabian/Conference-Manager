package com.conference.backend.messenger.controllers.launchers;

import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.data.utils.Role;
import com.conference.backend.data.utils.Value;
import com.conference.backend.data.utils.base.Launcher;
import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.messenger.controllers.systems.OpenConversationSystem;
import com.conference.backend.messenger.managers.MessengerManager;
import com.conference.backend.security.CustomRequestCache;
import com.conference.backend.users.UserLoginManager;
import com.conference.backend.users.UserManager;
import com.conference.frontend.ConferenceUtils;
import com.conference.frontend.messenger.MessengerView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewConversationsLauncher extends Launcher implements Startable {
    private final String HOME = "0";
    private final String START_NEW_CONVERSATION = "1";
    private final String VIEW_ARCHIVED_CONVERSATIONS = "2";
    private final String OPEN_CONVERSATION = "3";

    private final Map<String, Value> MENU_CHOICE_TO_NAME_AND_ROLES_1 = new HashMap<String, Value>() {{
        put(HOME, new Value("Go home", Role.values()));
        put(START_NEW_CONVERSATION, new Value("Start a new conversation", Role.values()));
        put(VIEW_ARCHIVED_CONVERSATIONS, new Value("View archived conversations", Role.values()));
    }};

    private final Map<String, Value> MENU_CHOICE_TO_NAME_AND_ROLES_2 = new HashMap<String, Value>() {{
        put(HOME, new Value("Go home", Role.values()));
        put(START_NEW_CONVERSATION, new Value("Start a new conversation", Role.values()));
        put(VIEW_ARCHIVED_CONVERSATIONS, new Value("View archived conversations", Role.values()));
        put(OPEN_CONVERSATION, new Value("Open a Conversation", Role.values()));
    }};

    private final Map<String, Value> MENU_CHOICE_TO_NAME_AND_ROLES_3 = new HashMap<String, Value>() {{
        put(HOME, new Value("Go home", Role.values()));
        put(START_NEW_CONVERSATION, new Value("Start a new conversation", Role.values()));
        put(OPEN_CONVERSATION, new Value("Open a Conversation", Role.values()));
    }};


    private final UserLoginManager userLoginManager;
    private final MessengerManager messengerManager;
    private final UserManager userManager;
    private final ConferenceUtils conferenceUtils;
    private final MessengerView messengerView;
    private final SendMessageLauncher sendMessageLauncher;
    private final OpenConversationSystem openConversationSystem;
    private final CustomRequestCache<List<String>> customRequestCache;

    /**
     * Constructs a new instance of ViewConversationsLauncher with the given data.
     *
     * @param conferenceEventManager The ConferenceEventManager associated with this instance
     * @param userManager The UserManager associated with this instance
     * @param userLoginManager The UserLoginManager associated with this instance
     * @param messengerManager he MessengerManager associated with this instance
     */
    public ViewConversationsLauncher(UserLoginManager userLoginManager,
                                     ConferenceEventManager conferenceEventManager,
                                     MessengerManager messengerManager,
                                     UserManager userManager) {
        this.userLoginManager = userLoginManager;
        this.messengerManager = messengerManager;
        this.userManager = userManager;
        this.conferenceUtils = new ConferenceUtils();
        this.messengerView = new MessengerView();
        this.sendMessageLauncher = new SendMessageLauncher(conferenceEventManager,
                userLoginManager, messengerManager, userManager);
        customRequestCache = new CustomRequestCache<>(1);
        this.openConversationSystem = new OpenConversationSystem(userLoginManager,
                messengerManager,
                customRequestCache);
    }

    /**
     * Reads input from the client and processes input to check if it is valid.
     * If the input is valid, start a new thread.
     */
    @Override
    public void start() {
        List<String> conversationList = messengerManager
                .getMessageManager(userLoginManager.getCurrentUserId()).getUnarchivedConversationsIds();

        if (conversationList.isEmpty()) {
            messengerView.displayNoExistingConversationsMessageNoChoice();
            requestInputHelper(MENU_CHOICE_TO_NAME_AND_ROLES_1);
        } else {
            messengerView.displaySeeBelowForConversationsMessage();
            viewConversations(conversationList);
            requestInputHelper(MENU_CHOICE_TO_NAME_AND_ROLES_2);
        }
    }

    /**
     * A helper method for requesting input from a user
     *
     * @param map A HashMap of available user input options and their associated processes
     */
    private void requestInputHelper(Map<String, Value> map) {
        List<String> validOptions = conferenceUtils
                .getValidOptionsForUser(userManager.getRolesByUserId(userLoginManager.getCurrentUserId()),
                        map);
        this.messengerView.displayOptions(validOptions, map);
        try {
            String choice = this.requestInput(validOptions, map, messengerView);
            process(choice);
        } catch (IOException e) {
            this.messengerView.displaySomethingWentWrong();
        }
    }

    /**
     * Displays a list of conversations
     *
     * @param conversationList A List of conversation objects
     */
    private void viewConversations(List<String> conversationList) {
        for (int i = 0; i < conversationList.size(); i++) {
            StringBuilder thisConvo = new StringBuilder();
            String conversationId = conversationList.get(i);
            for (String id : this.messengerManager.getConversationMemberIdsByConvoId(conversationId)) {
                if (!(id.equals(userLoginManager.getCurrentUserId()))) {
                    thisConvo.append(this.messengerManager.getUserManager().getNameById(id)).append(", ");
                }
            }
            String status = this.messengerManager.getMessageManager(userLoginManager
                    .getCurrentUserId()).getConversationsMap()
                    .get(conversationId);
            String members = thisConvo.substring(0, thisConvo.length() - 2);
            messengerView.displayConversation(i, members, status);
        }
    }
    /**
     * Processes the string input from the current user and starts the system/launcher associated with that choice.
     *
     * @param choice a sting input from the current user
     */
    private void process(String choice) {
        switch(choice) {
            case HOME:
                break;
            case START_NEW_CONVERSATION:
                this.sendMessageLauncher.start();
                break;
            case VIEW_ARCHIVED_CONVERSATIONS:
                List<String> conversationList = messengerManager
                        .getMessageManager(userLoginManager.getCurrentUserId()).getArchivedConversationsIds();

                if (conversationList.isEmpty()) {
                    messengerView.displayNoExistingConversationsMessageNoChoice();
                } else {
                    viewConversations(conversationList);
                    customRequestCache.saveRequest(conversationList);
                    requestInputHelper(MENU_CHOICE_TO_NAME_AND_ROLES_3);
                }
                break;
            case OPEN_CONVERSATION:
                if (customRequestCache.isEmpty()) {
                    customRequestCache.saveRequest(messengerManager
                            .getMessageManager(userLoginManager.getCurrentUserId()).getUnarchivedConversationsIds());
                }
                this.openConversationSystem.start();
                break;
        }
    }
}
