/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.ui.service;

/**
 * UIReportService
 *
 * @author Sergey Kichenko
 * @created 07.08.2015
 */
public interface UIReportService {

    void execute(final long reportId, final UIReportServiceCallback callback);
}
