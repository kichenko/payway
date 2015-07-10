/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.kioskcashier;

import com.payway.messaging.model.IdentifiableDto;
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
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class BanknoteNominalEncashmentDto extends IdentifiableDto {

    private static final long serialVersionUID = 1991467777897674558L;

    private final int quantity;

    public BanknoteNominalEncashmentDto(long id, int quantity) {
        super(id);
        this.quantity = quantity;
    }

}
