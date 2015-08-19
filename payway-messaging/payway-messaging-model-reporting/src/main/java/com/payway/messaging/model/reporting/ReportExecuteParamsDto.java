/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting;

import com.payway.messaging.model.AbstractDto;
import lombok.ToString;

import java.util.List;

/**
 * ReportExecuteParamsDto
 *
 * @author Sergey Kichenko
 * @created 06.08.2015
 */
@ToString(callSuper = true)
public class ReportExecuteParamsDto extends AbstractDto {

    private static final long serialVersionUID = -3719423264537700550L;

    private final long reportId;

    private final boolean ignorePagination;

    private final ReportExportFormatTypeDto format;

    private final List<ReportParameterDto> params;

    public ReportExecuteParamsDto(long reportId, boolean ignorePagination, ReportExportFormatTypeDto format, List<ReportParameterDto> params) {
        this.reportId = reportId;
        this.ignorePagination = ignorePagination;
        this.format = format;
        this.params = params;
    }

    public long getReportId() {
        return reportId;
    }

    public boolean isIgnorePagination() {
        return ignorePagination;
    }

    public ReportExportFormatTypeDto getFormat() {
        return format;
    }

    public List<ReportParameterDto> getParams() {
        return params;
    }

}
