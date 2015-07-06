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
 * BanknoteNominalEncashmentDto
 *
 * @author Sergey Kichenko
 * @created 03.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class BanknoteNominalEncashmentDto extends AbstractDto {

    private static final long serialVersionUID = 1991467777897674558L;

    private final long banknoteNominalId;
    private final int quantity;
}
