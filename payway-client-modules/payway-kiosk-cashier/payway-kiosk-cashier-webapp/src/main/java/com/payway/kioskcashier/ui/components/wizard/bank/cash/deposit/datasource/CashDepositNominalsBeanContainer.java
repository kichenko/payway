/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource;

import com.vaadin.data.util.BeanContainer;
import lombok.extern.slf4j.Slf4j;

/**
 * CashDepositNominalsBeanContainer
 *
 * @author Sergey Kichenko
 * @created 22.07.15 00:00
 */
@Slf4j
public class CashDepositNominalsBeanContainer extends BeanContainer<Long, BankNoteCashModel> {

    private static final long serialVersionUID = -3496881705838064114L;

    public CashDepositNominalsBeanContainer() {
        super(BankNoteCashModel.class);
        setBeanIdProperty("id");
    }

}
