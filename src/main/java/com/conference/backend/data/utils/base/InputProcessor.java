package com.conference.backend.data.utils.base;

import com.conference.frontend.EntityView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

/**
 *  General controller for taking commonly-needed input from users.
 *
 */
public class InputProcessor {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private final EntityView entityView = new EntityView();


    /**
     *  Prompt user to input "y" or "n", repeat until valid input given
     *
     *  @return "y" or "n", depending on user input
     */
    protected String requestBoolean() {
        String input = "";
        boolean validInput = false;
        do {
            try {
                input = br.readLine().trim();
            } catch (IOException e) {
                entityView.displaySomethingWentWrong();
            }
            if (!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n")) {
                entityView.displayNeedBoolean();
            } else {
                validInput = true;
            }
        } while (!validInput);

        return input;
    }

    /**
     *  Prompt user to input a positive integer, repeat until valid input given
     *
     *  @return the positive integer given
     */
    protected String requestPositiveInteger() {
        boolean isPositive = false;
        String input = "";
        do {
            try {
                input = br.readLine().trim();
            } catch (IOException ioException) {
                entityView.displaySomethingWentWrong();
            }

            try {
                try {
                    isPositive = Integer.parseInt(input) > 0;
                } catch (NumberFormatException e) {
                    entityView.displayEnterInteger();
                }

                if (!isPositive) {
                    entityView.displayEnterPositiveInteger();
                }
            } catch (NumberFormatException e) {
                entityView.displayEnterInteger();
            }
        } while (!isPositive);

        return input;
    }

    /**
     * Prompt user to choose one of the valid options, repeat until valid input given
     *
     * @param validOptions a list of valid options
     * @param repeat the message shown if invalid input is given
     *
     * @return the valid option chosen
     */
    protected String requestInput(List<String> validOptions, String repeat) {
        boolean badType = false;
        String input = "";

        do {
            entityView.displayChooseBetween();
            try {
                input = br.readLine().trim();
            } catch (IOException ioException) {
                entityView.displaySomethingWentWrong();
            }

            badType = !validOptions.contains(input);
            if (badType) {
                entityView.displayNotAnOption();
                entityView.displayString(repeat);
            }
        } while (badType);

        return input;
    }

    /**
     * Prompt user to choose one of the valid options, repeat until valid input given
     * @param validOptions the list of valid options
     * @return the valid option chosen
     */
    protected String requestInputWithoutRepeat(List<String> validOptions) {
        String choice = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            do {
                choice = br.readLine();
            } while (!validOptions.contains(choice));
        } catch (IOException e) {
            entityView.displaySomethingWentWrong();
        }

        return choice;
    }

    /**
     * Prompt user to enter a date before the {@code other} date
     *
     * @param repeat the message shown if invalid input is given
     * @param other the date that must be before the given date
     *
     * @return the valid date given
     */
    protected Date requestDate(String repeat, Date other) {
        Date date = new Date();
        String input = "";
        boolean dateValid = false;
        do {
            try {
                input = br.readLine().trim() + ":00";
            } catch (IOException ioException) {
                entityView.displaySomethingWentWrong();
            }

            try {
                date = new Date(Date.parse(input));

                if (date.after(other)) {
                    dateValid = true;
                } else {
                    entityView.displayDateBefore();
                    entityView.displayString(repeat);
                    dateValid = false;
                }

            } catch (IllegalArgumentException e) {
                entityView.displayInvalidDate();
                entityView.displayString(repeat);
                dateValid = false;
            }
        } while (!dateValid);
        
        return date;
    }
}
