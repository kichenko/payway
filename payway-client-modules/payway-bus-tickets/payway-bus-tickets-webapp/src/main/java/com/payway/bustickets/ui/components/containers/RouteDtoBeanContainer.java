/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.components.containers;

import com.payway.messaging.model.bustickets.RouteDto;
import com.vaadin.data.util.BeanContainer;
import lombok.extern.slf4j.Slf4j;

/**
 * RouteDtoBeanContainer
 *
 * @author Sergey Kichenko
 * @created 10.06.15 00:00
 */
@Slf4j
public class RouteDtoBeanContainer extends BeanContainer<String, RouteDto> {

    private static final long serialVersionUID = -3496881705838064114L;

    public RouteDtoBeanContainer() {
        super(RouteDto.class);
        setBeanIdProperty("mnemonics");
    }
}
