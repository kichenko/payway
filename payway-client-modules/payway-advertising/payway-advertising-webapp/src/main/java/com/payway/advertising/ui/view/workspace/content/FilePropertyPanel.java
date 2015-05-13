/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.payway.advertising.core.service.DbAgentFileOwnerService;
import com.payway.advertising.core.service.DbAgentFileService;
import com.payway.advertising.model.DbAgentFile;
import com.payway.advertising.ui.view.core.UIHelpers;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * FilePropertyPanel
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
@Slf4j
public class FilePropertyPanel extends VerticalLayout {

    @UiField
    private Button btnOk;

    @UiField
    private Button btnDiscard;

    @UiField
    private TabSheet tabSheetFileProperty;

    @UiField
    private FilePropertyTabGeneral tabGeneral;

    @UiField
    private FilePropertyTabAdditional tabAdditional;

    private final FieldGroup fieldGroup = new FieldGroup();

    @Getter
    @Setter
    private DbAgentFileOwnerService dbAgentFileOwnerService;

    @Getter
    @Setter
    private DbAgentFileService dbAgentFileService;

    private DbAgentFile dbAgentFile;

    public FilePropertyPanel() {
        init();
    }

    private void init() {
        setSizeFull();
        addComponent(Clara.create("FilePropertyTabs.xml", this));
        tabSheetFileProperty.addTab(tabGeneral, "General");
        tabSheetFileProperty.addTab(tabAdditional, "Additional");

        //set custom container for owner combobox
        tabGeneral.getCbFileType().setContainerDataSource(new DbAgentFileOwnerBeanItemContainer(dbAgentFileOwnerService));

        //bind fields
        fieldGroup.bind(tabGeneral.getEditFileName(), "name");
        fieldGroup.bind(tabGeneral.getCbOwner(), "owner");
        fieldGroup.bind(tabGeneral.getCbFileType(), "kind");
        fieldGroup.bind(tabAdditional.getEditExpression(), "expression");
        fieldGroup.bind(tabAdditional.getChCountHints(), "countHints");

        btnOk.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    UIHelpers.showLoadingIndicator();
                    fieldGroup.commit();
                    dbAgentFileService.update(dbAgentFile);
                } catch (Exception ex) {
                    log.error("", ex);
                    UIHelpers.showErrorNotification("", "Error saving file property data");
                } finally {
                    UIHelpers.closeLoadingIndicator();
                }
            }
        });

        btnDiscard.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UIHelpers.showLoadingIndicator();
                fieldGroup.discard();
                UIHelpers.closeLoadingIndicator();
            }
        });
    }

    public void selectItem(DbAgentFile dbAgentFile) {
        this.dbAgentFile = dbAgentFile;
    }
}
