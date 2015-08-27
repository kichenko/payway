/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.kioskcashier.event;

import com.payway.messaging.model.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * EncashmentMissingAndWrongDetailDto
 *
 * @author Sergey Kichenko
 * @created 25.08.2015
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class EncashmentMissingAndWrongDetailDto extends AbstractDto {

    private static final long serialVersionUID = -6855157447800250414L;

    private final int seqNo;
    private final String terminalName;
    private final double amount;
}
