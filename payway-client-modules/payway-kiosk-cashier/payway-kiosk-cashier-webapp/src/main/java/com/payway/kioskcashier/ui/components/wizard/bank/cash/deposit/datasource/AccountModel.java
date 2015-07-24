/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource;

import com.payway.kioskcashier.ui.model.core.BeanNameModel;
import lombok.Getter;

/**
 * AccountModel
 *
 * @author Sergey Kichenko
 * @created 22.07.15 00:00
 */
@Getter
public class AccountModel extends BeanNameModel {

    public AccountModel(long id, String name) {
        super(id, name);
    }
}
