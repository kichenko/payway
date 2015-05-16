/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.google.gwt.thirdparty.guava.common.base.Function;
import com.google.gwt.thirdparty.guava.common.base.Predicate;
import com.google.gwt.thirdparty.guava.common.collect.FluentIterable;
import com.google.gwt.thirdparty.guava.common.collect.Iterables;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.payway.advertising.core.service.DbAgentFileOwnerService;
import com.payway.advertising.core.service.DbAgentFileService;
import com.payway.advertising.core.service.file.FileSystemManagerService;
import com.payway.advertising.core.service.file.FileSystemManagerServiceSecurity;
import com.payway.advertising.core.service.file.FileSystemObject;
import com.payway.advertising.core.service.user.UserService;
import com.payway.advertising.core.service.utils.SettingsService;
import com.payway.advertising.core.validator.Validator;
import com.payway.advertising.model.DbAgentFile;
import com.payway.advertising.model.DbFileType;
import com.payway.advertising.ui.component.BreadCrumbs;
import com.payway.advertising.ui.component.TextEditDialogWindow;
import com.payway.advertising.ui.utils.UIUtils;
import com.payway.advertising.ui.view.core.AbstractView;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Html5File;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.vaadin.peter.contextmenu.ContextMenu;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * ContentConfigurationView
 *
 * @author Sergey Kichenko
 * @created 22.04.15 00:00
 */
@Slf4j
@UIScope
@Component(value = "content-configuration")
public class ContentConfigurationView extends AbstractView implements UploadListener, ContextMenu.ContextMenuItemClickListener, ContextMenu.ContextMenuOpenedListener.TableListener, ContextMenu.ContextMenuOpenedListener.ComponentListener {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContextMenuItemData {

        public enum MenuAction {

            ROW_REFRESH_FOLDER,
            ROW_NEW_FOLDER,
            ROW_EDIT,
            ROW_REMOVE,
            TABLE_NEW_FOLDER,
            TABLE_REFRESH_FOLDER
        }

        private MenuAction action;
        private Object data;
    }

    @UiField
    private Table gridFileExplorer;

    @UiField
    private FilePropertyPanel panelFileProperty;

    @UiField
    private Button btnFileUpload;

    @UiField
    private BreadCrumbs breadCrumbs;

    @UiField
    private CssLayout layoutFileUpload;

    @UiField
    private HorizontalSplitPanel splitPanel;

    @UiField
    private VerticalLayout layoutDragDropPanel;

    private ContextMenu gridContextMenu = new ContextMenu();

    private UploadTaskWindow uploadTaskWindow = new UploadTaskWindow();

    @Getter
    @Setter
    @Autowired
    @Qualifier("localFileManagerService")
    private FileSystemManagerService fileSystemManagerService;

    @Getter
    @Setter
    @Autowired
    @Qualifier("fileSystemManagerServiceSecurity")
    private FileSystemManagerServiceSecurity fileSystemManagerServiceSecurity;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("settingsService")
    private SettingsService settingsService;

    @Autowired
    @Qualifier("fileNameValidator")
    private Validator fileNameValidator;

    @Getter
    @Setter
    @Autowired
    @Qualifier("dbAgentFileOwnerService")
    private DbAgentFileOwnerService dbAgentFileOwnerService;

    @Getter
    @Setter
    @Autowired
    @Qualifier("dbAgentFileService")
    private DbAgentFileService dbAgentFileService;

    @Getter
    private String rootUserConfigPath;

    @Getter
    @Setter
    private String currentRelativePath;

    private boolean isFileGridLoadedOnActivate = false;

    @PostConstruct
    public void postConstruct() {
        init();
    }

    private void init() {
        setSizeFull();
        addComponent(Clara.create("ContentConfigurationView.xml", this));

        splitPanel.setFirstComponent(gridFileExplorer);
        splitPanel.setSecondComponent(panelFileProperty);

        initRootUserConfigPath();
        initCurrentRelativePath();

        initGridFileExplorerTable();
        initFileUploadButton();
        initDragAndDropWrapper();
        initBreadCrumbs();
        initTabSheetProperty();
    }

    private void initRootUserConfigPath() {
        rootUserConfigPath = settingsService.getLocalConfigPath() + userService.getUser().getLogin() + settingsService.getSeparator();
    }

    private void initCurrentRelativePath() {
        currentRelativePath = "";
    }

    private String getCurrentPath() {
        return StringUtils.isBlank(getCurrentRelativePath()) ? getRootUserConfigPath() : getRootUserConfigPath() + getCurrentRelativePath() + settingsService.getSeparator();
    }

