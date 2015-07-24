/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.kioskcashier;

import com.payway.messaging.model.AbstractDto;
import java.util.Date;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * CashDepositDto
 *
 * @author Sergey Kichenko
 * @created 23.07.15 00:00
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class CashDepositDto extends AbstractDto {

    private static final long serialVersionUID = 7653490635392556201L;

    private final Date created;
    private final long depositedBy;
    private final long account;
    private final String teller;
    private final boolean shortage;
    private final boolean surplus;
    private final double total;

    private final List<Long> countings;
    private final List<CashDepositNominalDto> nominals;
    private final List<CountingDiscrepancyDto> discrepancies;

    public CashDepositDto(Date created, long depositedBy, long account, String teller, boolean shortage, boolean surplus, double total, List<Long> countings, List<CashDepositNominalDto> nominals, List<CountingDiscrepancyDto> discrepancies) {
        this.created = created;
        this.depositedBy = depositedBy;
        this.account = account;
        this.teller = teller;
        this.shortage = shortage;
        this.surplus = surplus;
        this.total = total;
        this.countings = countings;
        this.nominals = nominals;
        this.discrepancies = discrepancies;
    }

}
