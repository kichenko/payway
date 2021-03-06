/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.bus;

import com.google.common.eventbus.EventBus;
import com.payway.commons.webapp.config.SubscribeOnAppEventBus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * AppEventBusImpl
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class AppEventBusImpl implements AppEventBus, AppEventPublisher, BeanPostProcessor {

    @Getter
    @Setter
    private EventBus eventBus;

    @Override
    public void addSubscriber(Object subscriber) {
        eventBus.register(subscriber);
    }

    @Override
    public void removeSubscriber(Object subscriber) {
        eventBus.unregister(subscriber);
    }

    @Override
    public void sendNotification(Object event) {
        eventBus.post(event);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (AnnotationUtils.findAnnotation(bean.getClass(), SubscribeOnAppEventBus.class) != null) {
            log.debug("AppEventBus bean post processor got bean - {} with class - {}", beanName, bean.getClass().getName());
            addSubscriber(bean);
        }

        return bean;
    }

}
