/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.payway.advertising.core.service.AgentFileOwnerService;
import com.payway.advertising.core.service.AgentFileService;
import com.payway.advertising.core.service.file.FileSystemManagerService;
import com.payway.advertising.core.service.file.FileSystemManagerServiceSecurity;
import com.payway.advertising.core.service.file.FileSystemObject;
import com.payway.advertising.core.validator.DbAgentFileValidator;
import com.payway.advertising.core.validator.Validator;
import com.payway.advertising.model.DbAgentFile;
import com.payway.advertising.model.DbFileType;
import com.payway.advertising.ui.utils.UIUtils;
import com.payway.advertising.ui.view.core.WorkspaceView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
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

    public interface PropertySaveListener {

        void onSave(DbAgentFile file);
    }

    @UiField
    private Button btnOk;

    @UiField
    private TabSheet tabSheetFileProperty;

    @UiField
    private FilePropertyTabGeneral tabGeneral;

    @UiField
    private FilePropertyTabAdditional tabAdditional;

    private final FieldGroup fieldGroup = new FieldGroup();

    @Getter
    @Setter
    private AgentFileOwnerService agentFileOwnerService;

    @Getter
    @Setter
    private AgentFileService agentFileService;

    @Getter
    @Setter
    private FileSystemManagerService fileSystemManagerService;

    @Getter
    @Setter
    private FileSystemManagerServiceSecurity fileSystemManagerServiceSecurity;

    @Getter
    @Setter
    private BeanItem<DbAgentFile> beanItem;

    @Getter
    @Setter
    private PropertySaveListener listener;

    @Getter
    private final Validator dbAgentFileValidator = new DbAgentFileValidator();

    @Getter
    @Setter
    private String rootPath;

    @Getter
    @Setter
    private String relativePath;

    @Getter
    @Setter
    private WorkspaceView workspaceView;

    public FilePropertyPanel() {
        init();
    }

    private void init() {
        setSizeFull();
        addStyleName("tab-panel-file-property");
        addComponent(Clara.create("FilePropertyTabs.xml", this));

        btnOk.setIcon(new ThemeResource("images/btn_save_property.png"));

        tabSheetFileProperty.addTab(tabGeneral, "General", new ThemeResource("images/tab_general.png"));
        tabSheetFileProperty.addTab(tabAdditional, "Additional", new ThemeResource("images/tab_additional.png"));

        //bind fields
        fieldGroup.setBuffered(false);
        fieldGroup.bind(tabGeneral.getCbOwner(), "owner");
        fieldGroup.bind(tabGeneral.getCbFileType(), "kind");
        fieldGroup.bind(tabAdditional.getEditExpression(), "expression");
        fieldGroup.bind(tabAdditional.getChCountHints(), "isCountHits");

        btnOk.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    workspaceView.showProgressBar();
                    if (dbAgentFileValidator.validate(beanItem.getBean())) {

                        //set name only for new object, where id == null
                        getBeanItem().getBean().setName(getRelativePath());

                        //set digest only for new object, where id == null
                        if (getBeanItem().getBean().getId() == 0) {
                            String digest = fileSystemManagerServiceSecurity.digestMD5Hex(fileSystemManagerService.getInputStream(new FileSystemObject(getRootPath() + getRelativePath(), FileSystemObject.FileSystemType.FILE, FileSystemObject.FileType.FILE, 0L, null)));
                            getBeanItem().getBean().setDigest(digest);
                        }

                        agentFileService.save(getBeanItem().getBean());

                        if (getListener() != null) {
                            getListener().onSave(beanItem.getBean());
                        }
                    } else {
                        UIUtils.showErrorNotification("", "Error validate data");
                    }
                } catch (Exception ex) {
                    log.error("Error saving file property data", ex);
                    UIUtils.showErrorNotification("", "Error saving file property data");
                } finally {
                    workspaceView.hideProgressBar();
                }
            }
        });
    }

    public void initOwnerBeanContainer() {
        //set custom container for owner combobox
        tabGeneral.getCbOwner().setContainerDataSource(new DbAgentFileOwnerBeanItemContainer(agentFileOwnerService));
        tabGeneral.getCbOwner().setFilteringMode(FilteringMode.CONTAINS);
        tabGeneral.getCbOwner().setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tabGeneral.getCbOwner().setItemCaptionPropertyId("name");
    }

    public void initFileTypeBeanContainer() {
        //set custom container for file type combobox
        tabGeneral.getCbFileType().setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
        tabGeneral.getCbFileType().addItem(DbFileType.Unknown);
        tabGeneral.getCbFileType().setItemCaption(DbFileType.Unknown, "-Unknown-");
        tabGeneral.getCbFileType().addItem(DbFileType.Popup);
        tabGeneral.getCbFileType().setItemCaption(DbFileType.Popup, "-Popup-");
        tabGeneral.getCbFileType().addItem(DbFileType.Logo);
        tabGeneral.getCbFileType().setItemCaption(DbFileType.Logo, "-Logo-");
        tabGeneral.getCbFileType().addItem(DbFileType.Clip);
        tabGeneral.getCbFileType().setItemCaption(DbFileType.Clip, "-Clip-");
        tabGeneral.getCbFileType().addItem(DbFileType.Banner);
        tabGeneral.getCbFileType().setItemCaption(DbFileType.Banner, "-Banner-");
        tabGeneral.getCbFileType().addItem(DbFileType.Archive);
        tabGeneral.getCbFileType().setItemCaption(DbFileType.Archive, "-Archive-");
    }

    public void updateFileName(String fileName) {
        tabGeneral.getEditFileName().setReadOnly(false);
        tabGeneral.getEditFileName().setValue(fileName);
        tabGeneral.getEditFileName().setReadOnly(true);
    }

    public void showProperty(String rootPath, String relativePath, String fileName, BeanItem<DbAgentFile> beanItem) {

        setBeanItem(beanItem);
        setRootPath(rootPath);
        setRelativePath(relativePath);

        tabGeneral.getCbOwner().getContainerDataSource().removeAllItems();
        if (beanItem.getBean().getOwner() != null) {
            ((DbAgentFileOwnerBeanItemContainer) tabGeneral.getCbOwner().getContainerDataSource()).addItem(beanItem.getBean().getOwner());
        }

        tabGeneral.getEditFileName().setReadOnly(false);
        tabGeneral.getEditFileName().setValue(fileName);
        tabGeneral.getEditFileName().setReadOnly(true);
        fieldGroup.setItemDataSource(beanItem);

        tabGeneral.setEnabled(true);
        tabAdditional.setEnabled(true);

        btnOk.setEnabled(true);
    }

    public void clearProperty() {
        tabGeneral.getEditFileName().setReadOnly(false);
        tabGeneral.getEditFileName().setValue("");
        tabGeneral.getEditFileName().setReadOnly(true);
        tabGeneral.getCbOwner().select(null);
        tabGeneral.getCbFileType().select(null);
        tabAdditional.getEditExpression().setValue("");
        tabAdditional.getChCountHints().setValue(false);

        tabGeneral.setEnabled(false);
        tabAdditional.setEnabled(false);

        btnOk.setEnabled(false);
    }
}
