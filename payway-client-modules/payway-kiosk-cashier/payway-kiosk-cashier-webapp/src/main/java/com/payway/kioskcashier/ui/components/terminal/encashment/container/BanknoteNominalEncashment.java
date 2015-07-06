/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.terminal.encashment.container;

import com.payway.messaging.model.common.BanknoteTypeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * BanknoteNominalEncashment
 *
 * @author Sergey Kichenko
 * @created 06.07.15 00:00
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BanknoteNominalEncashment {

    private long id;
    private BanknoteTypeDto banknoteType;
    private String label;
    private double nominal;
    private int quantity;
}
