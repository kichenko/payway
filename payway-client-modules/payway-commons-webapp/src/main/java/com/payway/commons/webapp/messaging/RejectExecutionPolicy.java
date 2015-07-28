/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * RejectExecutionPolicy
 *
 * @author Sergey Kichenko
 * @created 25.04.15 00:00
 */
@Slf4j
public class RejectExecutionPolicy implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        log.error("Rejected execution of task [{}] in thread pool [{}]", r, executor);
    }
}
