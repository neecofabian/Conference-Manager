package com.conference.backend.conference_and_rooms.controllers.subcontrollers;

import com.conference.backend.data.utils.DistanceSpellChecker;
import com.conference.backend.data.utils.base.InputProcessor;
import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.conference_and_rooms.entities.ConferenceEvent;
import com.conference.backend.data.utils.base.SpellChecker;
import com.conference.backend.users.AppTrafficManager;
import com.conference.backend.users.UserLoginManager;
import com.conference.backend.users.UserManager;
import com.conference.frontend.conference.ConferenceSubView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Observer;

/**
 *  Controller for changing capacity of a {@link ConferenceEvent}.
 *
 */
public class ConferenceEventCapacitySystem extends InputProcessor implements Startable {
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private final ConferenceEventManager conferenceEventManager;
    private final UserManager userManager;
    private final UserLoginManager userLoginManager;
    private final AppTrafficManager appTrafficManager;

    private final ConferenceSubView conferenceSubView;

    private final SpellChecker distanceSpellChecker;

    public ConferenceEventCapacitySystem(ConferenceEventManager conferenceEventManager,
                                         UserLoginManager userLoginManager, AppTrafficManager appTrafficManager) {
        this.userLoginManager = userLoginManager;
        this.userManager = userLoginManager.getUserRepository();
        this.appTrafficManager = appTrafficManager;
        this.conferenceEventManager = conferenceEventManager;
        this.conferenceSubView = new ConferenceSubView();
        this.distanceSpellChecker = new DistanceSpellChecker<>(conferenceEventManager);
        conferenceEventManager.addObserver((Observer) distanceSpellChecker);
    }

    /**
     * Allows an Organizer user to assign a Speaker user to an existing {@link ConferenceEvent}. First presents a list
     * of Speakers then prompts Organizer to input the email of the Speaker that will be added to an event. Allows the
     * Speaker to be assigned to the event if Speaker email exists, ConferenceEvent name exists, there is not already
     * a speaker assigned to the event, and the Speaker does not have scheduling conflicts. The capacity of an event
     * that has passed cannot be changed.
     *
     */
    @Override
    public void start() {

        String candidateEvent = "";
        boolean eventExists;
        String candidateCapacity = "";
        boolean validCapacity = false;
        int capacity;

        changeCapacity:while(true) {
            do {
                conferenceSubView.displayPromptEventNameChangeCapacity();
                try {
                    candidateEvent = br.readLine();
                } catch (IOException e) {
                    conferenceSubView.displaySomethingWentWrong();
                }
                eventExists = conferenceEventManager.hasEvent(candidateEvent);

                if (candidateEvent.equalsIgnoreCase(".home")) {
                    conferenceSubView.printExit();
                    break changeCapacity;
                }

                String spellCheckAttempt = distanceSpellChecker.corrections(candidateEvent);

                if (!eventExists) {
                    conferenceSubView.displayEventDoesNotExist();
                    if (!spellCheckAttempt.equalsIgnoreCase(candidateEvent)) {
                        conferenceSubView.displayWeThinkYouMeant(conferenceEventManager
                                .getFormattedName(spellCheckAttempt));
                    }
                }
                else {
                    if (conferenceEventManager.getDateByEventName(candidateEvent).getStart()
                            .before(new Date(System.currentTimeMillis()))) {
                        conferenceSubView.displayEventPassed();
                        eventExists = false;
                    }
                }
            } while (!eventExists);

            do {
                conferenceSubView.displayPromptNewCapacity();
                candidateCapacity = requestPositiveInteger();
                capacity = Integer.parseInt(candidateCapacity);

                validCapacity = conferenceEventManager.updateConferenceEventCapacity(capacity,
                        userManager.getRolesByUserId(userLoginManager.getCurrentUserId()), candidateEvent);

                if (!validCapacity) {
                    conferenceSubView.displayCapacityErrorReasons();
                } else {
                    conferenceSubView.displaySuccessChangeCapacity(candidateEvent, candidateCapacity);
                    appTrafficManager.updateNumOfEditAttemptsMadeAllTime();
                }
            } while (!validCapacity);

            break changeCapacity;
        }

    }


}

