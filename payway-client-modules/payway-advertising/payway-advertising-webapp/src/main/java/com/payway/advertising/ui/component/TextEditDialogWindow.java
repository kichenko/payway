/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component;

import com.vaadin.server.ThemeResource;
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

    private static final long serialVersionUID = -2523315774088405157L;

    public interface TextEditDialogWindowEvent {

        boolean onOk(String text);

        boolean onCancel();
    }

    @UiField
    private Button btnOk;

    @UiField
    private Button btnCancel;

    @UiField
    private TextField editText;

    private TextEditDialogWindowEvent listener;

    public TextEditDialogWindow(String caption, String value, TextEditDialogWindowEvent eventListener) {
        setModal(true);
        setClosable(true);
        setDraggable(false);
        setResizable(false);
        setCaption(caption);
        setWidth(400, Unit.PIXELS);
        setHeight(150, Unit.PIXELS);
        setContent(Clara.create("TextEditDialogWindow.xml", this));

        btnOk.setIcon(new ThemeResource("images/components/text_edit_dialog_window/btn_ok.png"));
        btnCancel.setIcon(new ThemeResource("images/components/text_edit_dialog_window/btn_cancel.png"));

        listener = eventListener;
        editText.setValue(value);

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
                    if (listener.onCancel()) {
                        close();
                    }
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
