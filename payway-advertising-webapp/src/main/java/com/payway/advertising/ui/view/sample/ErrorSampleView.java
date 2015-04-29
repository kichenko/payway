/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.sample;

import com.payway.advertising.ui.view.core.AbstractAdminView;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;

/**
 * ErrorSampleView
 *
 * @author Sergey Kichenko
 * @created 22.04.15 00:00
 */
@SpringView(name = "error")
public class ErrorSampleView extends AbstractAdminView {

    public ErrorSampleView() {
        setSizeFull();
        addComponent(new Label("Hello from 'ErrorSampleView'"));
    }

    @Override
    public String name() {
        return "error";
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //
    }

}
