/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.core.container;

import com.payway.media.core.container.AbstractFormatContainer;
import com.vaadin.data.util.BeanContainer;
import lombok.extern.slf4j.Slf4j;

/**
 * FormatContainerBeanContainer
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
@Slf4j
public class FormatContainerBeanContainer extends BeanContainer<String, AbstractFormatContainer> {

    private static final long serialVersionUID = -3496881705838064114L;

    public FormatContainerBeanContainer() {
        super(AbstractFormatContainer.class);
        setBeanIdProperty("id");
    }
}
