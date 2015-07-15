/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.common;

import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import lombok.Getter;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * PanelTableButtons
 *
 * @author Sergey Kichenko
 * @created 13.07.15 00:00
 */
public class PanelTableButtons extends Panel {

    private static final long serialVersionUID = 2031134924914083166L;

    @Getter
    @UiField
    private Button btnAdd;

    @Getter
    @UiField
    private Table grid;

    public PanelTableButtons() {
        setContent(Clara.create("PanelTableButtons.xml", this));
    }
}
