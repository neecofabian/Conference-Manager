package com.conference.frontend;

import com.conference.backend.data.utils.Role;
import com.conference.backend.users.User;
import com.conference.backend.data.utils.Value;
import com.conference.frontend.main.ConferencePlanningSystem;

import java.util.*;

public class ConferenceUtils {
    /**
     * Checks if an input string is valid based on the given options.
     *
     * @param input The input the client code entered
     * @param validOptions The options from which the input should be in
     * @return {@code true} if the input is in the options
     */
    public boolean isValid(String input, List<String> validOptions) {
        return validOptions.contains(input);
    }

    /**
     * The options this {@link User} has access to
     *
     * @param roles The roles of the logged in {@link User}
     * @param map The map to show which menu options this {@link User} can see.
     *            {@link ConferencePlanningSystem} for usage.
     * @return a list of {@code String}s which this User can see
     */
    public List<String> getValidOptionsForUser(List<Role> roles, Map<String, Value> map) {
        HashSet<String> validOptions = new HashSet<>();
        for (String menuChoice : map.keySet()) {
            List<Role> roles1 = Arrays.asList(map.get(menuChoice).getRoles());
            for (Role r : roles) {
                if (roles1.contains(r)) {
                    validOptions.add(menuChoice);
                }
            }
        }
        return new ArrayList<>(validOptions);
    }
}
