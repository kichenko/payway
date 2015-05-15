/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import lombok.Getter;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * FilePropertyTabAdditional
 *
 * @author Sergey Kichenko
 * @created 12.10.15 00:00
 */
@Getter
public class FilePropertyTabAdditional extends VerticalLayout {

    @UiField
    private TextArea editExpression;

    @UiField
    private CheckBox chCountHints;
    
    public FilePropertyTabAdditional() {
        init();
    }

    private void init() {
        setSizeFull();
        addComponent(Clara.create("FilePropertyTabAdditional.xml", this));
    }
}
