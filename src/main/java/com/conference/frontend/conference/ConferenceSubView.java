package com.conference.frontend.conference;

import java.util.List;

public class ConferenceSubView extends ConferenceView {

    //------------------- ConferenceEventCreationSystem -------------------

    /**
     * Print error message when an inputted event name already exists
     *
     */
    public void displayNameExists() {
        this.displayError(">>> An event with this name already exists.");
    }

    /**
     * Print error message when an inputted event name contains ".home"
     *
     */
    public void displayContainsHome() {
        this.displayError(">>> An event name cannot contain \".home\"");
    }

    /**
     * Print an unordered list of all EventTypes
     * @param validTypes the valid event types
     */
    public void displayValidEventTypes(List<String> validTypes) {
        validTypes.stream()
                .forEach(e -> this.displayString("> " + e));
    }

    /**
     * Print a success message after an event is created
     * @param eventName the successfully added event
     */
    public void displaySuccessAddedEvent(String eventName) {
        this.displaySuccess("Congratulations! You added the event " + eventName);
    }

    /**
     * Print an error message after an event could not be created
     *
     */
    public void displayEventNotAdded() {
        this.displayError(">>> Event was not added. Make sure there are no conflicting times " +
                "with other events in this room.");
    }

    /**
     * Print an error message if the creation of a ConferenceEvent object throws an exception
     *
     */
    public void displayEmptyEvent() {
        this.displayError(">>> Empty Conference Event creation.");
    }

    /**
     * Print prompt to ask if user wants to see rooms by certain amenities
     *
     */
    public void displayAmenityDesiredTitle() {
        this.displayString("Choose which amenities you would like to sort " +
                "rooms by (y/n):");
    }


    //------------------- ConferenceEventDeletionSystem -------------------

    /**
     * Print prompt for event name to remove
     *
     */
    public void displayPromptRemoveEventName() {
        this.displayString("Please enter the name of which event you would like to remove from the conference" +
                " (type \".home\" to return to the main menu):");
    }

    /**
     * Print error message during event deletion
     *
     */
    public void displayCannotCancelEvent() {
        this.displayError(">>> You cannot cancel this event!");
    }

    /**
     * Print success message after event deletion
     * @param candidateEvent the event removed
     */
    public void displaySuccessRemovedEvent(String candidateEvent) {
        this.displaySuccess("You removed the event: " + candidateEvent);
    }


    //------------------- ConferenceEventCapacitySystem -------------------

    /**
     * Print prompt for event name to change its capacity
     *
     */
    public void displayPromptEventNameChangeCapacity() {
        this.displayString("Please enter the name of the event whose capacity you would like to change" +
                " (type \".home\" to return to the main menu):");
    }

    /**
     * Print prompt for new event capacity
     *
     */
    public void displayPromptNewCapacity() {
        this.displayString("Please enter the event's new capacity:");
    }

    /**
     * Print error for capacity reasons
     *
     */
    public void displayCapacityErrorReasons() {
        this.displayError(">>> This capacity is either greater than the event's room's capacity " +
                "or this capacity is less than the current number of attendees.");
    }

    /**
     * Print success message after changing event capacity
     * @param candidateEvent the event changed
     * @param candidateCapacity the capacity changed
     */
    public void displaySuccessChangeCapacity(String candidateEvent, String candidateCapacity) {
        this.displaySuccess("You successfully changed " + candidateEvent + "'s capacity " +
                "to " + candidateCapacity + "!");
    }


    //------------------- ConferenceEventAddSpeakerSystem -------------------

    /**
     * Print prompt for a speaker's email
     *
     */
    public void displayPromptSpeakerEmail() {
        this.displayString("Please enter the email of the speaker that you would like to add to an event" +
                " (type \".home\" to return to the main menu):");
    }

    /**
     * Print prompt for the event name to add a speaker to
     *
     */
    public void displayPromptEventNameForSpeaker() {
        this.displayString("Please enter the event name that you wish to add the speaker to" +
                " (type \".home\" to return to the main menu):");
    }

    /**
     * Print error if speaker cannot be added to an event
     *
     */
    public void displaySpeakerCannotBeAddedReasons() {
        this.displayError(">>> Speaker cannot be added to the event because either: the event " +
                "cannot have another speaker, or the speaker is already booked at this time.");
    }

}
