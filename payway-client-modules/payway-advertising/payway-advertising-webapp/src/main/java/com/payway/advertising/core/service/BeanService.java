/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

/**
 * BeanService
 *
 * @author Sergey Kichenko
 * @created 20.05.15 00:00
 */
public interface BeanService {

    Object getBean(String name);

    Object getBean(String name, Object... args);
}
