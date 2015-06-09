/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.components;

import com.vaadin.data.Property;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Tree;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SideBarMenu
 *
 * @author Sergey Kichenko
 * @created 21.04.15 00:00
 */
public final class SideBarMenu extends CustomComponent {

    private static final long serialVersionUID = 7791705742904164938L;

    private final Tree tree = new Tree();
    private final HierarchicalContainer container = new HierarchicalContainer();
    private final Map<String, MenuItem> items = new HashMap<>();
    private SideBarMenuItemClickListener clickListener;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MenuItem {

        private String tag;
        private String caption;
        private Resource icon;
        private List<MenuItem> childs;
    }

    public interface SideBarMenuItemClickListener {

        void onClickSideBarMenuItem(MenuItem menuItem);
    }

    public SideBarMenu() {
        setSizeFull();
        tree.setSizeFull();
        addStyleName("sidebar");
        setCompositionRoot(tree);
        
        container.addContainerProperty("caption", String.class, "");
        tree.setContainerDataSource(container);

        tree.setNullSelectionAllowed(false);
        tree.setItemCaptionPropertyId("caption");
        tree.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);

        tree.addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = -382717228031608542L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (clickListener != null && event.getProperty().getValue() != null) {
                    clickListener.onClickSideBarMenuItem(items.get((String) event.getProperty().getValue()));
                }
            }
        });
    }

    public void addMenuItem(final SideBarMenu.MenuItem item, final SideBarMenuItemClickListener listener) {
        clickListener = listener;
        recursiveAdd(null, item);
    }

    private void recursiveAdd(Object parentItemId, final SideBarMenu.MenuItem item) {

        container.addItem(item.getTag());
        container.getItem(item.getTag()).getItemProperty("caption").setValue(item.getCaption());
        container.setParent(item.getTag(), parentItemId);

        tree.setItemIcon(item.getTag(), item.getIcon());
        tree.setChildrenAllowed(item.getTag(), item.getChilds() == null ? false : item.getChilds().size() > 0);

        items.put(item.getTag(), item);

        if (item.getChilds() != null) {
            for (int i = 0; i < item.getChilds().size(); i++) {
                recursiveAdd(item.getTag(), item.getChilds().get(i));
            }
        }
    }

    public void clearMenuItems() {
        tree.removeAllItems();
    }

    public boolean select(int index) {

        if (index >= 0 && index < container.size()) {
            Object itemId = container.getIdByIndex(index);
            if (itemId != null) {
                tree.select(itemId);
                return true;
            }
        }

        return false;
    }
}
