package com.conference.frontend.conference;

import com.conference.backend.conference_and_rooms.entities.Room;
import com.conference.backend.conference_and_rooms.managers.RoomManager;
import com.conference.frontend.EntityView;

import java.util.List;

/**
 * A view for {@link Room}s.
 *
 */
public class RoomView extends EntityView {

    /**
     * Prints and formats the rooms in this Conference.
     * @param allRoomNames the list of room names to print
     * @param roomRepository the {@link RoomManager} to fetch the rooms and details from
     */
    public void displayRooms(List<String> allRoomNames, RoomManager roomRepository) {
        for (String roomName : allRoomNames) {
            System.out.println(roomName
                    + " " + String.format("(max capacity: %d)", roomRepository.getRoomCapacityByName(roomName)));

            List<String> amenities = roomRepository.getAmenityStringListByRoomName(roomName);
            System.out.print("\tAmenities:");

            if (amenities.size() == 0) {
                System.out.println();
            } else {
                for (String amenity : amenities) {
                    if (amenities.indexOf(amenity) == amenities.size() - 1) {
                        System.out.println(" " + amenity);
                    } else {
                        System.out.print(" " + amenity + ",");
                    }
                }
            }

            System.out.println("\tEvents: ");

            List<String> allEventNames = roomRepository.getConferenceEventsListByRoomName(roomName);

            allEventNames.stream().forEach(e -> System.out.println("\t- " + e ));

            System.out.println("");

        }
    }

    /**
     * Prints the title of all rooms in the conference event
     */
    public void displayAllRoomsTitle() {
        this.displayString("\n" + "All Rooms in the Conference Event:\n");
    }

    /**
     * Prints the title of Rooms Menu
     */
    public void displayTitleAllRooms() {
        this.displayTitle("Rooms Menu:");
    }

    /**
     * Prints the success of a QR Code being saved successfully
     */
    public void displayQRCodeSuccess() {
        this.displaySuccess("QR Code saved successfully!" + '\n');
     }

    /**
     * Prints the error of a QR Code not being saved successfully
     */
    public void displayQRCodeError() {
        this.displayError("QR Code did not save properly!" + '\n');
    }

    /**
     * Prints the prompt asking for the name of the room desired to be added and that .hom returns to the main menu
     */
    public void displayPromptRoomName() {
        this.displayString("Please enter the name of the room you wish to add:" +
                " (type \".home\" to return to the main menu):");
    }

    /**
     * Prints the prompt asking for the name of the room desired to be added and that .hom returns to the main menu
     */
    public void displayRoomNameExists() {
        this.displayError(">>> A room with this name already exists.");
    }

    /**
     * Prints the prompt asking for the maximum capacity of the room desired to be added
     */
    public void displayPromptMaxCapacity() {
        this.displayString("Please enter the maximum capacity of the room:");
    }

    /**
     * Prints the success message that the room with the desired name and maximum capacity was added
     * @param candidateRoomName the name of the {@link Room} that was successfully added
     * @param capacity the maximum capacity of the {@link Room} that was successfully added
     */
    public void displayRoomAdded(String candidateRoomName, int capacity) {
        this.displaySuccess("You added the room " + candidateRoomName + " with maximum capacity "
                + capacity +"!" + "\n");
    }

    /**
     * Prints the title telling the user to choose which amenities the room should have, answering using y or n
     */
    public void displayAmenityTitle() {
        this.displayString("Choose which amenities the room has (y/n):");
    }

    /**
     * Prints the message that no rooms fit the desired amenity criteria and that instead all the rooms will be displayed
     */
    public void displayNoRooms() { displayString("No such rooms fit the criteria. Here are all rooms: \n"); }

    /**
     * Prints the message that all the rooms that match the amenity criteria are to be displayed
     */
    public void displayRoomsMatchingCriteria() { displayString("These are all the rooms matching the criteria:");}

    /**
     * Prints the prompt asking the user to select one of the above rooms
     */
    public void displayPromptSelectRoom() { displayString("Please select one of the above rooms:");}

    /**
     * Prints the error that the room entered by the user does not exist
     */
    public void displayRoomDoesNotExist() {
        this.displayError(">>> This room does not exist.");
    }

    /**
     * Prints the error that the room entered by the user does not have the capacity for the event desired
     */
    public void displayRoomNotEnoughCapacity() {
        this.displayError(">>> This room does not have enough capacity for this event");
    }
}