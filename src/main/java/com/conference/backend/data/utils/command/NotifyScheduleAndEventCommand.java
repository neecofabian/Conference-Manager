package com.conference.backend.data.utils.command;

import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
import com.conference.backend.conference_and_rooms.entities.EventType;
import com.conference.backend.users.UserManager;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NotifyScheduleAndEventCommand implements MessageCreateListener {

    private ConferenceEventManager conferenceEventManager;
    private UserManager userManager;

    /**
     * Constructs an instance of this class with the given params.
     * @param conferenceEventManager the event repository
     * @param userManager the user repository
     */
    public NotifyScheduleAndEventCommand(ConferenceEventManager conferenceEventManager, UserManager userManager) {
        this.conferenceEventManager = conferenceEventManager;
        this.userManager = userManager;
    }

    /**
     * When a message is created it notifies the discord server.
     *
     * @param event the listener event
     */
    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        notifySchedule(conferenceEventManager, event);
        notifyByEventName(conferenceEventManager, userManager, event);
    }

    private void notifySchedule(ConferenceEventManager conferenceEventManager, MessageCreateEvent event) {
        if (event.getMessageContent().equalsIgnoreCase("!schedule")) {

            StringBuilder sb = new StringBuilder();

            List<String> rooms = conferenceEventManager.getRoomRepository().getRoomToStrings();
            for (String r : rooms) {
                sb.append(r + '\n');
            }

            MessageBuilder mb = new MessageBuilder()
                    .append(sb.toString() + '\n')
                    .append("Here's a QR Code for the schedule of every room:");

            for (int i = 0; i < conferenceEventManager.getRoomRepository().size(); i++) {
                mb.addAttachment(new File("phase2/src/dao/" + i + ".png"));
            }

            mb.send(event.getChannel());
        }
    }

    private void notifyByEventName(ConferenceEventManager conferenceEventManager,
                                   UserManager userManager, MessageCreateEvent event) {
        for (String s : conferenceEventManager.getConferenceEventNamesList()) {
            StringBuilder sb2 = new StringBuilder("\n \t• Event Type: " +
                    conferenceEventManager.getEventTypeByEventName(s));

            if (conferenceEventManager.getEventTypeByEventName(s) == EventType.TALK) {
                sb2.append( "\n \t• Speaker: ");
            }

            if (conferenceEventManager.getEventTypeByEventName(s) == EventType.PANEL) {
                sb2.append("\n \t• Speakers:");
            }

            if (event.getMessageContent().equalsIgnoreCase("!event " + s)) {
                String e =  " " + String.format("(%d/%d seats)", conferenceEventManager
                                .getAttendeeListSizeByEventName(s),
                        conferenceEventManager.getCapacityByEventName(s))
                        + "\n \t• Showing: " + conferenceEventManager.getDateByEventName(s)
                        + "\n \t• Where: " + conferenceEventManager.getRoomRepository().getRoomNameByEventName(s)
                        + sb2.toString();

                List<String> speakers = new ArrayList<>();
                for (String spe : conferenceEventManager.getSpeakerIdsByEventName(s)) {
                    if (userManager.hasUserById(spe)) {
                        speakers.add(spe);
                    }
                }

                List<String> speakerNames = new ArrayList<>();
                for (String id : speakers) {
                    speakerNames.add(userManager.getNameById(id) + " (" + userManager.getEmailByUserId(id) + ")");
                }
                String output = String.join(", ", speakerNames);

                new MessageBuilder()
                        .append(s, MessageDecoration.BOLD, MessageDecoration.UNDERLINE)
                        .append(e + output)
                        .setEmbed(new EmbedBuilder()
                                .setTitle("WOW")
                                .setDescription("That's an amazing event! ~ Official Conference Bot")
                                .setColor(null))
                        .send(event.getChannel());
            }
        }
    }
}
