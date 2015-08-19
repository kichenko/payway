/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.ui.service;

/**
 * UIReportServiceReportCallback
 *
 * @author Sergey Kichenko
 * @created 14.08.2015
 */
public interface UIReportServiceReportCallback extends UIReportServiceCallback {

    void metadata(long id, String name);

    void response(String fileName, byte[] content);
}
