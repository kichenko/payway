/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets;

import java.awt.image.BufferedImage;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.testng.annotations.Test;

/**
 * PdfBoxTest
 *
 * @author Sergey Kichenko
 * @created 16.06.15 00:00
 */
public class PdfBoxTest {

    @Test(enabled = false)
    public void testMe() throws Exception {
        try {
            //FileInputStream file             
            PDDocument doc = new PDDocument();
            //new PDStream(doc, new ByteArrayInputStream(response.getContent()));
            List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
            BufferedImage image = pages.get(0).convertToImage();
        } catch (Exception ex) {
            //
        }
    }
}
