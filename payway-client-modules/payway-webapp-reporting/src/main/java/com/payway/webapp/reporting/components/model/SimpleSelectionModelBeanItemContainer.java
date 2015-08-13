/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.components.model;

import com.vaadin.data.util.BeanItemContainer;

/**
 * SimpleSelectionModelBeanItemContainer
 *
 * @author Sergey Kichenko
 * @created 06.08.2015
 */
public class SimpleSelectionModelBeanItemContainer extends BeanItemContainer<SimpleSelectionModel> {

    private static final long serialVersionUID = 5229353933450208886L;

    public SimpleSelectionModelBeanItemContainer() {
        super(SimpleSelectionModel.class);
    }
}
