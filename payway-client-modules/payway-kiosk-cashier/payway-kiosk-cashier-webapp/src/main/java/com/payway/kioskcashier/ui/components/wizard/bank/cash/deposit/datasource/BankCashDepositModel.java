/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource;

import com.payway.kioskcashier.ui.model.core.BeanModel;
import com.payway.kioskcashier.ui.model.core.NoteCountingDiscrepancyModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * BankCashDepositModel
 *
 * @author Sergey Kichenko
 * @created 22.07.15 00:00
 */
@Getter
@Setter
@NoArgsConstructor
public class BankCashDepositModel extends BeanModel {

    private Date created;

    private String teller;

    private AccountModel account;

    private StuffModel depositedBy;

    private double total;

    private boolean surplus;

    private boolean shortage;

    private List<NoteCountingDepositModel> depositCountings = new ArrayList();

    private List<BankNoteCashModel> depositNominals = new ArrayList<>();

    private List<NoteCountingDiscrepancyModel> discrepancies = new ArrayList<>();
}
