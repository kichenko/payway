/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.kioskcashier;

import com.payway.messaging.model.IdentifiableDto;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

/**
 * SettingsDto
 *
 * @author Sergey Kichenko
 * @created 03.07.15 00:00
 */
@Getter
@ToString(callSuper = true)
public final class KioskEncashmentDto extends IdentifiableDto {

    private static final long serialVersionUID = -9117690013357031851L;

    private final long terminalId;

    private final String terminalName;

    private final int seqNum;

    private final Date arrivedDate;

    private final Date occuredDate;

    public KioskEncashmentDto(long id, long terminalId, String terminalName, int seqNum, Date arrivedDate, Date occuredDate) {
        super(id);
        this.terminalId = terminalId;
        this.terminalName = terminalName;
        this.seqNum = seqNum;
        this.arrivedDate = arrivedDate;
        this.occuredDate = occuredDate;
    }

}
