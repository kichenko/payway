/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SubscribeOnAppEventBus
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SubscribeOnAppEventBus {

}
