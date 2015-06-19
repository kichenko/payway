/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.core.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

/**
 * GraphicsConverterUtils
 *
 * @author Sergey Kichenko
 * @created 19.06.15 00:00
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GraphicsConverterUtils {

    public static BufferedImage convertPdfToImage(PDDocument doc, int page, int width, int height, int imageType, int dpi) throws Exception {
        return Thumbnails.of(((PDPage) doc.getDocumentCatalog().getAllPages().get(page)).convertToImage(imageType, dpi)).size(width, height).asBufferedImage();
    }

    public static BufferedImage convertPdfToImage(byte[] content, int page, int width, int height, int imageType, int dpi) throws Exception {
        return convertPdfToImage(PDDocument.load(new ByteArrayInputStream(content)), page, width, height, imageType, dpi);
    }

    public static byte[] convertPdfToImage(byte[] content, String imageFormat, int page, int width, int height, int imageType, int dpi) throws Exception {

        BufferedImage image = convertPdfToImage(PDDocument.load(new ByteArrayInputStream(content)), page, width, height, imageType, dpi);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, imageFormat, baos);

        return baos.toByteArray();
    }

}
