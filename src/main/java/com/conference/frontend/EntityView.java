package com.conference.frontend;

import com.conference.backend.data.utils.Value;
import com.conference.backend.users.User;

import java.util.List;
import java.util.Map;

/**
 * A view for entities.
 *
 */
public class EntityView {
    // String constants to make output coloured
    protected String ANSI_RESET = "\u001B[0m";
    protected String ANSI_RED = "\033[0;31m";
    protected String ANSI_BLUE = "\u001B[34m";
    protected String ANSI_CYAN = "\u001B[36m";
    protected String ANSI_PURPLE = "\u001B[35m";
    protected String ANSI_YELLOW = "\u001B[33m";
    protected String CYAN_BOLD = "\033[1;36m";
    protected String ANSI_TITLE = "\033[1;96m" + "\033[4;36m";

    public void displayString(Object o) {
        System.out.println(o);
    }

    /**
     * Displays a scary error to the string
     * @param s The string to print
     */
    public void displayError(String s) {
        System.out.println(ANSI_RED + s + ANSI_RESET);
    }

    /**
     * Displays a pretty success to the string
     * @param s The string to print
     */
    public void displaySuccess(String s) {
        System.out.println(ANSI_BLUE + s + ANSI_RESET);
    }

    /**
     * Displays a pretty success to the string
     * @param s The string to print
     */
    public void displayTitle(String s) {
        System.out.println(ANSI_TITLE + s + ANSI_RESET);
    }



    /**
     * Prints Exiting... animation to the screen.
     */
    public void printExit() {
        try {
            System.out.print(ANSI_CYAN + "Exiting");
            Thread.sleep(300);
            System.out.print(".");
            Thread.sleep(300);
            System.out.print(".");
            Thread.sleep(300);
            System.out.println("." + ANSI_RESET + "\n");
        } catch (InterruptedException e) {
            displayError("Failed to exit!");
        }
    }

    /**
     * Displays redirecting animation on the screen
     */
    public void displayRedirecting() {
        try {
            System.out.print(ANSI_YELLOW + "Redirecting");
            Thread.sleep(300);
            System.out.print(".");
            Thread.sleep(300);
            System.out.print(".");
            Thread.sleep(300);
            System.out.println("." + ANSI_RESET + "\n");
        } catch (InterruptedException e) {
            displayError("Failed to exit!");
        }
    }

    /**
     * Displays that user with this email does not exist
     */
    public void displayAUserWithThisEmailDoesNotExist() {
        this.displayError(">>> A user with this email does not exist.");
    }

    /**
     * Displays that user with this email already exists
     */
    public void displayAUserWithThisEmailAlreadyExists() {
        this.displayError(">>> A user with this email already exists.");
    }

    /**
     * Displays that this is not an option
     */
    public void displayNotAnOption() {
        this.displayError(">>> This is not an option.");
    }

    /**
     * Displays that email is not correctly imputed
     */
    public void displayNeedToInputEmail() {
        this.displayError(">>> You need to input an email!");
    }

    /**
     * Displays enter your email
     */
    public void displayPromptUserForEmail() {
        this.displayString("Please enter your Email:");
    }

    /**
     * Displays enter your password
     */
    public void displayPromptUserForPassword() {
        this.displayString("Please enter your Password:");
    }

    /**
     * Displays that password is correct
     */
    public void displayThisPasswordDoesNotExist() {
        this.displayError(">>> Password is incorrect.");
    }

    /**
     * Displays that the event does not exist
     */
    public void displayEventDoesNotExist() {
        this.displayError(">>> The event does not exist.");
    }

    /**
     * Displays that something is wrong
     */
    public void displaySomethingWentWrong() {
        this.displayError(">>> Something went wrong");
    }

    /**
     * Displays that please enter an integer
     */
    public void displayEnterInteger() {
        this.displayError(">>> Please enter an integer!");
    }

    /**
     * Displays that please enter a positive integer
     */
    public void displayEnterPositiveInteger() {
        this.displayError(">>> Please enter a positive integer.");
    }

    /**
     * Displays title of dashboard
     * @param s The title of the dashboard
     */
    public void displayDashboardTitle(String s) {
        this.displayString("\033[4;37m" + s + ANSI_RESET);
    }

//    public void displayAmenityError() {
//        this.displayError(">>> Please enter y or n.");
//    }

    /**
     * Displays pretty options to the {@link User}
     *
     * @param validOptions The options the {@link User} can see
     * @param map The map to show which menu options this {@link User} can see.
     *
     */
    public void displayOptions(List<String> validOptions, Map<String, Value> map) {
        System.out.println("Please choose between:");
        validOptions.stream()
                .forEach(s -> System.out.println(s + ": " + map.get(s).getString()));
    }

    /**
     * Displays choose between them
     */
    public void displayChooseBetween() {
        this.displayString("Please choose between:");
    }

    /**
     * Displays that this is not a menu option
     */
    public void displayNotAMenuOption() {
        this.displayError(">>> This is not a menu option!");
    }

    /**
     * Displays what we think they meant
     * @param s the guessed word
     */
    public void displayWeThinkYouMeant(String s) {
        this.displayString(ANSI_YELLOW + "We think you meant: " + s + ". If so, please try again." + ANSI_RESET);
    }

    /**
     * Displays enter a boolean
     */
    public void displayNeedBoolean() {
        this.displayError(">>> Please enter a boolean (y/n)!");
    }

    /**
     * Displays that the date is invalid
     */
    public void displayInvalidDate() {
        this.displayError(">>> This date is invalid.");
    }

    /**
     * Displays that the end time must be after start time
     */
    public void displayEndAfterStart(){
        this.displayError(">>> The End time must be after Start time.");
    }

    /**
     * Displays that this date is before the current time
     */
    public void displayDateBefore() {
        this.displayError(">>> This date is before the current time!");
    }

    /**
     * Displays the event has already passed
     */
    public void displayEventPassed() { this.displayError(">>> This event has already passed."); }
}
