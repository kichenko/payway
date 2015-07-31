/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import java.util.Date;
import lombok.Getter;
import lombok.ToString;

/**
 * QuickEncashmentReportSearchRequest
 *
 * @author Sergey Kichenko
 * @created 31.07.15 00:00
 */
@Getter
@ToString(callSuper = true)
public class QuickEncashmentReportSearchRequest extends AbstractEncashmentReportSearchRequest {

    private static final long serialVersionUID = -4701374182052057084L;

    private final Date date;

    public QuickEncashmentReportSearchRequest(String sessionId, String terminalName, int reportNo, Date date) {
        super(sessionId, terminalName, reportNo);
        this.date = date;
    }
}
