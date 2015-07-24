/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit;

import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStandartButtonStep;
import com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource.BankCashDepositModel;
import com.vaadin.data.util.BeanItem;
import lombok.extern.slf4j.Slf4j;

/**
 * AbstractBankCashDepositBeanItemWizardStep
 *
 * @author Sergey Kichenko
 * @created 22.07.15 00:00
 */
@Slf4j
public abstract class AbstractBankCashDepositBeanItemWizardStep extends AbstractWizardStandartButtonStep implements BeanItemWizardStep<BankCashDepositModel> {

    private static final long serialVersionUID = 173774986745230165L;

    protected BeanItem<BankCashDepositModel> beanItem;

    @Override
    public void setBeanItem(BeanItem<BankCashDepositModel> value) {
        beanItem = value;
    }

    @Override
    public BeanItem<BankCashDepositModel> getBeanItem() {
        return beanItem;
    }

    @Override
    public Class getBeanItemClass() {
        return BankCashDepositModel.class;
    }
}
