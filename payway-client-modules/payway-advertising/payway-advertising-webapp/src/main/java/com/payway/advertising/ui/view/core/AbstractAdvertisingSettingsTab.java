/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.core;

import com.payway.advertising.core.service.app.settings.SettingsAppService;
import com.vaadin.ui.VerticalLayout;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AbstractAdvertisingSettingsTab
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
@NoArgsConstructor
public abstract class AbstractAdvertisingSettingsTab extends VerticalLayout {
    
    private static final long serialVersionUID = 1808496245684417347L;
    
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    protected SettingsAppService settingsAppService;
    
    @Getter
    @Setter
    protected String caption;
    
    public AbstractAdvertisingSettingsTab(String caption, SettingsAppService settingsAppService) {
        setCaption(caption);
        setSettingsAppService(settingsAppService);
    }
    
    public abstract void load();
    
    public abstract void save() throws Exception;
    
    public abstract void cancel();
    
}
