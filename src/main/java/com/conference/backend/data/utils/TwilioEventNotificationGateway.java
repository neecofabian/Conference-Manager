//package com.conference.backend.data.utils;
//
//import com.conference.backend.conference_and_rooms.managers.ConferenceEventManager;
//import com.conference.backend.users.AppTrafficManager;
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;
//import java.util.Date;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class TwilioEventNotificationGateway {
//    private final ConferenceEventManager conferenceEventManager;
//    private final AppTrafficManager appTrafficManager;
//    private final String ADMIN_NUMBER = "+REDACTED";
//    private final String TWILIO_NUMBER = "+REDACTED";
//    private final String MAIN_ADMIN_NUMBER = "+REDACTED";
//    private final Timer timer;
//
//    /**
//     * Constructs an instance of this class with the given params.
//     * @param conferenceEventManager The conference event repository storing events
//     * @param appTrafficManager The app statistics
//     * @param accountSID the SID for Twilio API
//     * @param token the token for Twilio API
//     */
//    public TwilioEventNotificationGateway(ConferenceEventManager conferenceEventManager,
//                                          AppTrafficManager appTrafficManager,
//                                          String accountSID,
//                                          String token) {
//        this.conferenceEventManager = conferenceEventManager;
//        this.appTrafficManager = appTrafficManager;
//        Twilio.init(accountSID, token);
//        timer = new Timer();
//        updateTime();
//    }
//
//    private void updateTime() {
//        long interval = 1000; // 1 sec
//
//        timer.schedule(new TimerTask() {
//            public void run() {
//                if (notifyEventIsOccurring(TWILIO_NUMBER, ADMIN_NUMBER)) {
//                    timer.cancel();
//                    appTrafficManager.updateNotificationsSent();
//                }
//            }
//        }, 0, interval);
//    }
//
//    /**
//     * If an event is occurring in this Conference, send a text message to a recipient from a Twilio number.
//     * @param sender a verified caller ID
//     * @param recipient a Twilio number
//     * @return true if an event is occuring
//     */
//    public boolean notifyEventIsOccurring(String sender, String recipient) {
//        Date date = new Date(System.currentTimeMillis());
//
//        for (String name : conferenceEventManager.getConferenceEventNamesList()) {
//            String s = name + " is taking place in " + conferenceEventManager.getRoomByEventName(name).getRoomName()
//                    + " from " + conferenceEventManager.getDateByEventName(name);
//            if (date.after(conferenceEventManager.getDateByEventName(name).getStart())) {
//                Message.creator(new PhoneNumber(recipient),
//                        new PhoneNumber(sender), s).create();
//                Message.creator(new PhoneNumber(MAIN_ADMIN_NUMBER),
//                        new PhoneNumber(sender), s).create();
//                return true;
//            }
//        }
//        return false;
//    }
//}
