/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.server.messaging;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Обработчик отказов для TaskExecutor'а. Синглтон, т.к. не хранит состояния.
 *
 * @author Sergey Kichenko
 * @created 25.04.15 00:00
 */
@Component(value = "rejectPolicy")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RejectExecutionPolicy implements RejectedExecutionHandler {

    /**
     * Обработка отказов задач у TaskExecutor'а.
     */
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (r != null) {
            if (r instanceof MessageServerRequestHandler) {
                //
            } else {
                //
            }
        }
    }
}
