package com.davidkeen.test;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.EnumMap;
import java.util.Map;

public class Zxing {

    private static final String DATA = "THE DATA TO BE ENCODED";

    // The size in pixels of the QR code image
    private static final int IMAGE_SIZE = 250;

    private static BufferedImage stringToQrCodeImage(String data) throws WriterException {
        Map<EncodeHintType, Object> hintMap = new EnumMap<>(EncodeHintType.class);
        hintMap.put(EncodeHintType.MARGIN, 1);
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, IMAGE_SIZE, IMAGE_SIZE, hintMap);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    private static String imageToHtml(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", out);
        byte[] data = out.toByteArray();
        String base64bytes = Base64.getEncoder().encodeToString(data);
        String dataUrl = "data:image/png;base64," + base64bytes;
        return String.format("<html><img src='%s'/></html>", dataUrl);
    }

    public static void main(String[] args) throws Exception {
        BufferedImage image = stringToQrCodeImage(DATA);

        System.out.println(imageToHtml(image));

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);
    }
}
