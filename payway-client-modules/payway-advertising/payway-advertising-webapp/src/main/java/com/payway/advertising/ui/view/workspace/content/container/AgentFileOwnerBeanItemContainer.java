/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content.container;

import com.payway.advertising.model.DbAgentFileOwner;
import com.vaadin.data.util.BeanItemContainer;
import lombok.extern.slf4j.Slf4j;

/**
 * AgentFileOwnerBeanContainer
 *
 * @author Sergey Kichenko
 * @created 17.06.15 00:00
 */
@Slf4j
public class AgentFileOwnerBeanItemContainer extends BeanItemContainer<DbAgentFileOwner> {

    private static final long serialVersionUID = -5299501747511585644L;

    public AgentFileOwnerBeanItemContainer() {
        super(DbAgentFileOwner.class);
    }
}
