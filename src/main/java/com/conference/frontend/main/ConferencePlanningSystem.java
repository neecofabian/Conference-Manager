package com.conference.frontend.main;

import com.conference.backend.conference_and_rooms.controllers.RoomLauncher;
import com.conference.backend.data.utils.*;
import com.conference.backend.data.utils.base.Exitable;
import com.conference.backend.data.utils.base.Initializer;
import com.conference.backend.data.utils.base.Launcher;
import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.messenger.controllers.launchers.MessengerLauncher;
import com.conference.backend.security.CustomRequestCache;
import com.conference.backend.security.DataGateway;
import com.conference.backend.conference_and_rooms.controllers.ConferenceEventLauncher;
import com.conference.backend.users.AppTrafficManager;
import com.conference.backend.users.controllers.subcontrollers.UserLoginSystem;
import com.conference.backend.users.controllers.subcontrollers.UserSignUpSystem;
import com.conference.backend.users.controllers.event_signup_and_viewing.UserConferenceLauncher;
import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.messenger.managers.MessengerManager;
import com.conference.backend.users.UserLoginManager;
import com.conference.backend.users.UserManager;
import com.conference.frontend.ConferenceUtils;
import com.conference.frontend.DashboardDataView;

import java.io.IOException;
import java.util.*;

