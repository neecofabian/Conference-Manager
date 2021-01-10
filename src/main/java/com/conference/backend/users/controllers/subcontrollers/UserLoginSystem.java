package com.conference.backend.users.controllers.subcontrollers;

import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.exception.UserNotFoundException;
import com.conference.backend.users.AppTrafficManager;
import com.conference.backend.users.User;
import com.conference.backend.users.UserLoginManager;
import com.conference.frontend.users.UserView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Controller class for logging in {@link User}s.
 */
public class UserLoginSystem implements Startable {
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private final UserLoginManager userLoginManager;
    private final UserView userView;
    private final AppTrafficManager appTrafficManager;

    /**
     * Initializes UserLoginSystem.
     *
     * @param userLoginManager The {@code UserLoginManager} storing the current user and repository of users
     * @param appTrafficManager The {@code AppTrafficManager} to update statistics
     */
    public UserLoginSystem(UserLoginManager userLoginManager, AppTrafficManager appTrafficManager) {
        this.userLoginManager = userLoginManager;
        this.appTrafficManager = appTrafficManager;
        this.userView = new UserView();
    }

    /**
     * Prompts the user to login to the program by inputting their email and password.
     * Checks to see that the email and password are valid and correct.
     *
     */
    @Override
    public void start() {
        userView.displayLogin();

        String email = "";
        boolean emailDoesNotExist;
        do {
            userView.displayPromptUserForEmail();
            try {
                email = br.readLine();
            } catch (IOException e) {
                userView.displaySomethingWentWrong();
            }
            emailDoesNotExist = !userLoginManager.getUserRepository().hasUserByEmail(email);
            if (emailDoesNotExist) {
                userView.displayAUserWithThisEmailDoesNotExist();
            }
        } while (emailDoesNotExist);

        String plainTextPassword;
        boolean loggedIn = false;
        do {
            userView.displayPromptUserForPassword();
            try {
                plainTextPassword = br.readLine();
                loggedIn = userLoginManager.logInUser(email, plainTextPassword);
            } catch (UserNotFoundException | IOException e) {
                userView.displaySomethingWentWrong();
            }

            if (!loggedIn) {
                userView.displayThisPasswordDoesNotExist();
            }
        } while (!loggedIn);

        try {
            userLoginManager.setUser(userLoginManager.getUserRepository().getUserByEmailOrThrow(email));
        } catch (UserNotFoundException userNotFoundException) {
            userView.displayAUserWithThisEmailDoesNotExist();
        }

        appTrafficManager.updateLoginsAllTime();
    }
}
