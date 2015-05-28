/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.payway.advertising.model.DbAgentFileOwner;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    public interface CrudEventListener {

        void cancel();

        void save(DbAgentFileOwner owner);
    }

    private static final long serialVersionUID = 4552146499165628193L;

    @UiField
    private TextField txtName;

    @UiField
    private TextArea txtDescription;

    @UiField
    private Button btnCancel;

    @UiField
    private Button btnSave;

    private final FieldGroup fieldGroup = new FieldGroup();

    @Getter
    @Setter
    private CrudEventListener listener;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private DbAgentFileOwner owner;

    public AgentFileOwnerCRUDWindow(String caption, DbAgentFileOwner owner) {

        setModal(true);
        setCaption(caption);
        setResizable(false);
        setWidth(450, Unit.PIXELS);
        setHeight(275, Unit.PIXELS);
        setContent(Clara.create("AgentFileOwnerCRUDWindow.xml", this));

        btnSave.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (getListener() != null) {
                    getListener().save(getOwner());
                }
            }
        });

        btnCancel.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (getListener() != null) {
                    getListener().cancel();
                }
            }
        });

        setOwner(owner);
        bind();
    }

    private void bind() {

        fieldGroup.setBuffered(false);
        fieldGroup.bind(txtName, "name");
        fieldGroup.bind(txtDescription, "description");
        fieldGroup.setItemDataSource(new BeanItem(getOwner()));

        txtName.setNullRepresentation("");
        txtDescription.setNullRepresentation("");
    }

    public void show() {
        UI.getCurrent().addWindow(this);
    }
}
