/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.model.core;

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
    private ClarificationTypeModel kind;

    public NoteCountingDiscrepancyModel(ClarificationTypeModel kind) {
        this.kind = kind;
    }
}
