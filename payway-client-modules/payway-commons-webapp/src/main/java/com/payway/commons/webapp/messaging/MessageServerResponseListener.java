/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging;

import com.payway.commons.webapp.messaging.client.MessagingClient;
import com.payway.messaging.core.ResponseEnvelope;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;

/**
 * MessageServerResponseListener
 *
 * @author Sergey Kichenko
 * @created 27.04.15 00:00
 */
@Slf4j
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageServerResponseListener implements Runnable, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private MessagingClient messagingClient;

    private TaskExecutor serverTaskExecutor;

    private long timeOut;

    private TimeUnit timeUnit;

    @Value("5000")
    private long clientTimeOut;

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
                if (getMessagingClient().isConnected()) {
                    ResponseEnvelope envelope = getMessagingClient().<ResponseEnvelope>getClientQueue().poll(timeOut, timeUnit);
                    if (envelope != null) {
                        log.info("Receive & processing response message from the server");
                        serverTaskExecutor.execute((MessageServerResponseHandler) applicationContext.getBean("app.MessageServerResponseHandler", envelope));
                    }
                } else {
                    log.debug("Message server response listener cannot detect client message connection, start waiting [{}] ms...", clientTimeOut);
                    Thread.sleep(clientTimeOut);
                }
            } catch (InterruptedException ex) {
                log.error("Message server response listener detect interrupted exception, stop running and exit");
                break;
            } catch (Exception ex) {
                log.error("Message server response listener exception - [{}]", ex);
            }
        }

        if (running) {
            log.warn("Exit from message server listener in active status");
            running = false;
        } else {
            log.info("Exit from message server listener");
        }
    }
}
