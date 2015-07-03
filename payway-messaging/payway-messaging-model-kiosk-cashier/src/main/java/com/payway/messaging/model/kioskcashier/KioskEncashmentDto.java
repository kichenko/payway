/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.kioskcashier;

import com.payway.messaging.model.AbstractDto;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * SettingsDto
 *
 * @author Sergey Kichenko
 * @created 03.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public final class KioskEncashmentDto extends AbstractDto {

    private static final long serialVersionUID = -9117690013357031851L;

    private final long terminalId;
    private final String terminalName;
    private final int seqNum;
    private final Date arrivedDate;
    private final Date occuredDate;
}
