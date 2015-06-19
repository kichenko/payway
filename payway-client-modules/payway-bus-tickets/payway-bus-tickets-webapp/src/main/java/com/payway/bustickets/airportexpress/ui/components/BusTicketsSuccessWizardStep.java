/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.components;

import com.payway.bustickets.core.utils.GraphicsConverterUtils;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * BusTicketsParamsWizardStep
 *
 * @author Sergey Kichenko
 * @created 08.06.15 00:00
 */
@Slf4j
@Setter(AccessLevel.PRIVATE)
@Getter(AccessLevel.PRIVATE)
public class BusTicketsSuccessWizardStep extends AbstractWizardStep {

    private static final long serialVersionUID = 9042170733083703085L;

    @UiField
    private Image image;

    @UiField
    private Button btnPreview;

    @UiField
    private Button btnDownload;

    private Resource resource;
    private FileDownloader fileDownloader;

    public BusTicketsSuccessWizardStep() {
        init();
    }

    private void init() {
        setSizeFull();
        addComponent(Clara.create("BusTicketsSuccessWizardStep.xml", this));
    }

    public void setContentResource(Resource resource) {

        setResource(resource);

        if (getFileDownloader() != null) {
            getFileDownloader().remove();
        }

        setFileDownloader(new FileDownloader(getResource()));
        getFileDownloader().extend(getBtnDownload());
    }

    @UiHandler("btnPreview")
    public void clickPreview(Button.ClickEvent event) {

        BusTicketsPreviewWindow wnd = new BusTicketsPreviewWindow();
        wnd.setPreviewContent(resource);
        wnd.show();
    }

    public void setImagePdf(final byte[] content, String fileName) {

        image.setSource(new StreamResource(new StreamResource.StreamSource() {
            private static final long serialVersionUID = -2480723276190894707L;

            @Override
            public InputStream getStream() {
                try {
                    return new ByteArrayInputStream(GraphicsConverterUtils.convertPdfToImage(content, "png", 0, (int) image.getWidth(), (int) image.getHeight(), BufferedImage.TYPE_INT_RGB, 72));
                } catch (Exception ex) {
                    log.error("Bad converting pdf ticket previe to image - {}", ex);
                    return null;
                }
            }
        }, fileName));
    }
}
