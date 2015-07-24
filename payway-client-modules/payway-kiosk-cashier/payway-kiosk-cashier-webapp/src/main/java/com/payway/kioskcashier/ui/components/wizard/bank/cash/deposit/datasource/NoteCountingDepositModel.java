/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource;

import com.payway.kioskcashier.ui.model.core.BeanModel;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * NoteCountingDepositModel
 *
 * @author Sergey Kichenko
 * @created 22.07.15 00:00
 */
@Getter
@NoArgsConstructor
public class NoteCountingDepositModel extends BeanModel {

    private Date created;
    private String terminalName;
    private int seqNum;

    @Setter
    private boolean selected;

    public NoteCountingDepositModel(long id, Date created, String terminalName, int seqNum, boolean selected) {

        super(id);
        this.created = created;
        this.terminalName = terminalName;
        this.seqNum = seqNum;
        this.selected = selected;
    }
}