public class ConferencePlanningSystem extends Launcher implements
        Startable, Exitable, Initializer, Observer {

    // Twilio and Discord API Token/SID
    private final String ACCOUNT_SID = "REDACTED";
    private final String AUTH_TOKEN = "REDACTED";
    private final String DISCORD_BOT_TOKEN = "REDACTED";

    // Constants for the menu options
    private final String QUIT = "9";
    private final String LOGIN = "0";
    private final String SIGNUP = "1";

    private final String SEE_ALL_CONFERENCE_EVENTS = "0";
    private final String SIGNUP_AND_SEE_YOUR_CONFERENCE_EVENTS = "1";

    private final String SEE_ROOMS = "5";
    private final String MESSENGER = "4";

    private final String CREATE_ORGANIZER = "10";
    private final String CREATE_ATTENDEE = "11";
    private final String CREATE_VIP = "12";
    private final String CREATE_SPEAKER = "13";
    private final String SEE_STATS = "14";
    private final String LOGOUT = "8";

    /*
     *  Hashmap used to map an integer to a user friendly string along with the roles required.
     */
    private final Map<String, Value> MENU_CHOICE_TO_NAME_AND_ROLES2 = new HashMap<String, Value>() {{
        put(SEE_ALL_CONFERENCE_EVENTS, new Value("See all Conference Events", Role.values()));
        put(SIGNUP_AND_SEE_YOUR_CONFERENCE_EVENTS, new Value("Signup and See your Conference Events", Role.ATTENDEE));
        put(SEE_ROOMS, new Value("All Rooms", Role.ORGANIZER));
        put(CREATE_SPEAKER, new Value("Create a speaker account", Role.ORGANIZER));
        put(CREATE_ORGANIZER, new Value("Create a organizer account", Role.ORGANIZER));
        put(CREATE_ATTENDEE, new Value("Create a attendee account", Role.ORGANIZER));
        put(CREATE_VIP, new Value("Create a V.I.P. account", Role.ORGANIZER));
        put(SEE_STATS, new Value("See statistics of Conference", Role.ORGANIZER));
        put(LOGOUT, new Value("Logout", Role.values()));
        put(QUIT, new Value("Quit", Role.values()));
        put(MESSENGER, new Value("Messenger", Role.values()));
    }};

    /*
     *  Hashmap used to map an integer to a user friendly string for the main menu options.
     */
    private final Map<String, Value> MENU_STARTUP_TO_NAME = new HashMap<String, Value>() {{
        put(LOGIN, new Value("Login"));
        put(SIGNUP, new Value("Sign Up"));
        put(QUIT, new Value("Quit"));
    }};

    private final int CUSTOM_REQUEST_CAPACITY = 100;

    private final String DAO_FILE_PATH = "phase2/src/dao/";

    // Sessional stats
    private int numLogins;
    private int numSignups;
    private int numLogouts;

    private final ConferenceUtils conferenceUtils;
    private final MainView mainView;

    private final DataGateway<UserManager> dataUserGateway;
    private final DataGateway<ConferenceEventManager> dataEventGateway;
    private final DataGateway<MessengerManager> dataMessengerGateway;
    private final DataGateway<AppTrafficManager> dataAppTrafficGateway;

    private AppTrafficManager appTrafficManager;
    private DashboardDataView dashboardDataView;

    private UserManager userManager;
    private UserLoginManager userLoginManager;

    private ConferenceEventManager conferenceEventManager;
    private ConferenceEventLauncher conferenceEventLauncher;

    private MessengerManager messengerManager;

    private DiscordEventNotificationGateway discordEventNotificationGateway;
    private QRCodeGeneratorGateway qrCodeGeneratorGateway;
//    private TwilioEventNotificationGateway twilioEventNotificationGateway;

    private Startable userConferenceLauncher;
    private Startable roomLauncher;
    private Startable messengerLauncher;

    private Startable userLoginSystem;
    private Startable userSignUpSystem;

    private final CustomRequestCache<Role> roleCustomRequestCache;

    /**
     * No-arg constructor to initialize the {@code BufferedReader} and {@link ConferenceUtils}
     */
    public ConferencePlanningSystem() {
        dataUserGateway = new DataGateway<>();
        dataEventGateway = new DataGateway<>();
        dataMessengerGateway = new DataGateway<>();
        dataAppTrafficGateway = new DataGateway<>();

        conferenceUtils = new ConferenceUtils();
        mainView = new MainView();
        roleCustomRequestCache = new CustomRequestCache<>(CUSTOM_REQUEST_CAPACITY);

        // Initialize systems
        init();
    }

    private void save(UserManager userRepository,
                      ConferenceEventManager eventRepository, MessengerManager messengerRepository) {
        try {
            dataUserGateway.saveToFile(DAO_FILE_PATH + "user.ser", userRepository);
            dataEventGateway.saveToFile(DAO_FILE_PATH + "event.ser", eventRepository);
            dataMessengerGateway.saveToFile(DAO_FILE_PATH + "messenger.ser", messengerRepository);
        } catch (IOException e) {
            mainView.displaySomethingWentWrong();
        }
    }

    /**
     * Quits the program.
     */
    public void quit() {
        mainView.displayThankYou();
        System.exit(0);
    }

    /**
     * Update some statistics when the Observable changes
     * @param o the Observable
     * @param arg the arg to change
     */
    @Override
    public void update(Observable o, Object arg) {
        redirect();
    }

    /**
     * Initializes all the necessary launchers and systems.
     */
    @Override
    public void init() {
        try {
            appTrafficManager = dataAppTrafficGateway.readFromFile(DAO_FILE_PATH + "traffic.ser");
            userManager = dataUserGateway.readFromFile(DAO_FILE_PATH + "user.ser");
            messengerManager = dataMessengerGateway.readFromFile(DAO_FILE_PATH + "messenger.ser");
            conferenceEventManager = dataEventGateway.readFromFile(DAO_FILE_PATH + "event.ser");
        } catch (ClassNotFoundException e) {
            mainView.displaySomethingWentWrong();
        }

        userLoginManager = new UserLoginManager(userManager);
        messengerManager.setUserManager(userManager);

        // Set up Gateway
        discordEventNotificationGateway = new DiscordEventNotificationGateway(DISCORD_BOT_TOKEN,
                conferenceEventManager, userManager);
        qrCodeGeneratorGateway = new QRCodeGeneratorGateway();

        // Initializer Launchers
        userConferenceLauncher = new UserConferenceLauncher(userLoginManager, appTrafficManager,
                conferenceEventManager);
        conferenceEventLauncher = new ConferenceEventLauncher(userLoginManager, appTrafficManager,
                conferenceEventManager, messengerManager);
        roomLauncher = new RoomLauncher(userLoginManager, appTrafficManager,
                conferenceEventManager.getRoomRepository(), DAO_FILE_PATH);
        messengerLauncher = new MessengerLauncher(conferenceEventManager,
                userManager, userLoginManager, messengerManager);

        // Initialize Main Menu Systems
        userLoginSystem = new UserLoginSystem(userLoginManager, appTrafficManager);
        userSignUpSystem = new UserSignUpSystem(userLoginManager, appTrafficManager,
                roleCustomRequestCache, DAO_FILE_PATH);

        userManager.addObserver(this);

        dashboardDataView = new DashboardDataView(appTrafficManager, userManager, conferenceEventManager);
//        twilioEventNotificationGateway = new TwilioEventNotificationGateway(conferenceEventManager,
//                appTrafficManager, ACCOUNT_SID, AUTH_TOKEN);
    }

    private void redirect() {
        mainView.displayRedirecting();
        numSignups++;
        numLogins++;
    }

    /**
     * Initializes controllers and starts up the program.
     *
     */
    public void start() {
        mainView.displayConferenceTitle();

        // The options this User can see
        List<String> validOptions;

        //noinspection InfiniteLoopStatement
        while (true) {
            String choice = "";
            if (!userLoginManager.hasCurrentUser()) {

                validOptions = new ArrayList<>(MENU_STARTUP_TO_NAME.keySet());

                do {
                    mainView.displayOptions(validOptions, MENU_STARTUP_TO_NAME);

                    try {
                        choice = this.requestInput(validOptions, MENU_STARTUP_TO_NAME, mainView);
                    } catch (IOException e) {
                        mainView.displaySomethingWentWrong();
                    }

                    switch (choice) {
                        case LOGIN:
                            userLoginSystem.start();
                            numLogins++;
                            break;
                        case SIGNUP:
                            roleCustomRequestCache.saveRequest(Role.ATTENDEE);
                            userSignUpSystem.start();
                            userLoginSystem.start();
                            break;
                        case QUIT:
                            save(userManager, conferenceEventManager, messengerManager);
                            quit();
                    }
                } while (!userLoginManager.hasCurrentUser());
            }

            menu: while (true) {
                validOptions = conferenceUtils
                        .getValidOptionsForUser(userManager
                                        .getRolesByUserId(userLoginManager.getCurrentUserId()),
                                MENU_CHOICE_TO_NAME_AND_ROLES2);

                validOptions.sort((o1, o2) -> {
                    Integer x1 = Integer.parseInt(o1);
                    Integer x2 = Integer.parseInt(o2);

                    return x1.compareTo(x2);
                });

                mainView.displayMainMenu();
                mainView.displayOptions(validOptions, MENU_CHOICE_TO_NAME_AND_ROLES2);

                try {
                    choice = requestInput(validOptions, MENU_CHOICE_TO_NAME_AND_ROLES2, mainView);
                } catch (IOException e) {
                    mainView.displaySomethingWentWrong();
                }

                switch (choice) {
                    case SIGNUP_AND_SEE_YOUR_CONFERENCE_EVENTS:
                        userConferenceLauncher.start();
                        break;
                    case SEE_ALL_CONFERENCE_EVENTS:
                        conferenceEventLauncher.start();
                        break;
                    case MESSENGER:
                        messengerLauncher.start();
                        break;
                    case CREATE_SPEAKER:
                        roleCustomRequestCache.saveRequest(Role.SPEAKER);
                        userSignUpSystem.start();
                        break;
                    case CREATE_ORGANIZER:
                        roleCustomRequestCache.saveRequest(Role.ORGANIZER);
                        userSignUpSystem.start();
                        break;
                    case CREATE_ATTENDEE:
                        roleCustomRequestCache.saveRequest(Role.ATTENDEE);
                        userSignUpSystem.start();
                        break;
                    case CREATE_VIP:
                        roleCustomRequestCache.saveRequest(Role.ATTENDEE);
                        roleCustomRequestCache.saveRequest(Role.VIP);
                        userSignUpSystem.start();
                        break;
                    case SEE_ROOMS:
                        roomLauncher.start();
                        break;
                    case SEE_STATS:
                        dashboardDataView.displayLoginsAllTime();
                        dashboardDataView.displaySignUpsAllTime();
                        dashboardDataView.displaySpeakersAddedAllTime();
                        dashboardDataView.displayNumEventSignUpAllTime();
                        dashboardDataView.displayLogoutsAllTime();
                        dashboardDataView.displayNotificationSent();

                        dashboardDataView.displayLoginsThisSession(numLogins);
                        dashboardDataView.displaySignupsThisSession(numSignups);
                        dashboardDataView.displayLogoutsThisSession(numLogouts);

                        dashboardDataView.displayConferenceEventsCreated();
                        dashboardDataView.displayConferenceEventsDeleted();
                        dashboardDataView.displayEditAttemptsMade();

                        Arrays.asList(Role.values()).forEach(role ->
                                dashboardDataView.displayAllUsersWithRole(role));

                        dashboardDataView.displayNumberOfConferenceEvents();
                        dashboardDataView.displayTop5EventsWithHighestSeating();
                        dashboardDataView.displaySpeakersTalking();
                        dashboardDataView.displayTop5ActiveAttendees();
                        break;
                    case LOGOUT:
                        userLoginManager.setUser(null);
                        numLogouts++;
                        appTrafficManager.updateLogoutsAllTime();
                        mainView.displayRedirecting();
                        break menu;
                    case QUIT:
                        save(userManager, conferenceEventManager, messengerManager);
                        appTrafficManager.updateLogoutsAllTime();
                        quit();
                }
            }
        }
    }
}
