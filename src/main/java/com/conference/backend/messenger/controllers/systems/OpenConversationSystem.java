package com.conference.backend.messenger.controllers.systems;

import com.conference.backend.data.utils.base.InputProcessor;
import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.exception.MessageNotFoundException;
import com.conference.backend.messenger.managers.MessageManager;
import com.conference.backend.messenger.managers.MessengerManager;
import com.conference.backend.security.CustomRequestCache;
import com.conference.backend.users.UserLoginManager;
import com.conference.frontend.messenger.MessengerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenConversationSystem extends InputProcessor implements Startable {
    private final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private final MessengerView messengerView;
    private final UserLoginManager userLoginManager;
    private final MessengerManager messengerManager;
    private final CustomRequestCache<List<String>> customRequestCache;

    /**
     * Constructs a new instance of ViewConversationsLauncher with the given data.
     *
     * @param customRequestCache The CustomRequestCache associated with this instance for saving internal requests
     * @param userLoginManager The UserLoginManager associated with this instance
     * @param messengerManager he MessengerManager associated with this instance
     */
    public OpenConversationSystem(UserLoginManager userLoginManager,
                                  MessengerManager messengerManager,
                                  CustomRequestCache<List<String>> customRequestCache) {
        this.userLoginManager = userLoginManager;
        this.messengerManager = messengerManager;
        this.messengerView = new MessengerView();
        this.customRequestCache = customRequestCache;
    }

    /**
     * Displays the list of conversations and reads input from the client to choose which to view. Then display message
     * history of the conversation and reads input from the client to decide on further actions involving deleting
     * messages and conversations, archiving and unarchiving the conversation, and mark the conversation as Read/Unread
     */
    @Override
    public void start()  {
        List<String> conversationList = customRequestCache.pullRequest();

        messengerView.promptConversationSelection();
        ArrayList<String> validOptions = new ArrayList<>();

        for (int i = 0; i < conversationList.size(); i++) {
            validOptions.add(Integer.toString(i));
        }

        String choice = requestInputWithoutRepeat(validOptions);

        MessageManager messageManager = this.messengerManager.getMessageManager(userLoginManager.getCurrentUserId());
        String convoToView = conversationList.get(Integer.parseInt(choice));

        for(String message : this.messengerManager.getConvoMessageIdsByConvoId(convoToView)){
            if (!messageManager.getDeletedMessagesInConversations().containsKey(convoToView) ||
                    !messageManager.getDeletedMessagesInConversations().get(convoToView).contains(message)){
                String messageText;
                try {
                    messageText = this.messengerManager.getMessageTextByMessageIdAndConvoId(message, convoToView);
                    messengerView.displayMessage(this.messengerManager.getUserManager()
                            .getNameById(this.messengerManager.getSenderIdByMessageIdAndConvoId(message, convoToView)),
                            messageText + " (ID: " + message + ")");

                } catch (MessageNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        messageManager.markConversationAsRead(convoToView);
        messengerView.displayEndOfMessages(choice);

        ArrayList<String> validChoice = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6"));
        String newChoice = requestInputWithoutRepeat(validChoice);

        switch (newChoice) {
            case "0":
                break;
            case "1":
                try {
                    this.replyMessage(userLoginManager.getCurrentUserId(), convoToView);
                } catch (IOException e) {
                    messengerView.displaySomethingWentWrong();
                }
                break;
            case "2":
                messageManager.markConversationAsUnread(convoToView);
                break;
            case "3":
                messageManager.archiveConversation(convoToView);
                break;
            case "4":
                messageManager.removeConversationFromArchived(convoToView);
                break;
            case "5":
                messageManager.deleteConversation(convoToView,
                        this.messengerManager.getConvoMessageIdsByConvoId(convoToView));
                break;
            case "6":
                messengerView.promptSelectDeleteMessage();
                String msgChoice = "";
                try {
                    msgChoice = br.readLine();
                } catch (IOException e) {
                    messengerView.displaySomethingWentWrong();
                }

                String senderId;
                do {
                    try {
                        senderId = this.messengerManager.getSenderIdByMessageIdAndConvoId(msgChoice,convoToView);
                        break;
                    }
                    catch (MessageNotFoundException ignored) {}
                    messengerView.displayInvalidMessageId();
                    try {
                        msgChoice = br.readLine();
                    } catch (IOException e) {
                        messengerView.displaySomethingWentWrong();
                    }
                } while (true);

                if (senderId.equals(userLoginManager.getCurrentUserId())) {
                    messengerView.displayDeleteOptions1();
                    ArrayList<String> validDeleteOptions = new ArrayList<>(Arrays.asList("0", "1", "2"));

                    String deleteChoice = requestInputWithoutRepeat(validDeleteOptions);

                    switch(deleteChoice) {
                        case "0":
                            break;
                        case "1":
                            this.messengerManager.getMessageManager(userLoginManager.getCurrentUserId()).
                                    deleteMessageInConversation(convoToView, msgChoice);
                            break;
                        case "2":
                            try {
                                this.messengerManager.deleteMessageInAllConvosByMsgIdAndConvoId(convoToView, msgChoice);
                            } catch (MessageNotFoundException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                } else {
                    messengerView.displayDeleteOptions2();
                    ArrayList<String> validDeleteOptions = new ArrayList<>(Arrays.asList("0", "1"));
                    String deleteChoice = requestInputWithoutRepeat(validDeleteOptions);

                    switch(deleteChoice) {
                        case "0":
                            break;
                        case "1":
                            this.messengerManager.getMessageManager(userLoginManager.getCurrentUserId()).
                                    deleteMessageInConversation(convoToView, msgChoice);
                            break;
                    }
                }

                break;
        }
    }

    /**
     * System for allowing users to respond to a conversation
     *
     * @param currentUser The ID of the current user
     * @param conversation The ID of the conversation to which this user wishes to reply to
     * @throws  IOException if error occurs while reading from file
     */
    private void replyMessage(String currentUser, String conversation) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String messageText;
        this.messengerView.displayEnterMessagePrompt();
        messageText = br.readLine();
        this.messengerManager.replyToMessageInConversation(currentUser, conversation, messageText);
    }
}
