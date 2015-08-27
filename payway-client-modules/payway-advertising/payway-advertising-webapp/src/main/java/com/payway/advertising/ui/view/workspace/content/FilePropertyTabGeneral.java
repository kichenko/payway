/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.payway.commons.webapp.ui.components.ComboBoxEx;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
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

    private static final long serialVersionUID = -6741511197889825907L;

    @UiField
    private TextField editFileName;

    @UiField
    private ComboBoxEx cbOwner;

    @UiField
    private Button btnOwnerBook;

    @UiField
    private ComboBoxEx cbFileType;

    public FilePropertyTabGeneral() {
        init();
    }

    private void init() {
        setSizeFull();
        addComponent(Clara.create("FilePropertyTabGeneral.xml", this));
        btnOwnerBook.setIcon(new ThemeResource("images/btn_owner_book.png"));
        btnOwnerBook.addStyleName("common-no-space-image-button");
    }
}
