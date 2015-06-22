/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.components;

import com.payway.messaging.model.common.RetailerTerminalDto;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * RetailerTerminalPanel
 *
 * @author Sergey Kichenko
 * @created 11.06.15 00:00
 */
@Slf4j
public class RetailerTerminalPanel extends Panel {

    private static final long serialVersionUID = 7489679439383530412L;

    @UiField
    private Label lbTerminalName;

    @UiField
    private Label lbTerminalLocation;

    @UiField
    private Label lbRetailerName;

    @Getter
    private RetailerTerminalDto currentTerminal;

    public RetailerTerminalPanel() {
        init();
    }

    private void init() {
        setContent(Clara.create("RetailerTerminalView.xml", this));
    }

    private void refresh() {

        if (currentTerminal != null) {
            lbTerminalName.setValue(currentTerminal.getTerminalName());
            lbTerminalLocation.setValue(currentTerminal.getTerminalLocation());
            lbRetailerName.setValue(currentTerminal.getRetailerName());
        } else {
            lbTerminalName.setValue("");
            lbTerminalLocation.setValue("");
            lbRetailerName.setValue("");
        }
    }

    public void setUpCurrentTerminal(RetailerTerminalDto terminal) {

        if (terminal == null) {
            if (log.isDebugEnabled()) {
                log.debug("Empty terminal on set up");
            }
        }

        setCurrentTerminal(terminal);
    }

    private void setCurrentTerminal(RetailerTerminalDto dto) {
        currentTerminal = dto;
        refresh();
    }

    public long getRetailerTerminalId() {
        return getCurrentTerminal() == null ? 0 : getCurrentTerminal().getId();
    }
}
