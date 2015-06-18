/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.components;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * BusTicketsPreviewWindow
 *
 * @author Sergey Kichenko
 * @created 17.06.15 00:00
 */
@Slf4j
@Getter
public class BusTicketsPreviewWindow extends Window {

    private static final long serialVersionUID = -4749645029512546854L;

    @UiField
    private BrowserFrame browserFrame;

    public BusTicketsPreviewWindow() {
        init();
    }

    private void init() {

        setCaption("Preview ticket");

        setModal(true);
        setClosable(true);
        //setResizable(false);
        //setDraggable(false);
        setWindowMode(WindowMode.MAXIMIZED);
        setContent(Clara.create("BusTicketsPreview.xml", this));
    }

    public void show() {
        if (!isAttached()) {
            UI.getCurrent().addWindow(this);
        }
    }

    public void setPreviewContent(Resource resource) {
        browserFrame.setSource(resource);
    }
}
