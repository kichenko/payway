/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.settings.convertor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JsonWebAppSettingsConvert
 *
 * @author Sergey Kichenko
 * @created 24.08.2015
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JsonWebAppSettingsConvert {

}
