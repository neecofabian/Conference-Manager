package com.conference.frontend;

import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.conference_and_rooms.entities.ConferenceEvent;
import com.conference.backend.data.utils.Role;
import com.conference.backend.users.AppTrafficManager;
import com.conference.backend.users.User;
import com.conference.backend.users.UserManager;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardDataView extends EntityView {

    private AppTrafficManager appTrafficManager;
    private UserManager userManager;
    private ConferenceEventManager conferenceEventManager;

    /**
     * Constructs an instance of this class with the given params
     * @param appTrafficManager The app traffic stats repo
     * @param userManager The user repo
     * @param conferenceEventManager The repo storing conference events
     */
    public DashboardDataView(AppTrafficManager appTrafficManager,
                             UserManager userManager, ConferenceEventManager conferenceEventManager) {
        this.appTrafficManager = appTrafficManager;
        this.userManager = userManager;
        this.conferenceEventManager = conferenceEventManager;
    }

    /**
     * Displays the number of sign ups of all time
     */
    public void displaySignUpsAllTime() {
        this.displayDashboardTitle("Number of Signups of All Time: " + appTrafficManager.getNumSignUps() + '\n');
    }

    /**
     * Displays the number of event sign ups of all time
     */
    public void displayNumEventSignUpAllTime() {
        this.displayDashboardTitle("Number of Event Signups of All time: " +
                appTrafficManager.getNumEventSignUpAllTime() + '\n');
    }

    /**
     * Displays the number of notifications sent of all time
     */
    public void displayNotificationSent() {
        this.displayDashboardTitle("Number of Notification Sent to Admin All Time: " +
                appTrafficManager.getNotificationsSent() + '\n');
    }

    /**
     * Displays the number of logouts of all time.
     */
    public void displayLogoutsAllTime() {
        this.displayDashboardTitle("Number of Logouts of All Time: "
                + appTrafficManager.getLoginsAllTime() + '\n');
    }

    /**
     * Displays the number of successful login attempts of all time.
     */
    public void displayLoginsAllTime() {
        this.displayDashboardTitle("Number of Logins of All Time: " + appTrafficManager.getLoginsAllTime() + '\n');
    }

    /**
     * Displays the number of conference events deleted of all time.
     */
    public void displayConferenceEventsDeleted() {
        this.displayDashboardTitle("Number of Conference Events Deleted of All Time: "
                + appTrafficManager.getNumOfConferenceEventsDeletedAllTime() + '\n');
    }

    /**
     * Displays the number of conference events created of all time.
     */
    public void displayConferenceEventsCreated() {
        this.displayDashboardTitle("Number of Conference Events Created of All Time: " +
                appTrafficManager.getNumOfConferencesCreatedAllTime() + '\n');
    }

    /**
     * Displays the number of edit attempts made of all time.
     */
    public void displayEditAttemptsMade() {
        this.displayDashboardTitle("Number of Edit Attempts Made of All Time: " +
                appTrafficManager.getNumOfEditAttemptsMadeAllTime() + '\n');
    }

    /**
     * Number of speakers who were added of all time.
     */
    public void displaySpeakersAddedAllTime() {
        this.displayDashboardTitle("Number of Speakers who Spoke at Events of All Time: " +
                appTrafficManager.getNumOfSpeakersAddedAllTime() + '\n');
    }

    /**
     * Displays the login attempts this session
     * @param x the login attempts
     */
    public void displayLoginsThisSession(int x) {
        this.displayDashboardTitle("Number of logins this session: " + x + '\n');
    }

    /**
     * Displays the signups this session
     * @param x the sign up attempts
     */
    public void displaySignupsThisSession(int x) {
        this.displayDashboardTitle("Number of signups this session: " + x + '\n');
    }

    /**
     * Displays the logouts this session
     * @param x the logouts this session
     */
    public void displayLogoutsThisSession(int x) {
        this.displayDashboardTitle("Number of logouts this session: " + x+ '\n');
    }

    /**
     * Displays the notifications sent to the admin.
     * @param x the logouts this session.
     */
    public void displayNotificationsSentToAdmin(int x) {
        this.displayDashboardTitle("Number of phone notifications sent to admin: " + x+ '\n');
    }

    /**
     * Displays all Users with the following role and writes a pretty string
     * @param role The role to look for
     */
    public void displayAllUsersWithRole(Role role) {
        this.displayDashboardTitle("All " + role + "s");

        String s = userManager.getUsersWithRole(role).stream()
                .map(User::getEmail)
                .collect(Collectors.joining(", "));
        System.out.println(s + '\n');
    }

    /**
     * Displays the number of conference events
     */
    public void displayNumberOfConferenceEvents() {
        this.displayDashboardTitle("Number of Conference Events: "
                + conferenceEventManager.getConferenceEventsList().size() + '\n');

    }

    /**
     * Displays the top 5 events with the highest seating
     */
    public void displayTop5EventsWithHighestSeating() {
        this.displayDashboardTitle("Top 5 Events with the Highest Seating:");

        List<ConferenceEvent> conferenceEvents = conferenceEventManager.getConferenceEventsList();

        Collections.sort(conferenceEvents, (c1, c2) -> {
            Integer x1 = c1.getAttendeeList().size();
            Integer x2 = c2.getAttendeeList().size();

            return x1.compareTo(x2);
        });

        Collections.reverse(conferenceEvents);

        for (int i = 0; i < conferenceEvents.size(); i++) {
            ConferenceEvent ce = conferenceEvents.get(i);
            String ceStats = ce.getEventName() + " " + String.format("(%d/%d seats)",
                    ce.getAttendeeList().size(), ce.getCapacity());
            displayTop5(ceStats, i);
        }

        System.out.println();
    }

    /**
     * Displays the speakers talking at this conference
     */
    public void displaySpeakersTalking() {

        this.displayDashboardTitle("Speakers talking at this Conference:");

        List<User> speakers = userManager.getUsersWithRole(Role.SPEAKER);
        for (User u : speakers) {
            List<ConferenceEvent> conferenceEvents = conferenceEventManager.getSpeakerEvents(u.getId());
            String userStats = u.getFirstName() + " " + u.getLastName() + " (" + u.getEmail() + ")";

            String s = conferenceEvents.stream().
                    map(ConferenceEvent::getEventName).
                    collect(Collectors.joining(", "));

            if (conferenceEvents.size() > 0) {
                System.out.println(userStats + ": " + s + '\n');
            }
        }
    }

    /**
     * Displays the top 5 active attendees
     */
    public void displayTop5ActiveAttendees() {
        this.displayDashboardTitle("Top 5 Active Attendees:");

        List<User> attendees = userManager.getUsersWithRole(Role.ATTENDEE);

        Collections.sort(attendees, (u1, u2) -> {
            Integer x1 = u1.getConferenceEvents().size();
            Integer x2 = u2.getConferenceEvents().size();

            return x1.compareTo(x2);
        });

        Collections.reverse(attendees);

        for (int i = 0; i < attendees.size(); i++) {
            User u = attendees.get(i);
            String userStats = u.getFirstName() + " " + u.getLastName() + " (" + u.getEmail() + ")";
            displayTop5(userStats, i);
            System.out.println("Number of Events attended: " + u.getConferenceEvents().size() + '\n');
        }
    }

    private void displayTop5(String s, int i) {
        if (i == 0) {
            System.out.println("\033[4;33m" + ANSI_YELLOW + s + ANSI_RESET);
        } else if (i == 1) {
            System.out.println(ANSI_PURPLE + s + ANSI_RESET);
        } else if (i == 2) {
            System.out.println(ANSI_BLUE + s + ANSI_RESET);
        } else {
            System.out.println(s);
        }
    }
}
