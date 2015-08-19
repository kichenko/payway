/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging;

import com.google.common.util.concurrent.SettableFuture;
import com.payway.commons.webapp.messaging.client.MessagingClient;
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

    private MessagingClient messagingClient;

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

        final RequestEnvelope envelope = new RequestEnvelope();

        try {

            getServiceContext().put(envelope.getMessageId(), new MessageContextImpl(request.getTimeout(), callback));

            envelope.setOrigin(getMessagingClient().toString());
            envelope.setReplyTo(getMessagingClient().getClientQueueName());
            envelope.setBody(request);

            log.debug("Try send message to the server...");
            log.debug("Server queue = [{}] ", getMessagingClient().getServerQueueName());
            log.debug("Client queue = [{}] ", getMessagingClient().getClientQueueName());
            log.debug("Envelope = [{}] ", envelope);

            getMessagingClient().getServerQueue().offer(envelope, timeOut, timeUnit);
            log.debug("Message successfully sent to the server");

        } catch (Exception ex) {
            log.error("Cannot send message to the server - [{}]", ex);
            try {
                MessageContext ctx = getServiceContext().remove(envelope.getMessageId());
                if (ctx != null) {
                    ResponseCallBack cb = ctx.getCallback();
                    if (cb != null) {
                        cb.onLocalException(ex);
                    }
                }
            } catch (Exception e) {
                log.error("Cannot cleanup context map with message - [{}]", e);
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
