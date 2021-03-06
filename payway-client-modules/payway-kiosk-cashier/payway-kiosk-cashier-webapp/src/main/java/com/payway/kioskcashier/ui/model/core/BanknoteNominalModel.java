/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.model.core;

import com.payway.kioskcashier.ui.model.core.BeanModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * BanknoteNominalEncashmentModel
 *
 * @author Sergey Kichenko
 * @created 06.07.15 00:00
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BanknoteNominalModel extends BeanModel {

    private String label;
    private double nominal;
    private int quantity;

    public BanknoteNominalModel(long id, String label, double nominal, int quantity) {
        super(id);
        this.label = label;
        this.nominal = nominal;
        this.quantity = quantity;
    }

    /**
     * Calculated model field
     *
     * @return amount
     */
    public double getAmount() {
        return nominal * quantity;
    }
}
