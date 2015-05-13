/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import lombok.Getter;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * FilePropertyTabGeneral
 *
 * @author Sergey Kichenko
 * @created 12.10.15 00:00
 */
@Getter
public class FilePropertyTabGeneral extends VerticalLayout {

    @UiField
    private TextField editFileName;

    @UiField
    private ComboBox cbOwner;

    @UiField
    private Button btnOwnerBook;

    @UiField
    private ComboBox cbFileType;

    public FilePropertyTabGeneral() {
        init();
    }

    private void init() {
        setSizeFull();
        addComponent(Clara.create("FilePropertyTabGeneral.xml", this));
    }
}
