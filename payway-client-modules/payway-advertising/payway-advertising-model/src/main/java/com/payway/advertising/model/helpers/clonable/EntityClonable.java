/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.model.helpers.clonable;

/**
 * EntityClonable
 *
 * @author Sergey Kichenko
 * @param <T>
 * @created 21.05.15 00:00
 */
public interface EntityClonable<T extends Object> {

    T clone(T t);
}
