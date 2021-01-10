package com.conference.frontend.users;

import com.conference.frontend.EntityView;

public class UserView extends EntityView {

    /**
     * Displays a title.
     */
    public void displayYourConferenceEvents() {
        this.displayString("\n" + "Your Conference Events:\n");
    }

    /**
     * Displays a prompt whether the user would like to cancel an event.
     */
    public void displayConferenceEventsPromptCancel() {
        this.displayString("Please enter the name of which event you would like to cancel" +
                " (type \".home\" to return to the main menu):");
    }

    /**
     * Displays a title.
     */
    public void displayUpcomingEvents() {
        this.displayString("\n" + ANSI_TITLE + "Upcoming Conference Events:\n" + ANSI_RESET);
    }

    /**
     * Prompts users if they want to sign up for an event.
     */
    public void displayConferenceEventSignUp() {
        this.displayString("Please enter the name of which event you would like to sign up for (type" +
                " \".home\" to return to the main menu):");
    }

    /**
     * Displays to the user that they cannot sign up for an event.
     */
    public void displayCannotSignUpForEvent() {
        this.displayError(">>> You cannot cancel this event because you are not signed " +
                "up for it!");
    }

    /**
     * Displays the event that was cancelled
     * @param candidateEvent The cancelled event
     */
    public void displaySuccessEventCancelled(String candidateEvent) {
        this.displaySuccess("You cancelled the event: " + candidateEvent);
    }

    /**
     * Displays the event successfully signed up for.
     * @param candidateEvent the event signed up for
     */
    public void displaySuccessEventSignedUp(String candidateEvent) {
        this.displaySuccess("Congratulations! You signed up for the event: " + candidateEvent);
    }

    /**
     * Displays that user cannot sign up.
     */
    public void displayCannotSignUp() {
        this.displayError(">>> You cannot sign up for this event because you are" +
                " already signed up for " +
                "an event at this time OR you are already signed up for this event!");
    }

    /**
     * Displays that the User was created with empty credentials.
     */
    public void displayEmptyUserCreation() {
        this.displayError(">>> Empty User creation.");
    }

    /**
     * Displays a create an account title.
     */
    public void displayCreateAnAccount() {
        this.displayString(ANSI_TITLE + "Create an account:" + ANSI_RESET);
    }

    /**
     * Displays a login title
     */
    public void displayLogin() {
        this.displayString(ANSI_TITLE + "Please login:" + ANSI_RESET);
    }

    /**
     * Displays a signup title
     */
    public void displayTitle() {
        this.displayString(ANSI_TITLE + "Sign Up and See Events:" + ANSI_RESET);
    }

    /**
     * Displays an error if the user tried to sign up for a VIP access only event.
     */
    public void displayYouAreNotAVIP() {
        this.displayError(">>> You are not a VIP.");
    }
}
