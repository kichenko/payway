/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.messaging.client;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Component(value = "messagingClientRecoverTask")
public class MessagingClientRecoverTask implements Runnable {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    @Qualifier(value = "messagingClient")
    private IMessagingClient messagingClient;

    @Value("5000")
    private long recoverPeriod;

    @Value("true")
    private boolean running;

    private ApplicationEvent publishedEvent;

    public MessagingClientRecoverTask() {
        //
    }

    public MessagingClientRecoverTask(boolean publishEvent, ApplicationEvent publishedEvent) {
        this.publishedEvent = publishedEvent;
    }

    @Override
    public void run() {
        try {
            while (running) {

                if (log.isDebugEnabled()) {
                    log.debug("Try to recover messaging client...");
                }

                if (IMessagingClient.State.Connected.equals(messagingClient.getState())) {
                    running = false;
                    break;
                }

                if (messagingClient.construct()) {
                    if (log.isDebugEnabled()) {
                        log.debug("Recover messaging client success");
                    }

                    if (publishedEvent != null) {
                        applicationContext.publishEvent(publishedEvent);
                    }

                    break;
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Recover messaging client failed");
                    }
                }

                if (log.isDebugEnabled()) {
                    log.debug("Waiting for running recover messaging client task");
                }

                Thread.sleep(recoverPeriod);
            }
        } catch (Exception ex) {
            log.error("Bad messaging client recover task", ex);
        }
    }
}
