/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.server.messaging;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * Обработчик отказов для TaskExecutor'а. Синглтон, т.к. не хранит состояния.
 *
 * @author Sergey Kichenko
 * @created 25.04.15 00:00
 */
@Slf4j
public class RejectExecutionPolicy implements RejectedExecutionHandler {

    /**
     * Обработка отказов задач у TaskExecutor'а.
     */
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        log.error("thread is rejected");
        if (r != null && r instanceof MessageServerRequestHandler) {
            //
        }
    }
}
