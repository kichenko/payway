/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.messaging;

import com.payway.messaging.core.Body;
import com.payway.messaging.core.RequestEnvelope;
import com.payway.messaging.core.request.Request;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;

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
    public void sendMessage(Request request, ResponseCallBack callback) {

        String msgID = UUID.randomUUID().toString();
        LocalDateTime dateCreate = new LocalDateTime();
        LocalDateTime expiredDate = new LocalDateTime();

        try {
            log.info("Preparing a message to send to the server");
            RequestEnvelope envelope = new RequestEnvelope(msgID, dateCreate, expiredDate, getClientQueueName(), new Body(request));
            serviceContext.put(envelope.getMessageID(), new MessageContextImpl(envelope.getMessageID(), callback));
            log.info("Sending a message to the server");
            serverQueue.offer(envelope, timeOut, timeUnit);
            log.info("Message sent to the server");
        } catch (Exception ex) {
            try {
                MessageContextImpl ctx = (MessageContextImpl) serviceContext.remove(msgID);
                if (ctx != null) {
                    ResponseCallBack cb = ctx.getCallback();
                    if (cb != null) {
                        cb.onLocalException();
                    }
                }
            } catch (Exception e) {
                log.error("Error information about sending a message to the server", e);
            }

            log.error("Error sending message to the server", ex);
        }
    }
}
