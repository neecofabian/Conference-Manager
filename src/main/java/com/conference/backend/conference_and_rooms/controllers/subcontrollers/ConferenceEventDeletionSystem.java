package com.conference.backend.conference_and_rooms.controllers.subcontrollers;

import com.conference.backend.data.utils.DistanceSpellChecker;
import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.conference_and_rooms.entities.ConferenceEvent;
import com.conference.backend.data.utils.base.SpellChecker;
import com.conference.backend.exception.UserNotFoundException;
import com.conference.backend.users.AppTrafficManager;
import com.conference.backend.users.UserLoginManager;
import com.conference.backend.users.UserManager;
import com.conference.frontend.conference.ConferenceSubView;
import com.conference.frontend.conference.RoomView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 *  Controller for deleting a {@link ConferenceEvent}.
 *
 */
public class ConferenceEventDeletionSystem implements Startable {
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private final ConferenceEventManager conferenceEventManager;
    private final UserManager userManager;
    private final UserLoginManager userLoginManager;
    private final AppTrafficManager appTrafficManager;

    private final ConferenceSubView conferenceSubView;

    private final SpellChecker distanceSpellChecker;

    public ConferenceEventDeletionSystem(ConferenceEventManager conferenceEventManager,
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
     * Allows Organizers to delete an existing {@link ConferenceEvent} from the schedule and remove the event from each
     * Attendee's list of ConferenceEvents. Organizer can only delete a ConferenceEvent if the name exists and the event
     * has not passed.
     *
     */
    @Override
    public void start() {
        String candidateEvent = "";
        List<String> candidateAttendeeList = new ArrayList<>();
        boolean eventExists, canCancel = false;

        remove:do {
            conferenceSubView.displayPromptRemoveEventName();

            try {
                candidateEvent = br.readLine();
            } catch (IOException e) {
                conferenceSubView.displaySomethingWentWrong();
            }

            eventExists = conferenceEventManager.hasEvent(candidateEvent);

            if (candidateEvent.equalsIgnoreCase(".home")) {
                conferenceSubView.printExit();
                break remove;
            }

            String spellCheckAttempt = distanceSpellChecker.corrections(candidateEvent);

            if (!eventExists) {
                conferenceSubView.displayEventDoesNotExist();
                if (!spellCheckAttempt.equalsIgnoreCase(candidateEvent)) {
                    conferenceSubView.displayWeThinkYouMeant(conferenceEventManager
                            .getFormattedName(spellCheckAttempt));
                }
            } else {
                if (conferenceEventManager.getDateByEventName(candidateEvent)
                        .getStart().before(new Date(System.currentTimeMillis()))) {
                    conferenceSubView.displayEventPassed();
                    canCancel = false;
                    eventExists = false;
                } else {
                    // Manually add attendees into candidateAttendeeList (Shallow copy)
                    List<String> tempList = conferenceEventManager.getAttendeeListByConferenceEventName(candidateEvent);
                    for (String attendee : tempList) {
                        candidateAttendeeList.add(attendee);
                    }

                    canCancel = conferenceEventManager.removeEvent(userManager
                            .getRolesByUserId(userLoginManager.getCurrentUserId()), candidateEvent);
                    if (!canCancel) {
                        conferenceSubView.displayCannotCancelEvent();
                    }
                }
            }
        } while (!eventExists && !canCancel);

        if (eventExists && canCancel) {
            conferenceSubView.displaySuccessRemovedEvent(candidateEvent);
            appTrafficManager.updateNumOfConferenceEventsDeletedAllTime();
            appTrafficManager.updateNumOfEditAttemptsMadeAllTime();
            // Remove events from all Attendee's list of events
            for (String attendeeId : candidateAttendeeList) {
                userManager.removeConferenceEventFromConferenceEvents(attendeeId, candidateEvent);
            }
        }
    }


}

