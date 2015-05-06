/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace;

import com.payway.advertising.ui.view.core.AbstractView;
import com.payway.advertising.ui.view.core.FileUploadWindow;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.SourceIsTarget;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Table;
import com.vaadin.ui.Upload;
import java.io.IOException;
import java.io.OutputStream;
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
public class ContentConfigurationView extends AbstractView implements FileUploadWindow.FileUploadWindowAction, ContextMenu.ContextMenuItemClickListener, ContextMenu.ContextMenuOpenedListener.TableListener, ContextMenu.ContextMenuOpenedListener.ComponentListener {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContextMenuItemData {

        public enum MenuAction {

            ROW_NEW_FOLDER,
            ROW_EDIT,
            ROW_REMOVE,
            ROW_COPY,
            ROW_PAST,
            ROW_PROP_VIEW,
            ROW_PROP_NEW,
            ROW_PROP_EDIT,
            ROW_PROP_REMOVE,
            TABLE_MENU_NEW_FOLDER
        }

        private MenuAction action;
        private Object data;
    }

    public static class MyReceiver implements Upload.Receiver {

        @Override
        public OutputStream receiveUpload(String filename, String mimeType) {
            return new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    int k = 900;
                }
            };
        }

    }

    @UiField
    private Table gridFileExplorer;

    @UiField
    private Button btnFileUpload;

    private ContextMenu gridContextMenu = new ContextMenu();

    private UploadTaskWindow uploadTaskWindow = new UploadTaskWindow();

    @PostConstruct
    public void postConstruct() {
        setSizeFull();
        addComponent(Clara.create("ContentConfigurationView.xml", this));
        gridFileExplorer.addContainerProperty("Icon", String.class, null);
        gridFileExplorer.addContainerProperty("Caption", String.class, null);
        gridFileExplorer.addContainerProperty("Has Properties", String.class, null);
        gridFileExplorer.addContainerProperty("Progress", ProgressIndicator.class, null);

        gridFileExplorer.setSelectable(true);
        gridFileExplorer.setDragMode(Table.TableDragMode.ROW);

        gridContextMenu.setAsContextMenuOf(gridFileExplorer);
        gridContextMenu.addContextMenuTableListener(this);
        gridContextMenu.addContextMenuComponentListener(this);

        gridFileExplorer.addItem();
        gridFileExplorer.addItem();
        gridFileExplorer.addItem();

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

        btnFileUpload.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                FileUploadWindow wnd = new FileUploadWindow(new MyReceiver());
                wnd.setCaption("Select file to upload on server...");
                wnd.addUploadActionListener(ContentConfigurationView.this);
                wnd.show();
            }
        });
    }

    private void initContextMenuTableRow(ContextMenu menu, Object data) {
        if (menu != null) {
            ContextMenu.ContextMenuItem tmp;

            menu.removeAllItems();

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

            tmp = menu.addItem("New folder");
            tmp.setData(new ContextMenuItemData(ContextMenuItemData.MenuAction.TABLE_MENU_NEW_FOLDER, null));
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
    public void onOk(Upload upload) {
        UploadTaskWindow wnd = new UploadTaskWindow();
        wnd.addUploadTask(upload);
        wnd.show();
    }

    @Override
    public void onCancel() {
        //
    }

}
