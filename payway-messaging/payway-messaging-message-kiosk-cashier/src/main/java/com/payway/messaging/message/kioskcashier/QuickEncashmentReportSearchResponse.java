/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.model.kioskcashier.KioskEncashmentDto;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * QuickEncashmentReportSearchResponse
 *
 * @author Sergey Kichenko
 * @created 31.07.15 00:00
 */
@Getter
@ToString(callSuper = true)
public class QuickEncashmentReportSearchResponse extends AbstractEncashmentReportSearchResponse {

    private static final long serialVersionUID = -3277905000209895720L;

    public QuickEncashmentReportSearchResponse(List<KioskEncashmentDto> kioskEncashments) {
        super(kioskEncashments);
    }
}
