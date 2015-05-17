/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.core;

import com.vaadin.ui.Component;

/**
 * Интерфейс фабрики представлений
 *
 * @author Sergey Kichenko
 * @created 30.04.15 00:00
 */
public interface ViewFactory {

    Component view(String id);
}
