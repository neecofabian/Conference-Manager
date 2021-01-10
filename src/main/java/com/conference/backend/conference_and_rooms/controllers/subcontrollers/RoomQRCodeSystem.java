package com.conference.backend.conference_and_rooms.controllers.subcontrollers;

import com.conference.backend.conference_and_rooms.managers.RoomManager;
import com.conference.backend.data.utils.QRCodeGeneratorGateway;
import com.conference.backend.data.utils.base.Startable;
import com.conference.frontend.conference.RoomView;

/**
 *  Controller for creating QR codes for events happening in the Rooms
 *
 */
public class RoomQRCodeSystem implements Startable {

    private final RoomManager roomManager;
    private final QRCodeGeneratorGateway qrCodeGeneratorGateway;
    private final String filePath;
    private final RoomView roomView;

    public RoomQRCodeSystem(RoomManager roomManager, QRCodeGeneratorGateway qrCodeGeneratorGateway, String filePath) {
        this.roomManager = roomManager;
        this.qrCodeGeneratorGateway = qrCodeGeneratorGateway;
        this.filePath = filePath;
        this.roomView = new RoomView();
    }

    /**
     *  Generates QR code for events happening in the Rooms.
     *
     */
    @Override
    public void start() {
        try {
            qrCodeGeneratorGateway.generateQRCode(roomManager, filePath);
            roomView.displayQRCodeSuccess();
        } catch (Exception e) {
            roomView.displayQRCodeError();
        }
    }

}
