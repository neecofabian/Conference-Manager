package com.conference.backend.data.utils;

import com.conference.backend.data.utils.base.BarcodeGenerator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;

public class QRCodeGenerator implements BarcodeGenerator {

    /**
     * Generates a barcode image with the given text.
     * @param barcodeText The text to convert to an image
     * @return the image
     * @throws Exception if the barcode could not be generated
     */
    @Override
    public BufferedImage generateBarcodeImage(String barcodeText) throws Exception {
        QRCodeWriter bcw = new QRCodeWriter();
        BitMatrix bitMatrix = bcw.encode(barcodeText, BarcodeFormat.QR_CODE, 300, 150);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
