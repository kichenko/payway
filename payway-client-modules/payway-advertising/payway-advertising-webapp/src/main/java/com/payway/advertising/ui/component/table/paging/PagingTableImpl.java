/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component.table.paging;

import com.vaadin.data.Container;
import com.vaadin.ui.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * PagingTableImpl
 *
 * @author Sergey Kichenko
 * @created 27.05.15 00:00
 */
@Slf4j
public class PagingTableImpl extends Table implements IPagingTable {

    private final List<PageChangeListener> listeners = new ArrayList<>(1);

    public PagingTableImpl() {
        this(null);
    }

    public PagingTableImpl(String caption) {
        super(caption);
        setPageLength(0);
    }

    protected void firePageChangeEvent() {
        if (listeners != null) {
            for (PageChangeListener listener : listeners) {
                listener.changed();
            }
        }
    }

    @Override
    public void addPageChangeListener(PageChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removePageChangeListener(PageChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void setContainerDataSource(Container dataSource) {
        setContainerDataSource(dataSource, null);
    }

    @Override
    public void setContainerDataSource(Container dataSource, Collection<?> visibleIds) {

        if (!(dataSource instanceof Container.Indexed)) {
            throw new IllegalArgumentException("Bad datasource type, must implement Container.Indexed");
        }

        if (visibleIds != null) {
            super.setContainerDataSource(dataSource, visibleIds);
        } else {
            super.setContainerDataSource(dataSource);
        }
    }

    @Override
    public boolean nextPage() {
        boolean success = ((IPagingContainer) this.getContainerDataSource()).nextPage();
        if (success) {
            firePageChangeEvent();
            markAsDirtyRecursive();
        }
        return success;

    }

    @Override
    public boolean previousPage() {
        boolean success = ((IPagingContainer) this.getContainerDataSource()).previousPage();
        if (success) {
            firePageChangeEvent();
            markAsDirtyRecursive();
        }
        return success;
    }

    @Override
    public int getCurrentPage() {
        return ((IPagingContainer) this.getContainerDataSource()).getCurrentPage();
    }

    @Override
    public boolean setCurrentPage(int page) {
        boolean success = ((IPagingContainer) this.getContainerDataSource()).setCurrentPage(page);
        if (success) {
            firePageChangeEvent();
            markAsDirtyRecursive();
        }
        return success;
    }

    @Override
    public int getTotalPages() {
        return ((IPagingContainer) this.getContainerDataSource()).getTotalPages();
    }

    @Override
    public int getPageSize() {
        return ((IPagingContainer) this.getContainerDataSource()).getPageSize();
    }

    @Override
    public boolean setPageSize(int pageSize) {
        //setPageLength(pageSize);
        boolean success = ((IPagingContainer) this.getContainerDataSource()).setPageSize(pageSize);
        if (success) {
            firePageChangeEvent();
            markAsDirtyRecursive();
        }
        return success;
    }

    @Override
    public boolean refresh() {
        boolean success = ((IPagingContainer) this.getContainerDataSource()).refresh();
        if (success) {
            firePageChangeEvent();
            markAsDirtyRecursive();
        }
        return success;
    }

    @Override
    public boolean addCriteria(String property, Object value) {
        boolean success = ((IPagingContainer) this.getContainerDataSource()).addCriteria(property, value);
        if (success) {
            firePageChangeEvent();
            markAsDirtyRecursive();
        }
        return success;
    }

    @Override
    public boolean removeCriteria(String property) {
        boolean success = ((IPagingContainer) this.getContainerDataSource()).removeCriteria(property);
        if (success) {
            firePageChangeEvent();
            markAsDirtyRecursive();
        }
        return success;
    }
}
