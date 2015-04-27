/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.messaging.client;

import com.payway.messaging.core.ResponseEnvelope;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.Response;
import com.payway.messaging.core.response.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Обработчик ответов с сервера.
 *
 * @author Sergey Kichenko
 * @created 27.04.15 00:00
 */
@NoArgsConstructor
@AllArgsConstructor
public class MessageServerResponseHandler implements Runnable {

    private MessageRequestContextHolderServiceImpl serviceContext;
    private ResponseEnvelope envelope;

    @Override
    public void run() {
        if (envelope != null) {
            MessageRequestContextHolderServiceImpl.MessageContext msgContext = serviceContext.get(envelope.getCorrelationID().getValue());
            if (msgContext != null) {
                if (msgContext.getCallback() != null) {
                    Response rsp = (Response) envelope.getBody().getMessage();
                    if (rsp instanceof SuccessResponse) {
                        msgContext.getCallback().onResponse((SuccessResponse) rsp);
                    } else if (rsp instanceof ExceptionResponse) {
                        msgContext.getCallback().onException((ExceptionResponse) rsp);
                    }
                }
            }
        }
    }
}
