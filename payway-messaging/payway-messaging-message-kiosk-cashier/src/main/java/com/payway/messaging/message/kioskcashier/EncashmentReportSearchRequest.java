/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.request.command.CommandRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * EncashmentReportSearchRequest
 *
 * @author Sergey Kichenko
 * @created 03.07.15 00:00
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class EncashmentReportSearchRequest extends CommandRequest {

    private static final long serialVersionUID = 7436126340351870702L;

    private String terminalName;
    private int reportNo;
}
