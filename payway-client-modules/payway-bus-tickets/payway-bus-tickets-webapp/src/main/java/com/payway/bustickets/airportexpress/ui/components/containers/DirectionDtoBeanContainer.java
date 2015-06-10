/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.components.containers;

import com.payway.messaging.model.bustickets.DirectionDto;
import com.vaadin.data.util.BeanContainer;
import lombok.extern.slf4j.Slf4j;

/**
 * DirectionDtoBeanContainer
 *
 * @author Sergey Kichenko
 * @created 10.06.15 00:00
 */
@Slf4j
public class DirectionDtoBeanContainer extends BeanContainer<String, DirectionDto> {

    private static final long serialVersionUID = -3496881705838064114L;

    public DirectionDtoBeanContainer() {
        super(DirectionDto.class);
        setBeanIdProperty("mnemonics");
    }
}
