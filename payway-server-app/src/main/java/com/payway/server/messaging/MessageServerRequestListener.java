/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.server.messaging;

import com.payway.messaging.core.AbstractEnvelope;
import com.payway.messaging.core.RequestEnvelope;
import java.util.concurrent.BlockingQueue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;

/**
 * Обработчик получения запросов. Всего будет один объект, больше не нужно.
 *
 * @author Sergey Kichenko
 * @created 25.04.15 00:00
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageServerRequestListener implements Runnable, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private TaskExecutor serverTaskExecutor;
    private BlockingQueue<RequestEnvelope> serverQueue;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    /**
     * Получение, отправка сообщения(конверта) на обработку в отдельный поток.
     */
    @Override
    public void run() {
        try {
            while (true) {
                AbstractEnvelope envelope = serverQueue.take();
                serverTaskExecutor.execute((MessageServerRequestHandler) applicationContext.getBean("messageServerRequestHandler", envelope));
            }
        } catch (Exception ex) {
            //
        }
    }
}
