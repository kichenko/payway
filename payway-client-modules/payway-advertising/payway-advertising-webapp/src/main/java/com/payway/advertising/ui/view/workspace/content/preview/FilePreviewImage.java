/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content.preview;

import com.vaadin.server.StreamResource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.peter.imagescaler.ImageScaler;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * FilePreviewImage
 *
 * @author Sergey Kichenko
 * @created 23.06.15 00:00
 */
@Slf4j
@NoArgsConstructor
public class FilePreviewImage extends AbstractFilePreview {

    private static final long serialVersionUID = 4340697649028702403L;

    @UiField
    private ImageScaler imageScaler;

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("FilePreviewImage.xml", this));
    }

    @Override
    protected void loadContent(InputStream stream, String fileName) {

        try (InputStream is = stream) {
            BufferedImage image = ImageIO.read(is);
            imageScaler.setImage(createContentResource(image, fileName), image.getWidth(), image.getHeight());
        } catch (Exception ex) {
            log.error("Cannot load image content for preview - ", ex);
        }
    }

    @Override
    public AbstractFilePreview build(InputStream stream, String fileName) {
        init();
        loadContent(stream, fileName);
        return this;
    }

    private StreamResource createContentResource(final BufferedImage image, String fileName) {

        StreamResource streamResource = new StreamResource(new StreamResource.StreamSource() {
            private static final long serialVersionUID = -2480723276190894707L;

            @Override
            public InputStream getStream() {

                try {
                    ByteArrayOutputStream imagebuffer = new ByteArrayOutputStream();
                    ImageIO.write(image, "png", imagebuffer);
                    return new ByteArrayInputStream(imagebuffer.toByteArray());
                } catch (Exception ex) {
                    log.error("Bad image resource - ", ex);
                }

                return null;
            }
        }, fileName);

        streamResource.setCacheTime(0);

        return streamResource;
    }
}
