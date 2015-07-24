/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.model.core;

import com.vaadin.data.util.BeanItemContainer;
import lombok.extern.slf4j.Slf4j;

/**
 * NoteCountingDiscrepancyModelBeanItemContainer
 *
 * @author Sergey Kichenko
 * @created 14.07.15 00:00
 */
@Slf4j
public class NoteCountingDiscrepancyModelBeanItemContainer extends BeanItemContainer<NoteCountingDiscrepancyModel> {

    private static final long serialVersionUID = -3496881705838064114L;

    public NoteCountingDiscrepancyModelBeanItemContainer() {
        super(NoteCountingDiscrepancyModel.class);
    }
}
