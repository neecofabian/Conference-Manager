package com.conference.frontend.main;

import com.conference.frontend.EntityView;

public class MainView extends EntityView {
    /**
     * Displays a pretty title
     */
    public void displayConferenceTitle() {
        this.displayTitle("The Official Conference Planning Managing System " +
                "Program 2020 (GOTY)" + "\033[0m");
        displayDiscordServer();

        this.displayString("\033[4;37m" + "Example accounts:" + "\033[0m");
        this.displayString("Organizer: org@f.com, Password: p");
        this.displayString("Attendee: att@f.com, Password: p");
        this.displayString("Speaker: spe@f.com, Password: p");
        this.displayString("VIP: vip@f.com, Password: p" + "\n");
    }

    /**
     * Displays a thank you message when you quit.
     */
    public void displayThankYou() {
        this.displaySuccess("Thank you!");
    }

    /**
     * Displays the discord server link.
     */
    private void displayDiscordServer() {
        this.displayString("Join the discord server for our Conference: https://discord.gg/KmTQ2fKzW5" + '\n');
    }

    /**
     * Displays to the user whether they want to login/signup/go home
     */
    public void displayLoginOrSignUp() {
        this.displayString("0: Login\n1: Sign up\n2: Go home");
    }

    /**
     * Display that a file is missing
     * @param filePath the file that is missing
     */
    public void displayFileIsMissing(String filePath) {
        this.displayError(">>> " + filePath + " is missing!");
    }

    /**
     * The main menu.
     */
    public void displayMainMenu() {
        this.displayTitle("Welcome Home:");
    }
}
