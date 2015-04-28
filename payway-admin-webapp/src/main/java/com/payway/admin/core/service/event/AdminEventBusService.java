/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.core.service.event;

import com.google.gwt.thirdparty.guava.common.eventbus.EventBus;
import com.google.gwt.thirdparty.guava.common.eventbus.SubscriberExceptionContext;
import com.google.gwt.thirdparty.guava.common.eventbus.SubscriberExceptionHandler;
import com.payway.admin.core.event.AdminBusEvent;
import java.io.Serializable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * AdminEventBusService
 *
 * @author Sergey Kichenko
 * @created 22.04.15 00:00
 */
@Component
@Scope("prototype")
public class AdminEventBusService implements SubscriberExceptionHandler, Serializable {

    private final EventBus eventBus = new EventBus(this);

    public void post(final AdminBusEvent event) {
        eventBus.post(event);
    }

    public void register(final Object object) {
        eventBus.register(object);
    }

    public void unregister(final Object object) {
        eventBus.unregister(object);
    }

    @Override
    public final void handleException(final Throwable exception, final SubscriberExceptionContext context) {
        exception.printStackTrace();
    }
}
