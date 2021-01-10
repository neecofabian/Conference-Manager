package com.conference.backend.users.controllers.event_signup_and_viewing.subcontrollers;

import com.conference.backend.data.utils.DistanceSpellChecker;
import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.conference_and_rooms.entities.ConferenceEvent;
import com.conference.backend.data.utils.base.SpellChecker;
import com.conference.backend.users.User;
import com.conference.backend.users.UserLoginManager;
import com.conference.backend.users.UserManager;
import com.conference.frontend.conference.ConferenceView;
import com.conference.frontend.users.UserView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Observer;

/**
 * Controller class for a User canceling a conference event they're signed up for.
 */
public class UserConferenceCancelSystem implements Startable {
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private final UserManager userManager;
    private final UserLoginManager userLoginManager;
    private final ConferenceEventManager conferenceEventManager;
    private final UserView userView;
    private final ConferenceView conferenceView;
    private final SpellChecker distanceSpellChecker;

    /**
     * Initializes this UserConferenceCancelSystem.
     *
     * @param userManager The {@code UserManager} storing users and repository of users
     * @param userLoginManager The {@code UserLoginManager} storing the current user and repository of users
     * @param conferenceEventManager The {@code ConferenceEventManager}
     *                               storing the repository of {@link ConferenceEvent}s
     */
    public UserConferenceCancelSystem(UserManager userManager,
                                      UserLoginManager userLoginManager,
                                      ConferenceEventManager conferenceEventManager) {
        this.userManager = userManager;
        this.userLoginManager = userLoginManager;
        this.conferenceEventManager = conferenceEventManager;
        this.userView = new UserView();
        this.conferenceView = new ConferenceView();
        distanceSpellChecker = new DistanceSpellChecker(conferenceEventManager);
        conferenceEventManager.addObserver((Observer) distanceSpellChecker);
    }

    /**
     * Prints out every {@link ConferenceEvent} that the logged in {@link User} is registered for. Prompts the
     * {@link User} to to cancel their enrollment in a specific {@link ConferenceEvent} they're registered for.
     * Cancels the user's enrollment in the event if viable.
     *
     */
    @Override
    public void start() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        conference: while(true) {
            userView.displayYourConferenceEvents();

            List<String> usersConferenceEventNames = userManager
                    .getConferenceEventsById(userLoginManager.getCurrentUserId());

            conferenceView.printConferenceEvents(usersConferenceEventNames, conferenceEventManager, userManager);

            String candidateEvent = "";
            boolean eventExists, canCancel = false;
            do {
                userView.displayConferenceEventsPromptCancel();
                try {
                    candidateEvent = br.readLine();
                } catch (IOException e) {
                    userView.displaySomethingWentWrong();
                }

                eventExists = userManager
                        .getConferenceEventsById(userLoginManager.getCurrentUserId()).contains(candidateEvent);

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
                } else {
                    canCancel = userManager.removeUserFromConferenceEvent(userLoginManager.getCurrentUserId(),
                            conferenceEventManager.getConferenceEvent(candidateEvent));
                    if (!canCancel) {
                        userView.displayCannotSignUpForEvent();
                    }
                }
            } while (!eventExists && !canCancel);

            if (eventExists && canCancel) {
                userView.displaySuccessEventCancelled(candidateEvent);
                eventExists = false;
                canCancel = false;
            }

        }
    }
}

