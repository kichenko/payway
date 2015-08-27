/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.payway.advertising.core.handlers.FileHandlerArgs;
import com.payway.advertising.core.service.AgentFileOwnerService;
import com.payway.advertising.core.service.AgentFileService;
import com.payway.advertising.core.service.app.settings.SettingsAppService;
import com.payway.advertising.core.service.bean.BeanService;
import com.payway.advertising.core.service.config.apply.ApplyConfigRunCallback;
import com.payway.advertising.core.service.config.apply.ConfigurationApplyService;
import com.payway.advertising.core.service.file.FileSystemManagerService;
import com.payway.advertising.core.service.file.FileSystemObject;
import com.payway.advertising.core.utils.Helpers;
import com.payway.advertising.model.DbAgentFile;
import com.payway.advertising.model.DbFileType;
import com.payway.advertising.model.helpers.clonable.DbAgentFileDeepCopyClonable;
import com.payway.advertising.ui.component.BreadCrumbs;
import com.payway.advertising.ui.component.TextEditDialogWindow;
import com.payway.advertising.ui.component.UploadButtonWrapper;
import com.payway.advertising.ui.component.UploadTaskPanel;
import com.payway.advertising.ui.upload.UploadTask;
import com.payway.advertising.ui.upload.UploadTaskDnD;
import com.payway.advertising.ui.upload.UploadTaskFileInput;
import com.payway.advertising.ui.utils.UIUtils;
import com.payway.advertising.ui.view.core.AbstractAdvertisingWorkspaceView;
import com.payway.commons.webapp.service.app.user.WebAppUserService;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.components.buiders.WindowBuilder;
import com.payway.commons.webapp.validator.Validator;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.Extension;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Html5File;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;
import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBoxListener;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.peter.contextmenu.ContextMenu;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * AdvertisingContentConfigurationView
 *
 * @author Sergey Kichenko
 * @created 22.04.15 00:00
 */
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component(value = AdvertisingContentConfigurationView.ADVERTISING_CONTENT_WORKSPACE_VIEW_ID)
public class AdvertisingContentConfigurationView extends AbstractAdvertisingWorkspaceView implements UploadTaskPanel.UploadEventListener, ContextMenu.ContextMenuItemClickListener, ContextMenu.ContextMenuOpenedListener.TableListener, ContextMenu.ContextMenuOpenedListener.ComponentListener {

    public static final String ADVERTISING_CONTENT_WORKSPACE_VIEW_ID = ADVERTISING_DEFAULT_WORKSPACE_VIEW_PREFIX + "ContentConfiguration";

    private static final long serialVersionUID = -8149543787791067201L;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContextMenuItemData implements Serializable {

        private static final long serialVersionUID = -3500210886880680122L;

        public enum MenuAction {

            ROW_REFRESH_FOLDER,
            ROW_NEW_FOLDER,
            ROW_EDIT,
            ROW_REMOVE,
            ROW_CFG_APPLY,
            ROW_FILE_SEQUENCE,
            ROW_DOWNLOAD_FILE,
            TABLE_NEW_FOLDER,
            TABLE_REFRESH_FOLDER,
            TABLE_CFG_APPLY,
            TABLE_FILE_SEQUENCE
        }

        private MenuAction action;
        private Object data;
    }

    @UiField
    private Table gridFileExplorer;

    @UiField
    private FilePropertyPanel panelFileProperty;

    @UiField
    private BreadCrumbs breadCrumbs;

    @UiField
    private HorizontalSplitPanel splitPanel;

    @UiField
    private Button btnDownloadFile;

    private ContextMenu gridContextMenu = new ContextMenu();

    @Autowired
    private ApplicationContext applicationContext;

    @Getter
    @Setter
    @Autowired
    private FileSystemManagerService fileSystemManagerService;

    @Autowired
    private SettingsAppService settingsAppService;

    @Autowired
    protected WebAppUserService userAppService;

    @Autowired
    @Qualifier("app.advertising.FileNameValidator")
    private Validator fileNameValidator;

    @Getter
    @Setter
    @Autowired
    private AgentFileOwnerService agentFileOwnerService;

    @Getter
    @Setter
    @Autowired
    private AgentFileService agentFileService;

    @Getter
    @Setter
    @Autowired
    private ConfigurationApplyService configurationApplyService;

    @Getter
    @Setter
    @Autowired
    private BeanService beanService;

    @Getter
    @Setter
    @Resource(name = "app.advertising.SupportedVideoFileExtensions")
    private List<String> supportedVideoFileExtensions;

    @Getter
    private String localConfigPath;

    @Setter
    private String currentPath;

    private boolean isFileGridLoadedOnActivate;

    @PostConstruct
    public void postConstruct() {
        init();
    }

    private void init() {
        setSizeFull();
        addComponent(Clara.create("AdvertisingContentConfigurationView.xml", this));

        splitPanel.setFirstComponent(gridFileExplorer);
        splitPanel.setSecondComponent(panelFileProperty);
        splitPanel.setSplitPosition(75, Unit.PERCENTAGE);

        initLocalConfigPath();
        initCurrentPath();

        initGridFileExplorerTable();
        initBreadCrumbs();
        initTabSheetProperty();
    }

    private void initLocalConfigPath() {
        localConfigPath = Helpers.addEndSeparator(fileSystemManagerService.canonicalization(Helpers.addEndSeparator(settingsAppService.getLocalConfigPath())));
    }

    private void initCurrentPath() {
        currentPath = getLocalConfigPath();
    }

    private String getCurrentPath() {
        return Helpers.addEndSeparator(currentPath);
    }

