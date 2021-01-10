package com.conference.backend.messenger.entities;

import com.conference.backend.data.utils.base.AbstractEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Message extends AbstractEntity {
    private static final long serialVersionUID = 64327642534435L;

    private String text;
    private String senderID;
    private LocalDateTime date;

    /**
     * Constructs a new instance of Message with the given data.
     *
     * @param text the text of the message
     * @param date the date time of the message (year, month, day, hour, minute, seconds)
     *          date uses java.time.LocalDateTime
     * @param senderID the ID of this sender
     */
    public Message(String text, LocalDateTime date, String senderID) {
        this.text = text;
        this.date = date;
        this.senderID = senderID;
        setId(this.hashCode() + "");
    }

    /**
     * Returns the text of the message
     *
     * @return the {@code String} of the message
     */
    public String getText() { return this.text; }

    /**
     * Returns the date the message was created
     *
     * @return the {@code LocalDateTime} the message was created
     */
    public LocalDateTime getDate() { return this.date; }

    /**
     * Returns the sender ID associated with this message
     *
     * @return the {@code String} of the Sender ID
     */
    public String getSenderID() { return this.senderID; }

    /**
     * Returns the {@code String} representation of the message
     *
     * @return the {@code String} representation of the message
     */
    @Override
    public String toString() {
        return this.text + " (" + this.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")" +
                "  msg ID = " + this.getId();
    }

    /**
     * Hashes the {@code Message} based on its attributes
     *
     * @return an {@code int} based on text, senderID, date
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), text, senderID, date);
    }
}
