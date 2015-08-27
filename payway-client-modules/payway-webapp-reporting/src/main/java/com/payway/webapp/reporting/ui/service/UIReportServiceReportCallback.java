/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.ui.service;

import com.payway.messaging.model.reporting.ReportExportFormatTypeDto;
import com.payway.messaging.model.reporting.ReportParameterDto;
import java.util.List;

/**
 * UIReportServiceReportCallback
 *
 * @author Sergey Kichenko
 * @created 14.08.2015
 */
public interface UIReportServiceReportCallback extends UIReportServiceCallback {

    void metadata(long id, String name, boolean ignorePagination, ReportExportFormatTypeDto format, List<ReportParameterDto> params);

    void response(String fileName, byte[] content);
}
