/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.payway.advertising.core.service.AgentFileOwnerService;
import com.payway.advertising.core.service.AgentFileService;
import com.payway.advertising.core.service.file.FileSystemManagerService;
import com.payway.advertising.core.service.file.FileSystemManagerServiceSecurity;
import com.payway.advertising.core.service.file.FileSystemObject;
import com.payway.advertising.core.validator.AgentFileExpressionValidator;
import com.payway.advertising.core.validator.AgentFileValidator;
import com.payway.advertising.model.DbAgentFile;
import com.payway.advertising.model.DbAgentFileOwner;
import com.payway.advertising.model.DbFileType;
import com.payway.advertising.ui.view.workspace.content.container.AgentFileOwnerBeanItemContainer;
import com.payway.advertising.ui.view.workspace.content.preview.FilePreviewPanel;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.validator.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * FilePropertyPanel
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
@Slf4j
public class FilePropertyPanel extends VerticalLayout {

    private static final long serialVersionUID = 6751364138726607005L;

    public interface PropertySaveListener {

        void onSave(DbAgentFile file);
    }

    @UiField
    private Button btnSave;

    @UiField
    private TabSheet tabSheetFileProperty;

    @UiField
    private FilePropertyTabGeneral tabGeneral;

    @UiField
    private FilePropertyTabAdditional tabAdditional;

    @UiField
    private FilePreviewPanel filePreviewPanel;

    private final FieldGroup fieldGroup = new FieldGroup();

    @Getter
    @Setter
    private PropertySaveListener listener;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private AgentFileOwnerService agentFileOwnerService;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private AgentFileService agentFileService;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private FileSystemManagerService fileSystemManagerService;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private FileSystemManagerServiceSecurity fileSystemManagerServiceSecurity;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private DbAgentFile bean;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private Validator agentFileExpressionValidator;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private Validator agentFileValidator;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private String rootPath;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private String relativePath;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private List<DbAgentFileOwner> owners = new ArrayList<>(0);

    public FilePropertyPanel() {
        init();
        setAgentFileExpressionValidator(new AgentFileExpressionValidator());
        setAgentFileValidator(new AgentFileValidator(getAgentFileExpressionValidator()));
    }

