package com.conference.backend.conference_and_rooms.controllers;

import com.conference.backend.conference_and_rooms.entities.Room;
import com.conference.backend.conference_and_rooms.managers.RoomManager;
import com.conference.backend.conference_and_rooms.controllers.subcontrollers.RoomCreationSystem;
import com.conference.backend.conference_and_rooms.controllers.subcontrollers.RoomQRCodeSystem;
import com.conference.backend.data.utils.QRCodeGeneratorGateway;
import com.conference.backend.data.utils.Role;
import com.conference.backend.data.utils.Value;
import com.conference.backend.data.utils.base.Launcher;
import com.conference.backend.data.utils.base.Startable;
import com.conference.backend.users.AppTrafficManager;
import com.conference.backend.users.UserLoginManager;
import com.conference.backend.users.UserManager;
import com.conference.frontend.ConferenceUtils;
import com.conference.frontend.conference.RoomView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomLauncher extends Launcher implements Startable {

    private final String HOME = "0";
    private final String ADD_ROOM = "1";
    private final String CREATE_ROOM_QR_CODE = "2";

    /**
     *  Hashmap used to map an integer to a user friendly string along with the roles required.
     */
    private final Map<String, Value> MENU_CHOICE_TO_NAME_AND_ROLES = new HashMap<String, Value>() {{
        put(HOME, new Value("Go home", Role.values()));
        put(ADD_ROOM, new Value("Create a new room", Role.ORGANIZER));
        put(CREATE_ROOM_QR_CODE, new Value("Save Schedule as QR Code", Role.ORGANIZER));
    }};

    private final UserManager userManager;
    private final UserLoginManager userLoginManager;
    private final AppTrafficManager appTrafficManager;
    private final RoomManager roomManager;
    private final QRCodeGeneratorGateway qrCodeGeneratorGateway;
    private final RoomCreationSystem roomCreationSystem;
    private final RoomQRCodeSystem roomQRCodeSystem;

    private final ConferenceUtils conferenceUtils;
    private final RoomView roomView;


    /**
     * Controller for viewing a menu of features related to {@link Room}s and choosing a feature.
     * @param userLoginManager the User repo
     * @param appTrafficManager the repo storing app stats
     * @param roomManager the repo storing the rooms
     * @param filePath the file path to save qr codes to
     */
    public RoomLauncher(UserLoginManager userLoginManager,
                        AppTrafficManager appTrafficManager,
                        RoomManager roomManager,
                        String filePath) {

        this.userLoginManager = userLoginManager;
        this.userManager = userLoginManager.getUserRepository();
        this.appTrafficManager = appTrafficManager;
        this.roomManager = roomManager;
        this.conferenceUtils = new ConferenceUtils();
        this.qrCodeGeneratorGateway = new QRCodeGeneratorGateway();

        roomCreationSystem = new RoomCreationSystem(roomManager, userManager, userLoginManager, filePath);
        roomQRCodeSystem = new RoomQRCodeSystem(roomManager, qrCodeGeneratorGateway, filePath);

        roomView = new RoomView();
    }

    /**
     *  Prompt users to choose between adding a room or creating a QR code for the room's description, or go home.
     *
     */
    @Override
    public void start() {
        List<String> validOptions = conferenceUtils
                .getValidOptionsForUser(userManager.getRolesByUserId(userLoginManager.getCurrentUserId()),
                        MENU_CHOICE_TO_NAME_AND_ROLES);

        roomView.displayTitleAllRooms();
        List<String> allRoomNames = roomManager.getRoomsNameList();
        roomView.displayRooms(allRoomNames,roomManager);
        roomView.displayOptions(validOptions, MENU_CHOICE_TO_NAME_AND_ROLES);

        try {
            String choice = this.requestInput(validOptions, MENU_CHOICE_TO_NAME_AND_ROLES, roomView);
            process(choice);
        } catch (IOException e) {
            roomView.displaySomethingWentWrong();
        }
    }

    /**
     * Check the user's selected option and call the appropriate system controller.
     * @param choice the menu choice
     */
    private void process(String choice) {
        switch (choice) {
            case HOME:
                break;
            case ADD_ROOM:
                roomCreationSystem.start();
                break;
            case CREATE_ROOM_QR_CODE:
                roomQRCodeSystem.start();
                break;
        }
    }

}
