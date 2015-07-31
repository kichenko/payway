/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.message.SessionCommandRequest;
import lombok.Getter;
import lombok.ToString;

/**
 * AbstractEncashmentReportSearchRequest
 *
 * @author Sergey Kichenko
 * @created 03.07.15 00:00
 */
@Getter
@ToString(callSuper = true)
public abstract class AbstractEncashmentReportSearchRequest extends SessionCommandRequest {

    private static final long serialVersionUID = 7436126340351870702L;

    protected final String terminalName;

    protected final int reportNo;

    public AbstractEncashmentReportSearchRequest(String sessionId, String terminalName, int reportNo) {
        super(sessionId);
        this.terminalName = terminalName;
        this.reportNo = reportNo;
    }
}
