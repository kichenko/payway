/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.server.messaging;

import com.payway.messaging.core.Body;
import com.payway.messaging.core.RequestEnvelope;
import com.payway.messaging.core.ResponseEnvelope;
import com.payway.messaging.core.header.CorrelationIDHeader;
import com.payway.messaging.core.header.DateExpiredHeader;
import com.payway.messaging.core.header.DateHeader;
import com.payway.messaging.core.header.MessageIDHeader;
import com.payway.messaging.core.service.DistributedObjectService;
import com.payway.messaging.message.response.auth.AuthSuccessComandResponse;
import com.payway.model.messaging.auth.UserImpl;
import java.util.concurrent.BlockingQueue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Обработчик входящих сообщений. Прототип т.к. хранит в себе конверт сообщения.
 *
 * @author Sergey Kichenko
 * @created 24.04.15 00:00
 */
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class MessageServerRequestHandler implements Runnable {

    /**
     * Конверт с сообшением
     */
    private RequestEnvelope envelope;

    /**
     * Сервис для работы с распределенными объектами
     */
    private DistributedObjectService dosService;

    public MessageServerRequestHandler(RequestEnvelope envelope) {
        setEnvelope(envelope);
    }

    /**
     * Обработка, выполнение бизнес-логики и отправка ответного сообщения.
     */
    @Override
    public void run() {
        try {
            if (envelope != null) {
                BlockingQueue<ResponseEnvelope> clientQueue = (BlockingQueue<ResponseEnvelope>) dosService.getQueueByName(envelope.getReplyTo().getValue());
                if (clientQueue != null) {
                    clientQueue.offer(new ResponseEnvelope(
                      new MessageIDHeader(),
                      new DateHeader(),
                      new DateExpiredHeader(),
                      new CorrelationIDHeader(envelope.getMessageID().getValue()),
                      new Body(new AuthSuccessComandResponse<>(new UserImpl("example", "example", "example", Boolean.TRUE, null))))
                    );
                }
            }
        } catch (Exception ex) {
            log.error("Ошибка обработки запроса", ex);
        }
    }
}
