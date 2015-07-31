/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.model.kioskcashier.KioskEncashmentDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * AbstractEncashmentReportSearchResponse
 *
 * @author Sergey Kichenko
 * @created 31.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public abstract class AbstractEncashmentReportSearchResponse implements SuccessResponse {

    private static final long serialVersionUID = 2857309521469739061L;

    private final List<KioskEncashmentDto> kioskEncashments;
}
