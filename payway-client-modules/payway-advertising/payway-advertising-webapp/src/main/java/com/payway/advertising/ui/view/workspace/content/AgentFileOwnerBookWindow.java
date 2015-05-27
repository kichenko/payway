/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.payway.advertising.core.service.AgentFileOwnerService;
import com.payway.advertising.model.DbAgentFileOwner;
import com.payway.advertising.ui.InteractionUI;
import com.payway.advertising.ui.component.table.paging.AgentFileOwnerPagingBeanContainer;
import com.payway.advertising.ui.component.table.paging.IPagingContainer;
import com.payway.advertising.ui.component.table.paging.PagingTableControls;
import com.payway.advertising.ui.component.table.paging.PagingTableImpl;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * AgentFileOwnerBookWindow
 *
 * @author Sergey Kichenko
 * @created 26.05.15 00:00
 */
@Slf4j
@NoArgsConstructor
public class AgentFileOwnerBookWindow extends Window {

    private static final long serialVersionUID = -3097395157377631255L;

    @UiField
    private Button btnAddNew;

    @UiField
    private TextField txtFilter;

    @UiField
    private PagingTableImpl gridOwners;

    @UiField
    private HorizontalLayout layoutGridControls;

    private AgentFileOwnerPagingBeanContainer gridContainer;

    @Getter
    @Setter
    private AgentFileOwnerService agentFileOwnerService;

    public AgentFileOwnerBookWindow(String caption, AgentFileOwnerService agentFileOwnerService) {
        setCaption(caption);
        setResizable(false);

        setContent(Clara.create("AgentFileOwnerBookWindow.xml", this));
        txtFilter.setIcon(FontAwesome.SEARCH);
        setAgentFileOwnerService(agentFileOwnerService);

        init();

        gridOwners.refresh();
    }

