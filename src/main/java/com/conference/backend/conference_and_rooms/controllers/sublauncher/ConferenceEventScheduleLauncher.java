package com.conference.backend.conference_and_rooms.controllers.sublauncher;

import com.conference.backend.data.utils.base.Launcher;
import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.conference_and_rooms.entities.ConferenceEvent;
import com.conference.backend.data.utils.Role;
import com.conference.backend.data.utils.Value;
import com.conference.backend.conference_and_rooms.managers.DateTimeConferenceEventSorter;
import com.conference.backend.exception.UserNotFoundException;
import com.conference.backend.messenger.managers.MessengerManager;
import com.conference.backend.users.AppTrafficManager;
import com.conference.backend.users.UserLoginManager;
import com.conference.backend.users.UserManager;
import com.conference.frontend.ConferenceUtils;
import com.conference.frontend.conference.ConferenceView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConferenceEventScheduleLauncher extends Launcher implements Startable {
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private final String HOME = "0";
    private final String SORT_BY_SPEAKER = "1";
    private final String SORT_BY_DATE = "2";

    /*
     *  Hashmap used to map an integer to a user friendly string along with the roles required.
     */
    private final Map<String, Value> MENU_CHOICE_TO_NAME_AND_ROLES = new HashMap<String, Value>() {{
        put(HOME, new Value("Go home", Role.values()));
        put(SORT_BY_SPEAKER, new Value("Sort by Speaker", Role.values()));
        put(SORT_BY_DATE, new Value("Sort by Date", Role.values()));
    }};

    private final ConferenceEventManager conferenceEventManager;
    private final UserManager userManager;
    private final UserLoginManager userLoginManager;
    private final AppTrafficManager appTrafficManager;
    private final MessengerManager messengerManager;

    private final ConferenceView conferenceView;
    private final ConferenceUtils conferenceUtils;

    public ConferenceEventScheduleLauncher(UserLoginManager userLoginManager,
                                           AppTrafficManager appTrafficManager,
                                           ConferenceEventManager conferenceEventManager,
                                           MessengerManager messengerManager) {

        this.conferenceEventManager = conferenceEventManager;
        this.userLoginManager = userLoginManager;
        this.userManager = userLoginManager.getUserRepository();
        this.appTrafficManager = appTrafficManager;
        this.messengerManager = messengerManager;

        conferenceView = new ConferenceView();
        conferenceUtils = new ConferenceUtils();
    }

    @Override
    public void start() {
        List<String> validOptions = conferenceUtils
                .getValidOptionsForUser(userManager.getRolesByUserId(userLoginManager.getCurrentUserId()),
                        MENU_CHOICE_TO_NAME_AND_ROLES);

        conferenceView.displayTitleSortSchedule();
        conferenceView.displayOptions(validOptions, MENU_CHOICE_TO_NAME_AND_ROLES);

        try {
            String choice = this.requestInput(validOptions, MENU_CHOICE_TO_NAME_AND_ROLES, conferenceView);
            process(choice);
        } catch (IOException | UserNotFoundException exception) {
            conferenceView.displaySomethingWentWrong();
        }
    }

    private void process(String choice) throws IOException, UserNotFoundException {
        switch (choice) {
            case HOME:
                break;
            case SORT_BY_SPEAKER:
                sortEventScheduleBySpeaker();
                break;
            case SORT_BY_DATE:
                sortEventScheduleByDate();
                break;
        }
    }

    /**
     * Allows Speakers to view the {@link ConferenceEvent}s that they are currently assigned to.
     *
     * @param speakerId The ID of the speaker to print events of
     */
    public void seeSpeakerEvents(String speakerId) {
        List<String> speakerConferenceEventNames = conferenceEventManager.getSpeakerEventNames(speakerId);

        if (speakerConferenceEventNames.isEmpty()) {
            conferenceView.displayThisSpeakerIsNotSpeaking();
        } else {
            conferenceView.displayTitleConferenceEventsSpeakingAt();
        }

        conferenceView.printConferenceEvents(speakerConferenceEventNames, conferenceEventManager, userManager);
    }

    /**
     * Helper function for chooseSortedEventSchedule that sorts Event Schedule by the events that a speaker speaks at.
     *
     * @throws UserNotFoundException if user was not found
     * @throws IOException if reader failed to read
     */
    private void sortEventScheduleBySpeaker() throws IOException, UserNotFoundException {
        String candidateSpeaker;
        Boolean speakerExists = false;
        do {
            conferenceView.displayPromptSpeakerEmail2();
            conferenceView.displayTitleAllSpeakers();
            conferenceView.displaySpeakersList(userManager);
            candidateSpeaker = br.readLine().trim();
            if (candidateSpeaker.equalsIgnoreCase(".home")) {
                conferenceView.printExit();
                break;
            }
            try {
                speakerExists = userManager.hasAnyOneOfRolesById(userManager.getUserIdByEmail(candidateSpeaker),
                        Role.SPEAKER);
            } catch (UserNotFoundException e) {
                conferenceView.displaySpeakerEmailNotFound();
            }
        } while(!speakerExists);
        seeSpeakerEvents(userManager.getUserIdByEmail(candidateSpeaker));
    }

    private void sortEventScheduleByDate() {
        DateTimeConferenceEventSorter sorter = new DateTimeConferenceEventSorter();
        Map<String, List<String>> sortedEvents = sorter
                .splitByDay(sorter
                        .sort(conferenceEventManager.getConferenceEventsList(), conferenceEventManager),
                        conferenceEventManager);

        conferenceView.printSortedByDateConferenceEvents(sortedEvents, conferenceEventManager, userManager);
    }

}
