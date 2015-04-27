/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.messaging.client;

import com.payway.messaging.core.Body;
import com.payway.messaging.core.RequestEnvelope;
import com.payway.messaging.core.header.DateExpiredHeader;
import com.payway.messaging.core.header.DateHeader;
import com.payway.messaging.core.header.MessageIDHeader;
import com.payway.messaging.core.header.ReplyToHeader;
import com.payway.messaging.core.request.Request;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class MessageServerSenderServiceImpl implements MessageServerSenderService {

    private BlockingQueue<RequestEnvelope> serverRequestQueue;
    private MessageRequestContextHolderServiceImpl serviceContext;
    private Long timeOut;
    private TimeUnit timeUnit;
    public String responseQueueId;

    @Override
    public void sendMessage(Request request, ResponseCallBack callback) {
        try {
            RequestEnvelope envelope = new RequestEnvelope(new MessageIDHeader(), new DateHeader(), new DateExpiredHeader(), new ReplyToHeader(getResponseQueueId()), new Body(request));
            serviceContext.put(envelope.getMessageID().value(), new MessageRequestContextHolderServiceImpl.MessageContext(envelope.getMessageID().value(), callback));
            serverRequestQueue.offer(envelope, timeOut, timeUnit);
        } catch (Exception ex) {
            //
        }
    }
}
