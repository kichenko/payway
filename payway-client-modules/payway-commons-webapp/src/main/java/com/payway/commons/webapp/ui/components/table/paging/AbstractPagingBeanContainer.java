/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.components.table.paging;

import com.google.common.base.Function;
import com.payway.messaging.model.common.data.OrderDto;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AbstractPagingBeanContainer
 *
 * @author Sergey Kichenko
 * @created 12.08.2015
 */
public abstract class AbstractPagingBeanContainer<IDTYPE, BEANTYPE> extends BeanContainer<IDTYPE, BEANTYPE> implements IPagingContainer {

    private static final long serialVersionUID = 4764103603428487518L;

    public static final class TransforemerSpringOrder2OrderDto implements Function<Sort.Order, OrderDto> {

        @Override
        public OrderDto apply(Sort.Order src) {
            return new OrderDto(Sort.Direction.ASC.equals(src.getDirection()), src.getProperty());
        }
    };

    protected Sort sort;
    protected long total = 0;
    protected int pageSize = 10;
    protected int currentPage = 0;
    protected boolean sorting = false;

    protected final Map<String, Object> criteries = new HashMap<>();

    @Setter
    @Getter
    protected IPagingLoadCallback errorListener;

    public AbstractPagingBeanContainer(Class<BEANTYPE> type) {
        super(type);
    }

    protected abstract boolean load();

    @Override
    public boolean nextPage() {

        if (currentPage < getTotalPages()) {
            currentPage += 1;
            return load();
        }

        return false;
    }

    @Override
    public boolean previousPage() {

        if (currentPage > 0) {
            currentPage -= 1;
            return load();
        }

        return false;
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public boolean setCurrentPage(int page) {

        if (page >= 0 && page < getTotalPages()) {
            currentPage = page;
            return load();
        }

        return false;
    }

    @Override
    public int getTotalPages() {
        return (int) Math.ceil((double) total / (double) pageSize);
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public boolean setPageSize(int pageSize) {

        if (this.pageSize != pageSize && pageSize > 0) {
            this.currentPage = 0;
            this.pageSize = pageSize;
            return load();
        }
        return false;
    }

    @Override
    public boolean refresh() {
        return load();
    }

    @Override
    public boolean addCriteria(String property, Object value) {
        criteries.put(property, value);
        return load();
    }

    @Override
    public boolean removeCriteria(String property) {
        criteries.remove(property);
        return load();
    }

    @Override
    public List<IDTYPE> getItemIds(int startIndex, int numberOfIds) {

        if (sorting) {
            load();
            sorting = false;
        }

        return super.getItemIds(startIndex, numberOfIds);
    }

    @Override
    public void sort(Object[] propertyIds, boolean[] ascending) {

        if ((propertyIds != null && ascending != null) && (propertyIds.length == ascending.length)) {
            List<Sort.Order> orders = new ArrayList<>(propertyIds.length);
            for (int i = 0; i < propertyIds.length; i++) {
                orders.add(new Sort.Order(ascending[i] ? Sort.Direction.ASC : Sort.Direction.DESC, (String) propertyIds[i]));
            }

            sort = new Sort(orders);
            sorting = true;
        }
    }

    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {
        return super.addItem(itemId);
    }

    @Override
    public boolean removeItem(Object itemId) {
        return super.removeItem(itemId);
    }
}
