/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.web.event.listener;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("15")
    private int awaitTerminationTimeOut;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {

        try {
            log.debug("Shutdown server task executor in context close event...");
            serverTaskExecutor.getThreadPoolExecutor().shutdownNow();
            if (serverTaskExecutor.getThreadPoolExecutor().awaitTermination(awaitTerminationTimeOut, TimeUnit.SECONDS)) {
                log.debug("Shutdown server task executor is successfully terminated");
            } else {
                log.debug("Shutdown server task executor is failed terminate");
            }
        } catch (Exception ex) {
            log.error("Failed shutdown server task executor in context close event -", ex);
        }
    }
}
