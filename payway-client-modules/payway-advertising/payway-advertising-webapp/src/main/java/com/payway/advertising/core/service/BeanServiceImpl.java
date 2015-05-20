/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * BeanServiceImpl
 *
 * @author Sergey Kichenko
 * @created 20.05.15 00:00
 */
@Component(value = "beanService")
public class BeanServiceImpl implements BeanService, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object getBean(String name, Object... args) {
        return applicationContext.getBean(name, args);
    }

    @Override
    public Object getBean(String name) {
        return applicationContext.getBean(name);
    }
}
