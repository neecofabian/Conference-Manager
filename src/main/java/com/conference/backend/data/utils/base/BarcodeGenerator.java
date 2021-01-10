package com.conference.backend.data.utils.base;

import java.awt.image.BufferedImage;

/**
 * Should be used for any type of barcode image generator.
 */
public interface BarcodeGenerator {
    /**
     * Generates a barcode image for any type of image desired.
     * @param barcodeText The text to generate a barcode for
     * @return the image
     * @throws Exception if some exception occurred.
     */
    BufferedImage generateBarcodeImage(String barcodeText) throws Exception;
}
