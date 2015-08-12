/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.ui.service.impl;

import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.messaging.UIResponseCallBackSupport;
import com.payway.commons.webapp.service.app.user.WebAppUserService;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.reporting.ExecuteReportRequest;
import com.payway.messaging.message.reporting.ExecuteReportResponse;
import com.payway.messaging.message.reporting.GenerateReportParametersUIRequest;
import com.payway.messaging.message.reporting.GenerateReportParametersUIResponse;
import com.payway.messaging.model.reporting.ReportExecuteParamsDto;
import com.payway.messaging.model.reporting.ReportExportFormatTypeDto;
import com.payway.messaging.model.reporting.ReportParameterDto;
import com.payway.webapp.reporting.ui.dialog.StandartReportParameterDialog;
import com.payway.webapp.reporting.ui.service.UIReportService;
import com.payway.webapp.reporting.ui.service.UIReportServiceCallback;
import com.vaadin.ui.UI;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * DefaultUIReportService
 *
 * @author Sergey Kichenko
 * @created 07.08.2015
 */
@Slf4j
@org.springframework.stereotype.Component(value = "app.reporting.ui.service.DefaultUIReportService")
public class DefaultUIReportService implements UIReportService {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MessageServerSenderService service;

    @Autowired
    protected WebAppUserService webAppUserService;

    @Override
    public void execute(final long reportId, final UIReportServiceCallback callback) {

        if (callback != null) {
            callback.begin();
        }

        //send report ui-params request
        service.sendMessage(new GenerateReportParametersUIRequest(reportId), new UIResponseCallBackSupport(UI.getCurrent(), new UIResponseCallBackSupport.ResponseCallBackHandler() {

            @Override
            public void doServerResponse(SuccessResponse response) {

                if (response instanceof GenerateReportParametersUIResponse) {

                    try {

                        GenerateReportParametersUIResponse rsp = (GenerateReportParametersUIResponse) response;
                        StandartReportParameterDialog dialog = (StandartReportParameterDialog) applicationContext.getBean(StandartReportParameterDialog.BEAN_NAME, rsp.getReportUi());

                        dialog.show(new StandartReportParameterDialog.ExecuteCallback() {

                            @Override
                            public void execute(long reportId, ReportExportFormatTypeDto format, List<ReportParameterDto> params) {

                                if (callback != null) {
                                    callback.begin();
                                }

                                //send execute report request
                                service.sendMessage(new ExecuteReportRequest(webAppUserService.getUser().getSessionId(), new ReportExecuteParamsDto(reportId, format, params)), new UIResponseCallBackSupport(UI.getCurrent(), new UIResponseCallBackSupport.ResponseCallBackHandler() {

                                    @Override
                                    public void doServerResponse(SuccessResponse response) {

                                        if (response instanceof ExecuteReportResponse) {
                                            ExecuteReportResponse rsp = (ExecuteReportResponse) response;
                                            if (callback != null) {
                                                callback.response(rsp.getFileName(), rsp.getContent());
                                                callback.end();
                                            }
                                        } else {
                                            processException(String.format("Cannot execute report, unknown response - [%s]", response), callback);
                                        }
                                    }

                                    @Override
                                    public void doServerException(ExceptionResponse exception) {
                                        processException(String.format("Cannot process response of execute report, exception=[%s]", exception), callback);
                                    }

                                    @Override
                                    public void doLocalException(Exception exception) {
                                        processException(String.format("Cannot process response of execute report, exception=[%s]", exception), callback);
                                    }

                                    @Override
                                    public void doTimeout() {
                                        processException(String.format("Cannot process response of execute report, timeout exception"), callback);
                                    }
                                }));
                            }

                            @Override
                            public void error(Exception ex) {
                                processException(String.format("Cannot execute report, exception - [%s]", ex), callback);
                            }
                        });

                        if (callback != null) {
                            callback.end();
                        }

                    } catch (Exception ex) {
                        processException(String.format("Cannot execute report, exception - [%s]", ex), callback);
                    }
                } else {
                    processException(String.format("Cannot show report parameter dialog, unknown response - [%s]", response), callback);
                }
            }

            @Override
            public void doServerException(ExceptionResponse exception) {
                processException(String.format("Cannot show report parameter dialog, exception=[%s]", exception), callback);
            }

            @Override
            public void doLocalException(Exception exception) {
                processException(String.format("Cannot show report parameter dialog, exception=[%s]", exception), callback);
            }

            @Override
            public void doTimeout() {
                processException(String.format("Cannot show report parameter dialog, timeout exception"), callback);
            }
        }));
    }

    private void processException(String msg, UIReportServiceCallback callback) {
        log.error(msg);
        if (callback != null) {
            callback.exception(new Exception(msg));
            callback.end();
        }
    }
}
