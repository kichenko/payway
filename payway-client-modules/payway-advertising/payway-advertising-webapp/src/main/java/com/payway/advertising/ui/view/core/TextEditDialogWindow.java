/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.core;

import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * TextEditDialogWindow
 *
 * @author Sergey Kichenko
 * @created 07.10.15 00:00
 */
public class TextEditDialogWindow extends Window {

    public interface TextEditDialogWindowEvent {

        boolean onOk(String text);

        void onCancel();
    }

    @UiField
    private Button btnOk;

    @UiField
    private Button btnCancel;

    @UiField
    private TextField editText;

    private TextEditDialogWindowEvent listener;

    public TextEditDialogWindow(String caption, TextEditDialogWindowEvent eventListener) {
        setModal(true);
        setClosable(true);
        setDraggable(false);
        setResizable(false);
        setCaption(caption);
        setContent(Clara.create("TextEditDialogWindow.xml", this));

        listener = eventListener;

        btnOk.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (listener != null) {
                    if (listener.onOk(editText.getValue())) {
                        UI.getCurrent().removeWindow(TextEditDialogWindow.this);
                    }
                }
            }
        });

        btnCancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (listener != null) {
                    listener.onCancel();
                }
            }
        });
    }

    public void show() {
        UI.getCurrent().addWindow(this);
    }

    @Override
    public void close() {

        if (listener != null) {
            listener.onCancel();
        }

        UI.getCurrent().removeWindow(this);
    }
}
