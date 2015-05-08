/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace;

import com.payway.advertising.ui.view.core.AbstractView;
import com.payway.advertising.ui.view.core.ProgressBarWindow;
import com.payway.advertising.ui.view.core.TextEditDialogWindow;
import com.payway.advertising.ui.view.workspace.content.FileExplorerItemData;
import com.payway.advertising.ui.view.workspace.content.FileExplorerType;
import com.payway.advertising.ui.view.workspace.content.FileSystemManagerService;
import com.payway.advertising.ui.view.workspace.content.FileSystemObject;
import com.payway.advertising.ui.view.workspace.content.FileUploadWindow;
import com.payway.advertising.ui.view.workspace.content.UploadTask;
import com.payway.advertising.ui.view.workspace.content.UploadTaskDnD;
import com.payway.advertising.ui.view.workspace.content.UploadTaskFileInput;
import com.payway.advertising.ui.view.workspace.content.UploadTaskWindow;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.SourceIsTarget;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Html5File;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private Panel panelDragDropFileUpload;

    @Autowired
    @Qualifier("localFileManagerService")
    private FileSystemManagerService fileSystemManagerService;

    private String currentRelativePath;
    private String rootPath;

    private ContextMenu gridContextMenu = new ContextMenu();
    private UploadTaskWindow uploadTaskWindow = new UploadTaskWindow();
    private ProgressBarWindow progressBarWindow = new ProgressBarWindow();

    @PostConstruct
    public void postConstruct() {
        setSizeFull();
        addComponent(Clara.create("ContentConfigurationView.xml", this));

        rootPath = "c:\\";
        currentRelativePath = "";

        initGridFileExplorerTable();
        initFileUploadButton();
        initDragAndDropWrapper();
    }

    private void initFileUploadButton() {
        btnFileUpload.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                new FileUploadWindow("Select file to upload on server...", new UploadTaskFileInput(), ContentConfigurationView.this).show();
            }
        });
    }

    private void initDragAndDropWrapper() {
        DragAndDropWrapper wrapper = new DragAndDropWrapper(new Panel());
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

        panelDragDropFileUpload.setContent(wrapper);
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
                        if (FileExplorerType.FOLDER.equals(bean.getFileType())) {
                            currentRelativePath = StringUtils.defaultIfBlank(bean.getPath(), "");
                            fillGridFileExplorer(rootPath + currentRelativePath);
                        }
                    }
                }
            }
        });

        //set DnD event handler
        gridFileExplorer.setDropHandler(new DropHandler() {
            @Override
            public AcceptCriterion getAcceptCriterion() {
                return SourceIsTarget.get();
            }

            @Override
            public void drop(DragAndDropEvent event
            ) {
                //
            }
        });

        //set column render
        gridFileExplorer.addGeneratedColumn("name", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {

                FileExplorerItemData bean = ((BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource()).getItem(itemId).getBean();

                if (bean != null) {
                    Label label = new Label();
                    label.setContentMode(ContentMode.HTML);

                    if (FileExplorerType.FOLDER.equals(bean.getFileType())) {
                        label.setIcon(FontAwesome.FOLDER);
                        label.setValue("<b>" + bean.getName() + "</b>");
                    } else if (FileExplorerType.FILE.equals(bean.getFileType())) {
                        label.setIcon(FontAwesome.FILE);
                        label.setValue("" + bean.getName() + "");
                    }

                    return label;
                }

                return "";
            }
        }
        );

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
        }
        );

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
        }
        );

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
    public void activate() {
        fillGridFileExplorer(rootPath + currentRelativePath);
    }

    @Override
    public boolean onOk(UploadTaskFileInput uploadTask) {
        if (uploadTask.getFileName() == null || uploadTask.getFileName().isEmpty()) {
            Notification.show("Please, select file to upload", Notification.Type.WARNING_MESSAGE);
            return false;
        } else {
            uploadTaskWindow.addUploadTask(uploadTask);
            uploadTaskWindow.show();
        }

        return true;
    }

    @Override
    public void onCancel() {
        //
    }

    private void fillGridFileExplorer(String path) {
        try {

            progressBarWindow.show();
            UI.getCurrent().push();

            BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
            if (container != null) {
                container.removeAllItems();
                List<FileSystemObject> list = fileSystemManagerService.list(new FileSystemObject(path, FileSystemObject.FileSystemObjectType.FOLDER, null, null, null), false);
                if (list != null) {
                    for (FileSystemObject f : list) {
                        container.addBean(new FileExplorerItemData(
                                f.getType().equals(FileSystemObject.FileSystemObjectType.FOLDER) ? FileExplorerType.FOLDER : FileExplorerType.FILE,
                                StringUtils.substringAfterLast(f.getPath(), "/"),
                                f.getPath(),
                                f.getSize(),
                                Boolean.FALSE
                        ));
                    }
                }
            }

            Thread.sleep(500);
        } catch (Exception ex) {
            Notification.show("Error then load grid file explorer", Notification.Type.WARNING_MESSAGE);
            UI.getCurrent().push();
        } finally {
            progressBarWindow.close();
        }
    }

    private void createNewFolder(final String path) {
        new TextEditDialogWindow("Create new folder", new TextEditDialogWindow.TextEditDialogWindowEvent() {
            @Override
            public boolean onOk(String text) {
                boolean isOk = false;
                if (StringUtils.isNotBlank(text)) {
                    try {
                        progressBarWindow.show();
                        UI.getCurrent().push();

                        //
                        fileSystemManagerService.create(new FileSystemObject());
                        //

                        BeanItemContainer<FileExplorerItemData> container = (BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource();
                        if (container != null) {
                            container.addBean(new FileExplorerItemData(FileExplorerType.FOLDER, text, path, 0L, false));
                        }

                        isOk = true;

                        Thread.sleep(500);
                    } catch (Exception ex) {
                        Notification.show("Error then create new folder", Notification.Type.WARNING_MESSAGE);
                        UI.getCurrent().push();
                    } finally {
                        progressBarWindow.close();
                    }

                    return isOk;
                } else {
                    Notification.show("Please, enter correct folder name", Notification.Type.WARNING_MESSAGE);
                }
                return false;
            }

            @Override
            public void onCancel() {
                //
            }
        }).show();
    }

    private void renameFolder() {
        new TextEditDialogWindow("Rename folder", new TextEditDialogWindow.TextEditDialogWindowEvent() {
            @Override
            public boolean onOk(String text) {
                boolean isOk = false;
                if (StringUtils.isNotBlank(text)) {
                    try {
                        progressBarWindow.show();
                        UI.getCurrent().push();

                        //
                        fileSystemManagerService.rename(new FileSystemObject(), new FileSystemObject());
                        //

                        isOk = true;

                        Thread.sleep(500);
                    } catch (Exception ex) {
                        Notification.show("Error then rename new folder", Notification.Type.WARNING_MESSAGE);
                        UI.getCurrent().push();
                    } finally {
                        progressBarWindow.close();
                    }

                    return isOk;
                } else {
                    Notification.show("Please, enter correct folder name", Notification.Type.WARNING_MESSAGE);
                }
                return false;
            }

            @Override
            public void onCancel() {
                //
            }
        }).show();
    }

    @Override
    public void contextMenuItemClicked(ContextMenu.ContextMenuItemClickEvent event) {
        ContextMenu.ContextMenuItem item = (ContextMenu.ContextMenuItem) event.getSource();
        if (item != null) {
            ContextMenuItemData data = (ContextMenuItemData) item.getData();
            if (data != null) {
                if (ContextMenuItemData.MenuAction.TABLE_REFRESH_FOLDER.equals(data.getAction())) {
                    fillGridFileExplorer(rootPath + currentRelativePath);
                } else if (ContextMenuItemData.MenuAction.ROW_REFRESH_FOLDER.equals(data.getAction())) {
                    fillGridFileExplorer(rootPath + currentRelativePath);
                } else if (ContextMenuItemData.MenuAction.ROW_NEW_FOLDER.equals(data.getAction()) || ContextMenuItemData.MenuAction.TABLE_NEW_FOLDER.equals(data.getAction())) {
                    createNewFolder(rootPath + currentRelativePath);
                } else if (ContextMenuItemData.MenuAction.ROW_EDIT.equals(data.getAction())) {
                    renameFolder();
                }
            }
        }
    }
}
