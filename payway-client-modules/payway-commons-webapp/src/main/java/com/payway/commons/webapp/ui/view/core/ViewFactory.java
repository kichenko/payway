/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.view.core;

import com.vaadin.ui.Component;

/**
 * ViewFactory
 *
 * @author Sergey Kichenko
 * @created 30.04.15 00:00
 */
public interface ViewFactory {

    Component view(String id);
}
