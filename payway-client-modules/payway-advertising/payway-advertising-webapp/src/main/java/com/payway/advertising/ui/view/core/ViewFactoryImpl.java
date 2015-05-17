/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.core;

import com.vaadin.ui.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Абстрактный компонент-представление
 *
 * @author Sergey Kichenko
 * @created 30.04.15 00:00
 */
@org.springframework.stereotype.Component
public class ViewFactoryImpl implements ViewFactory {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Component view(String id) {
        return applicationContext.getBean(id, Component.class);
    }

}
