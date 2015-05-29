/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.messaging;

import com.hazelcast.core.HazelcastInstanceNotActiveException;
import com.payway.messaging.core.ResponseEnvelope;
import java.util.concurrent.BlockingQueue;
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
    private TaskExecutor serverTaskExecutor;
    private BlockingQueue<ResponseEnvelope> clientQueue;

    private long timeOut;
    private TimeUnit timeUnit;

    private volatile boolean interrupt = false;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public void preDestroy() {
        interrupt = true;
    }

    @Override
    public void run() {

        if (log.isDebugEnabled()) {
            log.debug("Client queue={} ", clientQueue);
        }

        while (!interrupt) {
            try {
                log.info("Waiting for a response message from the server");
                ResponseEnvelope envelope = clientQueue.poll(timeOut, timeUnit);
                log.info("Getting the response message from the server, start processing");
                serverTaskExecutor.execute((MessageServerResponseHandler) applicationContext.getBean("messageServerResponseHandler", envelope));
            } catch (InterruptedException ex) {
                log.error("Server message listener thread is interrupted", ex);
                break;
            } catch (HazelcastInstanceNotActiveException ex) {
                log.error("Hazelcast instance is not active", ex);
                break;
            } catch (Exception ex) {
                log.error("Unknown exception in server message listener", ex);
            }
        }

        if (interrupt) {
            log.warn("Exit from message server listener in active status");
            interrupt = false;
        } else {
            log.info("Exit from message server listener ");
        }
    }
}
