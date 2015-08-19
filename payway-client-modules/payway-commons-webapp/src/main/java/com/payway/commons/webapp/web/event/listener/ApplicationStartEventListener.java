/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.web.event.listener;

import com.payway.commons.webapp.messaging.MessageServerResponseListener;
import com.payway.commons.webapp.messaging.client.MessagingClient;
import com.payway.commons.webapp.web.event.ApplicationStartEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

/**
 * ApplicationStartEventListener
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
@Slf4j
@Component(value = "app.ApplicationStartEventListener")
public class ApplicationStartEventListener implements ApplicationListener<ApplicationStartEvent> {

    @Autowired
    private MessagingClient client;

    @Autowired
    @Qualifier("app.ServerTaskExecutor")
    private TaskExecutor serverTaskExecutor;

    @Autowired
    private MessageServerResponseListener messageServerResponseListener;

    @Override
    public void onApplicationEvent(ApplicationStartEvent event) {
        
        try {
            client.startAsync();
            serverTaskExecutor.execute(messageServerResponseListener);
        } catch (Exception ex) {
            log.error("Application context start exception - ", ex);
        }
    }
}
