/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content.container;

import com.payway.advertising.core.service.AgentFileOwnerService;
import com.payway.advertising.model.DbAgentFileOwner;
import com.payway.commons.webapp.ui.components.table.paging.AbstractPagingBeanContainer;
import com.vaadin.data.util.BeanItem;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * AgentFileOwnerPagingBeanContainer
 *
 * @author Sergey Kichenko
 * @created 27.05.15 00:00
 */
@Slf4j
public class AgentFileOwnerPagingBeanContainer extends AbstractPagingBeanContainer<Long, DbAgentFileOwner> {

    private static final long serialVersionUID = 7687386813135663676L;

    @Setter
    @Getter
    private AgentFileOwnerService agentFileOwnerService;

    public AgentFileOwnerPagingBeanContainer(Class<DbAgentFileOwner> type, AgentFileOwnerService agentFileOwnerService) {
        super(type);
        setAgentFileOwnerService(agentFileOwnerService);
    }

    @Override
    protected boolean load() {

        boolean success = false;
        try {

            Page<DbAgentFileOwner> page;

            if (errorListener != null) {
                errorListener.begin();
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
                errorListener.end();
            }
        }

        return success;
    }
}
