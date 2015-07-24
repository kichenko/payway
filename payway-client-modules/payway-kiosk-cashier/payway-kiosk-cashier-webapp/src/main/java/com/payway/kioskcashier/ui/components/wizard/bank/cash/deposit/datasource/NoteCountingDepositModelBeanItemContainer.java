/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource;

import com.vaadin.data.util.BeanItemContainer;
import lombok.extern.slf4j.Slf4j;

/**
 * NoteCountingDepositModelBeanItemContainer
 *
 * @author Sergey Kichenko
 * @created 22.07.15 00:00
 */
@Slf4j
public class NoteCountingDepositModelBeanItemContainer extends BeanItemContainer<NoteCountingDepositModel> {

    private static final long serialVersionUID = 1491119078987607324L;

    public NoteCountingDepositModelBeanItemContainer() {
        super(NoteCountingDepositModel.class);
    }
}
