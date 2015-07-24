/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource;

import com.payway.kioskcashier.ui.model.core.BeanModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * BankNoteModel
 *
 * @author Sergey Kichenko
 * @created 23.07.15 00:00
 */
@Getter
@Setter
@NoArgsConstructor
public class BankNoteCashModel extends BeanModel {

    private long nominalId;
    private BanknoteTypeModel kind;
    private String label;
    private double nominal;
    private int quantity;
    private int delta;
}
