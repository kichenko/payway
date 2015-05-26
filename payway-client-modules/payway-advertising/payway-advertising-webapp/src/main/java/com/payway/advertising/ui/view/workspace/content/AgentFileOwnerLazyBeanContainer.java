/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.payway.advertising.model.DbAgentFileOwner;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import java.util.List;

/**
 * AgentFileOwnerBookWindow
 *
 * @author Sergey Kichenko
 * @created 26.05.15 00:00
 */
public class AgentFileOwnerLazyBeanContainer extends BeanContainer<Long, DbAgentFileOwner> {

    public AgentFileOwnerLazyBeanContainer(Class<? super DbAgentFileOwner> type) {
        super(type);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public BeanItem getItem(Object itemId) {
        return new BeanItem(itemId);
    }

    @Override
    public List<Long> getItemIds(int startIndex, int numberOfIds) {
        return null;

    }

    @Override
    public void sort(Object[] propertyIds, boolean[] ascending) {
        //
    }

    /**
     * Need this because of value change listener (otherwise selected item event
     * won't be fired).
     *
     * @param itemId
     * @return
     */
    @Override
    public boolean containsId(Object itemId) {
        return true;
    }
}
