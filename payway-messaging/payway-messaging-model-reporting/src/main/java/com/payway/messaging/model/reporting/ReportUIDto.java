/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting;

import com.payway.messaging.model.AbstractDto;
import com.payway.messaging.model.reporting.ui.ComponentStateDto;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * ReportUIDto
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@ToString(callSuper = true)
public final class ReportUIDto extends AbstractDto {

    private static final long serialVersionUID = 4353900234907820219L;

    private final long reportId;

    private final String reportName;

    private final String reportDescription;

    private final ComponentStateDto reportForm;

    private final List<ReportExportFormatTypeDto> formats;

    public ReportUIDto(long reportId, String reportName, String reportDescription, ComponentStateDto reportForm, List<ReportExportFormatTypeDto> formats) {
        this.reportId = reportId;
        this.reportName = reportName;
        this.reportDescription = reportDescription;
        this.reportForm = reportForm;
        this.formats = formats;
    }

    public long getReportId() {
        return reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public ComponentStateDto getReportForm() {
        return reportForm;
    }

    public List<ReportExportFormatTypeDto> getFormats() {
        return formats;
    }

}
