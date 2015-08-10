/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.web.event.listener;

import com.payway.commons.webapp.messaging.MessageServerResponseListener;
import com.payway.commons.webapp.messaging.client.MessagingClient;
import com.payway.commons.webapp.web.event.ApplicationStartEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * ApplicationStartEventListener
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
@Component(value = "app.ApplicationStartEventListener")
public class ApplicationStartEventListener implements ApplicationListener<ApplicationStartEvent> {

    @Autowired
    private MessagingClient client;

    @Autowired
    private ThreadPoolTaskExecutor serverTaskExecutor;

    @Autowired
    private MessageServerResponseListener messageServerResponseListener;

    @Override
    public void onApplicationEvent(ApplicationStartEvent event) {
        client.start(true);
        serverTaskExecutor.execute(messageServerResponseListener);
    }
}
