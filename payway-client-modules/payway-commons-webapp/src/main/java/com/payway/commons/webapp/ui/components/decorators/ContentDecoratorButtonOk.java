/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.components.decorators;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * ContentDecoratorButtonOk
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
public final class ContentDecoratorButtonOk extends ContentDecorator {

    private static final long serialVersionUID = -1750820200286993688L;

    public interface ButtonClickEvent {

        void click(ContentDecorator decorator, Button.ClickEvent event);
    }

    @UiField
    private VerticalLayout layoutContent;

    private ButtonClickEvent btnOkClickEvent;

    public ContentDecoratorButtonOk() {
        setSizeFull();
        init();
    }

    @Override
    protected void init() {
        addComponent(Clara.create(getClass().getResourceAsStream("ContentDecoratorButtonOk.xml"), this));
    }

    public ContentDecoratorButtonOk(Component content, ButtonClickEvent btnOkClickEvent) {
        setSizeFull();
        init();
        layoutContent.addComponent(content);
        this.btnOkClickEvent = btnOkClickEvent;
    }

    @UiHandler(value = "btnOk")
    public void buttonClickOk(Button.ClickEvent event) {
        if (btnOkClickEvent != null) {
            btnOkClickEvent.click(this, event);
        }
    }
}
