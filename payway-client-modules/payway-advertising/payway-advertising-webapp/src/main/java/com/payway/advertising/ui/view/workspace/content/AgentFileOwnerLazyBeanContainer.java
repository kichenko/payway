/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.payway.advertising.core.service.AgentFileOwnerService;
import com.payway.advertising.model.DbAgentFileOwner;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;

/**
 * AgentFileOwnerBookWindow
 *
 * @author Sergey Kichenko
 * @created 26.05.15 00:00
 */
@Slf4j
public class AgentFileOwnerLazyBeanContainer extends BeanContainer<Long, DbAgentFileOwner> {

    public interface LazyLoadExceptionCallback {

        void exception(Exception ex);
    }

    private static final long serialVersionUID = 1119649003699314945L;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private long count;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private boolean isDirty;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private String filterString;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private Sort sort;

    @Getter
    @Setter
    private LazyLoadExceptionCallback exceptionCallback;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private AgentFileOwnerService agentFileOwnerService;

    public AgentFileOwnerLazyBeanContainer(Class<DbAgentFileOwner> type, AgentFileOwnerService agentFileOwnerService, LazyLoadExceptionCallback exceptionCallback) {
        super(type);
        setDirty(true);
        setExceptionCallback(exceptionCallback);
        setAgentFileOwnerService(agentFileOwnerService);
    }

    @Override
    public int size() {
        try {
            if (isDirty()) {
                log.debug("@@@@");
                if (getFilterString() != null && getFilterString().isEmpty()) {
                    count = agentFileOwnerService.countFindByName(getFilterString());
                } else {
                    count = agentFileOwnerService.countList();
                }
                setDirty(false);
            }
        } catch (Exception ex) {
            log.error("Get items content in lazy container", ex);

            if (exceptionCallback != null) {
                exceptionCallback.exception(ex);
            }
        }

        return (int) count;
    }

    @Override
    public BeanItem getItem(Object itemId) {
        return new BeanItem(itemId);
    }

    @Override
    public List<Long> getItemIds(int startIndex, int numberOfIds) {

        List<Long> itemIds = new ArrayList(0);
        try {
            List<DbAgentFileOwner> owners;
            if (isFiltered()) {
                Set<Filter> filters = getFilters();
                for (Filter filter : filters) {
                    if (filter instanceof SimpleStringFilter) {
                        internalRemoveAllItems();

                        owners = agentFileOwnerService.findByName(((SimpleStringFilter) filter).getFilterString(), startIndex, numberOfIds, getSort());
                        itemIds = new ArrayList(owners.size());
                        setDirty(false);
                        log.debug("###");

                        for (DbAgentFileOwner owner : owners) {
                            internalAddItemAtEnd(owner.getId(), new BeanItem<>(owner), false);
                            itemIds.add(owner.getId());
                        }
                        break;
                    }
                }
            } else {
                internalRemoveAllItems();

                owners = agentFileOwnerService.list(startIndex, numberOfIds, getSort());
                itemIds = new ArrayList(owners.size());
                setDirty(false);
                log.debug("%%%");

                for (DbAgentFileOwner owner : owners) {
                    internalAddItemAtEnd(owner.getId(), new BeanItem<>(owner), false);
                    itemIds.add(owner.getId());
                }
            }
        } catch (Exception ex) {
            log.error("Get items content in lazy container", ex);
            if (exceptionCallback != null) {
                exceptionCallback.exception(ex);
            }
        }

        return itemIds;
    }

    @Override
    public void addFilter(Filter filter) throws UnsupportedFilterException {

        super.addFilter(filter);

        if (filter instanceof SimpleStringFilter) {
            String search = ((SimpleStringFilter) filter).getFilterString();
            if (search != null && !search.equals(getFilterString())) {
                setDirty(true);
                setFilterString(search);
            } else {
                setDirty(false);
            }
        }

    }

    @Override
    public void removeAllContainerFilters() {
        
        super.removeAllContainerFilters();
        
        setDirty(true);
        //setFilterString("");
    }

    @Override
    public void sort(Object[] propertyIds, boolean[] ascending) {

        if ((propertyIds != null && ascending != null) && (propertyIds.length == ascending.length)) {
            List<Sort.Order> orders = new ArrayList<>(propertyIds.length);
            for (int i = 0; i < propertyIds.length; i++) {
                orders.add(new Sort.Order(ascending[i] ? Sort.Direction.ASC : Sort.Direction.DESC, (String) propertyIds[i]));
            }

            setSort(new Sort(orders));
        }
    }

    @Override
    public boolean containsId(Object itemId) {
        // we need this because of value change listener (otherwise selected item event won't be fired)
        return true;
    }
}
