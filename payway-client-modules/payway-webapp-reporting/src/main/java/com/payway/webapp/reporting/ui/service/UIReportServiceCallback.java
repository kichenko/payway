/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.ui.service;

/**
 * UIReportServiceCallback
 *
 * @author Sergey Kichenko
 * @created 07.08.2015
 */
public interface UIReportServiceCallback {

    void begin();

    void end();

    void exception(Exception ex);

    void response(String fileName, byte[] content);
}
