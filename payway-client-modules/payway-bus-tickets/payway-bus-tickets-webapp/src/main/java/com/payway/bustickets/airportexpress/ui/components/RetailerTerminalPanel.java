/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.components;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * RetailerTerminalPanel
 *
 * @author Sergey Kichenko
 * @created 11.06.15 00:00
 */
public class RetailerTerminalPanel extends Panel {

    private static final long serialVersionUID = 7489679439383530412L;

    @UiField
    private HorizontalLayout layoutPopupRetailerTerminal;

    @UiField
    private Label lbTerminalName;

    @UiField
    private Label lbTerminalLocation;

    @UiField
    private Label lbRetailerName;

    private final PopupView popupRetailerTerminal = new PopupView(new RetailerTerminalPopupContent("Select"));

    public RetailerTerminalPanel() {
        init();
    }

    private void init() {
        //setCaption("Retailer terminal");
        setContent(Clara.create("RetailerTerminalView.xml", this));
        layoutPopupRetailerTerminal.addComponent(popupRetailerTerminal);
        popupRetailerTerminal.setHideOnMouseOut(false);
    }
}
