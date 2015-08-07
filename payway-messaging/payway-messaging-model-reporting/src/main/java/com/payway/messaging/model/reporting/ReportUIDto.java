/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting;

import com.payway.messaging.model.AbstractDto;
import com.payway.messaging.model.reporting.ui.ComponentStateDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * ReportUIDto
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public final class ReportUIDto extends AbstractDto {

    private static final long serialVersionUID = 4353900234907820219L;

    private final long reportId;
    private final String reportName;
     private final String reportDescription;
    private final ComponentStateDto reportForm;
    private final List<ReportExportFormatTypeDto> formats;
}
