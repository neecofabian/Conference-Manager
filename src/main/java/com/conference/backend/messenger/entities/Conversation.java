package com.conference.backend.messenger.entities;

import com.conference.backend.data.utils.base.AbstractEntity;
import com.conference.backend.exception.MessageNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Conversation extends AbstractEntity {
    private static final long serialVersionUID = 2630428346283L;

    private List<String> participants;
    private List<Message> messages;

    /**
     * Constructs a new instance of conversation with the given data.
     *
     * @param participants the List of IDs of the participants of the conversation
     */
    public Conversation(List<String> participants){
        this.participants = participants;
        this.messages = new ArrayList<>();
        this.setId(this.hashCode() + "");
    }

    /**
     * Constructs a new instance of conversation with the given data.
     *
     * @param participants a List of the String IDs of the participants of this conversation
     * @param firstMessage the String of the first message sent in this conversation
     */
    public Conversation(List<String> participants, Message firstMessage) {
        this(participants);
        this.messages.add(firstMessage);
        this.setId(this.hashCode() + "");
    }

    /**
     * Returns the IDs of the participating members of this conversation
     *
     * @return a List of Strings representing the IDs of the participants of this conversation
     */
    public List<String> getMembers() {
        return this.participants;
    }

    /**
     * Returns the messages sent in this conversation
     *
     * @return a List of Messages sent in this conversation
     */
    public List<Message> getMessages() {
        return this.messages;
    }

    /**
     * Returns the message IDs sent in this conversation
     *
     * @return a List of String IDs for the Messages sent in this conversation
     */
    public List<String> getMessageIds(){
        List<String> messageIds = new ArrayList<>();
        for (Message message : this.messages){
            messageIds.add(message.getId());
        }
        return messageIds;
    }

    /**
     * Hashes the {@code Conversation} based on its attributes
     * @return an {@code int} based on participants, messages
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), participants, messages);
    }


    /**
     * Sends the message in this conversation
     *
     * @param newMessage the message being sent in this conversation
     */
    public void sendMessage(Message newMessage){
        this.messages.add(newMessage);
    }

    /**
     * Deletes the message in this conversation
     *
     * @param message the message being deleted in this conversation
     */
    public void deleteMessage(Message message) { this.messages.remove(message); }

    /**
     * Returns the message in the conversation based on the message ID
     *
     * @param ID the String ID of the message
     * @return the {@code Message} with the given ID
     * @throws MessageNotFoundException if the message is not found
     */
    public Message getMessageByID(String ID) throws MessageNotFoundException {
        for (Message message: this.messages){
            if (message.getId().equals(ID)){
                return message;
            }
        } throw new MessageNotFoundException("There is no such message.");
    }


}
