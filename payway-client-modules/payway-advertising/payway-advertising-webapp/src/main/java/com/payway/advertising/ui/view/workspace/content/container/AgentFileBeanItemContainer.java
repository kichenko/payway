/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content.container;

import com.payway.advertising.model.DbAgentFile;
import com.vaadin.data.util.BeanItemContainer;
import lombok.extern.slf4j.Slf4j;

/**
 * AgentFileBeanItemContainer
 *
 * @author Sergey Kichenko
 * @created 19.08.2015
 */
@Slf4j
public class AgentFileBeanItemContainer extends BeanItemContainer<DbAgentFile> {

    private static final long serialVersionUID = 7572872618570949327L;

    public AgentFileBeanItemContainer() {
        super(DbAgentFile.class);
    }
}
