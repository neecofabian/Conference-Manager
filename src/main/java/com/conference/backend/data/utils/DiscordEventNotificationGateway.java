package com.conference.backend.data.utils;

import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.data.utils.base.Initializer;
import com.conference.backend.data.utils.command.NotifyScheduleAndEventCommand;
import com.conference.backend.users.UserManager;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;


public class DiscordEventNotificationGateway implements Initializer {
    private final DiscordApi api;
    private final ConferenceEventManager conferenceEventManager;
    private final UserManager userManager;


    /**
     * Constructs an instance of this class with the given params.
     *
     * @param token the Discord bot token
     * @param conferenceEventManager the event repository
     * @param userManager the user repository
     */
    public DiscordEventNotificationGateway(String token,
                                           ConferenceEventManager conferenceEventManager,
                                           UserManager userManager) {
        api = new DiscordApiBuilder().setToken(token).login().join();
        this.conferenceEventManager = conferenceEventManager;
        this.userManager = userManager;

        init();
    }

    /**
     * Initializes the message create listeners.
     */
    @Override
    public void init() {
        FallbackLoggerConfiguration.setDebug(false);
        FallbackLoggerConfiguration.setTrace(false);

        api.addMessageCreateListener(new NotifyScheduleAndEventCommand(conferenceEventManager, userManager));

        api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equalsIgnoreCase("!conference")) {
                event.getChannel().sendMessage("Event!");
            }

            if (event.getMessageContent().equalsIgnoreCase("!rick")) {
                event.getChannel().sendMessage("Roll! " + "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
            }
        });

    }
}
