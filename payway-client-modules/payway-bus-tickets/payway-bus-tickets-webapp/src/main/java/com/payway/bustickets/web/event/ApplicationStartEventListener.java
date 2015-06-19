/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.web.event;

import com.payway.web.event.ApplicationStartEvent;
import com.payway.web.event.ApplicationStartClientConnectedEvent;
import com.payway.commons.webapp.messaging.client.MessagingClientRecoverTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Component(value = "applicationStartEventListener")
public class ApplicationStartEventListener implements ApplicationListener<ApplicationStartEvent> {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    @Qualifier(value = "serverTaskExecutor")
    private TaskExecutor serverTaskExecutor;

    @Override
    public void onApplicationEvent(ApplicationStartEvent event) {
        serverTaskExecutor.execute((MessagingClientRecoverTask) applicationContext.getBean("messagingClientRecoverTask", new ApplicationStartClientConnectedEvent(this)));
    }
}
