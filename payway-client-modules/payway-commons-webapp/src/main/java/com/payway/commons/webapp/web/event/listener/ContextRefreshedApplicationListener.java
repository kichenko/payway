/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.web.event.listener;

import com.payway.commons.webapp.messaging.MessageServerResponseListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Запуск потока для обработки поступающих от сервера ответов.
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
@Slf4j
@Component(value = "app.ContextRefreshedApplicationListener")
public class ContextRefreshedApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ThreadPoolTaskExecutor serverTaskExecutor;

    @Autowired
    private MessageServerResponseListener messageServerResponseListener;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        serverTaskExecutor.execute(messageServerResponseListener);
    }
}
