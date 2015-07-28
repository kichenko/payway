/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.bean;

import java.io.Serializable;
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
@Component(value = "app.advertising.BeanService")
public class BeanServiceImpl implements BeanService, ApplicationContextAware, Serializable {

    private static final long serialVersionUID = 5046726960155159535L;

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
