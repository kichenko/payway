/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting;

/**
 * ReportExportFormatTypeDto
 *
 * @author Sergey Kichenko
 * @created 06.08.2015
 */
public enum ReportExportFormatTypeDto {

    HTML,
    PDF,
    XML,
    XLS,
    CSV,
    RTF,
    DOCX,
    XLSX;

    public String getCaption() {
        return name();
    }

    public int getId() {
        return ordinal();
    }
}
