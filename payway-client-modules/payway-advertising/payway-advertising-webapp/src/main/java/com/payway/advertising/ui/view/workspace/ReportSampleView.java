/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace;

import com.payway.advertising.ui.view.core.AbstractWorkspaceView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * ReportSampleView
 *
 * @author Sergey Kichenko
 * @created 22.04.15 00:00
 */
@UIScope
@Component(value = "reports")
public class ReportSampleView extends AbstractWorkspaceView {

    public ReportSampleView() {
        setSizeFull();
        addComponent(new Label("Hello from 'ReportSampleView'"));
    }

    @PostConstruct
    public void postConstruct() {
        //
    }

    @Override
    public void activate() {
        //
    }
}
