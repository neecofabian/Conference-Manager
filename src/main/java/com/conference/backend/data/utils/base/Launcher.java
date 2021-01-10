package com.conference.backend.data.utils.base;

import com.conference.backend.data.utils.DistanceSpellChecker;
import com.conference.backend.data.utils.Value;
import com.conference.frontend.ConferenceUtils;
import com.conference.frontend.EntityView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Launcher {
    private final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Requests input from the user given valid options and a map of options.
     * See {@link com.conference.backend.conference_and_rooms.controllers.ConferenceEventLauncher} for example usage.
     *
     *
     * @param validOptions the valid options
     * @param map the map with the string and roles
     * @param entityView the view to display the options
     * @return the input the client entered
     * @throws IOException if reading failed
     */
    protected String requestInput(List<String> validOptions, Map<String, Value> map, EntityView entityView)
            throws IOException {
        String choice;
        boolean menuOption;

        do {
            choice = br.readLine();
            menuOption = validOptions.contains(choice);
            if (!menuOption) {
                entityView.displayNotAMenuOption();
                entityView.displayOptions(validOptions, map);
            }
        } while (!menuOption);
        return choice;
    }
}
