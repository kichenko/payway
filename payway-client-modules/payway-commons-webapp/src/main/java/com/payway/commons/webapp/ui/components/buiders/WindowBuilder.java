/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.components.buiders;

import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * WindowBuilder
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
public final class WindowBuilder {

    private final Window window = new Window();

    private boolean sizeUndefined;

    public WindowBuilder() {
        setDefaults();
    }

    private void setDefaults() {

        window.setCaption("");
        window.setModal(false);
        window.setClosable(false);
        window.setResizable(false);
        window.setDraggable(false);
        window.setWindowMode(WindowMode.NORMAL);

        sizeUndefined = false;
    }

    public WindowBuilder withContent(Component content) {
        window.setContent(content);
        return this;
    }

    public WindowBuilder withCaption(String caption) {
        window.setCaption(caption);
        return this;
    }

    public WindowBuilder withModal() {
        window.setModal(true);
        return this;
    }

    public WindowBuilder withClosable() {
        window.setClosable(true);
        return this;
    }

    public WindowBuilder withResizable() {
        window.setResizable(true);
        return this;
    }

    public WindowBuilder withMaximazedWindowMode() {
        window.setWindowMode(WindowMode.MAXIMIZED);
        return this;
    }

    public WindowBuilder withDraggable() {
        window.setDraggable(true);
        return this;
    }

    public WindowBuilder withWidth(float width, Sizeable.Unit unit) {
        window.setWidth(width, unit);
        return this;
    }

    public WindowBuilder withHeight(float height, Sizeable.Unit unit) {
        window.setHeight(height, unit);
        return this;
    }

    public WindowBuilder withWidthPx(float width) {
        window.setWidth(width, Sizeable.Unit.PIXELS);
        return this;
    }

    public WindowBuilder withHeightPx(float height) {
        window.setHeight(height, Sizeable.Unit.PIXELS);
        return this;
    }

    public WindowBuilder withSizeUndefined() {
        sizeUndefined = true;
        return this;
    }

    public Window build() {

        if (window.getContent() != null && sizeUndefined) {
            window.getContent().setSizeUndefined();
        }

        return window;
    }

    public Window buildAndShow() {
        UI.getCurrent().addWindow(build());
        return window;
    }
}
