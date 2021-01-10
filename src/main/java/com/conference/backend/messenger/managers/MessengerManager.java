package com.conference.backend.messenger.managers;

import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.conference_and_rooms.entities.ConferenceEvent;
import com.conference.backend.data.utils.*;
import com.conference.backend.exception.*;
import com.conference.backend.messenger.entities.Conversation;
import com.conference.backend.messenger.entities.Message;
import com.conference.backend.users.User;
import com.conference.backend.users.UserManager;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class MessengerManager implements Serializable {
    private Map<String, MessageManager> messageManagerMap;
    private List<Conversation> allConversations;
    private UserManager userManager;
    private static final long serialVersionUID = 74532853098L;


    /**
     * Constructs a new instance of MessengerManager.
     *
     * @param userManager the UserManager associated with this MessengerManager
     */
    public MessengerManager(UserManager userManager) {
        this.allConversations = new ArrayList<>();
        this.messageManagerMap = new HashMap<>();
        this.userManager = userManager;

    }

    /**
     * Adds a new MessageManager mapped to the userID
     *
     * @param userID the user ID to whom this MessageManager belongs
     */
    public void addMessageManager(String userID) {
        if (!this.messageManagerMap.containsKey(userID)) {
            MessageManager newMessageManager = new MessageManager(userID);
            this.messageManagerMap.put(userID, newMessageManager);
        }
    }

    /**
     * Returns the UserManager associated with this MessengerManager.
     *
     * @return the {@code UserManager} UserManager associated with this MessageManager.
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Sets the UserManager associated with this MessengerManager.
     *
     * @param userManager The UserManager to be set for this MessageManager.
     */
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * Returns the MessageManager associated with the userID.
     *
     * @param userID the user ID to whom the desired MessageManager belongs to
     * @return the {@code MessageManger} MessageManager associated with this userID.
     */
    public MessageManager getMessageManager(String userID) {
        return this.messageManagerMap.get(userID);
    }

    /**
     * Instantiates a new Conversation with one message
     *
     * @param userID the user ID sending the message
     * @param receivers a list of user IDs who of the users receiving the message in this conversation
     * @param messageText the String text body of the message being sent
     * @throws EmptyReceiversException if the receiver list is empty
     */
    private void sendMessageInNewConversation(String userID, List<String> receivers, String messageText) throws EmptyReceiversException {
        if (receivers.isEmpty()) {
            throw new EmptyReceiversException("There are no receivers.");
        }
        Message message = new Message(messageText, LocalDateTime.now(), userID);
        receivers.add(userID);
        Conversation newConversation = new Conversation(receivers, message);
        this.allConversations.add(newConversation);
        for (String participant : receivers) {
            MessageManager messageManager = this.getMessageManager(participant);
            messageManager.addConversation(newConversation.getId());
            this.getMessageManager(userID).markConversationAsRead(newConversation.getId());
        }
    }

    /**
     * Adds a message to an existing Conversation
     *
     * @param userID the String ID of the user replying
     * @param conversationId the String ID of the conversation being replied in
     * @param messageText the String text body of the message being replied
     */
    public void replyToMessageInConversation(String userID, String conversationId, String messageText) {
        Message message = new Message(messageText, LocalDateTime.now(), userID);
        Conversation conversation = this.getConvoByConvoId(conversationId);
        if (conversation != null) {
            conversation.sendMessage(message);
        }
        if (conversation != null) {
            for (String participantID: conversation.getMembers()){
                MessageManager messageManager = this.getMessageManager(participantID);
                if (!messageManager.getConversationsMap().containsKey(conversationId)){
                    messageManager.addConversation(conversationId);
                }
                messageManager.markConversationAsUnread(conversationId);
                messageManager.removeConversationFromArchived(conversationId);
            }
        }
        this.getMessageManager(userID).markConversationAsRead(conversationId);
    }

    /**
     * Sends a message to all attendees of a speaker's talks
     *
     * @param speakerID the String ID of the speaker sending this message
     * @param eventNames the list of event IDs of the events this speaker is speaking at
     * @param messageText the text body of the message being sent
     * @param conferenceEventManager the ConferenceEventManager associated with this session
     * @throws EmptyReceiversException if the list of receivers is empty
     */
    public void speakerSendToAllAttendeesOfTalks(String speakerID,
                                                 List<String> eventNames,
                                                 String messageText,
                                                 ConferenceEventManager conferenceEventManager)
            throws EmptyReceiversException {

        List<String> attendeeIDs = new ArrayList<>();
        for (String eventName : eventNames) {
            ConferenceEvent conferenceEvent = conferenceEventManager.getConferenceEvent(eventName);
            for (String attendeeID : conferenceEvent.getAttendeeList()) {
                if (!attendeeIDs.contains(attendeeID)){
                    attendeeIDs.add(attendeeID);}
            }
        }
        this.sendMessageInNewConversation(speakerID, attendeeIDs, messageText);
    }

    /**
     * Sends a message to all users of a given role
     *
     * @param organizerID the ID of the organizer who is sending this message
     * @param sendToRole the role of users who will receive this message
     * @param messageText the text body of the message being sent
     * @throws EmptyReceiversException if the receiver list is empty
     */
    public void organizerSendToAllUserRole(String organizerID,
                                           String messageText,
                                           Role sendToRole) throws EmptyReceiversException {
        List<String> allSendToRole = new ArrayList<>();
        for (User user : this.userManager.getUsers()) {
            if (user.getRoles().contains(sendToRole)) {
                allSendToRole.add(user.getId());
            }
        }
        this.sendMessageInNewConversation(organizerID, allSendToRole, messageText);
    }

    /**
     * Sends a message to all users of a given role
     *
     * @param organizerOrAttendee the ID of the organizer or attendee who is sending this message
     * @param attendeeOrSpeaker the ID of the attendee or speaker who will receive this message
     * @param messageText the text body of the message being sent
     * @throws EmptyReceiversException if the list of receivers is empty
     */
    public void organizerOrAttendeeSendToAttendeeOrSpeaker(String organizerOrAttendee,
                                                           String attendeeOrSpeaker,
                                                           String messageText) throws EmptyReceiversException {
        List<String> attendeeOrSpeakerIDs = new ArrayList<>();
        attendeeOrSpeakerIDs.add(attendeeOrSpeaker);
        this.sendMessageInNewConversation(organizerOrAttendee, attendeeOrSpeakerIDs, messageText);
    }

    /**
     * Deletes a given message from a given conversation for everyone
     *
     * @param conversationId the ID of the conversation to which the message belongs
     * @param messageId the ID of the message being deleted
     * @throws MessageNotFoundException if the message with the given ID is not found
     */
    public void deleteMessageInAllConvosByMsgIdAndConvoId(String conversationId,
                                                          String messageId) throws MessageNotFoundException {
        Conversation conversation = this.getConvoByConvoId(conversationId);
        Message message = this.getMessageByMessageIdAndConvoId(messageId, conversationId);
        if (conversation != null) {
            conversation.deleteMessage(message);
        }
    }

    /**
     * Returns a list of conversation members' IDs in a given conversation given the Conversation ID
     *
     * @param convoId the ID of the conversation
     * @return the {@code List<String>} List of IDs of the participants in the conversation.
     */
    public List<String> getConversationMemberIdsByConvoId(String convoId) {
        Conversation convo = null;
        for (Conversation conversation : allConversations) {
            if (conversation.getId().equals(convoId)) {
                convo = conversation;
            }
        }
        if (convo != null) {
            return convo.getMembers();
        } return null;
    }

    /**
     * Returns a list of message IDs in a given conversation given the Conversation ID
     *
     * @param convoId the ID of the conversation
     * @return the {@code List<String>} List of IDs of the messages in the conversation.
     */
    public List<String> getConvoMessageIdsByConvoId(String convoId) {
        for (Conversation conversation : allConversations){
            if (conversation.getId().equals(convoId)){
                return conversation.getMessageIds();
            }
        } return null;
    }

    /**
     * Returns a given conversation according to its ID
     *
     * @param convoId the ID of the conversation
     * @return the {@code Conversation} associated with the convoId.
     */
    private Conversation getConvoByConvoId (String convoId) {
        for (Conversation conversation : allConversations){
            if (conversation.getId().equals(convoId)){
                return conversation;
            }
        }
        return null;
    }

    /**
     * Returns the message based on a given conversation ID and Message ID
     *
     * @param convoId the ID of the conversation containing the message
     * @param messageId the ID of the message
     * @return the {@code Message} associated with the messageId and convoId.
     * @throws  MessageNotFoundException if the conversation with the given ID does not contain a message with
     * the given ID
     */
    private Message getMessageByMessageIdAndConvoId(String messageId,
                                                    String convoId) throws MessageNotFoundException {
        return Objects.requireNonNull(this.getConvoByConvoId(convoId)).getMessageByID(messageId);
    }

    /**
     * Returns the ID of a user who sent a given message in a given conversation
     *
     * @param convoId the ID of the conversation
     * @param messageId the ID of the message
     * @return the {@code String} ID of the sender that sent the message associated with the messageID and convoID
     * @throws MessageNotFoundException if the conversation with the given ID does not contain a message with the
     * given ID
     */
    public String getSenderIdByMessageIdAndConvoId(String messageId,
                                                   String convoId) throws MessageNotFoundException {
        return Objects.requireNonNull(this.getConvoByConvoId(convoId)).getMessageByID(messageId).getSenderID();
    }

    /**
     * Returns a the text body of a given message in a given conversation
     *
     * @param convoId the ID of the conversation
     * @param messageId the ID of the message
     * @return the {@code String} text associated with the messageId and convoId.
     * @throws MessageNotFoundException if the conversation with the given ID does not contain a message with the
     * given ID
     */
    public String getMessageTextByMessageIdAndConvoId(String messageId,
                                                      String convoId) throws MessageNotFoundException {
        return Objects.requireNonNull(this.getConvoByConvoId(convoId)).getMessageByID(messageId).getText();
    }

    /**
     * Updates the MessageManager stored in this messengerManager based on the UserManager stored in this
     * messengerManager
     *
     */
    public void update(){
        for (User user: this.userManager.getUsers()){
            String id = user.getId();
            if (!this.messageManagerMap.containsKey(id)){
                addMessageManager(id);
            }
        }
    }

}