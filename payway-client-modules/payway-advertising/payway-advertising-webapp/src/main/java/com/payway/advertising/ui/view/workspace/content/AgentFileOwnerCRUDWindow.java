/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.vaadin.ui.Button;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import lombok.NoArgsConstructor;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * AgentFileOwnerCRUDWindow
 *
 * @author Sergey Kichenko
 * @created 26.05.15 00:00
 */
@NoArgsConstructor
public class AgentFileOwnerCRUDWindow extends Window {

    private static final long serialVersionUID = 4552146499165628193L;

    @UiField
    private TextField txtName;

    @UiField
    private TextArea txtDescription;

    @UiField
    private Button btnCancel;

    @UiField
    private Button btnSave;

    public AgentFileOwnerCRUDWindow(String caption) {
        setCaption(caption);
        setResizable(false);
        //setDraggable(false);
        setWidth(450, Unit.PIXELS);
        setHeight(275, Unit.PIXELS);

        setContent(Clara.create("AgentFileOwnerCRUDWindow.xml", this));
    }
}
