package com.conference.frontend.messenger;

import com.conference.backend.data.utils.Role;
import com.conference.backend.messenger.entities.Message;
import com.conference.backend.users.User;
import com.conference.frontend.EntityView;

import java.util.ArrayList;
import java.util.List;

public class MessengerView extends EntityView {

    /**
     * Displays the main options available for the messenger system
     */
    public void displayMainOptions() {
        this.displayString("Please choose between:");
        this.displayString("0: Compose a new message\n1: Manage your conversations and reply to messages\n2: " +
                "Manage and view your contacts\n.home: Return to the main menu");
    }

    /**
     * Displays a message according to the current user's role
     * @param roles a list of roles belonging to the current user
     */
    public void displayGreeting(List<Role> roles) {
        this.displayTitle("Welcome To Your Messenger!");
        if(roles.contains(Role.ORGANIZER)) {
            this.displayString("As an organizer, you can message any attendee or speaker at the conference.\n");
        }
        else if(roles.contains(Role.SPEAKER)) {
            this.displayString("As a speaker, you can message anyone who has registered to attend your talk(s).\n");
        }
        else if (roles.contains(Role.ATTENDEE)) {
            this.displayString("As an attendee (or VIP), you can message anyone at this conference.\n");
        }
    }

    /**
     * Displays a prompt for the user to select the recipients of a message
     */
    public void displaySendMessageGreeting() {
        this.displayString("Who would you like to send a message to? See options below:");
    }


