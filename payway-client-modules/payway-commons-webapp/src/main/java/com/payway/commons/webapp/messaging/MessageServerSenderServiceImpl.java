/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging;

import com.payway.commons.webapp.messaging.client.IMessagingClient;
import com.payway.commons.webapp.messaging.client.IMessagingQueue;
import com.payway.messaging.core.RequestEnvelope;
import com.payway.messaging.core.request.Request;
import com.payway.messaging.message.request.auth.AuthCommandRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

/**
 * Реализация сервиса отправки сообщений на сервер.
 *
 * @author Sergey Kichenko
 * @created 27.04.15 00:00
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class MessageServerSenderServiceImpl implements MessageServerSenderService {

    private IMessagingClient messagingClient;

    private MessageRequestContextHolderService serviceContext;

    private Long timeOut;

    private TimeUnit timeUnit;

    @Value("${app.id}")
    private String appId;

    @Override
    public void auth(String userName, String password, String remoteAddress, ResponseCallBack callback) {
        sendMessage(new AuthCommandRequest(userName, password, appId, remoteAddress), callback);
    }

    @Override
    public void sendMessage(Request request, ResponseCallBack callback) {

        RequestEnvelope envelope = new RequestEnvelope();

        try {

            IMessagingQueue<RequestEnvelope> serverQueue;

            log.info("Preparing a message to send to the server");

            serviceContext.put(envelope.getMessageId(), new MessageContextImpl(envelope.getMessageId(), callback));

            envelope.setOrigin(messagingClient.toString());
            envelope.setReplyTo(messagingClient.getClientQueue().getName());
            envelope.setBody(request);

            serverQueue = messagingClient.<RequestEnvelope>getServerQueue();

            log.info("Sending a message to the server");
            if (log.isDebugEnabled()) {
                log.debug("Server queue={} ", serverQueue);
                log.debug("Client queue name={} ", messagingClient.getClientQueue().getName());
                log.debug("Envelope={} ", envelope);
            }

            serverQueue.offer(envelope, timeOut, timeUnit);
            log.info("Message sent to the server");

        } catch (Exception ex) {
            log.error("Bad sending message to the server", ex);
            try {
                MessageContextImpl ctx = (MessageContextImpl) serviceContext.remove(envelope.getMessageId());
                if (ctx != null) {
                    ResponseCallBack cb = ctx.getCallback();
                    if (cb != null) {
                        cb.onLocalException(ex);
                    }
                }
            } catch (Exception e) {
                log.error("Bad clearing fail message", e);
            }
        }
    }
}
