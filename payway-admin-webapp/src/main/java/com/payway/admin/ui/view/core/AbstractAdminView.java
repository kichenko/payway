/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.ui.view.core;

import com.vaadin.ui.VerticalLayout;

/**
 * AbstractAdminView
 *
 * @author Sergey Kichenko
 * @created 22.04.15 00:00
 */
public abstract class AbstractAdminView extends VerticalLayout implements AdminView {

    @Override
    public String name() {
        return "";
    }

    @Override
    public boolean isStateful() {
        return false;
    }
}