    @Override
    public void activate() {
        getMenuBar().setVisible(true);
        getButtonFileUploadToolBar().setVisible(true);
        if (!isFileGridLoadedOnActivate) {
            try {
                ((InteractionUI) UI.getCurrent()).showProgressBar();
                FileSystemObject fo = new FileSystemObject(getCurrentPath(), FileSystemObject.FileType.FOLDER, 0L, null);
                if (!fileSystemManagerService.exist(fo)) {
                    fileSystemManagerService.create(fo);
                }
                fillGridFileExplorer(getCurrentPath());
                panelFileProperty.clearProperty();
                isFileGridLoadedOnActivate = true;

                getUploadTaskPanel().setUploadEventListener(this);
                getUploadTaskPanel().setBeanService(getBeanService());

                activateFileUploadButton();
                activateDragAndDropWrapper();
                activateMenuBar();

            } catch (Exception ex) {
                log.error("Bad activating configuration workspace", ex);
                ((InteractionUI) UI.getCurrent()).showNotification("", "Can't activate configuration workspace", Notification.Type.ERROR_MESSAGE);
            } finally {
                ((InteractionUI) UI.getCurrent()).closeProgressBar();
            }
        }
    }

    private void activateMenuBar() {
        buildMenuBar();
    }

    private boolean checkVideoFileOnExist(String path, String fileName) throws Exception {

        if (!settingsAppService.isConvertVideoFiles()) {
            return false;
        }

        //it's video file, checking... 
        if (supportedVideoFileExtensions.contains(StringUtils.substringAfterLast(fileName, ".").toLowerCase())) {
            if (settingsAppService.getCurrentFormatContainer() != null) {
                return fileSystemManagerService.exist(new FileSystemObject(path + Helpers.changeFileExt(fileName, settingsAppService.getCurrentFormatContainer().getFileExt()), FileSystemObject.FileType.FILE, 0L, null));
            }
        }

        return false;
    }

    private void activateFileUploadButton() {

        UploadButtonWrapper.UploadStartedEventProcessor processor;

        processor = new UploadButtonWrapper.UploadStartedEventProcessor() {
            @Override
            public boolean process(Upload upload, Upload.StartedEvent event) {
                boolean ok = false;
                if (upload != null) {
                    UploadTaskFileInput task = new UploadTaskFileInput(settingsAppService.getTemporaryUploadDirPath(), getCurrentPath(), settingsAppService.getUploadBufferSize());

                    task.setUploadObject(upload);
                    task.setFileName(event.getFilename());
                    task.setFileSize(event.getContentLength());
                    task.setTmpFileExt(settingsAppService.getTemporaryFileExt());
                    upload.setReceiver(task);

                    if (fileNameValidator.validate(task.getFileName())) {
                        try {
                            if (checkVideoFileOnExist(getCurrentPath(), task.getFileName()) || fileSystemManagerService.exist(new FileSystemObject(getCurrentPath() + task.getFileName(), FileSystemObject.FileType.FILE, 0L, null))) {
                                task.interrupt();
                                ((InteractionUI) UI.getCurrent()).showNotification("", "File already downloaded on server", Notification.Type.ERROR_MESSAGE);
                            } else {
                                getUploadTaskPanel().addUploadTask(task);
                                ok = true;
                            }
                        } catch (Exception ex) {
                            log.error("Unknown file upload error", ex);
                            ((InteractionUI) UI.getCurrent()).showNotification("", "Unknown file upload error", Notification.Type.ERROR_MESSAGE);
                        }
                    } else {
                        ((InteractionUI) UI.getCurrent()).showNotification("", "Invalid file name", Notification.Type.ERROR_MESSAGE);
                    }

                    if (!ok) {
                        task.interrupt();
                    }
                }

                return ok;
            }
        };

        getButtonFileUploadToolBar().setStartedEventProcessorListener(processor);
        getFileUploadPanel().addButtonUploadStartedEventProcessor(processor);
    }

    private void activateDragAndDropWrapper() {
        getFileUploadPanel().addDragAndDropHandler(new DropHandler() {
            private static final long serialVersionUID = 607722003300263265L;

            @Override
            public AcceptCriterion getAcceptCriterion() {
                return AcceptAll.get();
            }

            @Override
            public void drop(DragAndDropEvent event) {
                Html5File files[] = ((DragAndDropWrapper.WrapperTransferable) event.getTransferable()).getFiles();
                if (files != null) {
                    for (final Html5File file : files) {
                        if (fileNameValidator.validate(file.getFileName())) {

                            UploadTask task = new UploadTaskDnD(settingsAppService.getTemporaryUploadDirPath(), getCurrentPath(), settingsAppService.getUploadBufferSize());

                            task.setFileName(file.getFileName());
                            task.setTmpFileExt(settingsAppService.getTemporaryFileExt()); //set tmp file ext
                            task.setFileSize(file.getFileSize());
                            task.setUploadObject(file);

                            try {
                                if (checkVideoFileOnExist(getCurrentPath(), task.getFileName()) || fileSystemManagerService.exist(new FileSystemObject(getCurrentPath() + task.getFileName(), FileSystemObject.FileType.FILE, 0L, null))) {
                                    task.interrupt();
                                    ((InteractionUI) UI.getCurrent()).showNotification("", "File already downloaded on server", Notification.Type.ERROR_MESSAGE);
                                } else {
                                    getUploadTaskPanel().addUploadTask(task);
                                }
                            } catch (Exception ex) {
                                log.error("Unknown file upload error", ex);
                                ((InteractionUI) UI.getCurrent()).showNotification("", "Unknown file upload error", Notification.Type.ERROR_MESSAGE);
                            }
                        } else {
                            ((InteractionUI) UI.getCurrent()).showNotification("", "Invalid file name, skipped", Notification.Type.TRAY_NOTIFICATION);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onFinish(UploadTask task, FileHandlerArgs args) {

        //update grid then file is upload
        if (getCurrentPath().equals(task.getDestFilePath())) {
            BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
            if (container != null) {
                container.addBean(new FileExplorerItemData(FileExplorerItemData.FileType.File, args.getDstFileName(), task.getDestFilePath() + args.getDstFileName(), args.getLength(), args.getAgentFile(), new LocalDateTime()));
            }
        }
    }

    private void buildMenuBar() {

        FileExplorerItemData bean = null;
        BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();

        if (container != null && gridFileExplorer.getValue() != null) {
            bean = container.getItem(gridFileExplorer.getValue()).getBean();
        }

        if (gridFileExplorer.getValue() == null) {
            buildTableCommonMenuBarItems();
        } else {
            buildTableRowsMenuBarItems(bean);
        }
    }

    private void buildTableCommonMenuBarItems() {

        getMenuBar().removeItems();

        //Refresh
        getMenuBar().addItem("Refresh", new ThemeResource("images/grid_files_menu_item_refresh.png"), new MenuBar.Command() {
            private static final long serialVersionUID = 7160936162824727503L;

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                actionMenuRefreshFolder();
            }
        });

        //New folder
        getMenuBar().addItem("New folder", new ThemeResource("images/grid_files_menu_item_new_folder.png"), new MenuBar.Command() {
            private static final long serialVersionUID = 7160936162824727503L;

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                actionMenuNewFolder();
            }
        });

        //Apply
        getMenuBar().addItem("Apply", new ThemeResource("images/grid_files_menu_item_cfg_apply.png"), new MenuBar.Command() {
            private static final long serialVersionUID = 7160936162824727503L;

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                actionMenuApplyConfig();
            }
        });

        //Sequence
        getMenuBar().addItem("Sequence", new ThemeResource("images/grid_files_menu_item_file_sequence.png"), new MenuBar.Command() {
            private static final long serialVersionUID = 7160936162824727503L;

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                actionMenuFileSequence();
            }
        });
    }

    private void buildTableRowsMenuBarItems(FileExplorerItemData bean) {

        getMenuBar().removeItems();

        //Refresh
        getMenuBar().addItem("Refresh", new ThemeResource("images/grid_files_menu_item_refresh.png"), new MenuBar.Command() {
            private static final long serialVersionUID = 7160936162824727503L;

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                actionMenuRefreshFolder();
            }
        });

        //New folder
        getMenuBar().addItem("New folder", new ThemeResource("images/grid_files_menu_item_new_folder.png"), new MenuBar.Command() {
            private static final long serialVersionUID = 7160936162824727503L;

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                actionMenuNewFolder();
            }
        });

        //Edit
        getMenuBar().addItem("Edit", new ThemeResource("images/grid_files_menu_item_edit.png"), new MenuBar.Command() {
            private static final long serialVersionUID = 7160936162824727503L;

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                actionMenuEditFileOrFolder();
            }
        });

        //Remove
        getMenuBar().addItem("Remove", new ThemeResource("images/grid_files_menu_item_remove.png"), new MenuBar.Command() {
            private static final long serialVersionUID = 7160936162824727503L;

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                actionMenuRemoveFileOrFolder();
            }
        });

