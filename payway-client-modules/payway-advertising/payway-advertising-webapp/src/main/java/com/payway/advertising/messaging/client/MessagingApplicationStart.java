/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.messaging.client;

import com.payway.advertising.web.ApplicationStartEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

/**
 *
 * @author sergey
 */
@Component(value = "messagingApplicationStart")
public class MessagingApplicationStart implements ApplicationListener<ApplicationStartEvent> {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    @Qualifier(value = "serverTaskExecutor")
    private TaskExecutor serverTaskExecutor;

    @Override
    public void onApplicationEvent(ApplicationStartEvent event) {
        serverTaskExecutor.execute((MessagingClientRecoverTask) applicationContext.getBean("messagingClientRecoverTask"));
    }
}
