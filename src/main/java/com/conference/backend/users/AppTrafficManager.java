package com.conference.backend.users;

import java.io.Serializable;

/**
 * The use case class that stores and manages all the app traffic statistics.
 */
public class AppTrafficManager implements Serializable {
    private static final long serialVersionUID = 237842345054675024L;

    AppTrafficStats appTrafficStats;

    /**
     * Initializes this AppTrafficManager.
     */
    public AppTrafficManager() {
        appTrafficStats = new AppTrafficStats();
    }

    /**
     * Fetches the statistic for the total number of notifications sent to the phone.
     *
     * @return the total number of notifications sent to the phone
     */
    public Integer getNotificationsSent() {
        return appTrafficStats.getNotificationsSent();
    }

    /**
     * Updates the statistic for the total number of notifications sent to the phone.
     */
    public void updateNotificationsSent() {
        appTrafficStats.setNotificationsSent(appTrafficStats.getNotificationsSent() + 1);
    }

    /**
     * Fetches the statistic for the total number of logins made by all users.
     *
     * @return the total number of logins made by all users
     */
    public Integer getLoginsAllTime() {
        return appTrafficStats.getLoginsAllTime();
    }

    /**
     * Updates the statistic for the total number of logins made by all users.
     */
    public void updateLoginsAllTime() {
        appTrafficStats.setLoginsAllTime(appTrafficStats.getLoginsAllTime() + 1);
    }

    /**
     * Fetches the statistic for the total number of logouts made by all users.
     *
     * @return the total number of logouts made by all users
     */
    public Integer getLogoutsAllTime() {
        return appTrafficStats.getLogoutsAllTime();
    }

    /**
     * Updates the statistic for the total number of logouts made by all users.
     */
    public void updateLogoutsAllTime() {
        appTrafficStats.setLogoutsAllTime(appTrafficStats.getLogoutsAllTime() + 1);
    }

    /**
     * Fetches the statistic for the total number of events signups.
     *
     * @return the total number of events signups
     */
    public Integer getNumEventSignUpAllTime() {
        return appTrafficStats.getNumEventSignUpAllTime();
    }

    /**
     * Updates the statistic for the total number of events signups.
     */
    public void updateNumEventSignUpAllTime() {
        appTrafficStats.setNumEventSignUpAllTime(appTrafficStats.getNumEventSignUpAllTime() + 1);
    }

    /**
     * Fetches the statistic for the total number of signups made by all users.
     *
     * @return the total number of signups made by all users
     */
    public Integer getNumSignUps() {
        return appTrafficStats.getNumSignUps();
    }

    /**
     * Updates the statistic for the total number of signups made by all users.
     */
    public void updateNumSignUps() {
        appTrafficStats.setNumSignUps(appTrafficStats.getNumSignUps() + 1);
    }

    /**
     * Fetches the statistic for the total number of conference events deleted.
     *
     * @return the total number of conference events deleted
     */
    public Integer getNumOfConferenceEventsDeletedAllTime() {
        return appTrafficStats.getNumOfConferenceEventsDeletedAllTime();
    }

    /**
     * Updates the statistic for the total number of conference events deleted.
     */
    public void updateNumOfConferenceEventsDeletedAllTime() {
        appTrafficStats.setNumOfConferenceEventsDeletedAllTime(appTrafficStats
                .getNumOfConferenceEventsDeletedAllTime() + 1);
    }

    /**
     * Fetches the statistic for the total number of conference events created.
     *
     * @return the total number of conference events created
     */
    public Integer getNumOfConferencesCreatedAllTime() {
        return appTrafficStats.getNumOfConferencesCreatedAllTime();
    }

    /**
     * Updates the statistic for the total number of conference events created.
     */
    public void updateNumOfConferencesCreatedAllTime() {
        appTrafficStats.setNumOfConferencesCreatedAllTime(appTrafficStats.getNumOfConferencesCreatedAllTime() + 1);
    }

    /**
     * Fetches the statistic for the total number of edit attempts made.
     *
     * @return the total number of edit attempts made
     */
    public Integer getNumOfEditAttemptsMadeAllTime() {
        return appTrafficStats.getNumOfEditAttemptsMadeAllTime();
    }

    /**
     * Updates the statistic for the total number of edit attempts made.
     */
    public void updateNumOfEditAttemptsMadeAllTime() {
        appTrafficStats.setNumOfEditAttemptsMadeAllTime(appTrafficStats.getNumOfEditAttemptsMadeAllTime() + 1);
    }

    /**
     * Fetches the statistic for the total number of speakers added.
     *
     * @return the total number of speakers added
     */
    public Integer getNumOfSpeakersAddedAllTime() {
        return appTrafficStats.getNumOfSpeakersAddedAllTime();
    }

    /**
     * Updates the statistic for the total number of speakers added.
     */
    public void updateNumOfSpeakersAddedAllTime() {
        appTrafficStats.setNumOfSpeakersAddedAllTime(appTrafficStats.getNumOfSpeakersAddedAllTime() + 1);
    }
}
