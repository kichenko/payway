/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.core.servlet;

import com.payway.admin.messaging.client.MessageServerResponseListener;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *
 * @author Sergey Kichenko
 * @created 28.04.15 00:00
 */
@Setter
@Getter
@Slf4j
public class MyBean {

    private ThreadPoolTaskExecutor serverTaskExecutor;
    private MessageServerResponseListener messageServerResponseListener;

    public void init() {
        log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        serverTaskExecutor.execute(messageServerResponseListener);
    }
}
