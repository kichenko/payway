/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.messaging;

import com.payway.messaging.core.ResponseEnvelope;
import java.util.concurrent.BlockingQueue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MessageServerResponseListener implements Runnable, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private TaskExecutor serverTaskExecutor;
    private BlockingQueue<ResponseEnvelope> clientQueue;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ResponseEnvelope envelope = clientQueue.take();
                serverTaskExecutor.execute((MessageServerResponseHandler) applicationContext.getBean("messageServerResponseHandler", envelope));
            } catch (Exception ex) {
                log.error("Ошибка получения ответа от сервера", ex);
            }
        }
    }
}
