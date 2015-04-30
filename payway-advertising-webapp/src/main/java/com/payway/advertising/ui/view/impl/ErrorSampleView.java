/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.impl;

import com.payway.advertising.ui.view.core.AbstractView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * ErrorSampleView
 *
 * @author Sergey Kichenko
 * @created 22.04.15 00:00
 */
@UIScope
@Component(value = "error")
public class ErrorSampleView extends AbstractView {

    public ErrorSampleView() {
        setSizeFull();
        addComponent(new Label("Hello from 'ErrorSampleView'"));
    }

    @PostConstruct
    public void postConstruct() {
        //
    }
}
