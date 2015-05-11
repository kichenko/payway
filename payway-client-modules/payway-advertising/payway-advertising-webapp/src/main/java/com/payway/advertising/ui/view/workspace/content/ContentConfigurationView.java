/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.payway.advertising.core.service.FileSystemManagerService;
import com.payway.advertising.core.service.FileSystemObject;
import com.payway.advertising.core.service.SettingsService;
import com.payway.advertising.core.service.UserService;
import com.payway.advertising.ui.component.BreadCrumbs;
import com.payway.advertising.ui.view.core.AbstractView;
import com.payway.advertising.ui.view.core.ProgressBarWindow;
import com.payway.advertising.ui.view.core.TextEditDialogWindow;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.SourceIsTarget;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DragAndDropWrapper;
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
public class ContentConfigurationView extends AbstractView implements FileUploadWindow.FileUploadWindowEvent, ContextMenu.ContextMenuItemClickListener, ContextMenu.ContextMenuOpenedListener.TableListener, ContextMenu.ContextMenuOpenedListener.ComponentListener {

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
            ROW_COPY,
            ROW_PAST,
            ROW_MOVE,
            ROW_PROP_VIEW,
            ROW_PROP_NEW,
            ROW_PROP_EDIT,
            ROW_PROP_REMOVE,
            TABLE_NEW_FOLDER,
            TABLE_REFRESH_FOLDER
        }

        private MenuAction action;
        private Object data;
    }

    @UiField
    private Table gridFileExplorer;

    @UiField
    private Button btnFileUpload;

    @UiField
    private BreadCrumbs breadCrumbs;

    @UiField
    private CssLayout panelFileUpload;

    @UiField
    private VerticalLayout dragDropPanel;

    @Autowired
    @Qualifier("localFileManagerService")
    private FileSystemManagerService fileSystemManagerService;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("settingsService")
    private SettingsService settingsService;

    private String rootUserConfigPath;
    private String currentRelativePath;
    private boolean isFileGridLoadedOnActivate = false;

    private ContextMenu gridContextMenu = new ContextMenu();
    private UploadTaskWindow uploadTaskWindow = new UploadTaskWindow();
    private ProgressBarWindow progressBarWindow = new ProgressBarWindow();

    @PostConstruct
    public void postConstruct() {
        setSizeFull();
        addComponent(Clara.create("ContentConfigurationView.xml", this));

        initRootUserConfigPath();
        initCurrentRelativePath();

        initGridFileExplorerTable();
        initFileUploadButton();
        initDragAndDropWrapper();
        initBreadCrumbs();
    }

    private void initRootUserConfigPath() {
        rootUserConfigPath = settingsService.getLocalConfigPath() + userService.getUser().getUserName();
    }

    private void initCurrentRelativePath() {
        currentRelativePath = rootUserConfigPath;
    }

    private String getCurrentPath() {
        return currentRelativePath + settingsService.getSeparator();
    }

    private String getRootUserConfigPath() {
        return rootUserConfigPath;
    }

    private void showLoadingIndicator() {
        progressBarWindow.show();
        UI.getCurrent().push();
    }

    private void closeLoadingIndicator() {
        progressBarWindow.close();
        UI.getCurrent().push();
    }

    private void showNotificationError(String title, String message) {
        Notification.show(message, Notification.Type.WARNING_MESSAGE);
        UI.getCurrent().push();
    }

    @Override
    public void activate() {
        if (!isFileGridLoadedOnActivate) {
            try {
                showLoadingIndicator();
                FileSystemObject fo = new FileSystemObject(getCurrentPath(), FileSystemObject.FileSystemObjectType.FOLDER, 0L, null, null);
                if (!fileSystemManagerService.exist(fo)) {
                    fileSystemManagerService.create(fo);
                }
                fillGridFileExplorer(getCurrentPath());
                isFileGridLoadedOnActivate = true;
            } catch (Exception ex) {
                log.error("Error on activate view", ex);
                showNotificationError("", "Error loading file explorer on activate");
            } finally {
                closeLoadingIndicator();
            }
        }
    }

    private void initFileUploadButton() {
        btnFileUpload.setIcon(FontAwesome.FOLDER);
        btnFileUpload.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                new FileUploadWindow("Choose a file to upload", new UploadTaskFileInput(), ContentConfigurationView.this).show();
            }
        });
    }

    private void initDragAndDropWrapper() {
        DragAndDropWrapper wrapper = new DragAndDropWrapper(dragDropPanel);
        wrapper.setSizeFull();

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
                        UploadTask uploadTask = new UploadTaskDnD();
                        uploadTask.setFileName(file.getFileName());
                        uploadTask.setUploadObject(file);
                        uploadTaskWindow.addUploadTask(uploadTask);
                    }
                    uploadTaskWindow.show();
                }
            }
        });

        panelFileUpload.addComponent(wrapper);
    }

    private void initBreadCrumbs() {
        breadCrumbs.addCrumb("", FontAwesome.HOME, getRootUserConfigPath());
        breadCrumbs.addBreadCrumbSelectListener(new BreadCrumbs.BreadCrumbSelectListener() {
            @Override
            public void selected(int index) {
                try {
                    showLoadingIndicator();
                    String path = (String) breadCrumbs.getCrumbState(index);
                    currentRelativePath = path;
                    fillGridFileExplorer(getCurrentPath());
                } catch (Exception ex) {
                    showNotificationError("", "Error load file explorer");
                } finally {
                    closeLoadingIndicator();
                }
            }
        });
    }

    private void initGridFileExplorerTable() {

        //set common
        gridFileExplorer.setSelectable(true);
        gridFileExplorer.setDragMode(Table.TableDragMode.ROW);
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
                    BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
                    if (container != null) {
                        FileExplorerItemData bean = container.getItem(event.getItemId()).getBean();
                        if (bean != null && FileExplorerType.FOLDER.equals(bean.getFileType())) {
                            try {
                                showLoadingIndicator();
                                currentRelativePath = StringUtils.defaultIfBlank(bean.getPath(), "");
                                fillGridFileExplorer(getCurrentPath());
                                breadCrumbs.addCrumb(bean.getName(), FontAwesome.FOLDER, bean.getPath());
                                breadCrumbs.selectCrumb(breadCrumbs.size() - 1, false);
                            } catch (Exception ex) {
                                showNotificationError("", "Error load file explorer");
                            } finally {
                                closeLoadingIndicator();
                            }
                        }
                    }
                }
            }
        });

        //set grid DnD event handler
        gridFileExplorer.setDropHandler(new DropHandler() {
            @Override
            public AcceptCriterion getAcceptCriterion() {
                return SourceIsTarget.get();
            }

            @Override
            public void drop(DragAndDropEvent event) {
                //
            }
        });

        //set column name render
        gridFileExplorer.addGeneratedColumn("name", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                FileExplorerItemData bean = ((BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource()).getItem(itemId).getBean();
                if (bean != null) {
                    Label label = new Label();
                    if (FileExplorerType.FOLDER.equals(bean.getFileType())) {
                        label.setIcon(FontAwesome.FOLDER);
                    } else if (FileExplorerType.FILE.equals(bean.getFileType())) {
                        label.setIcon(FontAwesome.FILE);
                    }
                    label.setValue(bean.getName());

                    return label;
                }

                return "";
            }
        });

        //set column size render
        gridFileExplorer.addGeneratedColumn("size", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                String value = "";
                FileExplorerItemData bean = ((BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource()).getItem(itemId).getBean();
                if (bean != null) {
                    if (FileExplorerType.FILE.equals(bean.getFileType())) {
                        value = Long.toString(bean.getSize());
                    }
                }
                return value;
            }
        });

        //set column hasProperty render
        gridFileExplorer.addGeneratedColumn("hasProperty", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                FileExplorerItemData bean = ((BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource()).getItem(itemId).getBean();
                if (bean != null) {
                    if (FileExplorerType.FILE.equals(bean.getFileType())) {
                        return bean.getHasProperty();
                    }
                }
                return "";
            }
        });

        //set column view
        gridFileExplorer.setColumnHeader("name", "File name");
        gridFileExplorer.setColumnHeader("size", "File size");
        gridFileExplorer.setColumnHeader("hasProperty", "Has property");
        gridFileExplorer.setVisibleColumns("name", "size", "hasProperty");
    }

    private void initContextMenuTableRow(ContextMenu menu, Object data) {
        if (menu != null) {
            ContextMenu.ContextMenuItem tmp;

            menu.removeAllItems();

            tmp = menu.addItem("Refresh");
            tmp.setSeparatorVisible(true);
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.ROW_REFRESH_FOLDER, data));
            tmp.addItemClickListener(this);

            tmp = menu.addItem("New folder");
            tmp.setSeparatorVisible(true);
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.ROW_NEW_FOLDER, data));
            tmp.addItemClickListener(this);

            tmp = menu.addItem("Edit");
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.ROW_EDIT, data));
            tmp.addItemClickListener(this);

            tmp = menu.addItem("Remove");
            tmp.setSeparatorVisible(true);
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.ROW_REMOVE, data));
            tmp.addItemClickListener(this);

            ContextMenu.ContextMenuItem prop = menu.addItem("Properties");

            tmp = prop.addItem("View");
            tmp.setSeparatorVisible(true);
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.ROW_PROP_VIEW, data));
            tmp.addItemClickListener(this);

            tmp = prop.addItem("New");
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.ROW_PROP_NEW, data));
            tmp.addItemClickListener(this);

            tmp = prop.addItem("Edit");
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.ROW_PROP_EDIT, data));
            tmp.addItemClickListener(this);

            tmp = prop.addItem("Remove");
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.ROW_PROP_REMOVE, data));
            tmp.addItemClickListener(this);
        }
    }

    private void initContextMenuTable(ContextMenu menu) {
        if (menu != null) {
            ContextMenu.ContextMenuItem tmp;

            menu.removeAllItems();

            tmp = menu.addItem("Refresh");
            tmp.setSeparatorVisible(true);
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.TABLE_REFRESH_FOLDER, null));
            tmp.addItemClickListener(this);

            tmp = menu.addItem("New folder");
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

    @Override
    public boolean onOk(UploadTaskFileInput uploadTask) {
        boolean isOk = true;
        if (uploadTask.getFileName() == null || uploadTask.getFileName().isEmpty()) {
            Notification.show("Please, select file to upload", Notification.Type.WARNING_MESSAGE);
            isOk = false;
        } else {
            uploadTaskWindow.addUploadTask(uploadTask);
            uploadTaskWindow.show();
        }
        return isOk;
    }

    @Override
    public void onCancel() {
        //
    }

    private void fillGridFileExplorer(String path) throws Exception {
        try {
            BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
            if (container != null) {
                container.removeAllItems();
                List<FileSystemObject> list = fileSystemManagerService.list(new FileSystemObject(path, FileSystemObject.FileSystemObjectType.FOLDER, null, null, null), false);
                for (FileSystemObject f : list) {
                    container.addBean(new FileExplorerItemData(
                      FileSystemObject.FileSystemObjectType.FOLDER.equals(f.getType()) ? FileExplorerType.FOLDER : FileExplorerType.FILE,
                      StringUtils.substringAfterLast(f.getPath(), settingsService.getSeparator()),
                      f.getPath(),
                      f.getSize(),
                      Boolean.FALSE
                    ));
                }
            }
            //#sleep
            Thread.sleep(500);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void createNewFolder(final String path) {
        new TextEditDialogWindow("Create new folder", new TextEditDialogWindow.TextEditDialogWindowEvent() {
            @Override
            public boolean onOk(String text) {
                boolean isOk = false;
                if (StringUtils.isNotBlank(text)) {
                    try {
                        showLoadingIndicator();

                        FileSystemObject fo = new FileSystemObject(path + text, FileSystemObject.FileSystemObjectType.FOLDER, 0L, null, null);

                        //do create new folder
                        fileSystemManagerService.create(fo);

                        BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
                        if (container != null) {
                            container.addBean(new FileExplorerItemData(FileExplorerType.FOLDER, text, path + text, 0L, false));
                        }

                        //#sleep
                        Thread.sleep(500);
                        isOk = true;
                    } catch (Exception ex) {
                        log.error("Error create new folder", ex);
                        showNotificationError("", "Error create new folder");
                    } finally {
                        closeLoadingIndicator();
                    }
                } else {
                    Notification.show("Please, enter correct folder name", Notification.Type.WARNING_MESSAGE);
                }
                return isOk;
            }

            @Override
            public void onCancel() {
                //
            }
        }).show();
    }

    private void renameFileOrFolder(final Object selectedItemId) {
        new TextEditDialogWindow("Rename", new TextEditDialogWindow.TextEditDialogWindowEvent() {
            @Override
            public boolean onOk(String text) {
                boolean isOk = false;
                if (StringUtils.isNotBlank(text)) {
                    try {
                        showLoadingIndicator();
                        BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
                        if (container != null) {
                            FileExplorerItemData bean = container.getItem(selectedItemId).getBean();
                            if (bean != null) {
                                FileSystemObject foOld = new FileSystemObject(bean.getPath(), FileExplorerType.FILE.equals(bean.getFileType()) ? FileSystemObject.FileSystemObjectType.FILE : FileSystemObject.FileSystemObjectType.FOLDER, 0L, null, null);
                                FileSystemObject foNew = new FileSystemObject(StringUtils.substringBeforeLast(bean.getPath(), "/") + text, FileExplorerType.FILE.equals(bean.getFileType()) ? FileSystemObject.FileSystemObjectType.FILE : FileSystemObject.FileSystemObjectType.FOLDER, 0L, null, null);

                                //do rename
                                fileSystemManagerService.rename(foOld, foNew);

                                //update grid item
                                bean.setName(text);

                                //#sleep
                                Thread.sleep(500);
                                isOk = true;
                            }
                        }
                    } catch (Exception ex) {
                        log.error("Error rename", ex);
                        showNotificationError("", "Error rename");
                    } finally {
                        closeLoadingIndicator();
                    }
                } else {
                    Notification.show("Please, enter correct folder name", Notification.Type.WARNING_MESSAGE);
                }
                return isOk;
            }

            @Override
            public void onCancel() {
                //
            }
        }).show();
    }

    private void removeFileOrFolder(final Object selectedItemId) {
        try {
            showLoadingIndicator();

            BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
            if (container != null) {
                FileExplorerItemData bean = container.getItem(selectedItemId).getBean();
                if (bean != null) {
                    FileSystemObject fo = new FileSystemObject(bean.getPath(), FileExplorerType.FILE.equals(bean.getFileType()) ? FileSystemObject.FileSystemObjectType.FILE : FileSystemObject.FileSystemObjectType.FOLDER, 0L, null, null);

                    //do remove
                    fileSystemManagerService.delete(fo);

                    //update grid
                    container.removeItem(selectedItemId);

                    //#sleep
                    Thread.sleep(500);
                }
            }
        } catch (Exception ex) {
            log.error("Error remove", ex);
            showNotificationError("", "Error remove");
        } finally {
            closeLoadingIndicator();
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
                        showLoadingIndicator();
                        fillGridFileExplorer(getCurrentPath());
                    } catch (Exception ex) {
                        showNotificationError("", "Error refresh file explorer");
                    } finally {
                        closeLoadingIndicator();
                    }
                } else if (ContextMenuItemData.MenuAction.ROW_REFRESH_FOLDER.equals(data.getAction())) {
                    try {
                        showLoadingIndicator();
                        fillGridFileExplorer(getCurrentPath());
                    } catch (Exception ex) {
                        showNotificationError("", "Error refresh file explorer");
                    } finally {
                        closeLoadingIndicator();
                    }
                } else if (ContextMenuItemData.MenuAction.ROW_NEW_FOLDER.equals(data.getAction()) || ContextMenuItemData.MenuAction.TABLE_NEW_FOLDER.equals(data.getAction())) {
                    createNewFolder(getCurrentPath());
                } else if (ContextMenuItemData.MenuAction.ROW_EDIT.equals(data.getAction())) {
                    Object itemId = gridFileExplorer.getValue();
                    if (itemId != null) {
                        renameFileOrFolder(gridFileExplorer.getValue());
                    } else {
                        showNotificationError("", "Choose item to rename");
                    }
                }
            } else if (ContextMenuItemData.MenuAction.ROW_REMOVE.equals(data.getAction())) {
                Object itemId = gridFileExplorer.getValue();
                if (itemId != null) {
                    removeFileOrFolder(gridFileExplorer.getValue());
                } else {
                    showNotificationError("", "Choose item to remove");
                }
            }
        }
    }
}
