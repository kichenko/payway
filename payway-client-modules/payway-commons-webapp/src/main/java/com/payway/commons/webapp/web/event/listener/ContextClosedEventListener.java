/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.web.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * ContextClosedEventListener
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
@Slf4j
@Component(value = "app.ContextClosedEventListener")
class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    private ThreadPoolTaskExecutor serverTaskExecutor;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {

        log.debug("Run shutdown server task executor on context close event...");
        serverTaskExecutor.getThreadPoolExecutor().shutdown();
    }
}
