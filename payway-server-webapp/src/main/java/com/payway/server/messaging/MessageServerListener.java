/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.server.messaging;

import com.payway.messaging.core.Envelope;
import java.util.concurrent.BlockingQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Обработчик получения клиентских пакетов. Всего будет один объект, больше не
 * нужно.
 *
 * @author Sergey Kichenko
 * @created 25.04.15 00:00
 */
@Component(value = "messageServerListener")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MessageServerListener implements Runnable {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    @Qualifier("serverTaskExecutor")
    private TaskExecutor serverTaskExecutor;

    @Autowired
    @Qualifier("blockingQueue")
    private BlockingQueue<Envelope> blockingQueue;

    /**
     * Получение, отправка сообщения(конверта) на обработку в отдельный поток.
     */
    @Override
    public void run() {
        try {
            while (true) {
                Envelope envelope = blockingQueue.take();
                serverTaskExecutor.execute((MessageServerRequestHandler) applicationContext.getBean("messageServerRequestHandler", envelope));
            }
        } catch (Exception ex) {
            //
        }
    }
}
