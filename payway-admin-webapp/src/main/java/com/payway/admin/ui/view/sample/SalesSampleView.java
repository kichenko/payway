/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.ui.view.sample;

import com.payway.admin.ui.view.core.AbstractAdminView;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;

/**
 * SalesSampleView
 *
 * @author Sergey Kichenko
 * @created 22.04.15 00:00
 */
@SpringView(name = "sales")
public class SalesSampleView extends AbstractAdminView {

    public SalesSampleView() {
        setSizeFull();
        addComponent(new Label("Hello from 'SalesSampleView'"));
    }

    @Override
    public String name() {
        return "sales";
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //
    }

}