    private void init() {

        setSizeFull();
        addStyleName("tab-panel-file-property");
        addComponent(Clara.create("FilePropertyTabs.xml", this));

        btnSave.setIcon(new ThemeResource("images/btn_save_property.png"));

        tabSheetFileProperty.addTab(tabGeneral, "General", new ThemeResource("images/tab_general.png"));
        tabSheetFileProperty.addTab(tabAdditional, "Additional", new ThemeResource("images/tab_additional.png"));

        //bind fields
        fieldGroup.setBuffered(false);
        fieldGroup.bind(tabGeneral.getCbOwner(), "owner");
        fieldGroup.bind(tabGeneral.getCbFileType(), "kind");
        fieldGroup.bind(tabGeneral.getSpinSeqNo(), "seqNo");
        fieldGroup.bind(tabAdditional.getEditExpression(), "expression");
        fieldGroup.bind(tabAdditional.getChCountHints(), "isCountHits");

        tabGeneral.getBtnOwnerBook().addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;

            @Override
            public void buttonClick(Button.ClickEvent event) {

                Window wnd = new AgentFileOwnerBookWindow("Agent owners book", getAgentFileOwnerService());
                wnd.setModal(true);

                wnd.addCloseListener(new Window.CloseListener() {
                    private static final long serialVersionUID = 751019052034176230L;

                    @Override
                    public void windowClose(Window.CloseEvent e) {
                        refreshOwners();
                    }
                });

                UI.getCurrent().addWindow(wnd);
                wnd.markAsDirtyRecursive(); //#hack, because window is wrong painted
            }
        });
    }

    private void refreshOwners() {

        try {
            ((InteractionUI) UI.getCurrent()).showProgressBar();

            setOwners(agentFileOwnerService.list());

            Object selectedItemId = tabGeneral.getCbOwner().getValue();
            ((AgentFileOwnerBeanItemContainer) tabGeneral.getCbOwner().getContainerDataSource()).removeAllItems();
            ((AgentFileOwnerBeanItemContainer) tabGeneral.getCbOwner().getContainerDataSource()).addAll(getOwners());

            tabGeneral.getCbOwner().setValue(selectedItemId);

        } catch (Exception ex) {
            log.error("Refresh agent owner list", ex);
            ((InteractionUI) UI.getCurrent()).showNotification("Refresh agent owner list", "Bad refresh agent owner list", Notification.Type.ERROR_MESSAGE);
        } finally {
            ((InteractionUI) UI.getCurrent()).closeProgressBar();
        }
    }

    @UiHandler("btnSave")
    public void clickSave(Button.ClickEvent event) {

        try {
            ((InteractionUI) UI.getCurrent()).showProgressBar();

            if (getBean().getId() == null) {
                //set name only for new object, where id == null
                getBean().setName(StringUtils.substringAfter(getRelativePath(), getRootPath()));

                //set digest only for new object, where id == null
                String digest = fileSystemManagerServiceSecurity.digestMD5Hex(fileSystemManagerService.getInputStream(new FileSystemObject(getRelativePath(), FileSystemObject.FileType.FILE, 0L, null)));
                getBean().setDigest(digest);
            }

            if (agentFileValidator.validate(getBean())) {
                agentFileService.save(getBean());
                if (getListener() != null) {
                    getListener().onSave(getBean());
                }
            } else {
                ((InteractionUI) UI.getCurrent()).showNotification("", "Can't save, invalid file property values", Notification.Type.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            log.error("Can't save file property", ex);
            ((InteractionUI) UI.getCurrent()).showNotification("", "Can't save file property", Notification.Type.ERROR_MESSAGE);
        } finally {
            ((InteractionUI) UI.getCurrent()).closeProgressBar();
        }
    }

    public void setUp(AgentFileService agentFileService, AgentFileOwnerService agentFileOwnerService, FileSystemManagerService fileSystemManagerService, FileSystemManagerServiceSecurity fileSystemManagerServiceSecurity) {

        setAgentFileService(agentFileService);
        setAgentFileOwnerService(agentFileOwnerService);
        setFileSystemManagerService(fileSystemManagerService);
        setFileSystemManagerServiceSecurity(fileSystemManagerServiceSecurity);
    }

    public void setUpOwnerBeanContainer() {

        tabGeneral.getCbOwner().setContainerDataSource(new AgentFileOwnerBeanItemContainer());
        tabGeneral.getCbOwner().setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tabGeneral.getCbOwner().setItemCaptionPropertyId("name");
        tabGeneral.getCbOwner().setNullSelectionAllowed(true);

        try {
            setOwners(getAgentFileOwnerService().list());
            ((AgentFileOwnerBeanItemContainer) tabGeneral.getCbOwner().getContainerDataSource()).removeAllItems();
            ((AgentFileOwnerBeanItemContainer) tabGeneral.getCbOwner().getContainerDataSource()).addAll(getOwners());
        } catch (Exception ex) {
            log.error("Set up owner bean container failed - {}", ex);
        }
    }

    public void setUpFileTypeBeanContainer() {

        tabGeneral.getCbFileType().setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT_DEFAULTS_ID);

        tabGeneral.getCbFileType().addItem(DbFileType.Unknown);
        tabGeneral.getCbFileType().setItemCaption(DbFileType.Unknown, "Unknown");

        tabGeneral.getCbFileType().addItem(DbFileType.Popup);
        tabGeneral.getCbFileType().setItemCaption(DbFileType.Popup, "Popup");

        tabGeneral.getCbFileType().addItem(DbFileType.Logo);
        tabGeneral.getCbFileType().setItemCaption(DbFileType.Logo, "Logo");

        tabGeneral.getCbFileType().addItem(DbFileType.Clip);
        tabGeneral.getCbFileType().setItemCaption(DbFileType.Clip, "Clip");

        tabGeneral.getCbFileType().addItem(DbFileType.Banner);
        tabGeneral.getCbFileType().setItemCaption(DbFileType.Banner, "Banner");

        tabGeneral.getCbFileType().addItem(DbFileType.Archive);
        tabGeneral.getCbFileType().setItemCaption(DbFileType.Archive, "Archive");
    }

    public void updateFileName(String fileName) {
        tabGeneral.getEditFileName().setReadOnly(false);
        tabGeneral.getEditFileName().setValue(fileName);
        tabGeneral.getEditFileName().setReadOnly(true);
    }

    public void showProperty(String rootPath, String relativePath, String fileName, DbAgentFile bean) {

        setBean(bean);
        setRootPath(rootPath);
        setRelativePath(relativePath);

        if (getBean().getOwner() != null) {
            tabGeneral.getCbOwner().setValue(bean.getOwner().getId());
        } else {
            tabGeneral.getCbOwner().setValue(null);
        }

        tabGeneral.getEditFileName().setReadOnly(false);
        tabGeneral.getEditFileName().setValue(fileName);
        tabGeneral.getEditFileName().setReadOnly(true);
        fieldGroup.setItemDataSource(new BeanItem<>(getBean()));

        tabGeneral.setEnabled(true);
        tabAdditional.setEnabled(true);
        btnSave.setEnabled(true);

        filePreviewPanel.setFileSystemManagerService(fileSystemManagerService);
        filePreviewPanel.show(fileName, relativePath, bean == null ? null : bean.getKind());
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
        btnSave.setEnabled(false);

        filePreviewPanel.clear();
    }
}
