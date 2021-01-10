package com.conference.backend.conference_and_rooms.controllers.subcontrollers;

import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.conference_and_rooms.entities.ConferenceEvent;
import com.conference.backend.data.utils.Role;
import com.conference.backend.exception.UserNotFoundException;
import com.conference.backend.users.AppTrafficManager;
import com.conference.backend.users.User;
import com.conference.backend.users.UserLoginManager;
import com.conference.backend.users.UserManager;
import com.conference.frontend.conference.ConferenceSubView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

/**
 *  Controller for adding speakers to a {@link ConferenceEvent}.
 *
 */
public class ConferenceEventAddSpeakerSystem implements Startable {
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private final ConferenceEventManager conferenceEventManager;
    private final UserManager userManager;
    private final UserLoginManager userLoginManager;
    private final AppTrafficManager appTrafficManager;

    private final ConferenceSubView conferenceSubView;

    public ConferenceEventAddSpeakerSystem(ConferenceEventManager conferenceEventManager,
                                           UserLoginManager userLoginManager, AppTrafficManager appTrafficManager) {
        this.userLoginManager = userLoginManager;
        this.userManager = userLoginManager.getUserRepository();
        this.appTrafficManager = appTrafficManager;
        this.conferenceEventManager = conferenceEventManager;
        this.conferenceSubView = new ConferenceSubView();
    }

    /**
     * Prints every {@link User} with {@link Role} speaker's name and email. Prompts the {@link User} organizer to enter
     * the email of the speaker desired to be added to a {@link ConferenceEvent}. Displays all {@link ConferenceEvent}s
     * and prompts the {@link User} to add the chosen speaker to a specific {@link ConferenceEvent}. Adds the speaker
     * to the event if possible (and if the event has not passed).
     *
     */
    @Override
    public void start() {
        String candidateSpeaker = "";
        String candidateEvent = "";
        boolean speakerExists = false;
        boolean eventExists;
        boolean canAddSpeaker = false;

        add: while(true) {
            do {
                conferenceSubView.displayPromptSpeakerEmail();
                conferenceSubView.displayTitleAllSpeakers();
                conferenceSubView.displaySpeakersList(userManager);
                try {
                    candidateSpeaker = br.readLine().trim();
                } catch (IOException e) {
                    conferenceSubView.displaySomethingWentWrong();
                }

                if (candidateSpeaker.equalsIgnoreCase(".home")) {
                    conferenceSubView.printExit();
                    break add;
                }
                try {
                    speakerExists = userManager.hasAnyOneOfRolesById(userLoginManager.getUserRepository()
                                            .getUserIdByEmail(candidateSpeaker),
                            Role.SPEAKER);
                } catch (UserNotFoundException e) {
                    conferenceSubView.displaySpeakerEmailNotFound();
                }
            } while(!speakerExists);

            conferenceSubView.printConferenceEvents(conferenceEventManager
                    .getConferenceEventNamesList(), conferenceEventManager, userManager);

            do {
                conferenceSubView.displayPromptEventNameForSpeaker();
                try {
                    candidateEvent = br.readLine().trim();
                } catch (IOException e) {
                    conferenceSubView.displaySomethingWentWrong();
                }
                if (candidateEvent.equalsIgnoreCase(".home")) {
                    conferenceSubView.printExit();
                    break add;
                }

                eventExists = conferenceEventManager.hasEvent(candidateEvent);

                if (!eventExists) {
                    conferenceSubView.displayEventDoesNotExist();
                } else {
                    if (conferenceEventManager.getDateByEventName(candidateEvent).getStart()
                            .before(new Date(System.currentTimeMillis()))) {
                        conferenceSubView.displayEventPassed();
                    } else {
                        try {
                            canAddSpeaker = conferenceEventManager
                                    .addSpeakerToEvent(userLoginManager.getUserRepository()
                                            .getRolesByUserId(userLoginManager.getCurrentUserId()),
                                    userManager.getRolesByUserId(userManager.getUserIdByEmail(candidateSpeaker)),
                                    userManager.getUserIdByEmail(candidateSpeaker), candidateEvent);
                        } catch (UserNotFoundException e) {
                            e.printStackTrace();
                        }
                        if (!canAddSpeaker) {
                            conferenceSubView.displaySpeakerCannotBeAddedReasons();
                        }
                    }
                }

            } while (!eventExists || !canAddSpeaker);

            String speakerId = "";
            try {
                speakerId = userManager.getUserIdByEmail(candidateSpeaker);
            } catch (UserNotFoundException e) {
                conferenceSubView.displayAUserWithThisEmailDoesNotExist();
            }

            conferenceSubView.displaySuccessAddedSpeaker(
                    userManager.getFirstNameById(speakerId),
                    userManager.getLastNameById(speakerId),
                    candidateSpeaker,
                    candidateEvent
            );

            appTrafficManager.updateNumOfSpeakersAddedAllTime();

            break add;
        }
    }


}
