package com.conference.frontend.conference;

import com.conference.backend.conference_and_rooms.entities.DateInterval;
import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.conference_and_rooms.entities.ConferenceEvent;
import com.conference.backend.conference_and_rooms.entities.EventType;
import com.conference.backend.data.utils.Role;
import com.conference.backend.users.User;
import com.conference.backend.users.UserManager;
import com.conference.frontend.EntityView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ConferenceView extends EntityView {

    /**
     * Prints and formats every {@link ConferenceEvent} that is saved in the eventRepository.
     *
     * @param eventNames      The names of the ConferenceEvents to print
     * @param eventRepository To get the date of the event and its room
     * @param userRepository  The user repository {@link UserManager} to get the name of the {@link User}
     */
    public void printConferenceEvents(List<String> eventNames,
                                      ConferenceEventManager eventRepository,
                                      UserManager userRepository) {

        for (String eventName : eventNames) {
            StringBuilder sb = new StringBuilder();

            if (eventRepository.getEventTypeByEventName(eventName) == EventType.VIP_SOCIAL) {
                sb.insert(0, ANSI_PURPLE);
            }

            sb.append("\n \t* Event Type: ").append(eventRepository.getEventTypeByEventName(eventName));

            StringBuilder sb2 = new StringBuilder();

            if (eventRepository.getEventTypeByEventName(eventName) == EventType.TALK) {
                sb2.append("\n \t* Speaker: ");
            } else if (eventRepository.getEventTypeByEventName(eventName) == EventType.PANEL
                    || eventRepository.getEventTypeByEventName(eventName) == EventType.VIP_SOCIAL) {
                sb2.append("\n \t* Speakers:");
            }

            DateInterval temp = eventRepository.getDateByEventName(eventName);
            String dateToShow = temp.getStart().after(new Date(System.currentTimeMillis())) ? temp.toString() :
                    "(This event has passed)";

            System.out.println(eventName
                    + " " + String.format("(%d/%d seats)", eventRepository.getAttendeeListSizeByEventName(eventName),
                    eventRepository.getCapacityByEventName(eventName))
                    + "\n \t* Showing: " + dateToShow
                    + "\n \t* Where: " + eventRepository.getRoomRepository().getRoomNameByEventName(eventName)
                    + sb.toString() + ANSI_RESET + sb2.toString());

            List<String> speakers = new ArrayList<>();
            for (String s : eventRepository.getSpeakerIdsByEventName(eventName)) {
                if (userRepository.hasUserById(s)) {
                    speakers.add(s);
                }
            }
            speakers.stream().forEach(s -> System.out.println("\t\t- " + userRepository.getNameById(s)
                    + " (" +
                    userRepository.getEmailByUserId(s) + ")"));
            System.out.println();
        }
    }

    /**
     * Prints and formats every {@link ConferenceEvent} that is saved in the eventRepository after sorting by events.
     *
     * @param sortedEvents A list of already sorted {@link ConferenceEvent}
     * @param eventRepository To get the date of the event and its room
     * @param userRepository The user repository {@link UserManager} to get the name of the {@link User}
     */
    public void printSortedByDateConferenceEvents(Map<String, List<String>> sortedEvents,
                                                  ConferenceEventManager eventRepository, UserManager userRepository) {
        List<String> dayStringList = new ArrayList<>(sortedEvents.keySet());

        dayStringList.sort((a, b) -> {
            Date d1 = new Date();
            Date d2 = new Date();
            DateFormat format = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.ENGLISH);

            try {
                d1 = format.parse(a);
                d2 = format.parse(b);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return d1.compareTo(d2);
        });

        for (String dayString : dayStringList) {
            this.displayString(dayString + "\n");
            this.printConferenceEvents(sortedEvents.get(dayString), eventRepository, userRepository);
        }
    }

    /**
     * Prints title "Conference Menu:".
     *
     */
    public void displayTitleConferenceEventMenu() {
        this.displayTitle("Conference Event Menu:");
    }

    /**
     * Prints title "All Conference Events:".
     *
     */
    public void displayTitleMenuOptionsSeeConference() {
        this.displayTitle("All Conference Events:");
    }

    /**
     * Prints title "Conference Event Editor:".
     *
     */
    public void displayTitleMenuOptionEditing() {
        this.displayTitle("Conference Event Editor:");
    }

    /**
     * Prints title "All Conference Events in the Schedule:"
     *
     */
    public void displayTitleAllConferenceEvents() {
        this.displayString("\n" + "All Conference Events in the Schedule:\n");
    }

    /**
     * Prints title "Sorting Menu:".
     *
     */
    public void displayTitleSortSchedule() {
        this.displayTitle("Sorting Menu:");
    }

    /**
     * Prints title "All Speakers:".
     *
     */
    public void displayTitleAllSpeakers() {
        this.displayString("All Speakers:");
    }

    /**
     * Prints prompt to enter speaker's email whose events are to be displayed.
     *
     */
    public void displayPromptSpeakerEmail2() {
        this.displayString("Please enter the email of the speaker whose events you would like to display" +
                " (type \".home\" to return to the main menu):");
    }

    /**
     * Prints error that speaker email was not found.
     *
     */
    public void displaySpeakerEmailNotFound() {
        this.displayError(">>> Speaker email was not found.");
    }

    /**
     * Prints message that speaker has no events.
     *
     */
    public void displayThisSpeakerIsNotSpeaking() {
        this.displayString("(This speaker has no events)");
    }


    /**
     * Prints success message that speaker has been added to the event.
     *
     * @param firstName first name of added Speaker
     * @param lastName last name of added Speaker
     * @param email email of added Speaker
     * @param candidateEvent {@link ConferenceEvent} that Speaker was added to
     */
    public void displaySuccessAddedSpeaker(String firstName, String lastName, String email, String candidateEvent) {
        this.displaySuccess("You added the speaker " + firstName + " "
                + lastName + " (" + email + ") to the event " + candidateEvent + "!");
    }

    /**
     * Prints title "Here are the conference events:".
     *
     */
    public void displayTitleConferenceEventsSpeakingAt() {
        this.displayString("\n" + "Here are the conference events:\n");
    }

    /**
     * Prints list of Speakers registered in the planning system.
     *
     * @param userRepository The user repository {@link UserManager} to get the names of Speakers
     */
    public void displaySpeakersList(UserManager userRepository) {
        userRepository.getUsersWithRole(Role.SPEAKER).stream()
                .forEach(u -> this.displayString("\t- " + u.getFirstName() + " " + u.getLastName() + " (" +
                        u.getEmail() + ")"));

    }

    /**
     * Prints prompt "Please select one of the above rooms:".
     *
     */
    public void displaySelectRooms() {
        this.displayString("Please select one of the above rooms:");
    }

}