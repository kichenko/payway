/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.payway.advertising.core.service.AgentFileService;
import com.payway.advertising.core.service.app.settings.SettingsAppService;
import com.payway.advertising.model.DbAgentFile;
import com.payway.advertising.ui.view.workspace.content.container.AgentFileBeanItemContainer;
import com.payway.commons.webapp.ui.InteractionUI;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.SourceIsTarget;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * FileSequenceView
 *
 * @author Sergey Kichenko
 * @created 19.08.2015
 */
@Slf4j
@NoArgsConstructor
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component(value = FileSequenceView.FILE_SEQUENCE_VIEW_ID)
public class FileSequenceView extends VerticalLayout {

    private static final long serialVersionUID = 7242829547449869124L;

    public static final String FILE_SEQUENCE_VIEW_ID = "app.advertising.ui.FileSequenceView";

    public interface ActionCallBack {

        void save(Button.ClickEvent event, com.vaadin.ui.Component compnent);

        void refresh(List<DbAgentFile> files);

        void cancel(Button.ClickEvent event, com.vaadin.ui.Component compnent);
    }

    @UiField
    private Table tableFileSequence;

    @Getter
    @Setter
    @Autowired
    private AgentFileService agentFileService;

    @Autowired
    private SettingsAppService settingsAppService;

    private AgentFileBeanItemContainer container = new AgentFileBeanItemContainer();

    private ActionCallBack callback;

    @PostConstruct
    public void postConstruct() {
        init();
    }

    public FileSequenceView(ActionCallBack callback) {
        this.callback = callback;
    }

    private void init() {

        setSizeFull();
        addComponent(Clara.create("FileSequenceView.xml", this));

        container = new AgentFileBeanItemContainer();
        tableFileSequence.setContainerDataSource(container);

        tableFileSequence.setRowHeaderMode(Table.RowHeaderMode.INDEX);
        tableFileSequence.setColumnHeader("name", "File name");
        tableFileSequence.setVisibleColumns("name");
        tableFileSequence.setSortEnabled(false);
        tableFileSequence.setSelectable(true);

        tableFileSequence.setColumnWidth(null, 40);
        tableFileSequence.setColumnAlignment(null, Table.Align.RIGHT);
        tableFileSequence.setColumnAlignment("name", Table.Align.LEFT);

        //set column name render
        tableFileSequence.addGeneratedColumn("name", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 2855441121974230973L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {

                DbAgentFile bean = ((BeanItemContainer<DbAgentFile>) tableFileSequence.getContainerDataSource()).getItem(itemId).getBean();
                if (bean == null) {
                    return "";
                }

                Label label = new Label();
                String template = "<div><img src=\"%s\" style=\"vertical-align:middle;\"><span style=\"padding-left:5px;\">%s</span></div>";
                String path = settingsAppService.getContextPath() + "/VAADIN/themes/" + UI.getCurrent().getTheme() + "/images/";
                String fileName = "file_" + bean.getKind().name().toLowerCase() + ".png";

                label.setContentMode(ContentMode.HTML);
                label.setValue(String.format(template, path + fileName, bean.getName()));

                return label;
            }
        });

        tableFileSequence.setDragMode(Table.TableDragMode.ROW);
        tableFileSequence.setDropHandler(new DropHandler() {
            private static final long serialVersionUID = 607722003300263265L;

            @Override
            public void drop(DragAndDropEvent event) {

                Transferable transfer;
                Object srcItemId, dstItemId;
                AbstractSelect.AbstractSelectTargetDetails target;

                transfer = event.getTransferable();
                if (transfer.getSourceComponent() != tableFileSequence) {
                    return;
                }

                target = (AbstractSelect.AbstractSelectTargetDetails) event.getTargetDetails();

                srcItemId = transfer.getData("itemId");
                dstItemId = target.getItemIdOver();
                if (srcItemId.equals(dstItemId)) {
                    return;
                }

                if (VerticalDropLocation.TOP.equals(target.getDropLocation())) {
                    Object tmpItemId;
                    container.removeItem(srcItemId);
                    tmpItemId = container.prevItemId(dstItemId);
                    container.addItemAfter(tmpItemId, srcItemId);
                } else if (VerticalDropLocation.BOTTOM.equals(target.getDropLocation())) {
                    container.removeItem(srcItemId);
                    container.addItemAfter(dstItemId, srcItemId);
                } else if (VerticalDropLocation.MIDDLE.equals(target.getDropLocation())) {

                    Object prevItemId = container.prevItemId(dstItemId);
                    if (prevItemId == null) {
                        container.removeItem(srcItemId);
                        container.addItemAfter(null, srcItemId);
                    } else if (!prevItemId.equals(srcItemId)) {
                        container.removeItem(dstItemId);
                        container.removeItem(srcItemId);
                        container.addItemAfter(prevItemId, srcItemId);
                        container.addItemAfter(srcItemId, dstItemId);
                    } else {
                        container.removeItem(srcItemId);
                        container.addItemAfter(dstItemId, srcItemId);
                    }
                }

                tableFileSequence.select(srcItemId);
                if (container.nextItemId(dstItemId) == null || container.nextItemId(srcItemId) == null ) {
                    tableFileSequence.setCurrentPageFirstItemId(srcItemId);
                }
            }

            @Override
            public AcceptCriterion getAcceptCriterion() {
                return SourceIsTarget.get();
            }
        });

        try {
            ((InteractionUI) UI.getCurrent()).showProgressBar();
            List<DbAgentFile> list = agentFileService.findAll(new Sort(new Sort.Order(Sort.Direction.ASC, "seqNo"), new Sort.Order(Sort.Direction.ASC, "name")));
            container.addAll(list);
        } catch (Exception ex) {
            log.error("Cannot load files", ex);
            ((InteractionUI) UI.getCurrent()).showNotification("", "Cannot load files", Notification.Type.ERROR_MESSAGE);
        } finally {
            ((InteractionUI) UI.getCurrent()).closeProgressBar();
        }
    }

    private void saveAgentFileSequence() {

        int seq = 1;
        for (DbAgentFile file : container.getItemIds()) {
            file.setSeqNo(seq);
            seq += 1;
        }

        agentFileService.saveAll(container.getItemIds());
    }

    @UiHandler(value = "btnCancel")
    public void onClickCancel(Button.ClickEvent event) {
        if (callback != null) {
            callback.cancel(event, this);
        }
    }

    @UiHandler(value = "btnSave")
    public void onClickSave(Button.ClickEvent event) {

        if (callback != null) {
            try {
                ((InteractionUI) UI.getCurrent()).showProgressBar();
                saveAgentFileSequence();
                callback.refresh(container.getItemIds());
                callback.save(event, this);
            } catch (Exception ex) {
                log.error("Cannot save files sequence", ex);
                ((InteractionUI) UI.getCurrent()).showNotification("", "Cannot save files sequence", Notification.Type.ERROR_MESSAGE);
            } finally {
                ((InteractionUI) UI.getCurrent()).closeProgressBar();
            }
        }
    }
}
