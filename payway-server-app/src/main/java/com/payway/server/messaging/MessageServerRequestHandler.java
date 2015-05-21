/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.server.messaging;

import com.payway.messaging.core.Body;
import com.payway.messaging.core.RequestEnvelope;
import com.payway.messaging.core.ResponseEnvelope;
import com.payway.messaging.core.response.exception.common.CommonExceptionResponse;
import com.payway.messaging.core.service.DistributedObjectService;
import com.payway.messaging.message.request.auth.AuthCommandRequest;
import com.payway.messaging.message.request.configuration.ApplyConfigurationRequest;
import com.payway.messaging.message.response.auth.AuthSuccessComandResponse;
import com.payway.messaging.message.response.configuration.ApplySuccessConfigurationResponse;
import com.payway.messaging.model.message.auth.UserDto;
import com.payway.messaging.model.message.settings.SettingsDto;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;

/**
 * Обработчик входящих сообщений. Прототип т.к. хранит в себе конверт сообщения.
 *
 * @author Sergey Kichenko
 * @created 24.04.15 00:00
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class MessageServerRequestHandler implements Runnable {

    /**
     * Конверт с сообшением
     */
    private RequestEnvelope envelope;

    /**
     * Сервис для работы с распределенными объектами
     */
    private DistributedObjectService dosService;

    /**
     * Время ожидания отправки ответа
     */
    private Long timeOut;

    /**
     * Единица время ожидания отправки ответа
     */
    private TimeUnit timeUnit;

    public MessageServerRequestHandler(RequestEnvelope envelope) {
        setEnvelope(envelope);
    }

    /**
     * Обработка, выполнение бизнес-логики и отправка ответного сообщения.
     */
    @Override
    public void run() {
        try {
            log.info("Start processing the request message from the client");
            if (envelope != null) {
                log.info("Start of message processing from the client");
                if (log.isDebugEnabled()) {
                    log.debug("Envelope={} ", envelope);
                }
                if (envelope.getReplyTo() != null && !envelope.getReplyTo().isEmpty()) {
                    BlockingQueue<ResponseEnvelope> clientQueue = (BlockingQueue<ResponseEnvelope>) dosService.getQueueByName(envelope.getReplyTo());
                    if (clientQueue != null) {

                        ResponseEnvelope env = null;

                        String msgID = UUID.randomUUID().toString();
                        LocalDateTime dateCreate = new LocalDateTime();
                        LocalDateTime dateExpired = new LocalDateTime();
                        String correlationMsgID = envelope.getMessageID();

                        if (envelope.getBody().getMessage() instanceof AuthCommandRequest) {
                            env = new ResponseEnvelope(msgID, dateCreate, dateExpired, correlationMsgID, new Body(new AuthSuccessComandResponse<>(new UserDto(((AuthCommandRequest) envelope.getBody().getMessage()).getUser().getUsername(), ((AuthCommandRequest) envelope.getBody().getMessage()).getUser().getPassword(), ((AuthCommandRequest) envelope.getBody().getMessage()).getUser().getUsername(), false, new SettingsDto("server-config")))));
                        } else if (envelope.getBody().getMessage() instanceof ApplyConfigurationRequest) {
                            env = new ResponseEnvelope(msgID, dateCreate, dateExpired, correlationMsgID, new Body(new ApplySuccessConfigurationResponse()));
                        } else {
                            env = new ResponseEnvelope(msgID, dateCreate, dateExpired, correlationMsgID, new Body(new CommonExceptionResponse()));
                        }

                        log.info("Sending a response to the client");
                        clientQueue.offer(env, timeOut, timeUnit);
                        log.info("The response is sent to the client");
                    } else {
                        log.error("Failed to get the queue to send a response to the client");
                    }
                } else {
                    log.info("Unknown return address of the client request");
                }
                log.info("End of message processing from the client");
            }
            log.info("End processing the request message from the client");
        } catch (Exception ex) {
            log.error("Error processing the request from the client", ex);
        }
    }
}
