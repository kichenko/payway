/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging;

import com.payway.messaging.core.ResponseEnvelope;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.Response;
import com.payway.messaging.core.response.SuccessResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * MessageServerResponseHandler
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
            log.debug("Start processing the response message from the server");
            if (envelope != null) {
                log.debug("Start of message processing from the server");
                log.debug("Envelope={} ", envelope);
                MessageContext msgContext = (MessageContext) serviceContext.remove(envelope.getRequestId());
                if (msgContext != null) {
                    if (msgContext.getCallback() != null) {
                        Response rsp = (Response) envelope.getBody();
                        if (rsp instanceof SuccessResponse) {
                            msgContext.getCallback().onServerResponse((SuccessResponse) rsp);
                        } else if (rsp instanceof ExceptionResponse) {
                            msgContext.getCallback().onServerException((ExceptionResponse) rsp);
                        }
                    } else {
                        log.debug("Invalid context data");
                    }
                } else {
                    log.debug("Not found in the context of the reply message server");
                }
                log.debug("End of message processing from the server");
            } else {
                log.debug("An empty message from the server");
            }
            log.debug("End processing the response message from the server");
        } catch (Exception ex) {
            log.error("Error processing the response from the server", ex);
        }
    }
}
