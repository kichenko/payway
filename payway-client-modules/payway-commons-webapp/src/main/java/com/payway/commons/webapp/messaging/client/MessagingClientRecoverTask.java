/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging.client;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * MessageClientRecoverTask
 *
 * @author Sergey Kichenko
 * @created 01.06.15 00:00
 */
@Slf4j
@Getter
@Setter
@Scope(value = "prototype")
@Component(value = "app.MessagingClientRecoverTask")
public class MessagingClientRecoverTask implements Runnable {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private IMessagingClient messagingClient;

    @Value("${app.client.recover.task.period}")
    private long recoverPeriod;

    @Value("true")
    private boolean running;

    private ApplicationEvent event;

    public MessagingClientRecoverTask() {
        //
    }

    public MessagingClientRecoverTask(ApplicationEvent event) {
        this.event = event;
    }

    @Override
    public void run() {
        try {

            if (log.isDebugEnabled()) {
                log.debug("Start recover task executing");
            }

            while (running) {

                if (log.isDebugEnabled()) {
                    log.debug("Execute recover messaging client task");
                }

                if (IMessagingClient.State.Connected.equals(messagingClient.getState())) {
                    running = false;
                    break;
                }

                try {

                    messagingClient.construct();

                    if (log.isDebugEnabled()) {
                        log.debug("Recover messaging client task executing success");
                    }

                    if (event != null) {

                        if (log.isDebugEnabled()) {
                            log.debug("Recover messaging client task publish event {}", event);
                        }

                        applicationContext.publishEvent(event);
                    }

                    break;
                } catch (Exception ex) {
                    log.error("Recover messaging client task failed", ex);
                    if (ex.getCause() != null && ex.getCause().getClass().getName().equals(InterruptedException.class.getName())) {
                        Thread.currentThread().interrupt();
                    }
                }

                if (log.isDebugEnabled()) {
                    log.debug("Start waiting for running recover messaging client task");
                }

                Thread.sleep(recoverPeriod);

                if (log.isDebugEnabled()) {
                    log.debug("End waiting for running recover messaging client task");
                }

            }
        } catch (Exception ex) {
            log.error("Bad executing messaging client recover task", ex);
        }

        if (log.isDebugEnabled()) {
            log.debug("End recover task executing");
        }
    }
}
