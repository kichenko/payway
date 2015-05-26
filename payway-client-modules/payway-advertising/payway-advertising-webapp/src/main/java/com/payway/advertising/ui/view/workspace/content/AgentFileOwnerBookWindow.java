/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.payway.advertising.ui.component.pagetable.PagedTable;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import lombok.NoArgsConstructor;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * AgentFileOwnerBookWindow
 *
 * @author Sergey Kichenko
 * @created 26.05.15 00:00
 */
@NoArgsConstructor
public class AgentFileOwnerBookWindow extends Window {

    private static final long serialVersionUID = -3097395157377631255L;

    @UiField
    private Button btnAddNew;

    @UiField
    private TextField txtFilter;

    @UiField
    private PagedTable gridOwners;

    @UiField
    private HorizontalLayout layoutGridControls;

    public AgentFileOwnerBookWindow(String caption) {
        setCaption(caption);
        setResizable(false);
        //setDraggable(false);

        setContent(Clara.create("AgentFileOwnerBookWindow.xml", this));
        layoutGridControls.addComponent(gridOwners.createControls());
        txtFilter.setIcon(FontAwesome.SEARCH);
    }
}
