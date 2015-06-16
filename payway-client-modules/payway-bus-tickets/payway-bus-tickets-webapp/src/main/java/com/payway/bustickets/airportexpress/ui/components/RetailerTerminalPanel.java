/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.components;

import com.payway.messaging.model.common.RetailerTerminalDto;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import java.util.List;
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
public class RetailerTerminalPanel extends Panel implements RetailerTerminalPopupContent.PopupTerminalStateChangeListener {
    
    private static final long serialVersionUID = 7489679439383530412L;
    
    private final PopupView popupRetailerTerminal = new PopupView(new RetailerTerminalPopupContent("Select", this));
    
    @UiField
    private HorizontalLayout layoutPopupRetailerTerminal;
    
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
        //setCaption("Retailer terminal");
        setContent(Clara.create("RetailerTerminalView.xml", this));
        layoutPopupRetailerTerminal.addComponent(popupRetailerTerminal);
        popupRetailerTerminal.setHideOnMouseOut(false);
        
        popupRetailerTerminal.addPopupVisibilityListener(new PopupView.PopupVisibilityListener() {
            private static final long serialVersionUID = -8094656211290721252L;
            
            @Override
            public void popupVisibilityChange(PopupView.PopupVisibilityEvent event) {
                if (event.isPopupVisible()) {
                    RetailerTerminalPopupContent cmp = (RetailerTerminalPopupContent) popupRetailerTerminal.getContent();
                    if (cmp != null) {
                        cmp.setSelectedTerminal(currentTerminal);
                    }
                }
            }
        });
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
    
    public void setUpTerminals(List<RetailerTerminalDto> terminals) {
        
        if (terminals == null || terminals.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("Empty terminal list on set up");
            }    
            
            refresh(); 
            popupRetailerTerminal.setVisible(false);
            
            return;
        }
        
        if (popupRetailerTerminal.getContent() != null) {
            RetailerTerminalPopupContent cmp = (RetailerTerminalPopupContent) popupRetailerTerminal.getContent();
            if (cmp != null) {
                cmp.setUpTerminals(terminals);
            }
        }
        
        if (terminals.size() == 1) {
            popupRetailerTerminal.setVisible(false);
        } else {
            popupRetailerTerminal.setVisible(true);
        }
        
        setCurrentTerminal(terminals.get(0));
    }
    
    private void setCurrentTerminal(RetailerTerminalDto dto) {
        currentTerminal = dto;
        refresh();
    }
    
    @Override
    public void save() {
        
        popupRetailerTerminal.setPopupVisible(false);
        if (popupRetailerTerminal.getContent() != null) {
            RetailerTerminalPopupContent cmp = (RetailerTerminalPopupContent) popupRetailerTerminal.getContent();
            if (cmp != null) {
                setCurrentTerminal(cmp.getSelectedTerminal());
            }
        }
    }
    
    @Override
    public void cancel() {
        popupRetailerTerminal.setPopupVisible(false);
    }
    
    public long getRetailerTerminalId() {
        return getCurrentTerminal() == null ? 0 : getCurrentTerminal().getId();
    }
}
