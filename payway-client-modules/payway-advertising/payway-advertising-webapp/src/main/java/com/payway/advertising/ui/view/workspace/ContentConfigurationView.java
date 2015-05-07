/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace;

import com.payway.advertising.ui.view.core.AbstractView;
import com.payway.advertising.ui.view.workspace.content.FileExplorerItemData;
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
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Html5File;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    //private FileSystemManagerService fileSystemManagerService;
    private ContextMenu gridContextMenu = new ContextMenu();
    private UploadTaskWindow uploadTaskWindow = new UploadTaskWindow();

    @PostConstruct
    public void postConstruct() {
        setSizeFull();
        addComponent(Clara.create("ContentConfigurationView.xml", this));

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

        //set click event handler
        gridFileExplorer.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    //
                } else {
                    //
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
            public void drop(DragAndDropEvent event) {
                //
            }
        });

        //set column render
        gridFileExplorer.addGeneratedColumn("hello", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {  
                
                //BeanItem <FileExplorerItemData> data = ((BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource()).getItem(itemId);
                
                Label label = new Label();
                label.setIcon(null);
                label.setContentMode(ContentMode.HTML);
                label.setValue("");
                return label;
            }
        });

        //set column view
        gridFileExplorer.setColumnHeader("fileType", "File type");
        gridFileExplorer.setColumnHeader("name", "File name");
        gridFileExplorer.setColumnHeader("size", "File size");
        gridFileExplorer.setColumnHeader("hasProperty", "Has property");
        gridFileExplorer.setVisibleColumns("hello", "name", "size", "hasProperty");

        //load data to table 
        ((BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource()).addBean(new FileExplorerItemData());
        ((BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource()).addBean(new FileExplorerItemData());
        ((BeanItemContainer<FileExplorerItemData>) gridFileExplorer.getContainerDataSource()).addBean(new FileExplorerItemData());
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

            tmp = menu.addItem("Copy");
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.ROW_COPY, data));
            tmp.addItemClickListener(this);

            tmp = menu.addItem("Paste");
            tmp.setSeparatorVisible(true);
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.ROW_PAST, data));
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
    public void contextMenuItemClicked(ContextMenu.ContextMenuItemClickEvent event) {
        ContextMenu.ContextMenuItem item = (ContextMenu.ContextMenuItem) event.getSource();
        if (item != null) {
            ContextMenuItemData data = (ContextMenuItemData) item.getData();
            if (data != null) {
                Notification.show(data.getAction().toString());
            }
        }
    }

    @Override
    public void activate() {
        //
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
        //StreamVariable
    }
}
