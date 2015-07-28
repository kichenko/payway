/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.web.event.listener;

import com.payway.commons.webapp.messaging.client.MessagingClientRecoverTask;
import com.payway.commons.webapp.web.event.ApplicationStartClientConnectedEvent;
import com.payway.commons.webapp.web.event.ApplicationStartEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.task.TaskExecutor;
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
    private ApplicationContext applicationContext;

    @Autowired
    private TaskExecutor serverTaskExecutor;

    @Override
    public void onApplicationEvent(ApplicationStartEvent event) {
        serverTaskExecutor.execute((MessagingClientRecoverTask) applicationContext.getBean("app.MessagingClientRecoverTask", new ApplicationStartClientConnectedEvent(this)));
    }
}
