/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.kioskcashier;

import com.payway.messaging.model.AbstractDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * CountingDiscrepancyDto
 *
 * @author Sergey Kichenko
 * @created 03.07.15 00:00
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class CountingDiscrepancyDto extends AbstractDto {

    private static final long serialVersionUID = -39220935973348395L;

    private final ClarificationTypeDto clarificationType;
    private final String clarification;
    private final double amount;

    public CountingDiscrepancyDto(ClarificationTypeDto clarificationType, String clarification, double amount) {
        this.clarificationType = clarificationType;
        this.clarification = clarification;
        this.amount = amount;
    }
}
