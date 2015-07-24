/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit;

import com.vaadin.data.util.BeanItem;

/**
 * BeanItemWizardStep
 *
 * @author Sergey Kichenko
 * @created 08.06.15 00:00
 */
public interface BeanItemWizardStep<BT> {

    void setBeanItem(BeanItem<BT> value);

    BeanItem<BT> getBeanItem();

    Class getBeanItemClass();
}
