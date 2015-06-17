/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content.container;

import com.payway.advertising.core.service.AgentFileOwnerService;
import com.payway.advertising.model.DbAgentFileOwner;
import com.payway.commons.webapp.ui.components.table.paging.IPagingContainer;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * AgentFileOwnerPagingBeanContainer
 *
 * @author Sergey Kichenko
 * @created 27.05.15 00:00
 */
@Slf4j
public class AgentFileOwnerPagingBeanContainer extends BeanContainer<Long, DbAgentFileOwner> implements IPagingContainer {

    private int currentPage = 0;
    private int pageSize = 10;
    private long total = 0;
    private boolean sorting = false;
    private Sort sort;

    @Setter
    @Getter
    private IPagingLoadCallback errorListener;

    private final Map<String, Object> criteries = new HashMap<>();

    @Setter
    @Getter
    private AgentFileOwnerService agentFileOwnerService;

    public AgentFileOwnerPagingBeanContainer(Class<DbAgentFileOwner> type, AgentFileOwnerService agentFileOwnerService) {
        super(type);
        setAgentFileOwnerService(agentFileOwnerService);
    }

    protected boolean load() {

        boolean success = false;
        try {

            Page<DbAgentFileOwner> page;

            if (errorListener != null) {
                errorListener.start();
            }

            internalRemoveAllItems();

            if (criteries.containsKey("name")) {
                page = agentFileOwnerService.findByName((String) criteries.get("name"), new PageRequest(currentPage, pageSize, sort));
            } else {
                page = agentFileOwnerService.list(new PageRequest(currentPage, pageSize, sort));
            }

            if (page == null) {
                throw new Exception("Fail load items from database");
            }

            total = page.getTotalElements();

            if (currentPage >= getTotalPages()) {
                currentPage = (getTotalPages() - 1) < 0 ? 0 : getTotalPages() - 1;
            }

            if (page.hasContent()) {
                for (DbAgentFileOwner owner : page.getContent()) {
                    internalAddItemAtEnd(owner.getId(), new BeanItem<>(owner), false);
                }
            }

            success = true;
        } catch (Exception ex) {
            log.error("Load container data", ex);

            if (errorListener != null) {
                errorListener.exception(ex);
            }
        } finally {
            if (errorListener != null) {
                errorListener.finish();
            }
        }

        return success;
    }

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
    public List<Long> getItemIds(int startIndex, int numberOfIds) {

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
