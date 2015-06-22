/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.components.containers;

import com.payway.messaging.model.common.ChoiceDto;
import com.vaadin.data.util.BeanContainer;
import lombok.extern.slf4j.Slf4j;

/**
 * ChoiceDtoBeanContainer
 *
 * @author Sergey Kichenko
 * @created 10.06.15 00:00
 */
@Slf4j
public class ChoiceDtoBeanContainer extends BeanContainer<String, ChoiceDto> {

    private static final long serialVersionUID = -3496881705838064114L;

    public ChoiceDtoBeanContainer() {
        super(ChoiceDto.class);
        setBeanIdProperty("mnemonics");
    }
}
