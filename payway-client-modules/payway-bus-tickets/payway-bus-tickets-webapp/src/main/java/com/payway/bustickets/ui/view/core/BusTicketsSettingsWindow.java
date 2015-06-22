/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.view.core;

import com.payway.bustickets.ui.components.containers.RetailerTerminalDtoBeanContainer;
import com.payway.messaging.model.common.RetailerTerminalDto;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * AbstractUI
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
public class BusTicketsSettingsWindow extends Window {

    private static final long serialVersionUID = -8641642849659411019L;

    public interface SettingsSaveListener {

        @Setter
        @Getter
        @AllArgsConstructor
        public static class SettingsSaveEvent {

            private BusTicketsSettingsWindow source;
        }

        boolean onSave(SettingsSaveEvent event);
    }

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private RetailerTerminalDto currentRetailerTerminal;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private List<RetailerTerminalDto> terminals;

    @UiField
    private ComboBox cbRetailerTerminals;

    @UiField
    private Label lbTerminalName;

    @UiField
    private Label lbTerminalLocation;

    @UiField
    private Label lbRetailerName;

    @Setter(AccessLevel.PRIVATE)
    private SettingsSaveListener listener;

    public BusTicketsSettingsWindow(final RetailerTerminalDto currentRetailerTerminal, final List<RetailerTerminalDto> terminals, SettingsSaveListener listener) {

        setListener(listener);
        setTerminals(terminals);
        setCurrentRetailerTerminal(currentRetailerTerminal);
        init();
    }

    private void init() {

        setContent(Clara.create("BusTicketsSettingsWindow.xml", this));
        cbRetailerTerminals.setContainerDataSource(new RetailerTerminalDtoBeanContainer());
        ((RetailerTerminalDtoBeanContainer) cbRetailerTerminals.getContainerDataSource()).addAll(terminals != null ? terminals : new ArrayList<RetailerTerminalDto>(0));
        cbRetailerTerminals.select(currentRetailerTerminal.getTerminalName());

        setModal(true);
        setClosable(true);
        setResizable(false);
        setDraggable(false);
        setCaption("Settings");
    }

    @UiHandler(value = "cbRetailerTerminals")
    public void onRetailerTerminalsValueChange(Property.ValueChangeEvent event) {

        if (event.getProperty().getValue() != null) {
            RetailerTerminalDto terminal = ((RetailerTerminalDtoBeanContainer) cbRetailerTerminals.getContainerDataSource()).getItem(event.getProperty().getValue()).getBean();
            if (terminal != null) {
                lbTerminalName.setValue(terminal.getTerminalName());
                lbTerminalLocation.setValue(terminal.getTerminalLocation());
                lbRetailerName.setValue(terminal.getRetailerName());
                return;
            }
        }

        lbTerminalName.setValue("");
        lbTerminalLocation.setValue("");
        lbRetailerName.setValue("");
    }

    @UiHandler(value = "btnCancel")
    public void onClickCancel(Button.ClickEvent event) {
        close();
    }

    @UiHandler(value = "btnSave")
    public void onClickSave(Button.ClickEvent event) {
        if (cbRetailerTerminals.getValue() != null) {
            RetailerTerminalDto terminal = ((RetailerTerminalDtoBeanContainer) cbRetailerTerminals.getContainerDataSource()).getItem(cbRetailerTerminals.getValue()).getBean();
            if (terminal != null && listener != null) {
                this.setCurrentRetailerTerminal(terminal);
                if (listener.onSave(new SettingsSaveListener.SettingsSaveEvent(this))) {
                    close();
                    return;
                }
            }
        }
    }

    public void show() {
        if (!isAttached()) {
            UI.getCurrent().addWindow(this);
        }
    }
}
