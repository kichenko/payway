/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting;

import com.payway.messaging.model.AbstractDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * ReportExecuteParamsDto
 *
 * @author Sergey Kichenko
 * @created 06.08.2015
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public class ReportExecuteParamsDto extends AbstractDto {

    private static final long serialVersionUID = -3719423264537700550L;

    private final long reportId;
    private final boolean ignorePagination;
    private final ReportExportFormatTypeDto format;
    private final List<ReportParameterDto> params;
}
