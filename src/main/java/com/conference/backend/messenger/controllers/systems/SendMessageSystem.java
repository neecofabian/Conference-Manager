package com.conference.backend.messenger.controllers.systems;

import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.data.utils.Role;
import com.conference.backend.data.utils.base.InputProcessor;
import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.exception.EmptyReceiversException;
import com.conference.backend.exception.UserNotFoundException;
import com.conference.backend.messenger.managers.MessengerManager;
import com.conference.backend.users.UserLoginManager;
import com.conference.frontend.messenger.MessengerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SendMessageSystem extends InputProcessor implements Startable {
    private final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private final ConferenceEventManager conferenceEventManager;
    private final MessengerView messengerView;
    private final MessengerManager messengerManager;
    private final UserLoginManager userLoginManager;

    /**
     * Constructs a new instance of ViewConversationsLauncher with the given data.
     *

     * @param userLoginManager The UserLoginManager associated with this instance
     * @param messengerManager The MessengerManager associated with this instance
     * @param conferenceEventManager The ConferenceEventManager associated with this instance
     * @param messengerView The MessengerView associated with this class
     */
    public SendMessageSystem(ConferenceEventManager conferenceEventManager,
                             MessengerView messengerView,
                             MessengerManager messengerManager,
                             UserLoginManager userLoginManager) {
        this.conferenceEventManager = conferenceEventManager;
        this.messengerView = messengerView;
        this.messengerManager = messengerManager;
        this.userLoginManager = userLoginManager;
    }

    /**
     * Sends a message to users of a given role
     *
     * @param role The role of the users to which the current user would like to message
     */
    public void sendToRole(Role role) {
        String messageText = "";
        this.messengerView.displayEnterMessagePrompt();

        try {
            messageText = br.readLine();
        } catch (IOException ioException) {
            messengerView.displaySomethingWentWrong();
        }

        try {
            messengerManager.organizerSendToAllUserRole(userLoginManager.getCurrentUserId(),
                    messageText, role);
            messengerView.displayMessageSuccess();
        } catch (EmptyReceiversException e){
            messengerView.displayEmptyListError();
        }
    }

    /**
     * Sends a message to all the attendees of the current users talks
     *
     */
    public void sendToAttendeesOfEvent() {
        String messageText = "";
        this.messengerView.displayEnterMessagePrompt();

        try {
            messageText = br.readLine();
        } catch (IOException ioException) {
            messengerView.displaySomethingWentWrong();
        }

        List<String> conferenceEvents = conferenceEventManager
                .getSpeakerEventNames(userLoginManager.getCurrentUserId());
        try {
            messengerManager.speakerSendToAllAttendeesOfTalks(userLoginManager
                    .getCurrentUserId(), conferenceEvents, messageText, this.conferenceEventManager);
            messengerView.displayMessageSuccess();
        } catch (EmptyReceiversException e){
            messengerView.displayEmptyListError();
        }
    }

    /**
     * Sends a message to all the attendees of one of the current users talks
     *
     */

    public void sendToAttendeeOfOneEvent() {
        List<String> eventNames = conferenceEventManager
                .getSpeakerEventNames(userLoginManager.getCurrentUserId());

        List<String> validTalkOptions = messengerView.displayTalkOptions(eventNames);

        String talkChoice = requestInputWithoutRepeat(validTalkOptions);
        String messageText = "";
        this.messengerView.displayEnterMessagePrompt();

        try {
            messageText = br.readLine();
        } catch (IOException ioException) {
            messengerView.displaySomethingWentWrong();
        }

        ArrayList<String> talk = new ArrayList<>();
        talk.add(eventNames.get(Integer.parseInt(talkChoice)));
        try {
            this.messengerManager.speakerSendToAllAttendeesOfTalks(userLoginManager
                    .getCurrentUserId(), talk, messageText, this.conferenceEventManager);
            messengerView.displayMessageSuccess();
        } catch (EmptyReceiversException e) {
            messengerView.displayEmptyListError();
        }
    }

    /**
     * Sends a message to a specific attendee or speaker
     *
     */
    public void sendToAttendeeOrSpeaker() {
        this.messengerView.displayEmailPrompt();

        String email = "";
        boolean emailDoesNotExist;
        boolean isEqual = true;

        do {
            try {
                email = br.readLine();
            } catch (IOException e) {
                messengerView.displaySomethingWentWrong();
            }
            emailDoesNotExist = !userLoginManager.getUserRepository().hasUserByEmail(email);

            if (emailDoesNotExist) {
                messengerView.displayAUserWithThisEmailDoesNotExist();
            } else {
                try {
                    isEqual = userLoginManager.getUserRepository().getUserIdByEmail(email)
                            .equals(userLoginManager.getCurrentUserId());
                } catch (UserNotFoundException e) {
                    messengerView.displayAUserWithThisEmailDoesNotExist();
                }

                if (isEqual) {
                    messengerView.displaySendToYourself();
                }
            }

        } while (emailDoesNotExist || isEqual);

        String recipientId = "";

        try {
            recipientId = this.messengerManager.getUserManager().getUserIdByEmail(email);
        } catch (UserNotFoundException e) {
            this.messengerView.displayAUserWithThisEmailDoesNotExist();
        }
        String messageText = "";
        this.messengerView.displayEnterMessagePrompt();
        try {
            messageText = br.readLine();
        } catch (IOException ioException) {
            messengerView.displaySomethingWentWrong();
        }
        try {
            this.messengerManager.organizerOrAttendeeSendToAttendeeOrSpeaker(userLoginManager
                    .getCurrentUserId(), recipientId, messageText);
            messengerView.displayMessageSuccess();
        } catch (EmptyReceiversException e) {
            messengerView.displayEmptyListError();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        // noImplementation
    }
}
