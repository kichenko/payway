/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content.container;

import com.payway.advertising.core.service.AgentFileOwnerService;
import com.payway.advertising.model.DbAgentFileOwner;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * DbAgentFileOwnerBeanItemContainer
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
@Slf4j
public class AgentFileOwnerBeanItemContainer extends BeanItemContainer<DbAgentFileOwner> {

    private AgentFileOwnerService service;

    public AgentFileOwnerBeanItemContainer() {
        super(DbAgentFileOwner.class);
    }

    public AgentFileOwnerBeanItemContainer(AgentFileOwnerService service) {
        this();
        this.service = service;
    }

    @Override
    protected void addFilter(Filter filter) throws UnsupportedFilterException {
        SimpleStringFilter simpleFilter = (SimpleStringFilter) filter;
        if (simpleFilter != null) {
            try {
                doFilter(simpleFilter.getFilterString());
            } catch (Exception ex) {
                log.error("", ex);
            }
        } else {
            throw new UnsupportedFilterException();
        }
    }

    private void doFilter(String filterString) throws Exception {
        removeAllItems();
        if (!StringUtils.isBlank(filterString)) {
            addAll(service.findByName(filterString));
        } else {
            //addAll(service.list());
            addAll(new ArrayList<DbAgentFileOwner>(0));
        }
    }
}
