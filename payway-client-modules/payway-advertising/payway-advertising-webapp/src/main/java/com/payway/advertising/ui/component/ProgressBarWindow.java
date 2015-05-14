/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.ui.component;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.vaadin.teemu.clara.Clara;

/**
 * ProgressBarWindow
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
public class ProgressBarWindow extends Window {

    public ProgressBarWindow() {
        setModal(true);
        setClosable(false);
        setDraggable(false);
        setResizable(false);
        setContent(Clara.create("ProgressBarWindow.xml", this));
    }

    public void show() {
        UI.getCurrent().addWindow(this);
    }

    @Override
    public void close() {
        UI.getCurrent().removeWindow(this);
    }
}
