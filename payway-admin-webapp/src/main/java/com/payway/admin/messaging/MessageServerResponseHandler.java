/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.messaging;

import com.payway.messaging.core.ResponseEnvelope;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.Response;
import com.payway.messaging.core.response.SuccessResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Обработчик полученных ответов с сервера.
 *
 * @author Sergey Kichenko
 * @created 27.04.15 00:00
 */
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class MessageServerResponseHandler implements Runnable {

    private MessageRequestContextHolderServiceImpl serviceContext;
    private ResponseEnvelope envelope;

    public MessageServerResponseHandler(ResponseEnvelope envelope) {
        setEnvelope(envelope);
    }

    @Override
    public void run() {
        try {
            if (envelope != null) {
                MessageRequestContextHolderServiceImpl.MessageContext msgContext = serviceContext.remove(envelope.getCorrelationID().getValue());
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
        } catch (Exception ex) {
            log.error("Ошибка обработки ответа с сервера", ex);
        }
    }
}
