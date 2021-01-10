package com.conference;

import com.conference.backend.data.utils.base.Startable;
import com.conference.frontend.main.ConferencePlanningSystem;

public class Application {
    public static void main(String[] args) {
        final String dir = System.getProperty("user.dir");
        System.out.println("current dir = " + dir);
        Startable conferencePlanningSystem = new ConferencePlanningSystem();
        conferencePlanningSystem.start();
    }
}
