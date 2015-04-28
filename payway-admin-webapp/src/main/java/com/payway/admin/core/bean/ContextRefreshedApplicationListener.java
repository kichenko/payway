/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.core.bean;

import com.payway.admin.messaging.MessageServerResponseListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Запуск потока для обработки поступающих от сервера ответов.
 *
 * @author Sergey Kichenko
 * @created 28.04.15 00:00
 */
@Slf4j
@Component
public class ContextRefreshedApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    @Qualifier("serverTaskExecutor")
    private ThreadPoolTaskExecutor serverTaskExecutor;

    @Autowired
    @Qualifier("messageServerResponseListener")
    private MessageServerResponseListener messageServerResponseListener;

    @Autowired
    @Qualifier("serverQueueName")
    private String s1;

    @Autowired
    @Qualifier("clientQueueName")
    private String s2;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.debug("#####");
        log.debug(s1);
        log.debug(s2);
        serverTaskExecutor.execute(messageServerResponseListener);
    }
}
