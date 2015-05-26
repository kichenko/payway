/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.messaging;

import com.hazelcast.core.HazelcastInstance;
import com.payway.messaging.core.RequestEnvelope;
import com.payway.messaging.core.request.Request;
import com.payway.messaging.message.request.auth.AuthCommandRequest;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    HazelcastInstance hzInstance;

    /**
     * Серверная очередь
     */
    private BlockingQueue<RequestEnvelope> serverQueue;

    /**
     * Сервис контекста сообщений
     */
    private MessageRequestContextHolderService serviceContext;

    /**
     * Имя клиентской очереди
     */
    public String clientQueueName;

    /**
     * Время ожидания отправки ответа
     */
    private Long timeOut;

    /**
     * Единица время ожидания отправки ответа
     */
    private TimeUnit timeUnit;

    @Override
    public void auth(String userName, String password, ResponseCallBack callback) {
        sendMessage(new AuthCommandRequest(userName, password), callback);
    }

    @Override
    public void sendMessage(Request request, ResponseCallBack callback) {

        log.info("Preparing a message to send to the server");
        RequestEnvelope re = new RequestEnvelope(hzInstance.toString(), getClientQueueName(), request);
        serviceContext.put(re.getMessageId(), new MessageContextImpl(re.getMessageId(), callback));

        try {
            log.info("Sending a message to the server");
            if (log.isDebugEnabled()) {
                log.debug("Server queue={} ", serverQueue);
                log.debug("Client queue name={} ", clientQueueName);
                log.debug("Envelope={} ", re);
            }
            serverQueue.offer(re, timeOut, timeUnit);
            log.info("Message sent to the server");
        } catch (Exception ex) {
            try {
                MessageContextImpl ctx = (MessageContextImpl) serviceContext.remove(re.getMessageId());
                if (ctx != null) {
                    ResponseCallBack cb = ctx.getCallback();
                    if (cb != null) {
                        cb.onLocalException(ex);
                    }
                }
            } catch (Exception e) {
                log.error("Error information about sending a message to the server", e);
            }

            log.error("Error sending message to the server", ex);
        }
    }
}