    @Override
    public void activate() {
        if (!isFileGridLoadedOnActivate) {
            try {
                UIUtils.showLoadingIndicator();
                FileSystemObject fo = new FileSystemObject(getCurrentPath(), FileSystemObject.FileSystemObjectType.FOLDER, 0L, null, null, null);
                if (!fileSystemManagerService.exist(fo)) {
                    fileSystemManagerService.create(fo);
                }
                fillGridFileExplorer(getCurrentPath());
                isFileGridLoadedOnActivate = true;
            } catch (Exception ex) {
                log.error("Error on activate view", ex);
                UIUtils.showErrorNotification("", "Error loading file explorer on activate");
            } finally {
                UIUtils.closeLoadingIndicator();
            }
        }
    }

    private void initFileUploadButton() {
        btnFileUpload.setIcon(new ThemeResource("images/file_attach.png"));
        btnFileUpload.addStyleName("common-no-space-image-button");
        btnFileUpload.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UploadTaskFileInput task = new UploadTaskFileInput(getCurrentPath(), settingsService.getUploadBufferSize());
                task.setTmpFileExt(settingsService.getTemporaryFileExt()); //set tmp file ext
                task.addListener(ContentConfigurationView.this);
                new FileUploadWindow("Choose a file to upload", task,
                  new FileUploadWindow.FileUploadWindowEvent() {
                      @Override
                      public boolean onOk(UploadTaskFileInput uploadTask) {
                          boolean isOk = false;
                          if (StringUtils.isBlank(uploadTask.getFileName())) {
                              UIUtils.showErrorNotification("", "Please, select file to upload");
                          } else {
                              try {
                                  if (fileSystemManagerService.exist(new FileSystemObject(uploadTask.getPath() + uploadTask.getFileName(), FileSystemObject.FileSystemObjectType.FILE, 0L, null, null, null))) {
                                      UIUtils.showErrorNotification("", "File already downloaded on server");
                                  } else {
                                      uploadTaskWindow.addUploadTask(uploadTask);
                                      uploadTaskWindow.show();
                                      isOk = true;
                                  }
                              } catch (Exception ex) {
                                  log.error("Unknown file upload error", ex);
                                  UIUtils.showErrorNotification("", "Unknown file upload error");
                              }
                          }

                          return isOk;
                      }

                      @Override
                      public void onCancel() {
                          //
                      }
                  }
                ).show();
            }
        });
    }

    private void initDragAndDropWrapper() {
        DragAndDropWrapper wrapper = new DragAndDropWrapper(layoutDragDropPanel);
        wrapper.setDropHandler(new DropHandler() {
            @Override
            public AcceptCriterion getAcceptCriterion() {
                return AcceptAll.get();
            }

            @Override
            public void drop(DragAndDropEvent event) {
                Html5File files[] = ((DragAndDropWrapper.WrapperTransferable) event.getTransferable()).getFiles();
                if (files != null) {
                    for (final Html5File file : files) {
                        UploadTask task = new UploadTaskDnD(getCurrentPath(), settingsService.getUploadBufferSize());
                        task.addListener(ContentConfigurationView.this);
                        task.setFileName(file.getFileName());
                        task.setTmpFileExt(settingsService.getTemporaryFileExt()); //set tmp file ext
                        task.setFileSize(file.getFileSize());
                        task.setUploadObject(file);
                        try {
                            if (fileSystemManagerService.exist(new FileSystemObject(task.getPath() + task.getFileName(), FileSystemObject.FileSystemObjectType.FILE, 0L, null, null, null))) {
                                task.interrupt();
                                UIUtils.showErrorNotification("", "File already downloaded on server");
                            } else {
                                uploadTaskWindow.addUploadTask(task);
                                uploadTaskWindow.show();
                            }
                        } catch (Exception ex) {
                            log.error("Unknown file upload error", ex);
                            UIUtils.showErrorNotification("", "Unknown file upload error");
                        }
                    }
                }
            }
        });

        wrapper.setSizeFull();
        layoutFileUpload.addComponent(wrapper);
    }

    @Override
    public void updateProgress(UploadTask task, long readBytes, long contentLength) {
        //
    }

    @Override
    public void uploadFailed(UploadTask task, boolean isInterrupted) {
        //
    }

    @Override
    public void uploadSucceeded(UploadTask task) {
        //update grid then file is upload
        if (getCurrentPath().equals(task.getPath())) {
            BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
            if (container != null) {
                container.addBean(new FileExplorerItemData(FileExplorerItemData.FileType.Folder, task.getFileName(), task.getPath() + task.getFileName(), task.getFileSize(), new DbAgentFile("", null, null, "", "", false, null), null));
            }
        }
    }

    private void initBreadCrumbs() {
        breadCrumbs.addCrumb("", new ThemeResource("images/bread_crumb_home.png"), "");
        breadCrumbs.addBreadCrumbSelectListener(new BreadCrumbs.BreadCrumbSelectListener() {
            @Override
            public void selected(int index) {
                try {
                    UIUtils.showLoadingIndicator();
                    setCurrentRelativePath((String) breadCrumbs.getCrumbState(index));
                    fillGridFileExplorer(getCurrentPath());
                } catch (Exception ex) {
                    log.error("Error load file explorer", ex);
                    UIUtils.showErrorNotification("", "Error load file explorer");
                } finally {
                    UIUtils.closeLoadingIndicator();
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
                    UIUtils.showLoadingIndicator();
                    setCurrentRelativePath(StringUtils.defaultIfBlank(bean.getPath(), ""));
                    fillGridFileExplorer(getCurrentPath());
                    breadCrumbs.addCrumb(bean.getName(), new ThemeResource("images/bread_crumb_folder.png"), bean.getPath());
                    breadCrumbs.selectCrumb(breadCrumbs.size() - 1, false);
                } catch (Exception ex) {
                    log.error("Error load file explorer", ex);
                    UIUtils.showErrorNotification("", "Error load file explorer");
                } finally {
                    UIUtils.closeLoadingIndicator();
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

        //set menu
        gridContextMenu.setAsContextMenuOf(gridFileExplorer);
        gridContextMenu.addContextMenuTableListener(this);
        gridContextMenu.addContextMenuComponentListener(this);

        //set double click event handler
        gridFileExplorer.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    processGridDoubleClick(event.getItemId());
                }
            }
        });

        //set to detect row select change
        gridFileExplorer.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Object itemId = gridFileExplorer.getValue();
                if (itemId != null) {
                    BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
                    if (container != null) {
                        FileExplorerItemData bean = container.getItem(itemId).getBean();
                        if (FileExplorerItemData.FileType.File.equals(bean.getFileType())) {
                            panelFileProperty.showProperty(getRootUserConfigPath(), bean.getPath(), bean.getName(), new BeanItem<>((DbAgentFile) bean.getProperty().clone()));
                        } else {
                            panelFileProperty.clearProperty();
                        }
                    }
                }
            }
        });

        //set column name render
        gridFileExplorer.addGeneratedColumn("name", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                FileExplorerItemData bean = ((BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource()).getItem(itemId).getBean();
                if (bean != null) {

                    Label label = new Label();

                    String fileName = "folder.png";
                    String template = "<div><img src=\"%s\" style=\"vertical-align:middle;\"><span style=\"padding-left:5px;\">%s</span></div>";
                    String srcImagePath = VaadinService.getCurrentRequest().getContextPath() + "/VAADIN/themes/" + UI.getCurrent().getTheme() + "/images/";

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
        gridFileExplorer.addGeneratedColumn("last", new Table.ColumnGenerator() {
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
        gridFileExplorer.setColumnHeader("last", "File last modified time");

        gridFileExplorer.setVisibleColumns("name", "size", "last");
    }

    private void initContextMenuTableRow(ContextMenu menu, Object data) {
        if (menu != null) {
            ContextMenu.ContextMenuItem tmp;

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
        BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
        if (container != null) {

            container.removeAllItems();

            //select all file objects
            List<FileSystemObject> listAll = fileSystemManagerService.list(new FileSystemObject(path, FileSystemObject.FileSystemObjectType.FOLDER, null, null, null, null), false);

            //select only files
            List<FileSystemObject> listFiles = FluentIterable.from(listAll).filter(new Predicate<FileSystemObject>() {
                @Override
                public boolean apply(FileSystemObject file) {
                    return FileSystemObject.FileSystemObjectType.FILE.equals(file.getType());
                }
            }).toList();

            //select file poperties
            List<DbAgentFile> properties = dbAgentFileService.findAllByName(Lists.transform(listFiles, new Function<FileSystemObject, String>() {
                @Override
                public String apply(FileSystemObject obj) {
                    return StringUtils.substring(obj.getPath(), getRootUserConfigPath().length());
                }
            }));

            //fill grid
            for (final FileSystemObject f : listAll) {

                //skip tmp files
                if (FileSystemObject.FileSystemObjectType.FILE.equals(f.getType()) && StringUtils.right(f.getPath(), settingsService.getTemporaryFileExt().length()).equals(settingsService.getTemporaryFileExt())) {
                    continue;
                }

                //find property for file
                DbAgentFile property = Iterables.find(properties, new Predicate<DbAgentFile>() {
                    @Override
                    public boolean apply(DbAgentFile file) {
                        return file.getName().equals(StringUtils.substring(f.getPath(), getRootUserConfigPath().length()));
                    }
                }, new DbAgentFile("", null, null, "", "", false, userService.getConfiguration()));

                container.addBean(new FileExplorerItemData(
                  FileSystemObject.FileSystemObjectType.FOLDER.equals(f.getType()) ? FileExplorerItemData.FileType.Folder : FileExplorerItemData.FileType.File,
                  StringUtils.substringAfterLast(f.getPath(), settingsService.getSeparator()), //only name
                  StringUtils.substring(f.getPath(), getRootUserConfigPath().length()), //relative path
                  f.getSize(),
                  property,
                  f.getLastModifiedTime()
                ));
            }
        }
    }

    private void createNewFolder(final String path) {
        new TextEditDialogWindow("Create new folder", "New folder", new TextEditDialogWindow.TextEditDialogWindowEvent() {
            @Override
            public boolean onOk(String text) {
                boolean isOk = false;
                if (fileNameValidator.validate(text)) {
                    try {
                        UIUtils.showLoadingIndicator();

                        FileSystemObject fo = new FileSystemObject(path + text, FileSystemObject.FileSystemObjectType.FOLDER, 0L, null, null, null);

                        //create new folder
                        fileSystemManagerService.create(fo);

                        BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
                        if (container != null) {
                            container.addBean(new FileExplorerItemData(FileExplorerItemData.FileType.Folder, text, getCurrentRelativePath() + settingsService.getSeparator() + text, 0L, new DbAgentFile("", null, null, "", "", false, userService.getConfiguration()), null));
                        }
                        isOk = true;
                    } catch (Exception ex) {
                        log.error("Error create new folder", ex);
                        UIUtils.showErrorNotification("", "Error create new folder");
                    } finally {
                        UIUtils.closeLoadingIndicator();
                    }
                } else {
                    Notification.show("Please, enter correct folder name", Notification.Type.WARNING_MESSAGE);
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
        BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
        if (container != null) {
            final BeanItem<FileExplorerItemData> item = container.getItem(selectedItemId);
            if (item != null && item.getBean() != null) {
                new TextEditDialogWindow("Rename", item.getBean().getName(), new TextEditDialogWindow.TextEditDialogWindowEvent() {
                    @Override
                    public boolean onOk(String text) {
                        boolean isOk = false;
                        if (fileNameValidator.validate(text)) {
                            try {
                                String pathNew;
                                FileSystemObject foOld;
                                FileSystemObject foNew;

                                if (!StringUtils.contains(item.getBean().getPath(), settingsService.getSeparator())) {
                                    //root level
                                    pathNew = text;
                                } else {
                                    //child level
                                    pathNew = StringUtils.substringBeforeLast(item.getBean().getPath(), settingsService.getSeparator()) + settingsService.getSeparator() + text;
                                }

                                foOld = new FileSystemObject(getRootUserConfigPath() + item.getBean().getPath(), FileExplorerItemData.FileType.File.equals(item.getBean().getFileType()) ? FileSystemObject.FileSystemObjectType.FILE : FileSystemObject.FileSystemObjectType.FOLDER, 0L, null, null, null);
                                foNew = new FileSystemObject(getRootUserConfigPath() + pathNew, FileExplorerItemData.FileType.File.equals(item.getBean().getFileType()) ? FileSystemObject.FileSystemObjectType.FILE : FileSystemObject.FileSystemObjectType.FOLDER, 0L, null, null, null);

                                UIUtils.showLoadingIndicator();

                                //step-1: refresh properties
                                dbAgentFileService.updateByNamePrefix(item.getBean().getPath(), pathNew);

                                //step-2: refresh files/folders
                                fileSystemManagerService.rename(foOld, foNew);

                                //upd grid item
                                item.getBean().setName(text);
                                item.getBean().setPath(pathNew);

                                if (item.getBean().getProperty() != null) {
                                    item.getBean().getProperty().setName(pathNew);
                                    panelFileProperty.updateFileName(text);
                                }

                                //#hack to update change data model value
                                gridFileExplorer.refreshRowCache();
                                isOk = true;
                            } catch (Exception ex) {
                                log.error("Error rename", ex);
                                UIUtils.showErrorNotification("", "Error rename");
                            } finally {
                                UIUtils.closeLoadingIndicator();
                            }
                        } else {
                            Notification.show("Please, enter correct folder name", Notification.Type.WARNING_MESSAGE);
                        }
                        return isOk;
                    }

                    @Override
                    public boolean onCancel() {
                        return true;
                    }
                }).show();
            }
        }
    }

    private void removeFileOrFolder(final Object selectedItemId) {
        try {
            UIUtils.showLoadingIndicator();

            BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
            if (container != null) {
                FileExplorerItemData bean = container.getItem(selectedItemId).getBean();
                if (bean != null) {
                    FileSystemObject fo = new FileSystemObject(getRootUserConfigPath() + bean.getPath(), FileExplorerItemData.FileType.File.equals(bean.getFileType()) ? FileSystemObject.FileSystemObjectType.FILE : FileSystemObject.FileSystemObjectType.FOLDER, 0L, null, null, null);

                    //step-1: remove properties from db
                    dbAgentFileService.deleteByNamePrefix(bean.getPath());

                    //step-2: remove files/folders from file system
                    fileSystemManagerService.delete(fo);

                    //upd grid
                    container.removeItem(selectedItemId);
                }
            }
        } catch (Exception ex) {
            log.error("Error remove", ex);
            UIUtils.showErrorNotification("", "Error remove");
        } finally {
            UIUtils.closeLoadingIndicator();
        }
    }

    @Override
    public void contextMenuItemClicked(ContextMenu.ContextMenuItemClickEvent event) {
        ContextMenu.ContextMenuItem item = (ContextMenu.ContextMenuItem) event.getSource();
        if (item != null) {
            ContextMenuItemData data = (ContextMenuItemData) item.getData();
            if (data != null) {
                if (ContextMenuItemData.MenuAction.TABLE_REFRESH_FOLDER.equals(data.getAction())) {
                    try {
                        UIUtils.showLoadingIndicator();
                        panelFileProperty.clearProperty();
                        fillGridFileExplorer(getCurrentPath());
                    } catch (Exception ex) {
                        log.error("Error refresh file explorer", ex);
                        UIUtils.showErrorNotification("", "Error refresh file explorer");
                    } finally {
                        UIUtils.closeLoadingIndicator();
                    }
                } else if (ContextMenuItemData.MenuAction.ROW_REFRESH_FOLDER.equals(data.getAction())) {
                    try {
                        UIUtils.showLoadingIndicator();
                        panelFileProperty.clearProperty();
                        fillGridFileExplorer(getCurrentPath());
                    } catch (Exception ex) {
                        log.error("Error refresh file explorer", ex);
                        UIUtils.showErrorNotification("", "Error refresh file explorer");
                    } finally {
                        UIUtils.closeLoadingIndicator();
                    }
                } else if (ContextMenuItemData.MenuAction.ROW_NEW_FOLDER.equals(data.getAction()) || ContextMenuItemData.MenuAction.TABLE_NEW_FOLDER.equals(data.getAction())) {
                    createNewFolder(getCurrentPath());
                } else if (ContextMenuItemData.MenuAction.ROW_EDIT.equals(data.getAction())) {
                    Object itemId = gridFileExplorer.getValue();
                    if (itemId != null) {
                        renameFileOrFolder(gridFileExplorer.getValue());
                    } else {
                        UIUtils.showErrorNotification("", "Choose item to rename");
                    }
                } else if (ContextMenuItemData.MenuAction.ROW_REMOVE.equals(data.getAction())) {
                    Object itemId = gridFileExplorer.getValue();
                    if (itemId != null) {
                        removeFileOrFolder(gridFileExplorer.getValue());
                    } else {
                        UIUtils.showErrorNotification("", "Choose item to remove");
                    }
                }
            }
        }
    }

    private void initTabSheetProperty() {

        panelFileProperty.setDbAgentFileOwnerService(getDbAgentFileOwnerService());
        panelFileProperty.setDbAgentFileService(getDbAgentFileService());
        panelFileProperty.setFileSystemManagerService(getFileSystemManagerService());
        panelFileProperty.setFileSystemManagerServiceSecurity(getFileSystemManagerServiceSecurity());

        //
        panelFileProperty.initOwnerBeanContainer();
        panelFileProperty.initFileTypeBeanContainer();
        //

        //event on property success save
        panelFileProperty.setListener(new FilePropertyPanel.PropertySaveListener() {
            @Override
            public void onSave(DbAgentFile file) {
                Object selectedItemId = gridFileExplorer.getValue();
                BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
                if (container != null) {
                    FileExplorerItemData bean = container.getItem(selectedItemId).getBean();
                    if (bean != null) {
                        bean.setProperty((DbAgentFile) file.clone());

                        //#hack to update change data model value
                        gridFileExplorer.refreshRowCache();
                    }
                }
            }
        });
    }
}
