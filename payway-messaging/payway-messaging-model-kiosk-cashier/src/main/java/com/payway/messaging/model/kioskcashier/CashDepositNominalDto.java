/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.kioskcashier;

import com.payway.messaging.model.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * CashDepositNoteDetailDto
 *
 * @author Sergey Kichenko
 * @created 23.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class CashDepositNominalDto extends AbstractDto {

    private static final long serialVersionUID = 4344180306238101554L;

    protected final long nominalId;
    protected final int quantity;
    protected final int delta;
}
