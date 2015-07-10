/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.common;

import com.payway.messaging.model.IdentifiableDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * CurrencyDto
 *
 * @author sergey kichenko
 * @created 19.06.15 00:00
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class CurrencyDto extends IdentifiableDto {

    private static final long serialVersionUID = -2688718057650482496L;

    private final String name;

    private final String shotName;

    private final String iso;

    private final boolean defaultCurrency;

    public CurrencyDto(long id, String name, String shotName, String iso, boolean defaultCurrency) {
        super(id);
        this.name = name;
        this.shotName = shotName;
        this.iso = iso;
        this.defaultCurrency = defaultCurrency;
    }

}
