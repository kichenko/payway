/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.components.decorators;

import com.vaadin.ui.VerticalLayout;

/**
 * ContentDecorator
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
public abstract class ContentDecorator extends VerticalLayout {

    private static final long serialVersionUID = -273046552443378408L;

    protected abstract void init();

}
