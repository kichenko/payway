/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.server.messaging;

import com.hazelcast.core.HazelcastInstance;
import com.payway.messaging.core.RequestEnvelope;
import com.payway.messaging.core.ResponseEnvelope;
import com.payway.messaging.core.response.exception.common.CommonExceptionResponse;
import com.payway.messaging.message.advertising.AdvertisingApplyConfigurationRequest;
import com.payway.messaging.message.advertising.AdvertisingApplySuccessConfigurationResponse;
import com.payway.messaging.message.advertising.AdvertisingSettingsRequest;
import com.payway.messaging.message.advertising.AdvertisingSettingsResponse;
import com.payway.messaging.message.request.auth.AuthCommandRequest;
import com.payway.messaging.message.response.auth.AuthSuccessCommandResponse;
import com.payway.messaging.model.advertising.SettingsDto;
import com.payway.messaging.model.user.UserDto;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class MessageServerRequestHandler implements Runnable {

    @Autowired
    HazelcastInstance hzInstance;

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
                        String rid = envelope.getMessageId();
                        String origin = hzInstance.toString();

                        if (envelope.getBody() instanceof AuthCommandRequest) {
                            env = new ResponseEnvelope(rid, origin, new AuthSuccessCommandResponse(new UserDto(((AuthCommandRequest) envelope.getBody()).getUserName(), null, null), null));
                        } else if (envelope.getBody() instanceof AdvertisingApplyConfigurationRequest) {
                            env = new ResponseEnvelope(rid, origin, new AdvertisingApplySuccessConfigurationResponse());
                        } else if (envelope.getBody() instanceof AdvertisingSettingsRequest) {
                            env = new ResponseEnvelope(rid, origin, new AdvertisingSettingsResponse(new SettingsDto("file:///${user.home}/var/apps/server/original-cfg")));
                        } else {
                            env = new ResponseEnvelope(rid, origin, new CommonExceptionResponse("Bad Request Type: " + envelope.getBody().getClass().getName()));
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
