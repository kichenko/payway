/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace;

import com.payway.advertising.ui.view.core.AbstractView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Table;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * ContentConfigurationView
 *
 * @author Sergey Kichenko
 * @created 22.04.15 00:00
 */
@UIScope
@Component(value = "content-configuration")
public class ContentConfigurationView extends AbstractView {
    
    @UiField
    private Table gridFileExplorer; 

    public ContentConfigurationView() {
        setSizeFull();
        //addComponent(new Label("Hello from 'DashBoardSampleView'"));
    }
    
    @PostConstruct
    public void postConstruct() {
        //
    }
}
