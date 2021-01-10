package com.conference.backend.messenger.managers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageManager implements Serializable {

    private final String userID;
    private final Map<String, String> conversationsMap;
    private final Map<String, List<String>> deletedMessagesInConversations;
    private final List<String> archivedConversations;
    private static final long serialVersionUID = 6308543284L;

    /**
     * Constructs a new instance of MessageManager for a specific user.
     *
     * @param userID the ID of the user associated with this MessageManager
     */
    public MessageManager(String userID) {
        this.conversationsMap = new HashMap<>();
        this.userID = userID;
        this.archivedConversations = new ArrayList<>();
        this.deletedMessagesInConversations = new HashMap<>();

    }

    /**
     * Returns the ID of the user associated with this MessageManager.
     *
     * @return the {@code String} ID of the user associated with this MessageManager
     */
    public String getUserID() {
        return this.userID;
    }

    /**
     * Returns a List of all the conversations in this MessageManager.
     *
     * @return the List of IDs of the conversations in this MessageManager
     */
    public List<String> getUnarchivedConversationsIds() {
        List<String> conversationsList = new ArrayList<>();
        for (String conversation : this.conversationsMap.keySet()){
            if (!this.archivedConversations.contains(conversation)){
                conversationsList.add(conversation);
            }
        }
        return conversationsList;
    }

    /**
     * Returns the conversation map with key: conversation ID and value: "Read or "Unread"
     *
     * @return the Map of conversations with (Key: conversationID) and (value: "Read or "Unread")
     */
    public Map<String, String> getConversationsMap() {
        return this.conversationsMap;
    }

    /**
     * Adds the conversation to this MessageManager with status "Unread"
     *
     * @param conversation the String ID of the conversation being added to this MessageManager
     */
    public void addConversation(String conversation){ this.conversationsMap.put(conversation, "Unread"); }

    /**
     * Archives the conversation by adding the conversation ID to the archivedConversation list
     *
     * @param conversation the String ID of the conversation being archived.
     */
    public void archiveConversation(String conversation) {
        if (!this.archivedConversations.contains(conversation)) {
            this.archivedConversations.add(conversation);
        }
    }

    /**
     * Unarchives the conversation by removeing the conversation ID from the archivedConversation list.
     *
     * @param conversation the String ID of the conversation being unarchived.
     */
    public void removeConversationFromArchived (String conversation) {
        this.archivedConversations.remove(conversation);
    }

    /**
     * Deletes the message in the conversation for only this MessageManager by adding the the message ID to the
     * deletedMessagesInConversations Map.
     *
     * @param conversation the String ID of the conversation being edited.
     * @param message the Strinhg ID of the message being deleted.
     */
    public void deleteMessageInConversation (String conversation, String message){
        if (this.deletedMessagesInConversations.containsKey(conversation)){
            if (!this.deletedMessagesInConversations.get(conversation).contains(message)){
                this.deletedMessagesInConversations.get(conversation).add(message);
            }
        }
        else {
            List<String> array = new ArrayList<>();
            array.add(message);
            this.deletedMessagesInConversations.put(conversation, array);
        }
    }

    /**
     * Marks the conversation as unread.
     *
     * @param conversation String ID of the conversation being marked.
     */
    public void markConversationAsUnread(String conversation){
        this.conversationsMap.replace(conversation, "Unread");
    }

    /**
     * Marks the conversation as read.
     *
     * @param conversation the String ID of the conversation being marked.
     */
    public void markConversationAsRead(String conversation){
        this.conversationsMap.replace(conversation, "Read");
    }

    /**
     * Deletes the conversation from this MessageManager along with all its messages
     *
     * @param conversation the String ID of the conversation being deleted.
     * @param messageListIDs the List of String IDs of the messages in the conversation being deleted also
     */
    public void deleteConversation(String conversation, List<String> messageListIDs){
        this.conversationsMap.remove(conversation);
        this.archivedConversations.remove(conversation);
        for (String message: messageListIDs){
            this.deleteMessageInConversation(conversation, message);
        }
    }

    /**
     * Returns the deleted messages associated with this MessageManager.
     *
     * @return the {@code Map<String, List<String>>} of deleted messages with key: ConversationID,
     * value: List of Message IDs
     */
    public Map<String, List<String>> getDeletedMessagesInConversations() {
        return deletedMessagesInConversations;
    }


    /**
     * Returns the IDs of the archived conversations associated with this MessageManager
     *
     * @return the {@code List<String>} of archived conversation IDs
     */
    public List<String> getArchivedConversationsIds() {
        return archivedConversations;
    }
}

