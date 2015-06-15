/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.components;

import com.payway.bustickets.airportexpress.ui.components.containers.RetailerTerminalDtoBeanContainer;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.messaging.model.common.RetailerTerminalDto;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.UI;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * RetailerTerminalPopupViewContent
 *
 * @author Sergey Kichenko
 * @created 11.06.15 00:00
 */
@Slf4j
public class RetailerTerminalPopupContent implements PopupView.Content {
    
    public interface PopupTerminalStateChangeListener {
        
        void save();
        
        void cancel();
    }
    
    private static final long serialVersionUID = 1055678631383122709L;
    
    private String caption = "select";
    private Component content;
    
    @Getter
    @UiField
    private ComboBox cbRetailerTerminal;
    
    @UiField
    private Button btnSave;
    
    @UiField
    private Button btnCancel;
    
    private final PopupTerminalStateChangeListener listener;
    
    public RetailerTerminalPopupContent(String caption, PopupTerminalStateChangeListener listener) {
        this.caption = caption;
        this.listener = listener;
        init();
    }
    
    private void init() {
        
        content = Clara.create("RetailerTerminalPopupContentView.xml", this);
        cbRetailerTerminal.setContainerDataSource(new RetailerTerminalDtoBeanContainer());
        
        btnSave.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (listener != null) {
                    
                    if (cbRetailerTerminal.getValue() == null) {
                        ((InteractionUI) UI.getCurrent()).showNotification("Selecting retail terminal", "Please, select retail terminal before save", Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                    
                    listener.save();
                }
            }
        });
        
        btnCancel.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (listener != null) {
                    listener.cancel();
                }
            }
        });
    }
    
    @Override
    public final Component getPopupComponent() {
        return content;
    }
    
    @Override
    public final String getMinimizedValueAsHTML() {
        return caption;
    }
    
    public void setUpTerminals(List<RetailerTerminalDto> terminals) {
        
        if (terminals == null || terminals.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("Empty terminal list on set up container data source");
            }
            
            return;
        }
        
        ((RetailerTerminalDtoBeanContainer) cbRetailerTerminal.getContainerDataSource()).removeAllItems();
        ((RetailerTerminalDtoBeanContainer) cbRetailerTerminal.getContainerDataSource()).addAll(terminals);
    }
    
    public RetailerTerminalDto getSelectedTerminal() {
        
        BeanItem<RetailerTerminalDto> item = ((RetailerTerminalDtoBeanContainer) cbRetailerTerminal.getContainerDataSource()).getItem(cbRetailerTerminal.getValue());
        if (item != null) {
            return item.getBean();
        }
        
        return null;
    }
    
    public void setSelectedTerminal(RetailerTerminalDto dto) {
        cbRetailerTerminal.select(dto.getTerminalName());
    }
}
