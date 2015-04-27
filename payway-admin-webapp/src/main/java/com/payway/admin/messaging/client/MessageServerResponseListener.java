/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.messaging.client;

import com.payway.messaging.core.ResponseEnvelope;
import java.util.concurrent.BlockingQueue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;

/**
 * Слушатель ответов с сервера. Запускается в потоке при старте приложения.
 * Синглтон, т.к. нужен один на все приложение.
 *
 * @author Sergey Kichenko
 * @created 27.04.15 00:00
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageServerResponseListener implements Runnable, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private TaskExecutor serverTaskExecutor;
    private BlockingQueue<ResponseEnvelope> serverResponseQueue;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ResponseEnvelope envelope = serverResponseQueue.take();
                serverTaskExecutor.execute((MessageServerResponseHandler) applicationContext.getBean("messageServerResponseHandler", envelope));
            } catch (Exception ex) {
                //
            }
        }
    }
}
