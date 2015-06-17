/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.components;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
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
}
