/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.ui.view.core;

import com.vaadin.navigator.View;

/**
 * AdminView
 *
 * @author Sergey Kichenko
 * @created 22.04.15 00:00
 */
public interface AdminView extends View {

    /**
     * The tag name of view. Used in uri navigation (ex:
     * http://www.site.com/app/#!dashboard).
     */
    String name();

    /**
     * Tag of stateful view state, then navigate.
     */
    boolean isStateful();
}
