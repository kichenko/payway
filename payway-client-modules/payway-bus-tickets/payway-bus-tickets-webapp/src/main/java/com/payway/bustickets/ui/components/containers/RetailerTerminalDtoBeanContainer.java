/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.components.containers;

import com.payway.messaging.model.common.RetailerTerminalDto;
import com.vaadin.data.util.BeanContainer;
import lombok.extern.slf4j.Slf4j;

/**
 * RouteDtoBeanContainer
 *
 * @author Sergey Kichenko
 * @created 11.06.15 00:00
 */
@Slf4j
public class RetailerTerminalDtoBeanContainer extends BeanContainer<String, RetailerTerminalDto> {

    private static final long serialVersionUID = -3496881705838064114L;

    public RetailerTerminalDtoBeanContainer() {
        super(RetailerTerminalDto.class);
        setBeanIdProperty("terminalName");
    }
}
