package com.conference.backend.users;

import com.conference.backend.data.utils.base.AbstractEntity;

/**
 * The entity class that represents all app traffic statistics for the application.
 */
public class AppTrafficStats extends AbstractEntity {
    private static final long serialVersionUID = 3782648273648273L;

    private int notificationsSent;
    private int loginsAllTime;
    private int logoutsAllTime;
    private int numEventSignUpAllTime;
    private int numSignUps;
    private int numOfConferenceEventsDeletedAllTime;
    private int numOfConferencesCreatedAllTime;
    private int numOfEditAttemptsMadeAllTime;
    private int numOfSpeakersAddedAllTime;

    /**
     * Fetches the statistic for the total number of conference events deleted.
     *
     * @return the total number of conference events deleted
     */
    public Integer getNumOfConferenceEventsDeletedAllTime() {
        return numOfConferenceEventsDeletedAllTime;
    }

    /**
     * Sets a new statistic for the total number of conference events deleted.
     *
     * @param numOfConferenceEventsDeletedAllTime the new number of conference events deleted
     */
    public void setNumOfConferenceEventsDeletedAllTime(int numOfConferenceEventsDeletedAllTime) {
        this.numOfConferenceEventsDeletedAllTime = numOfConferenceEventsDeletedAllTime;
    }

    /**
     * Fetches the statistic for the total number of conference events created.
     *
     * @return the total number of conference events created
     */
    public Integer getNumOfConferencesCreatedAllTime() {
        return numOfConferencesCreatedAllTime;
    }

    /**
     * Sets a new statistic for the total number of conference events created.
     * @param numOfConferencesCreatedAllTime the new number of conference events created
     */
    public void setNumOfConferencesCreatedAllTime(int numOfConferencesCreatedAllTime) {
        this.numOfConferencesCreatedAllTime = numOfConferencesCreatedAllTime;
    }

    /**
     * Fetches the statistic for the total number of edit attempts made.
     *
     * @return the total number of edit attempts made
     */
    public Integer getNumOfEditAttemptsMadeAllTime() {
        return numOfEditAttemptsMadeAllTime;
    }

    /**
     * Sets a new statistic for the total number of edit attempts made.
     *
     * @param numOfEditAttemptsMadeAllTime the new number of edit attempts made
     */
    public void setNumOfEditAttemptsMadeAllTime(int numOfEditAttemptsMadeAllTime) {
        this.numOfEditAttemptsMadeAllTime = numOfEditAttemptsMadeAllTime;
    }

    /**
     * Fetches the statistic for the total number of speakers added.
     *
     * @return the total number of speakers added
     */
    public Integer getNumOfSpeakersAddedAllTime() {
        return numOfSpeakersAddedAllTime;
    }

    /**
     * Sets a new statistic for the total number of speakers added.
     *
     * @param numOfSpeakersAddedAllTime the new number of speakers added
     */
    public void setNumOfSpeakersAddedAllTime(int numOfSpeakersAddedAllTime) {
        this.numOfSpeakersAddedAllTime = numOfSpeakersAddedAllTime;
    }

    /**
     * Fetches the statistic for the total number of notifications sent to the phone.
     *
     * @return the total number of notifications sent to the phone
     */
    public Integer getNotificationsSent() {
        return notificationsSent;
    }

    /**
     * Sets a new statistic for the total number of notifications sent to the phone.
     *
     * @param notificationsSent the new number of notifications sent to the phone
     */
    public void setNotificationsSent(Integer notificationsSent) {
        this.notificationsSent = notificationsSent;
    }

    /**
     * Fetches the statistic for the total number of logins made by all users.
     *
     * @return the total number of logins made
     */
    public Integer getLoginsAllTime() {
        return loginsAllTime;
    }

    /**
     * Sets a new statistic for the total number of logins made by all users.
     *
     * @param loginsAllTime the new number of logins made
     */
    public void setLoginsAllTime(Integer loginsAllTime) {
        this.loginsAllTime = loginsAllTime;
    }

    /**
     * Fetches the statistic for the total number of logouts made by all users.
     *
     * @return the total number of logouts made
     */
    public Integer getLogoutsAllTime() {
        return logoutsAllTime;
    }

    /**
     * Sets a new statistic for the total number of logouts made by all users.
     *
     * @param logoutsAllTime the new number of logouts made
     */
    public void setLogoutsAllTime(Integer logoutsAllTime) {
        this.logoutsAllTime = logoutsAllTime;
    }

    /**
     * Fetches the statistic for the total number of events signups.
     *
     * @return the total number of events signups
     */
    public Integer getNumEventSignUpAllTime() {
        return numEventSignUpAllTime;
    }

    /**
     * Sets a new statistic for the total number of events signups.
     *
     * @param numEventSignUpAllTime the new total number of events signups
     */
    public void setNumEventSignUpAllTime(Integer numEventSignUpAllTime) {
        this.numEventSignUpAllTime = numEventSignUpAllTime;
    }

    /**
     * Fetches the statistic for the total number of signups made by all users.
     *
     * @return the total number of signups made
     */
    public Integer getNumSignUps() {
        return numSignUps;
    }

    /**
     * Sets a new statistic for the total number of signups made by all users.
     *
     * @param numSignUps the new number of signups made
     */
    public void setNumSignUps(Integer numSignUps) {
        this.numSignUps = numSignUps;
    }
}