        if (bean != null) {
            if (FileExplorerItemData.FileType.File.equals(bean.getFileType())) {
                //Download
                getMenuBar().addItem("Download", new ThemeResource("images/grid_files_menu_item_download_file.png"), new MenuBar.Command() {
                    private static final long serialVersionUID = 7160936162824727503L;

                    @Override
                    public void menuSelected(MenuBar.MenuItem selectedItem) {
                        actionMenuDownloadFile();
                    }
                });
            }
        }

        //Apply
        getMenuBar().addItem("Apply", new ThemeResource("images/grid_files_menu_item_cfg_apply.png"), new MenuBar.Command() {
            private static final long serialVersionUID = 7160936162824727503L;

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                actionMenuApplyConfig();
            }
        });

        //Sequence
        getMenuBar().addItem("Sequence", new ThemeResource("images/grid_files_menu_item_file_sequence.png"), new MenuBar.Command() {
            private static final long serialVersionUID = 7160936162824727503L;

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                actionMenuFileSequence();
            }
        });
    }

    private void initBreadCrumbs() {
        breadCrumbs.addCrumb("", new ThemeResource("images/bread_crumb_home.png"), getLocalConfigPath());
        breadCrumbs.addBreadCrumbSelectListener(new BreadCrumbs.BreadCrumbSelectListener() {
            private static final long serialVersionUID = 1291962818597591728L;

            @Override
            public void selected(int index) {
                try {
                    ((InteractionUI) UI.getCurrent()).showProgressBar();
                    setCurrentPath((String) breadCrumbs.getCrumbState(index));
                    fillGridFileExplorer(getCurrentPath());
                } catch (Exception ex) {
                    log.error("Bad refresh file explorer", ex);
                    ((InteractionUI) UI.getCurrent()).showNotification("", "Can't refresh file explorer", Notification.Type.ERROR_MESSAGE);
                } finally {
                    buildMenuBar();
                    ((InteractionUI) UI.getCurrent()).closeProgressBar();
                }
            }
        });
    }

    private void processGridDoubleClick(Object itemId) {
        BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
        if (container != null) {
            FileExplorerItemData bean = container.getItem(itemId).getBean();
            if (bean != null && FileExplorerItemData.FileType.Folder.equals(bean.getFileType())) {
                try {
                    ((InteractionUI) UI.getCurrent()).showProgressBar();
                    setCurrentPath(StringUtils.defaultIfBlank(bean.getPath(), ""));
                    fillGridFileExplorer(getCurrentPath());
                    breadCrumbs.addCrumb(bean.getName(), new ThemeResource("images/bread_crumb_folder.png"), bean.getPath());
                    breadCrumbs.selectCrumb(breadCrumbs.size() - 1, false);
                } catch (Exception ex) {
                    log.error("Bad refresh file explorer", ex);
                    ((InteractionUI) UI.getCurrent()).showNotification("", "Can't refresh file explorer", Notification.Type.ERROR_MESSAGE);
                } finally {
                    ((InteractionUI) UI.getCurrent()).closeProgressBar();
                }
            }
        }
    }

    private void initGridFileExplorerTable() {

        //set common
        gridFileExplorer.setImmediate(true);
        gridFileExplorer.setSelectable(true);
        gridFileExplorer.addStyleName("grid-file-explorer");
        gridFileExplorer.setContainerDataSource(new BeanItemContainer<>(FileExplorerItemData.class));

        //grid sorting
        ((BeanItemContainer) gridFileExplorer.getContainerDataSource()).setItemSorter(
                new DefaultItemSorter() {
                    private static final long serialVersionUID = 1790648675965489066L;

                    @Override
                    protected int compareProperty(Object propertyId, boolean sortDirection, Item item1, Item item2) {

                        FileExplorerItemData bean1 = ((BeanItem<FileExplorerItemData>) item1).getBean();
                        FileExplorerItemData bean2 = ((BeanItem<FileExplorerItemData>) item2).getBean();

                        int result = 0;

                        if (bean1.getFileType() != null && bean2.getFileType() != null) {
                            //folder+folder, file+file
                            if ((FileExplorerItemData.FileType.Folder.equals(bean1.getFileType()) && bean1.getFileType().equals(bean2.getFileType()))
                            || FileExplorerItemData.FileType.File.equals(bean1.getFileType()) && bean1.getFileType().equals(bean2.getFileType())) {
                                if (propertyId.equals("name")) {
                                    result = StringUtils.defaultIfBlank(bean1.getName(), "").compareTo(StringUtils.defaultIfBlank(bean2.getName(), ""));
                                } else if (propertyId.equals("size")) {
                                    //folder
                                    if (FileExplorerItemData.FileType.Folder.equals(bean1.getFileType())) {
                                        result = StringUtils.defaultIfBlank(bean1.getName(), "").compareTo(StringUtils.defaultIfBlank(bean2.getName(), ""));
                                    } else { //file
                                        result = Long.compare(bean1.getSize(), bean2.getSize());
                                    }
                                } else if (propertyId.equals("lastModifiedTime")) {
                                    result = bean1.getLastModifiedTime().compareTo(bean2.getLastModifiedTime());
                                }
                            } //folder+file
                            else if (FileExplorerItemData.FileType.Folder.equals(bean1.getFileType()) && !bean1.getFileType().equals(bean2.getFileType())) {
                                result = -1;
                            } //file+foder
                            else if (FileExplorerItemData.FileType.File.equals(bean1.getFileType()) && !bean1.getFileType().equals(bean2.getFileType())) {
                                result = 1;
                            }
                        } else {
                            result = super.compareProperty(propertyId, sortDirection, item1, item2);
                        }

                        return sortDirection ? result : -1 * result;
                    }

                    @Override
                    public int compare(Object o1, Object o2) {
                        return super.compare(o1, o2);
                    }
                });

        //set menu
        gridContextMenu.setAsContextMenuOf(gridFileExplorer);

        gridContextMenu.addContextMenuTableListener(
                this);
        gridContextMenu.addContextMenuComponentListener(
                this);

        //set double click event handler
        gridFileExplorer.addItemClickListener(
                new ItemClickEvent.ItemClickListener() {
                    private static final long serialVersionUID = -2318797984292753676L;

                    @Override
                    public void itemClick(ItemClickEvent event) {
                        if (event.isDoubleClick()) {
                            processGridDoubleClick(event.getItemId());
                        }
                    }
                });

        //set to detect row select change
        gridFileExplorer.addValueChangeListener(
                new Property.ValueChangeListener() {
                    private static final long serialVersionUID = -382717228031608542L;

                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        Object itemId = gridFileExplorer.getValue();
                        if (itemId != null) {
                            BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
                            if (container != null) {
                                FileExplorerItemData bean = container.getItem(itemId).getBean();
                                if (FileExplorerItemData.FileType.File.equals(bean.getFileType())) {
                                    panelFileProperty.showProperty(getLocalConfigPath(), bean.getPath(), bean.getName(), new DbAgentFileDeepCopyClonable().clone((DbAgentFile) bean.getProperty()));
                                } else {
                                    panelFileProperty.clearProperty();
                                }
                            }
                        }

                        buildMenuBar();
                    }
                });

        //set column name render
        gridFileExplorer.addGeneratedColumn("name", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 2855441121974230973L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                FileExplorerItemData bean = ((BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource()).getItem(itemId).getBean();
                if (bean != null) {

                    Label label = new Label();

                    String fileName = "folder.png";
                    String template = "<div><img src=\"%s\" style=\"vertical-align:middle;\"><span style=\"padding-left:5px;\">%s</span></div>";
                    String srcImagePath = settingsAppService.getContextPath() + "/VAADIN/themes/" + UI.getCurrent().getTheme() + "/images/";

                    label.setContentMode(ContentMode.HTML);
                    label.addStyleName("grid-file-explorer-row-label-common");

                    if (FileExplorerItemData.FileType.Folder.equals(bean.getFileType())) {
                        label.addStyleName("grid-file-explorer-row-label-folder");
                        fileName = "folder.png";
                    } else if (FileExplorerItemData.FileType.File.equals(bean.getFileType())) {
                        label.addStyleName("grid-file-explorer-row-label-file");
                        if (bean.getProperty() != null && bean.getProperty().getId() != null) {
                            if (bean.getProperty().getKind() != null) {
                                fileName = "file_" + bean.getProperty().getKind().name().toLowerCase() + ".png";
                            } else {
                                fileName = "file_" + DbFileType.Unknown.name().toLowerCase() + ".png";
                            }
                        } else {
                            fileName = "file.png";
                        }
                    }

                    label.setValue(String.format(template, srcImagePath + fileName, bean.getName()));
                    return label;
                }

                return "";
            }
        });

        //set column size render
        gridFileExplorer.addGeneratedColumn("size", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 2855441121974230973L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                FileExplorerItemData bean = ((BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource()).getItem(itemId).getBean();
                if (bean != null) {

                    Label label = new Label();

                    label.setContentMode(ContentMode.HTML);
                    label.addStyleName("grid-file-explorer-row-label-common");

                    if (FileExplorerItemData.FileType.Folder.equals(bean.getFileType())) {
                        label.addStyleName("grid-file-explorer-row-label-folder");
                    } else if (FileExplorerItemData.FileType.File.equals(bean.getFileType())) {
                        label.addStyleName("grid-file-explorer-row-label-file");
                    }

                    if (FileExplorerItemData.FileType.File.equals(bean.getFileType())) {
                        label.setValue(UIUtils.formatFileSize(bean.getSize()));
                    }

                    return label;
                }

                return "";
            }
        });

        //set column date render
        gridFileExplorer.addGeneratedColumn("lastModifiedTime", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 2855441121974230973L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                FileExplorerItemData bean = ((BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource()).getItem(itemId).getBean();
                if (bean != null) {

                    Label label = new Label();

                    label.setContentMode(ContentMode.HTML);
                    label.addStyleName("grid-file-explorer-row-label-common");

                    if (FileExplorerItemData.FileType.Folder.equals(bean.getFileType())) {
                        label.addStyleName("grid-file-explorer-row-label-folder");
                    } else if (FileExplorerItemData.FileType.File.equals(bean.getFileType())) {
                        label.addStyleName("grid-file-explorer-row-label-file");
                    }

                    label.setValue(bean.getLastModifiedTime().toString("dd.MM.yyyy HH:mm:ss"));

                    return label;
                }

                return "";
            }
        });

        //set column view
        gridFileExplorer.setColumnHeader("name", "File name");
        gridFileExplorer.setColumnHeader("size", "File size");
        gridFileExplorer.setColumnHeader("lastModifiedTime", "File last modified time");

        gridFileExplorer.setColumnAlignment("name", Table.Align.LEFT);
        gridFileExplorer.setColumnAlignment("size", Table.Align.RIGHT);
        gridFileExplorer.setColumnAlignment("lastModifiedTime", Table.Align.CENTER);
        
        gridFileExplorer.setColumnExpandRatio("name", 0.75F);
        gridFileExplorer.setColumnExpandRatio("size", 0.1F);
        gridFileExplorer.setColumnExpandRatio("lastModifiedTime", 0.15F);

        gridFileExplorer.setVisibleColumns("name", "size", "lastModifiedTime");
        gridFileExplorer.sort(new Object[]{"name"}, new boolean[]{true});
    }

    private void initContextMenuTableRow(ContextMenu menu, Object data) {
        if (menu != null) {

            ContextMenu.ContextMenuItem tmp;
            FileExplorerItemData bean = null;
            BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();

            if (container != null && gridFileExplorer.getValue() != null) {
                bean = container.getItem(gridFileExplorer.getValue()).getBean();
            }

            menu.removeAllItems();

            tmp = menu.addItem("Refresh", new ThemeResource("images/grid_files_menu_item_refresh.png"));
            tmp.setSeparatorVisible(true);
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.ROW_REFRESH_FOLDER, data));
            tmp.addItemClickListener(this);

            tmp = menu.addItem("New folder", new ThemeResource("images/grid_files_menu_item_new_folder.png"));
            tmp.setSeparatorVisible(true);
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.ROW_NEW_FOLDER, data));
            tmp.addItemClickListener(this);

            tmp = menu.addItem("Edit", new ThemeResource("images/grid_files_menu_item_edit.png"));
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.ROW_EDIT, data));
            tmp.addItemClickListener(this);

            tmp = menu.addItem("Remove", new ThemeResource("images/grid_files_menu_item_remove.png"));
            tmp.setSeparatorVisible(true);
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.ROW_REMOVE, data));
            tmp.addItemClickListener(this);

            if (bean != null) {
                if (FileExplorerItemData.FileType.File.equals(bean.getFileType())) {
                    tmp = menu.addItem("Download", new ThemeResource("images/grid_files_menu_item_download_file.png"));
                    tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.ROW_DOWNLOAD_FILE, data));
                    tmp.addItemClickListener(this);
                }
            }

            tmp = menu.addItem("Apply", new ThemeResource("images/grid_files_menu_item_cfg_apply.png"));
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.ROW_CFG_APPLY, data));
            tmp.addItemClickListener(this);

            tmp = menu.addItem("Sequence", new ThemeResource("images/grid_files_menu_item_file_sequence.png"));
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.ROW_FILE_SEQUENCE, data));
            tmp.addItemClickListener(this);
        }
    }

    private void initContextMenuTable(ContextMenu menu) {
        if (menu != null) {
            ContextMenu.ContextMenuItem tmp;

            menu.removeAllItems();

            tmp = menu.addItem("Refresh", new ThemeResource("images/grid_files_menu_item_refresh.png"));
            tmp.setSeparatorVisible(true);
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.TABLE_REFRESH_FOLDER, null));
            tmp.addItemClickListener(this);

            tmp = menu.addItem("New folder", new ThemeResource("images/grid_files_menu_item_new_folder.png"));
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.TABLE_NEW_FOLDER, null));
            tmp.setSeparatorVisible(true);
            tmp.addItemClickListener(this);

            tmp = menu.addItem("Apply", new ThemeResource("images/grid_files_menu_item_cfg_apply.png"));
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.TABLE_CFG_APPLY, null));
            tmp.addItemClickListener(this);

            tmp = menu.addItem("Sequence", new ThemeResource("images/grid_files_menu_item_file_sequence.png"));
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.TABLE_FILE_SEQUENCE, null));
            tmp.addItemClickListener(this);
        }
    }

    private void initContextMenuTableHeader(ContextMenu menu) {
        if (menu != null) {
            menu.removeAllItems();
        }
    }

    private void initContextMenuTableFooter(ContextMenu menu) {
        if (menu != null) {
            menu.removeAllItems();
        }
    }

    @Override
    public void onContextMenuOpenFromRow(ContextMenu.ContextMenuOpenedOnTableRowEvent event) {
        initContextMenuTableRow(event.getContextMenu(), event.getItemId());
    }

    @Override
    public void onContextMenuOpenFromHeader(ContextMenu.ContextMenuOpenedOnTableHeaderEvent event) {
        initContextMenuTableHeader(event.getContextMenu());
    }

    @Override
    public void onContextMenuOpenFromFooter(ContextMenu.ContextMenuOpenedOnTableFooterEvent event) {
        initContextMenuTableFooter(event.getContextMenu());
    }

    @Override
    public void onContextMenuOpenFromComponent(ContextMenu.ContextMenuOpenedOnComponentEvent event) {
        initContextMenuTable(event.getContextMenu());
        event.getContextMenu().open(event.getX(), event.getY());
    }

    private void fillGridFileExplorer(String path) throws Exception {

        gridFileExplorer.setValue(null);

        BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
        if (container != null) {

            container.removeAllItems();

            //select all file objects
            List<FileSystemObject> listAll = fileSystemManagerService.list(new FileSystemObject(path, FileSystemObject.FileType.FOLDER, 0L, null), true, false);

            //select only files
            List<FileSystemObject> listFiles = FluentIterable.from(listAll).filter(new Predicate<FileSystemObject>() {
                @Override
                public boolean apply(FileSystemObject file) {
                    return FileSystemObject.FileType.FILE.equals(file.getFileType());
                }
            }).toList();

            //select file poperties
            Set<DbAgentFile> properties = agentFileService.findAllByName(FluentIterable.from(listFiles).transform(new Function<FileSystemObject, String>() {
                @Override
                public String apply(FileSystemObject obj) {
                    return StringUtils.substring(obj.getPath(), getLocalConfigPath().length());
                }
            }).toSet());

            //fill grid
            for (final FileSystemObject f : listAll) {

                //skip tmp files
                if (FileSystemObject.FileType.FILE.equals(f.getFileType()) && StringUtils.right(f.getPath(), settingsAppService.getTemporaryFileExt().length()).equals(settingsAppService.getTemporaryFileExt())) {
                    continue;
                }

                //find property for file
                DbAgentFile property = Iterables.find(properties, new Predicate<DbAgentFile>() {
                    @Override
                    public boolean apply(DbAgentFile file) {
                        return file.getName().equals(StringUtils.substring(f.getPath(), getLocalConfigPath().length()));
                    }
                }, new DbAgentFile("", null, null, "", "", false, 0));

                container.addBean(new FileExplorerItemData(
                        FileSystemObject.FileType.FOLDER.equals(f.getFileType()) ? FileExplorerItemData.FileType.Folder : FileExplorerItemData.FileType.File,
                        f.getName(), //name
                        f.getPath(),
                        f.getSize(),
                        property,
                        f.getLastModifiedTime()
                ));
            }

            //sort grid
            gridFileExplorer.sort();
        }
    }

    private void createNewFolder(final String path) {
        new TextEditDialogWindow("Create new folder", "New folder", new TextEditDialogWindow.TextEditDialogWindowEvent() {
            @Override
            public boolean onOk(String text) {
                boolean isOk = false;
                if (fileNameValidator.validate(text)) {
                    try {
                        ((InteractionUI) UI.getCurrent()).showProgressBar();

                        FileSystemObject fo = new FileSystemObject(path + text, FileSystemObject.FileType.FOLDER, 0L, null);

                        //create new folder
                        if (!fileSystemManagerService.exist(fo)) {
                            fileSystemManagerService.create(fo);
                        } else {
                            throw new Exception("Folder already exist");
                        }

                        BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
                        if (container != null) {
                            container.addBean(new FileExplorerItemData(FileExplorerItemData.FileType.Folder, text, Helpers.addEndSeparator(getCurrentPath()) + text, 0L, new DbAgentFile("", null, null, "", "", false, 0), new LocalDateTime()));
                            gridFileExplorer.sort();
                        }
                        isOk = true;
                    } catch (Exception ex) {
                        log.error("Error create new folder", ex);
                        ((InteractionUI) UI.getCurrent()).showNotification("", "Can't create new folder", Notification.Type.ERROR_MESSAGE);
                    } finally {
                        ((InteractionUI) UI.getCurrent()).closeProgressBar();
                    }
                } else {
                    ((InteractionUI) UI.getCurrent()).showNotification("", "Invalid folder name", Notification.Type.WARNING_MESSAGE);
                }
                return isOk;
            }

            @Override
            public boolean onCancel() {
                return true;
            }
        }).show();
    }

    private void renameFileOrFolder(final Object selectedItemId) {

        final BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
        if (container == null) {
            return;
        }

        final BeanItem<FileExplorerItemData> item = container.getItem(selectedItemId);
        if (item == null || item.getBean() == null) {
            return;
        }

        new TextEditDialogWindow("Rename", item.getBean().getName(), new TextEditDialogWindow.TextEditDialogWindowEvent() {
            @Override
            public boolean onOk(String text) {

                boolean isOk = false;
                if (fileNameValidator.validate(text)) {
                    try {

                        String pathNew;
                        FileSystemObject foOld;
                        FileSystemObject foNew;

                        pathNew = Helpers.addEndSeparator(StringUtils.substringBeforeLast(item.getBean().getPath(), "/")) + text;

                        if (!StringUtils.contains(pathNew, getLocalConfigPath())) {
                            log.error("Bad rename, not equal root file path  root = [{}], value = [{}]", pathNew, getLocalConfigPath());
                            ((InteractionUI) UI.getCurrent()).showNotification("", "Can't rename file/folder, not equal root file path", Notification.Type.ERROR_MESSAGE);
                            return isOk;
                        }

                        foOld = new FileSystemObject(item.getBean().getPath(), FileExplorerItemData.FileType.File.equals(item.getBean().getFileType()) ? FileSystemObject.FileType.FILE : FileSystemObject.FileType.FOLDER, 0L, null);
                        foNew = new FileSystemObject(pathNew, FileExplorerItemData.FileType.File.equals(item.getBean().getFileType()) ? FileSystemObject.FileType.FILE : FileSystemObject.FileType.FOLDER, 0L, null);

                        ((InteractionUI) UI.getCurrent()).showProgressBar();

                        //refresh properties & files/folders
                        agentFileService.updateByNamePrefix(StringUtils.substringAfter(item.getBean().getPath(), getLocalConfigPath()), StringUtils.substringAfter(pathNew, getLocalConfigPath()), foOld, foNew);

                        //upd grid item
                        item.getBean().setName(text);
                        item.getBean().setPath(pathNew);

                        //Refresh property panel after rename
                        if (FileExplorerItemData.FileType.File.equals(item.getBean().getFileType())) {
                            item.getBean().getProperty().setName(StringUtils.substringAfter(pathNew, getLocalConfigPath()));
                            panelFileProperty.refreshProperty(getLocalConfigPath(), item.getBean().getPath(), item.getBean().getName(), StringUtils.substringAfter(pathNew, getLocalConfigPath()));
                        }

                        //#hack to update change data model value
                        gridFileExplorer.refreshRowCache();
                        isOk = true;

                    } catch (Exception ex) {
                        log.error("Bad rename file/folder", ex);
                        ((InteractionUI) UI.getCurrent()).showNotification("", "Can't rename file/folder", Notification.Type.ERROR_MESSAGE);
                    } finally {
                        ((InteractionUI) UI.getCurrent()).closeProgressBar();
                    }
                } else {
                    ((InteractionUI) UI.getCurrent()).showNotification("", "Enter correct file/folder name", Notification.Type.WARNING_MESSAGE);
                }

                return isOk;
            }

            @Override
            public boolean onCancel() {
                return true;
            }
        }).show();
    }

    private void removeFileOrFolder(final Object selectedItemId) {

        try {
            ((InteractionUI) UI.getCurrent()).showProgressBar();

            BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
            if (container != null) {
                FileExplorerItemData bean = container.getItem(selectedItemId).getBean();
                if (bean != null) {
                    FileSystemObject fo = new FileSystemObject(bean.getPath(), FileExplorerItemData.FileType.File.equals(bean.getFileType()) ? FileSystemObject.FileType.FILE : FileSystemObject.FileType.FOLDER, 0L, null);

                    //remove properties & remove files/folders from file system
                    agentFileService.deleteByNamePrefix(StringUtils.substringAfter(bean.getPath(), getLocalConfigPath()), fo);

                    //upd grid
                    container.removeItem(selectedItemId);

                    //refresh property panel
                    panelFileProperty.clearProperty();
                }
            }
        } catch (Exception ex) {
            log.error("Bad remove file/folder", ex);
            ((InteractionUI) UI.getCurrent()).showNotification("", "Can't remove file/folder", Notification.Type.ERROR_MESSAGE);
        } finally {
            ((InteractionUI) UI.getCurrent()).closeProgressBar();
        }
    }

    private void actionMenuRefreshFolder() {

        try {
            ((InteractionUI) UI.getCurrent()).showProgressBar();
            panelFileProperty.clearProperty();
            fillGridFileExplorer(getCurrentPath());
        } catch (Exception ex) {
            log.error("Bad refresh file explorer", ex);
            ((InteractionUI) UI.getCurrent()).showNotification("", "Can't refresh file explore", Notification.Type.ERROR_MESSAGE);
        } finally {
            buildMenuBar();
            ((InteractionUI) UI.getCurrent()).closeProgressBar();
        }
    }

    private void actionMenuNewFolder() {
        createNewFolder(getCurrentPath());
    }

    private void actionMenuEditFileOrFolder() {

        Object itemId = gridFileExplorer.getValue();
        if (itemId != null) {
            renameFileOrFolder(gridFileExplorer.getValue());
        } else {
            ((InteractionUI) UI.getCurrent()).showNotification("", "Choose object to rename", Notification.Type.ERROR_MESSAGE);
        }
    }

    private void actionMenuRemoveFileOrFolder() {

        String caption = "Delete %s";
        String title = "Are you sure want to delete %s?";
        FileExplorerItemData.FileType kind = getGridFileExplorerFileObjectType();
        String arg = (kind == null) ? "?" : kind.getName();

        ((InteractionUI) UI.getCurrent()).showMessageBox(String.format(caption, arg), String.format(title, arg), Icon.INFO,
                new MessageBoxListener() {
                    @Override
                    public void buttonClicked(ButtonId buttonId) {
                        if (buttonId.equals(ButtonId.YES)) {

                            Object itemId = gridFileExplorer.getValue();
                            if (itemId != null) {
                                removeFileOrFolder(gridFileExplorer.getValue());
                            } else {
                                ((InteractionUI) UI.getCurrent()).showNotification("", "Choose object to remove", Notification.Type.ERROR_MESSAGE);
                            }
                        }
                    }
                },
                ButtonId.YES,
                ButtonId.NO
        );
    }

    private FileExplorerItemData.FileType getGridFileExplorerFileObjectType() {

        Object itemId = gridFileExplorer.getValue();
        if (itemId != null) {
            BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
            if (container != null) {
                FileExplorerItemData bean = container.getItem(itemId).getBean();
                if (bean != null) {
                    return bean.getFileType();
                }
            }
        }

        return null;
    }

    private void actionMenuApplyConfig() {

        ((InteractionUI) UI.getCurrent()).showMessageBox("Applying configuration", "Are you sure want to apply the configuration?", Icon.INFO,
                new MessageBoxListener() {
                    @Override
                    public void buttonClicked(ButtonId buttonId) {

                        if (buttonId.equals(ButtonId.OK)) {
                            if (StringUtils.isBlank(settingsAppService.getServerConfigPath())) {
                                log.error("Bad application settings (empty), cannot apply configuration");
                                ((InteractionUI) UI.getCurrent()).showNotification("", "Empty application settings, cannot apply configuration", Notification.Type.ERROR_MESSAGE);
                                return;
                            }

                            FileSystemObject serverPath = new FileSystemObject(fileSystemManagerService.canonicalization(settingsAppService.getServerConfigPath()), FileSystemObject.FileType.FOLDER, 0L, null);
                            FileSystemObject localPath = new FileSystemObject(Helpers.addEndSeparator(fileSystemManagerService.canonicalization(settingsAppService.getLocalConfigPath())), FileSystemObject.FileType.FOLDER, 0L, null);

                            configurationApplyService.apply(UI.getCurrent(), userAppService.getUser().getLogin(), localPath, serverPath, new ApplyConfigRunCallback() {
                                @Override
                                public void success() {
                                    UI.getCurrent().access(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((InteractionUI) UI.getCurrent()).showNotification("", "Configuration applying started", Notification.Type.TRAY_NOTIFICATION);
                                        }
                                    });
                                }

                                @Override
                                public void fail() {
                                    UI.getCurrent().access(new Runnable() {
                                        @Override
                                        public void run() {
                                            log.error("Bad applying server configuration (already applying)");
                                            ((InteractionUI) UI.getCurrent()).showNotification("", "Configuration already applying", Notification.Type.ERROR_MESSAGE);
                                        }
                                    });
                                }
                            });
                        }
                    }
                },
                ButtonId.OK,
                ButtonId.CANCEL
        );
    }

    private void actionMenuFileSequence() {

        new WindowBuilder()
                .withCaption("File sequence")
                .withContent((FileSequenceView) applicationContext.getBean(FileSequenceView.FILE_SEQUENCE_VIEW_ID, new FileSequenceView.ActionCallBack() {

                    @Override
                    public void save(Button.ClickEvent event, com.vaadin.ui.Component component) {
                        ((Window) component.getParent()).close();
                    }

                    @Override
                    public void refresh(List<DbAgentFile> files) {

                        BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
                        for (FileExplorerItemData data : container.getItemIds()) {
                            if (data.getProperty() != null && FileExplorerItemData.FileType.File.equals(data.getFileType())) {
                                for (DbAgentFile file : files) {
                                    if (file.getId().equals(data.getProperty().getId())) {
                                        data.getProperty().setSeqNo(file.getSeqNo());
                                        break;
                                    }
                                }
                            }
                        }

                        FileExplorerItemData data = (FileExplorerItemData) gridFileExplorer.getValue();
                        if (data != null && data.getProperty() != null && FileExplorerItemData.FileType.File.equals(data.getFileType())) {
                            panelFileProperty.refreshSeqNo(data.getProperty().getSeqNo());
                        }
                    }

                    @Override
                    public void cancel(Button.ClickEvent event, com.vaadin.ui.Component component) {
                        ((Window) component.getParent()).close();
                    }
                }))
                .withModal()
                .withClosable()
                .withSizeUndefined()
                .buildAndShow();
    }

    private void actionMenuDownloadFile() {

        Object itemId = gridFileExplorer.getValue();
        if (itemId != null) {
            BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
            if (container != null) {
                final FileExplorerItemData bean = container.getItem(itemId).getBean();
                if (bean != null) {

                    List<Extension> extentions = new ArrayList<>(btnDownloadFile.getExtensions());
                    if (!extentions.isEmpty()) {
                        btnDownloadFile.removeExtension(new ArrayList<>(btnDownloadFile.getExtensions()).get(0));
                    }

                    new FileDownloader(new StreamResource(
                            new StreamResource.StreamSource() {
                                private static final long serialVersionUID = -2480723276190894707L;

                                @Override
                                public InputStream getStream() {
                                    try {
                                        return fileSystemManagerService.getInputStream(new FileSystemObject(bean.getPath(), FileSystemObject.FileType.FILE, 0L, null));
                                    } catch (Exception ex) {
                                        log.error("Bad file download - {}", ex);
                                    }
                                    return null;
                                }
                            },
                            bean.getName()
                    )).extend(btnDownloadFile);

                    //#hack to download file with button
                    Page.getCurrent().getJavaScript().execute("document.getElementById('btnDownloadFile').click();");
                }
            }
        }
    }

    @Override
    public void contextMenuItemClicked(ContextMenu.ContextMenuItemClickEvent event) {

        ContextMenu.ContextMenuItem item = (ContextMenu.ContextMenuItem) event.getSource();
        if (item != null) {
            ContextMenuItemData data = (ContextMenuItemData) item.getData();
            if (data != null) {
                if (ContextMenuItemData.MenuAction.TABLE_REFRESH_FOLDER.equals(data.getAction())) {
                    actionMenuRefreshFolder();
                } else if (ContextMenuItemData.MenuAction.ROW_REFRESH_FOLDER.equals(data.getAction())) {
                    actionMenuRefreshFolder();
                } else if (ContextMenuItemData.MenuAction.ROW_NEW_FOLDER.equals(data.getAction()) || ContextMenuItemData.MenuAction.TABLE_NEW_FOLDER.equals(data.getAction())) {
                    actionMenuNewFolder();
                } else if (ContextMenuItemData.MenuAction.ROW_EDIT.equals(data.getAction())) {
                    actionMenuEditFileOrFolder();
                } else if (ContextMenuItemData.MenuAction.ROW_REMOVE.equals(data.getAction())) {
                    actionMenuRemoveFileOrFolder();
                } else if (ContextMenuItemData.MenuAction.ROW_DOWNLOAD_FILE.equals(data.getAction())) {
                    actionMenuDownloadFile();
                } else if (ContextMenuItemData.MenuAction.TABLE_CFG_APPLY.equals(data.getAction()) || ContextMenuItemData.MenuAction.ROW_CFG_APPLY.equals(data.getAction())) {

                    ((InteractionUI) UI.getCurrent()).showMessageBox("Applying configuration", "Are you sure want to apply the configuration?", Icon.INFO,
                            new MessageBoxListener() {
                                @Override
                                public void buttonClicked(ButtonId buttonId) {
                                    if (buttonId.equals(ButtonId.OK)) {
                                        actionMenuApplyConfig();
                                    }
                                }
                            },
                            ButtonId.OK,
                            ButtonId.CANCEL
                    );
                } else if (ContextMenuItemData.MenuAction.ROW_FILE_SEQUENCE.equals(data.getAction()) || ContextMenuItemData.MenuAction.TABLE_FILE_SEQUENCE.equals(data.getAction())) {
                    actionMenuFileSequence();
                }
            }
        }
    }

    private void initTabSheetProperty() {

        panelFileProperty.setUp(getAgentFileService(), getAgentFileOwnerService(), getFileSystemManagerService());
        panelFileProperty.setUpOwnerBeanContainer();
        panelFileProperty.setUpFileTypeBeanContainer();

        //event on property success save
        panelFileProperty.setListener(new FilePropertyPanel.PropertySaveListener() {
            @Override
            public void onSave(DbAgentFile file) {
                Object selectedItemId = gridFileExplorer.getValue();
                BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
                if (container != null) {
                    FileExplorerItemData bean = container.getItem(selectedItemId).getBean();
                    if (bean != null) {
                        bean.setProperty(new DbAgentFileDeepCopyClonable().clone((DbAgentFile) file));

                        //#hack to update change data model value
                        gridFileExplorer.refreshRowCache();
                    }
                }
            }
        });
    }
}
