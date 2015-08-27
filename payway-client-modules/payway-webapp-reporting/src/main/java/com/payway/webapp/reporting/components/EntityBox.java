/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.components;

import com.payway.webapp.reporting.components.model.SimpleSelectionModel;
import com.payway.webapp.reporting.components.model.SimpleSelectionModelBeanItemContainer;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.vaadin.teemu.clara.Clara;

/**
 * EntityBox
 *
 * @author Sergey Kichenko
 * @created 06.08.2015
 */
public class EntityBox extends VerticalLayout implements Container<SimpleSelectionModel> {

    private static final long serialVersionUID = 6926758324977235706L;

    public enum FetchStyle {

        Eager;
    }

    public enum ViewStyle {

        Combobox,
        ListBox
    }

    //TODO: now support only eager datasources
    private final FetchStyle fetchStyle = FetchStyle.Eager;

    @Setter
    @Getter
    private ViewStyle viewStyle = ViewStyle.Combobox;

    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private Component widget;

    public EntityBox() {
        init();
    }

    public EntityBox(String id, String caption, ViewStyle viewStyle) {

        setId(id);
        setSpacing(true);
        setWidth("100%");
        setCaption(caption);
        setViewStyle(viewStyle);

        init();
    }

    private void init() {

        if (ViewStyle.ListBox.equals(getViewStyle())) {

            ListBox lst = new ListBox();
            lst.setId(lst.getId() + "-" + getId());

            setWidget(lst);
            addComponent(getWidget());
        } else {

            ComboBox comboBox = (ComboBox) Clara.create(getClass().getResourceAsStream("/com/payway/webapp/reporting/components/ComboBox.xml"), this);
            comboBox.setId(comboBox.getId() + "-" + getId());
            comboBox.setItemCaptionPropertyId("caption");
            comboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
            comboBox.setContainerDataSource(new SimpleSelectionModelBeanItemContainer());

            setWidget(comboBox);
            addComponent(getWidget());
        }
    }

    @Override
    public void addAll(Collection<SimpleSelectionModel> collection) {

        if (ComboBox.class.equals(getWidget().getClass())) {
            ((SimpleSelectionModelBeanItemContainer) ((ComboBox) getWidget()).getContainerDataSource()).addAll(collection);
        } else if (ListBox.class.equals(getWidget().getClass())) {
            ((ListBox) getWidget()).addAll(collection);
        }
    }

    @Override
    public void add(SimpleSelectionModel item) {

        if (ComboBox.class.equals(getWidget().getClass())) {
            ((SimpleSelectionModelBeanItemContainer) ((ComboBox) getWidget()).getContainerDataSource()).addBean(item);
        } else if (ListBox.class.equals(getWidget().getClass())) {
            ((ListBox) getWidget()).add(item);
        }
    }

    @Override
    public List<SimpleSelectionModel> getSelected() {

        if (ComboBox.class.equals(getWidget().getClass())) {
            return Collections.singletonList((SimpleSelectionModel) ((ComboBox) getWidget()).getValue());
        } else if (ListBox.class.equals(getWidget().getClass())) {
            return ((ListBox) getWidget()).getSelected();
        }

        return new ArrayList<>(0);
    }

    @Override
    public void clear() {

        if (ComboBox.class.equals(getWidget().getClass())) {
            ((SimpleSelectionModelBeanItemContainer) ((ComboBox) getWidget()).getContainerDataSource()).removeAllItems();
        } else if (ListBox.class.equals(getWidget().getClass())) {
            ((ListBox) getWidget()).clear();
        }
    }

    @Override
    public void select(List<?> ids) {

        if (ids == null || ids.isEmpty()) {
            return;
        }

        if (ComboBox.class.equals(getWidget().getClass())) {
            SimpleSelectionModel selected = null;
            SimpleSelectionModelBeanItemContainer container = (SimpleSelectionModelBeanItemContainer) ((ComboBox) getWidget()).getContainerDataSource();

            for (SimpleSelectionModel m : container.getItemIds()) {
                if (m.getId().equals(ids.get(0))) {
                    selected = m;
                    break;
                }
            }

            ((ComboBox) getWidget()).select(selected);

        } else if (ListBox.class.equals(getWidget().getClass())) {
            ((ListBox) getWidget()).select(ids);
        }
    }
}
