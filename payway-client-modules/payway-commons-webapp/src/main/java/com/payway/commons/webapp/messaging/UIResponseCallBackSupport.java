/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging;

import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.vaadin.ui.UI;
import java.lang.ref.WeakReference;
import lombok.extern.slf4j.Slf4j;

/**
 * UIResponseCallBackSupport
 *
 * @author Sergey Kichenko
 * @created 11.06.15 00:00
 */
@Slf4j
public final class UIResponseCallBackSupport implements ResponseCallBack {

    public interface ResponseCallBackHandler {

        void doServerResponse(final SuccessResponse response);

        void doServerException(final ExceptionResponse exception);

        void doLocalException(final Exception exception);

        void doTimeout();
    }

    private final WeakReference<UI> ui;
    private final ResponseCallBackHandler handler;

    public UIResponseCallBackSupport(final UI ui, final ResponseCallBackHandler handler) {
        this.ui = new WeakReference(ui);
        this.handler = handler;
    }

    @Override
    public void onServerResponse(final SuccessResponse response) {
        if (ui.get() != null) {
            ui.get().access(new Runnable() {
                @Override
                public void run() {
                    if (handler != null) {
                        handler.doServerResponse(response);
                    } else {
                        log.error("Empty response callback handler");
                    }
                }
            });
        } else {
            log.error("Empty ui on success server response");
        }
    }

    @Override
    public void onServerException(final ExceptionResponse exception) {
        if (ui.get() != null) {
            ui.get().access(new Runnable() {
                @Override
                public void run() {
                    if (handler != null) {
                        handler.doServerException(exception);
                    } else {
                        log.error("Empty response callback handler");
                    }
                }
            });
        } else {
            log.error("Empty ui on exception server response");
        }
    }

    @Override
    public void onLocalException(final Exception exception) {
        if (ui.get() != null) {
            ui.get().access(new Runnable() {
                @Override
                public void run() {
                    if (handler != null) {
                        handler.doLocalException(exception);
                    } else {
                        log.error("Empty response callback handler");
                    }
                }
            });
        } else {
            log.error("Empty ui on server local exception response");
        }
    }

    @Override
    public void onTimeout() {
        if (ui.get() != null) {
            ui.get().access(new Runnable() {
                @Override
                public void run() {
                    if (handler != null) {
                        handler.doTimeout();
                    } else {
                        log.error("Empty response callback handler");
                    }
                }
            });
        } else {
            log.error("Empty ui on server response timeout");
        }
    }
}
