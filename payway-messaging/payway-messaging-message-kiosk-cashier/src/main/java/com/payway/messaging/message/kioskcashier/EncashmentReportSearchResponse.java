/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.model.kioskcashier.BanknoteNominalDto;
import com.payway.messaging.model.kioskcashier.KioskEncashmentDto;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * EncashmentReportSearchResponse
 *
 * @author Sergey Kichenko
 * @created 03.07.15 00:00
 */
@Getter
@ToString(callSuper = true)
public final class EncashmentReportSearchResponse implements SuccessResponse {

    private static final long serialVersionUID = 8950351195681515060L;

    private final KioskEncashmentDto kioskEncashment;

    private final List<BanknoteNominalDto> nominals;

    public EncashmentReportSearchResponse(KioskEncashmentDto kioskEncashment, List<BanknoteNominalDto> nominals) {
        this.kioskEncashment = kioskEncashment;
        this.nominals = nominals;
    }

}
