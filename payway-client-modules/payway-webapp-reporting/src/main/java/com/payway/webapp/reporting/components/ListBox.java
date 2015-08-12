/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.components;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.payway.webapp.reporting.components.model.SimpleSelectionModel;
import com.payway.webapp.reporting.components.model.SimpleSelectionModelBeanItemContainer;
import com.vaadin.data.Property;
import com.vaadin.data.util.MethodProperty;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * ListBox
 *
 * @author Sergey Kichenko
 * @created 05.08.2015
 */
public final class ListBox extends VerticalLayout implements Container<SimpleSelectionModel> {

    private static final long serialVersionUID = -6101545371631125845L;

    @UiField
    private Table tableListBox;

    private final SimpleSelectionModelBeanItemContainer container = new SimpleSelectionModelBeanItemContainer();

    public ListBox() {
        init();
    }

    public ListBox(String id, String caption) {

        init();

        setId(id);
        setCaption(caption);
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        walk2Id(this);
    }

    private void init() {

        setSizeFull();
        addComponent(Clara.create(getClass().getResourceAsStream("/com/payway/webapp/reporting/components/ListBox.xml"), this));

        tableListBox.setContainerDataSource(container);
        tableListBox.addGeneratedColumn("checked", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 2855441121974230973L;

            @Override
            public Object generateCell(final Table source, final Object itemId, Object columnId) {

                CheckBox checkBox = new CheckBox(null, source.getContainerDataSource().getItem(itemId).getItemProperty("selected"));

                VerticalLayout layout = new VerticalLayout(checkBox);
                layout.setComponentAlignment(checkBox, Alignment.MIDDLE_CENTER);

                return layout;
            }
        });

        tableListBox.setVisibleColumns("checked", "caption");
        tableListBox.setColumnAlignment("caption", Table.Align.LEFT);
        tableListBox.setColumnExpandRatio("checked", 0);
        tableListBox.setColumnExpandRatio("caption", 1);
    }

    private void walk2Id(Component cmp) {

        if (cmp instanceof ComponentContainer) {
            Iterator<Component> iterator = ((ComponentContainer) cmp).iterator();
            while (iterator.hasNext()) {
                walk2Id(iterator.next());
            }
        } else {
            if (!cmp.getId().isEmpty()) {
                cmp.setId(cmp.getId() + "-" + getId());
            }
        }
    }

    @UiHandler(value = "btnSelectAll")
    public void ClickButtonSelectAll(Button.ClickEvent event) {
        setSelectFlag(true);
        tableListBox.refreshRowCache();
    }

    @UiHandler(value = "btnClearAll")
    public void ClickButtonClearAll(Button.ClickEvent event) {
        setSelectFlag(false);
        tableListBox.refreshRowCache();
    }

    private void setSelectFlag(boolean flag) {
        for (SimpleSelectionModel bean : container.getItemIds()) {
            bean.setSelected(flag);
        }
    }

    @Override
    public void addAll(Collection<SimpleSelectionModel> collection) {
        container.addAll(collection);
    }

    @Override
    public void add(SimpleSelectionModel item) {
        container.addBean(item);
    }

    @Override
    public void clear() {
        container.removeAllItems();
    }

    @Override
    public List<SimpleSelectionModel> getSelected() {

        return FluentIterable.from(container.getItemIds()).filter(new Predicate<SimpleSelectionModel>() {
            @Override
            public boolean apply(SimpleSelectionModel src) {
                return src.isSelected();
            }
        }).toList();
    }

    @Override
    public void select(List<?> ids) {

        for (SimpleSelectionModel model : container.getItemIds()) {
            if (ids.contains(model.getId())) {
                model.setSelected(true);
                Property property = container.getContainerProperty(model, "selected");
                if (property instanceof MethodProperty) {
                    ((MethodProperty) property).fireValueChange();
                }
            }
        }
    }
}
