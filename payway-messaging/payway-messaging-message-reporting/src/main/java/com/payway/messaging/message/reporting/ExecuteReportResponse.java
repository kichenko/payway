/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.reporting;

import com.payway.messaging.core.response.SuccessResponse;
import lombok.Getter;
import lombok.ToString;

/**
 * ExecuteReportResponse
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@ToString(callSuper = true, exclude = {"content"})
public final class ExecuteReportResponse implements SuccessResponse {

    private static final long serialVersionUID = -6139587779688956792L;

    private final String fileName;

    private final byte[] content;

    public ExecuteReportResponse(String fileName, byte[] content) {
        this.fileName = fileName;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getContent() {
        return content;
    }

}
