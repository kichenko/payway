/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

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
public final class EncashmentReportSearchResponse extends AbstractEncashmentReportSearchResponse {

    private static final long serialVersionUID = 8950351195681515060L;

    private final List<BanknoteNominalDto> nominals;

    public EncashmentReportSearchResponse(List<KioskEncashmentDto> kioskEncashments, List<BanknoteNominalDto> nominals) {
        super(kioskEncashments);
        this.nominals = nominals;
    }
}
