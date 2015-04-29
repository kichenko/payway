/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.messaging;

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

    /**
     * Сервис контекста сообщений
     */
    private MessageRequestContextHolderService serviceContext;

    private ResponseEnvelope envelope;

    public MessageServerResponseHandler(ResponseEnvelope envelope) {
        setEnvelope(envelope);
    }

    @Override
    public void run() {
        try {
            log.info("Start processing the response message from the server");
            if (envelope != null) {
                log.info("Start of message processing from the server");
                MessageContextImpl msgContext = (MessageContextImpl) serviceContext.remove(envelope.getCorrelationID());
                if (msgContext != null) {
                    if (msgContext.getCallback() != null) {
                        Response rsp = (Response) envelope.getBody().getMessage();
                        if (rsp instanceof SuccessResponse) {
                            msgContext.getCallback().onServerResponse((SuccessResponse) rsp);
                        } else if (rsp instanceof ExceptionResponse) {
                            msgContext.getCallback().onServerException((ExceptionResponse) rsp);
                        }
                    } else {
                        log.info("Invalid context data");
                    }
                } else {
                    log.info("Not found in the context of the reply message server");
                }
                log.info("End of message processing from the server");
            } else {
                log.info("An empty message from the server");
            }
            log.info("End processing the response message from the server");
        } catch (Exception ex) {
            log.error("Error processing the response from the server", ex);
        }
    }
}
