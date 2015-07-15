/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.terminal.encashment.container;

import lombok.Getter;
import lombok.Setter;

/**
 * NoteCountingDiscrepancyModel
 *
 * @author Sergey Kichenko
 * @created 13.07.15 00:00
 */
@Getter
@Setter
public class NoteCountingDiscrepancyModel extends BeanModel {

    private String clarification;
    private double amount;

}
