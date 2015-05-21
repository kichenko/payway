/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui;

import com.payway.advertising.ui.bus.UIEventBus;
import com.vaadin.ui.UI;

/**
 * AbstractUI
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
public abstract class AbstractUI extends UI {

    public abstract UIEventBus getEventBus();
}
