/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging;

import com.google.common.util.concurrent.SettableFuture;
import com.payway.commons.webapp.messaging.client.IMessagingClient;
import com.payway.commons.webapp.messaging.client.IMessagingQueue;
import com.payway.messaging.core.RequestEnvelope;
import com.payway.messaging.core.request.Request;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.Response;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.request.auth.AuthLoginPasswordCommandRequest;
import com.payway.messaging.message.request.auth.AuthTokenCommandRequest;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * MessageServerSenderServiceImpl
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
        sendMessage(new AuthLoginPasswordCommandRequest(userName, password, appId, remoteAddress), callback);
    }

    @Override
    public void auth(String token, String remoteAddress, ResponseCallBack callback) {
        sendMessage(new AuthTokenCommandRequest(token, appId, remoteAddress), callback);
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

    @Override
    public Response sendMessage(Request request, long timeOut, TimeUnit timeUnit) throws Exception {

        final SettableFuture<Response> settableFuture = SettableFuture.<Response>create();
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        sendMessage(request, new ResponseCallBack() {

            @Override
            public void onServerResponse(SuccessResponse response) {
                settableFuture.set(response);
                countDownLatch.countDown();
            }

            @Override
            public void onServerException(ExceptionResponse ex) {
                settableFuture.setException(new Exception(ex.getMessage()));
                countDownLatch.countDown();
            }

            @Override
            public void onLocalException(Exception ex) {
                settableFuture.setException(ex);
                countDownLatch.countDown();
            }

            @Override
            public void onTimeout() {
                settableFuture.setException(new Exception("Time out"));
                countDownLatch.countDown();
            }
        });

        countDownLatch.await(timeOut, timeUnit);

        return settableFuture.get();
    }
}
