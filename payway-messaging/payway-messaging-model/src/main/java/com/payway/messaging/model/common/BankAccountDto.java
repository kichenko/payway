/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.common;

import com.payway.messaging.model.IdentifiableDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * BankAccountDto
 *
 * @author Sergey Kichenko
 * @created 21.07.15 00:00
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class BankAccountDto extends IdentifiableDto {

    private static final long serialVersionUID = -229897544356290552L;

    private final String account;

    public BankAccountDto(long id, String account) {
        super(id);
        this.account = account;
    }
}