    /**
     * Displays the options available for sending a message and returns a list of valid inputs
     * @param options a list of options in the form of strings
     * @return the list of message options
     */
    public List<String> displayMessageOptions(List<String> options) {
        List<String> validOptions= new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            this.displayString(i + ": " + options.get(i));
            validOptions.add(Integer.toString(i));
        }
        return validOptions;
    }

    /**
     * Displays the options available for sending a message to attendees of a specific talk, and
     * returns a list of valid inputs
     * @param eventNames a list of available event names in the form of strings
     * @return the list of talk options
     */
    public List<String> displayTalkOptions(List<String> eventNames) {
        this.displayString("Which of the following talk(s)'s attendees would you like to message? Enter number.");
        ArrayList<String> validTalkOptions = new ArrayList<>();
        for (int i = 0; i < eventNames.size(); i++) {
            this.displayString(i + ": " + eventNames.get(i));
            validTalkOptions.add(Integer.toString(i));
        }
        return validTalkOptions;
    }

    /**
     * Displays message telling the user they cannot send a message to an empty list
     */
    public void displayEmptyListError() {
        this.displayError(">>> Cannot send to empty list.");
    }

    /**
     * Displays message telling the user their message was sent successfully
     */
    public void displayMessageSuccess() {
        this.displaySuccess("Message Sent Successfully");
    }

    /**
     * Promts the user to type the text body of their message
     */
    public void displayEnterMessagePrompt() {
        this.displayString("Enter Message:");
    }

    /**
     * Displays the text body and sender of a message
     * @param senderName the name of the person sending the message
     * @param message the text body of the message
     */
    public void displayMessage(String senderName, String message) {
        this.displayString(senderName + ": " + message + "\n");
    }

    /**
     * displays a list of further options after a user has requested to see a list of messages in a conversation
     * @param choice the ID the the conversation which has been chosen for viewing
     */
    public void displayEndOfMessages(String choice) {
        this.displayString("End of messages in conversation " + choice + "\n");
        this.displayString("0: Go home \n" +
                "1: Reply to this conversation\n" +
                "2: Mark this conversation as unread \n" +
                "3: Archive this conversation\n" +
                "4: Unarchive this conversation \n" +
                "5: Delete this conversation \n" +
                "6: Delete a message.\n");
    }

    /**
     * Displays message telling the user the email they entered is invalid
     */
    public void displayInvalidEmailError() {
        this.displayError(">>> Please enter a valid user email.");
    }

    /**
     * Displays message telling the user they have no events scheduled.
     */
    public void displayNoTalksError() {
        this.displayError(">>> You have no events scheduled.");
    }

    /**
     * Prompts the current user to input an email
     */
    public void displayEmailPrompt() {
        this.displayString("Who would you like to message? Please enter their email:");
    }

    /**
     * Prompts the current user to select a conversation for viewing
     */
    public void promptConversationSelection() {
        this.displayString("Enter the number of the conversation you would like to view:");
    }

    /**
     * Prompts the current user to select a message for deletion
     */
    public void promptSelectDeleteMessage() {
        this.displayString("Which message would you like to delete? Please enter the msg ID");
    }

    /**
     * Displays message telling the user they have entered an invalid message ID
     */
    public void displayInvalidMessageId() {
        this.displayError(">>> Please enter a valid message ID.");
    }

    /**
     * Displays options for deleting a message
     */
    public void displayDeleteOptions1() {
        this.displayString("0: Go back\n1: Delete for you \n2: Delete for everyone\n");
    }

    /**
     * Displays alternate options for deleting a message
     */
    public void displayDeleteOptions2() {
        this.displayString("0: Go back\n1: Delete for you \n");
    }

    /**
     * Displays options for managing contacts
     */
    public void promptContactOptions1() {
        this.displayString("You currently have no contacts.\nPlease choose between:");
        this.displayString("0: Add new contact\n1: Go back");
    }
    /**
     * Displays alternate options for managing contacts
     */
    public void promptContactOptions2() {
        this.displayString("Please choose between:");
        this.displayString("0: View Contacts\n1: Add new contact\n2: Remove an existing contact\n3: Go back");
    }

    /**
     * Prompts the user to enter a new email address to their contact book
     */
    public void promptAddContact() {
        this.displayString("Enter the email address of the contact you would like to add:");
    }

    /**
     * Displays a message to the user confirming their new contact was added successfully
     */
    public void displayContactAddSuccess() {
        this.displaySuccess("Contact Added successfully");
    }

    /**
     * Displays message telling the user their desired contact was unable to be added
     */
    public void displayContactAddError() {
        this.displayError(">>> Unable to add this user to contacts.");
    }

    /**
     * Displays message telling the user that their contacts were loaded with an error
     */
    public void displayLoadContactError() {
        this.displayError(">>> One of your contacts was unable to be located.");
    }

    /**
     * Prompts the user for the email of the contact they would like to delete
     */
    public void promptRemoveContact() {
        this.displayString("Enter the email address of the contact you would like to remove:");
    }

    /**
     * Displays the information of a specific contact
     * @param contact the user selected to view as acontact of the current user
     */
    public void displayContact(User contact) {
        this.displayString(contact
                .getFirstName() + " " + contact.getLastName() + ": "+ contact.getEmail());
    }

    /**
     * Displays a message prompting the user to view a list of conversations
     */
    public void displaySeeBelowForConversationsMessage(){this.displayString("See below for a list of your conversations");}

    /**
     * Displays a message telling the user that they have no current/ongoing conversations
     */
    public void displayNoExistingConversationsMessageNoChoice(){this.displayString("(You have no existing conversations)");}

    /**
     * Displays a message telling the user that they have no current/ongoing conversations
     * and prompts the user to select from the available options
     */
    public void displayNoExistingConversationsMessage(){this.displayString("You have no existing conversations\nChoose from the options below:");}

    /**
     * Displays a conversation belonging to the current user
     * @param i the number associated with the specific conversations
     * @param members the people who have participated in this conversation
     * @param status the status of this conversation
     */
    public void displayConversation(int i, String members, String status){
        this.displayString(i + ". " + members + " (" + status + ")\n");
    }

    /**
     * Displays an error when a user tries to send a message to themselves
     */
    public void displaySendToYourself() {
        this.displayError(">>> You cannot send a message to yourself!");
    }

}
