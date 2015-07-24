/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.model.core;

import lombok.Getter;

/**
 * BeanNameModel
 *
 * @author Sergey Kichenko
 * @created 22.07.15 00:00
 */
@Getter
public abstract class BeanNameModel extends BeanModel {

    private final String name;

    public BeanNameModel(long id, String name) {
        super(id);
        this.name = name;
    }
}
