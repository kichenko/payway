/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.kioskcashier;

import com.payway.messaging.model.AbstractDto;
import com.payway.messaging.model.common.BanknoteTypeDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * BanknoteNominalDto
 *
 * @author Sergey Kichenko
 * @created 06.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class BanknoteNominalDto extends AbstractDto {

    private static final long serialVersionUID = 1968937770305449573L;

    private final BanknoteTypeDto banknoteType;
    private final String label;
    private final double nominal;

    public BanknoteNominalDto(long id, BanknoteTypeDto banknoteType, String label, double nominal) {
        this(banknoteType, label, nominal);
        setId(id);
    }
}
