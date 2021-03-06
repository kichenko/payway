/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting.ui;

import java.util.Collection;
import java.util.LinkedList;

/**
 * ComponentStateContainerDto
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
public class ComponentStateContainerDto extends ComponentStateDto {

    private static final long serialVersionUID = -1830164568853444253L;

    protected Collection<ComponentStateDto> childs = new LinkedList<>();

    public Collection<ComponentStateDto> getChilds() {
        return childs;
    }

}