    private void init() {

        gridContainer = new AgentFileOwnerPagingBeanContainer(DbAgentFileOwner.class, agentFileOwnerService);
        gridContainer.setErrorListener(new IPagingContainer.IErrorPagingLoad() {
            @Override
            public void error(Exception ex) {
                log.error("Load agent owners", ex);
                ((InteractionUI) UI.getCurrent()).showNotification("Load agent owners", "Error load agent owners", Notification.Type.ERROR_MESSAGE);
            }
        });

        gridOwners.setContainerDataSource(gridContainer);
        gridOwners.setImmediate(true);
        gridOwners.setSelectable(true);

        layoutGridControls.addComponent(new PagingTableControls(gridOwners));

        gridOwners.addGeneratedColumn("name", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 2855441121974230973L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                return ((BeanItem<DbAgentFileOwner>) gridContainer.getItem(itemId)).getBean().getName();
            }
        });

        gridOwners.addGeneratedColumn("description", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 2855441121974230973L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                return ((BeanItem<DbAgentFileOwner>) gridContainer.getItem(itemId)).getBean().getDescription();
            }
        });

        gridOwners.addGeneratedColumn("edit", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 2855441121974230973L;

            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {

                Button btn = new Button(FontAwesome.EDIT);
                btn.addStyleName("tiny");
                btn.addStyleName("friendly");
                btn.addStyleName("icon-only");

                btn.addClickListener(new Button.ClickListener() {
                    private static final long serialVersionUID = 5019806363620874205L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        final AgentFileOwnerCRUDWindow wnd = new AgentFileOwnerCRUDWindow("Edit agent owner", ((BeanItem<DbAgentFileOwner>) gridContainer.getItem(itemId)).getBean());
                        wnd.setListener(new AgentFileOwnerCRUDWindow.CrudEventListener() {
                            @Override
                            public void cancel() {
                                wnd.close();
                            }

                            @Override
                            public void save(DbAgentFileOwner owner) {
                                //save to DB
                                try {
                                    ((InteractionUI) UI.getCurrent()).showProgressBar();
                                    agentFileOwnerService.save(owner);
                                    gridOwners.markAsDirtyRecursive();
                                    wnd.close();
                                } catch (Exception ex) {
                                    log.error("Edit agent owner", ex);
                                    ((InteractionUI) UI.getCurrent()).showNotification("Edit agent owner", "Error edit agent owner", Notification.Type.ERROR_MESSAGE);
                                } finally {
                                    ((InteractionUI) UI.getCurrent()).closeProgressBar();
                                }
                            }
                        });

                        wnd.show();
                    }
                });

                HorizontalLayout layout = new HorizontalLayout(btn);
                layout.setSizeFull();
                layout.setComponentAlignment(btn, Alignment.MIDDLE_CENTER);

                return layout;
            }
        });

        gridOwners.addGeneratedColumn("delete", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 2855441121974230973L;

            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {

                Button btn = new Button(FontAwesome.ERASER);
                btn.addStyleName("tiny");
                btn.addStyleName("danger");
                btn.addStyleName("icon-only");

                btn.addClickListener(new Button.ClickListener() {
                    private static final long serialVersionUID = 5019806363620874205L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        //delete in DB
                        try {
                            ((InteractionUI) UI.getCurrent()).showProgressBar();
                            agentFileOwnerService.delete(((BeanItem<DbAgentFileOwner>) gridContainer.getItem(itemId)).getBean());
                            gridOwners.removeItem(itemId);
                            gridOwners.markAsDirtyRecursive();
                        } catch (Exception ex) {
                            log.error("Delete agent owner", ex);
                            ((InteractionUI) UI.getCurrent()).showNotification("Delete agent owner", "Error delete agent owner", Notification.Type.ERROR_MESSAGE);
                        } finally {
                            ((InteractionUI) UI.getCurrent()).closeProgressBar();
                        }
                    }
                });

                HorizontalLayout layout = new HorizontalLayout(btn);
                layout.setSizeFull();
                layout.setComponentAlignment(btn, Alignment.MIDDLE_CENTER);

                return layout;
            }
        });

        //set column view
        gridOwners.setColumnHeader("name", "Name");
        gridOwners.setColumnHeader("description", "Description");
        gridOwners.setColumnHeader("edit", "");
        gridOwners.setColumnHeader("delete", "");

        gridOwners.setVisibleColumns("name", "description", "edit", "delete");

        gridOwners.setColumnWidth("edit", 40);
        gridOwners.setColumnWidth("delete", 40);

        //add new
        btnAddNew.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;

            @Override
            public void buttonClick(Button.ClickEvent event) {

                final AgentFileOwnerCRUDWindow wnd = new AgentFileOwnerCRUDWindow("Create agent owner", new DbAgentFileOwner("", ""));
                wnd.setListener(new AgentFileOwnerCRUDWindow.CrudEventListener() {
                    @Override
                    public void cancel() {
                        wnd.close();
                    }

                    @Override
                    public void save(DbAgentFileOwner owner) {
                        //save to DB
                        try {
                            ((InteractionUI) UI.getCurrent()).showProgressBar();
                            agentFileOwnerService.save(owner);
                            gridOwners.markAsDirtyRecursive();
                            wnd.close();
                        } catch (Exception ex) {
                            log.error("Create agent owner", ex);
                            ((InteractionUI) UI.getCurrent()).showNotification("Create agent owner", "Error create agent owner", Notification.Type.ERROR_MESSAGE);
                        } finally {
                            ((InteractionUI) UI.getCurrent()).closeProgressBar();
                        }
                    }
                });

                wnd.show();
            }
        });

        //filter
        txtFilter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            private static final long serialVersionUID = -7737508544027152963L;

            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                if (event.getText() != null && !event.getText().isEmpty()) {
                    gridOwners.addCriteria("name", event.getText());
                } else {
                    gridOwners.removeCriteria("name");
                }
            }
        });
    }
}
