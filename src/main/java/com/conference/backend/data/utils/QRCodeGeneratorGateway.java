package com.conference.backend.data.utils;

import com.conference.backend.conference_and_rooms.managers.RoomManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class QRCodeGeneratorGateway {
    private QRCodeGenerator qrCodeGenerator;

    /**
     * Constructs an instance of this class.
     */
    public QRCodeGeneratorGateway() {
        qrCodeGenerator = new QRCodeGenerator();
    }

    /**
     * Generates a QR code for every room.
     * @param roomRepository The repo storing the rooms.
     * @param filePath The file path to save to
     * @throws Exception if the QR code could not be generated.
     */
    public void generateQRCode(RoomManager roomRepository, String filePath) throws Exception {
        List<String> roomToStrings = roomRepository.getRoomToStrings();

        for (int i = 0; i < roomRepository.size(); i++) {
            try {
                BufferedImage bi = qrCodeGenerator.generateBarcodeImage(roomToStrings.get(i));
                File outputFile = new File(filePath + i + ".png");
                ImageIO.write(bi, "png", outputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
