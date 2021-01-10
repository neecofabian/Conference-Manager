package com.conference.backend.conference_and_rooms.controllers.subcontrollers;

import com.conference.backend.conference_and_rooms.managers.RoomManager;
import com.conference.backend.conference_and_rooms.entities.Amenity;
import com.conference.backend.conference_and_rooms.entities.Room;
import com.conference.backend.data.utils.Role;
import com.conference.backend.data.utils.base.InputProcessor;
import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.security.PropertiesIterator;
import com.conference.backend.users.UserLoginManager;
import com.conference.backend.users.UserManager;
import com.conference.frontend.conference.RoomView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Controller for creating a {@link Room}.
 *
 */
public class RoomCreationSystem extends InputProcessor implements Startable {

    private final RoomManager roomManager;
    private final RoomView roomView;
    private final UserManager userManager;
    private final UserLoginManager userLoginManager;
    private final String filePath;

    private final Map<String, Amenity> AMENITY_NAME_TO_TYPE = new HashMap<String, Amenity>() {{
        put(Amenity.AV_EQUIPMENT.toString(), Amenity.AV_EQUIPMENT);
        put(Amenity.PODIUM.toString(), Amenity.PODIUM);
        put(Amenity.TABLES.toString(), Amenity.TABLES);
        put(Amenity.CHAIRS.toString(), Amenity.CHAIRS);
        put(Amenity.WIFI.toString(), Amenity.WIFI);
        put(Amenity.RESTROOMS.toString(), Amenity.RESTROOMS);
    }};


    public RoomCreationSystem(RoomManager roomManager, UserManager userManager, UserLoginManager userLoginManager,
                              String filePath) {
        this.roomManager = roomManager;
        this.userManager = userManager;
        this.userLoginManager = userLoginManager;
        this.roomView = new RoomView();
        this.filePath = filePath;
    }


    /**
     * Allows Organizers to create a {@link Room} if a room with the name doesn't already exist and the maximum capacity
     * of the room is a valid positive integer. The Organizer is prompted for the Amenities that the Room has and the
     * Amenities are added to the Room's Amenity list
     *
     */
    @Override
    public void start(){

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<Role> roles = userManager.getRolesByUserId(userLoginManager.getCurrentUserId());

        String candidateRoomName = "";
        boolean roomExists = false;
        Integer capacity = 1;

        add:while(true){
            do {
                roomView.displayPromptRoomName();

                try {
                    candidateRoomName = br.readLine().trim();
                } catch (IOException e) {
                    roomView.displaySomethingWentWrong();
                }

                if (candidateRoomName.equalsIgnoreCase(".home")) {
                    roomView.printExit();
                    break add;
                }

                roomExists = roomManager.hasRoom(candidateRoomName);

                if (roomExists) {
                    roomView.displayRoomNameExists();
                }
            } while(roomExists);

            roomView.displayPromptMaxCapacity();

            capacity = Integer.parseInt(requestPositiveInteger());
            Room room = roomManager.createRoom(candidateRoomName, capacity);
            roomManager.addRoom(roles, room);

            this.addAmenities(roomManager, candidateRoomName);
            roomView.displayRoomAdded(candidateRoomName, capacity);
            break add;
        }
    }


    /**
     * Helper method for start() that asks Organizers if a Room has each of the Amenities and adds them to the given
     * Room's Amenity list.
     *
     * @param roomRepository the RoomManager that the Room is in
     * @param roomName the name of the Room to add Amenities to
     */
    private void addAmenities(RoomManager roomRepository, String roomName) {
        PropertiesIterator prompts = new PropertiesIterator(filePath + "amenity_properties.txt");
        roomView.displayAmenityTitle();

        while (prompts.hasNext()) {
            String s;
            roomView.displayString(s = prompts.next());
            String input = requestBoolean();

            if (input.equalsIgnoreCase("y")) {
                roomRepository.addAmenityByRoomName(roomName, AMENITY_NAME_TO_TYPE.get(s.substring(0, s.length() -2)));
            }
        }
    }
}
