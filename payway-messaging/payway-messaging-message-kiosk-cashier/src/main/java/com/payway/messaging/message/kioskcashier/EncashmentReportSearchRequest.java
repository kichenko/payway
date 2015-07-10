/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.message.SessionCommandRequest;
import lombok.Getter;
import lombok.ToString;

/**
 * EncashmentReportSearchRequest
 *
 * @author Sergey Kichenko
 * @created 03.07.15 00:00
 */
@Getter
@ToString(callSuper = true)
public final class EncashmentReportSearchRequest extends SessionCommandRequest {
    
    private static final long serialVersionUID = 7436126340351870702L;
    
    private final String terminalName;

    private final int reportNo;
    
    public EncashmentReportSearchRequest(String sessionId, String terminalName, int reportNo) {
        super(sessionId);
        this.terminalName = terminalName;
        this.reportNo = reportNo;
    }

}
