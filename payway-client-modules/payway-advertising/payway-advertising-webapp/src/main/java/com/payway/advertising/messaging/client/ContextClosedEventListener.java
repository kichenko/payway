/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.payway.advertising.messaging.client;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 *
 * @author Admin
 */
@Slf4j
@Component
class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    @Qualifier(value = "serverTaskExecutor")
    private ThreadPoolTaskExecutor serverTaskExecutor;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.debug("****");
        try {
            serverTaskExecutor.getThreadPoolExecutor().shutdown();
            serverTaskExecutor.getThreadPoolExecutor().awaitTermination(20, TimeUnit.SECONDS);
        } catch (Exception ex) {
            //
        }
        log.debug("%%%%%%%");
    }
}
