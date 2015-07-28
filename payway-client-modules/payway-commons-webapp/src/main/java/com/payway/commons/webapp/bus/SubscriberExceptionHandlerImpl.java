/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.bus;

import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * SubscriberExceptionHandlerImpl
 *
 * @author Sergey Kichenko
 * @created 22.05.15 00:00
 */
@Slf4j
public class SubscriberExceptionHandlerImpl implements SubscriberExceptionHandler {

    @Override
    public void handleException(Throwable exception, SubscriberExceptionContext context) {
        log.error("Error in event bus [{}] with context [{}]", exception, context);
    }
}
