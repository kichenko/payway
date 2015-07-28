/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.web.event.listener;

import java.util.concurrent.TimeUnit;
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
        try {
            serverTaskExecutor.getThreadPoolExecutor().shutdown();
            serverTaskExecutor.getThreadPoolExecutor().awaitTermination(20, TimeUnit.SECONDS);

            if (serverTaskExecutor.getThreadPoolExecutor().isTerminated()) {
                log.info("Server task executor on context closed event terminated success");
            } else {
                log.warn("Server task executor on context closed event terminated failed");
            }

        } catch (Exception ex) {
            log.warn("Bad shutdown server task executor on context closed event", ex);
        }
    }
}
