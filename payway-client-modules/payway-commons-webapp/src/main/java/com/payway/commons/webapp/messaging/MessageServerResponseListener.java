/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging;

import com.payway.commons.webapp.messaging.client.IMessagingClient;
import com.payway.commons.webapp.messaging.client.IMessagingQueue;
import com.payway.messaging.core.ResponseEnvelope;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;

/**
 * Слушатель ответов с сервера. Запускается в потоке при старте приложения.
 * Синглтон, т.к. нужен один на все приложение.
 *
 * @author Sergey Kichenko
 * @created 27.04.15 00:00
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class MessageServerResponseListener implements Runnable, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private IMessagingClient messagingClient;
    private TaskExecutor serverTaskExecutor;

    private long timeOut;
    private TimeUnit timeUnit;
    private volatile boolean running = true;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public void preDestroy() {
        running = false;
    }

    @Override
    public void run() {

        while (running) {
            try {
                if (IMessagingClient.State.Connected.equals(messagingClient.getState())) {
                    IMessagingQueue<ResponseEnvelope> clientQueue = messagingClient.<ResponseEnvelope>getClientQueue();

                    if (log.isDebugEnabled()) {
                        log.debug("Client queue={} ", clientQueue);
                    }

                    log.info("Waiting for a response message from the server");
                    ResponseEnvelope envelope = clientQueue.poll(timeOut, timeUnit);
                    log.info("Getting the response message from the server, start processing");
                    serverTaskExecutor.execute((MessageServerResponseHandler) applicationContext.getBean("messageServerResponseHandler", envelope));
                } else {
                    Thread.sleep(5000);
                }
            } catch (InterruptedException ex) {
                log.error("Server message listener - Thread is interrupted", ex);
                break;
            } catch (Exception ex) {
                log.error("Server message listener - Unknown exception", ex);
            }
        }

        if (running) {
            log.warn("Exit from message server listener in active status");
            running = false;
        } else {
            log.info("Exit from message server listener ");
        }
    }
}