package com.conference.backend.users.controllers.event_signup_and_viewing.subcontrollers;

import com.conference.backend.data.utils.DistanceSpellChecker;
import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.conference_and_rooms.entities.ConferenceEvent;
import com.conference.backend.conference_and_rooms.entities.EventType;
import com.conference.backend.data.utils.Role;
import com.conference.backend.data.utils.base.SpellChecker;
import com.conference.backend.users.AppTrafficManager;
import com.conference.backend.users.User;
import com.conference.backend.users.UserLoginManager;
import com.conference.backend.users.UserManager;
import com.conference.frontend.conference.ConferenceView;
import com.conference.frontend.users.UserView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Observer;

/**
 * Controller class for a User to sign up for a conference event.
 */
public class UserConferenceSignUpSystem implements Startable {
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private final UserManager userManager;
    private final UserLoginManager userLoginManager;
    private final ConferenceEventManager conferenceEventManager;
    private final UserView userView;
    private final ConferenceView conferenceView;
    private final AppTrafficManager appTrafficManager;
    private final SpellChecker distanceSpellChecker;

    /**
     * Initializes this UserConferenceSignUpSystem.
     *
     * @param userManager The {@code UserManager} storing users and repository of users
     * @param userLoginManager The {@code UserLoginManager} storing the current user and repository of users
     * @param appTrafficManager The {@code AppTrafficManager} to update statistics
     * @param conferenceEventManager The {@code ConferenceEventManager}
     *      *                               storing the repository of {@link ConferenceEvent}s
     */
    public UserConferenceSignUpSystem(UserManager userManager,
                                      UserLoginManager userLoginManager,
                                      AppTrafficManager appTrafficManager,
                                      ConferenceEventManager conferenceEventManager) {
        this.userManager = userManager;
        this.userLoginManager = userLoginManager;
        this.appTrafficManager = appTrafficManager;
        this.conferenceEventManager = conferenceEventManager;
        this.userView = new UserView();
        this.conferenceView = new ConferenceView();
        distanceSpellChecker = new DistanceSpellChecker(conferenceEventManager);
        conferenceEventManager.addObserver((Observer) distanceSpellChecker);
    }

    /**
     * Prints out a list of every upcoming {@link ConferenceEvent} and prompts the logged in {@link User} to register
     * for an upcoming event if they're free in the same timeslot and if they're not already registered for the event.
     *
     */
    @Override
    public void start() {

        conference: while (true) {
            userView.displayUpcomingEvents();

            conferenceView.printConferenceEvents(conferenceEventManager.getConferenceEventNamesList(),
                    conferenceEventManager, userManager);

            String candidateEvent = "";
            boolean eventExists, canSignUp = false;
            do {
                userView.displayConferenceEventSignUp();
                try {
                    candidateEvent = br.readLine();
                } catch (IOException e) {
                    userView.displaySomethingWentWrong();
                }
                eventExists = conferenceEventManager.hasEvent(candidateEvent);

                if (candidateEvent.equalsIgnoreCase(".home")) {
                    userView.printExit();
                    break conference;
                }
                String spellCheckAttempt = distanceSpellChecker.corrections(candidateEvent);

                if (!eventExists) {
                    userView.displayEventDoesNotExist();
                    if (!spellCheckAttempt.equalsIgnoreCase(candidateEvent)) {
                        userView.displayWeThinkYouMeant(conferenceEventManager.getFormattedName(spellCheckAttempt));
                    }
                }

                if (eventExists
                        && conferenceEventManager.getEventTypeById(candidateEvent) == EventType.VIP_SOCIAL
                        && !userManager.hasAnyOneOfRolesById(userLoginManager.getCurrentUserId(), Role.VIP)
                        && !userManager.hasAnyOneOfRolesById(userLoginManager.getCurrentUserId(), Role.ORGANIZER)) {
                    userView.displayYouAreNotAVIP();
                } else if (eventExists && conferenceEventManager.getDateByEventName(candidateEvent).getStart()
                        .after(new Date(System.currentTimeMillis()))) {
                    canSignUp = userManager
                            .signUpUserForConferenceEvent(userLoginManager.getCurrentUserId(),
                                    conferenceEventManager, conferenceEventManager.getConferenceEvent(candidateEvent));
                    if (!canSignUp) {
                        userView.displayCannotSignUp();
                    }
                } else if (eventExists && !conferenceEventManager.getDateByEventName(candidateEvent).getStart()
                        .after(new Date(System.currentTimeMillis()))) {
                    userView.displayEventPassed();
                }
            } while (!eventExists && !canSignUp);

            if (eventExists && canSignUp) {
                appTrafficManager.updateNumEventSignUpAllTime();
                userView.displaySuccessEventSignedUp(candidateEvent);
                eventExists = false;
                canSignUp = false;
            }
        }
    }
}
