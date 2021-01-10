package com.conference.backend.users.controllers.subcontrollers;

import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.data.utils.Role;
import com.conference.backend.security.PropertiesIterator;
import com.conference.backend.exception.UserNotFoundException;
import com.conference.backend.security.CustomRequestCache;
import com.conference.backend.users.AppTrafficManager;
import com.conference.backend.users.User;
import com.conference.backend.users.UserLoginManager;
import com.conference.frontend.users.UserView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Controller class for signing up {@link User}s.
 */
public class UserSignUpSystem implements Startable {
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private final UserLoginManager userLoginManager;
    private final UserView userView;
    private final AppTrafficManager appTrafficManager;
    private final CustomRequestCache<Role> customRequestCache;
    private final String filePath;

    /**
     * Initializes the UserSignUpSystem.
     *
     * @param userLoginManager The {@code UserLoginManager} storing the current user and repository of users
     * @param appTrafficManager The {@code AppTrafficManager} to update statistics
     * @param customRequestCache The {@code CustomRequestCache} that saves internal requests
     * @param filePath the path to save to
     */
    public UserSignUpSystem(UserLoginManager userLoginManager, AppTrafficManager appTrafficManager,
                            CustomRequestCache<Role> customRequestCache, String filePath) {
        this.userLoginManager = userLoginManager;
        this.appTrafficManager = appTrafficManager;
        this.userView = new UserView();
        this.customRequestCache = customRequestCache;
        this.filePath = filePath;
    }

    /**
     * Prompts the user to sign up to the program if they are a new {@link User}. The {@link User} will enter a
     * valid email, password, their first name, and their last name which will sign them up as an Attendee account.
     */
    @Override
    public void start() {
        userView.displayCreateAnAccount();

        PropertiesIterator prompts = new PropertiesIterator(filePath + "user_properties.txt");
        ArrayList<String> temp = new ArrayList<>();

        try {
            while (prompts.hasNext()) {
                String s;
                userView.displayString(s = prompts.next());
                boolean hasUser;
                String input;
                boolean validEmail;
                if (s.contains("Email")) {
                    do {
                        input = br.readLine();
                        hasUser = userLoginManager.getUserRepository().hasUserByEmail(input);
                        if (hasUser) {
                            userView.displayAUserWithThisEmailAlreadyExists();
                            userView.displayString(s);
                        }
                        /*
                         Used the following link:
                         stackoverflow.com/questions/201323/how-to-validate-an-email-address-using-a-regular-expression
                         for email regex.
                         */
                        validEmail = input.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
                        if (!validEmail) {
                            userView.displayNeedToInputEmail();
                            userView.displayString(s);
                        }
                    } while (hasUser || !validEmail);
                } else {
                    input = br.readLine();
                }

                if (!input.equals("")) {
                    temp.add(input);
                }
            }
        } catch (IOException e) {
            userView.displaySomethingWentWrong();
        }
        try {
            userLoginManager.signUp(temp.get(0), temp.get(1), temp.get(2), temp.get(3));
            String id = "";
            try {
                id = userLoginManager.getUserRepository().getUserIdByEmail(temp.get(0));
            } catch (UserNotFoundException e) {
                userView.displaySomethingWentWrong();
            }

            while (!customRequestCache.isEmpty()) {
                userLoginManager.getUserRepository().addRoleToUserById(id, customRequestCache.pullRequest());
            }

            appTrafficManager.updateNumSignUps();
        }  catch (IndexOutOfBoundsException e) {
            userView.displayEmptyUserCreation();
        }
    }

}
