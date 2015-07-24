/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.wizard.terminal.encashment.datasource;

import com.payway.messaging.model.kioskcashier.KioskEncashmentDto;
import com.vaadin.data.util.BeanContainer;
import lombok.extern.slf4j.Slf4j;

/**
 * KioskEncashmentDtoModelBeanContainer
 *
 * @author Sergey Kichenko
 * @created 20.07.15 00:00
 */
@Slf4j
public class KioskEncashmentDtoModelBeanContainer extends BeanContainer<Long, KioskEncashmentDto> {

    private static final long serialVersionUID = -3496881705838064114L;

    public KioskEncashmentDtoModelBeanContainer() {
        super(KioskEncashmentDto.class);
        setBeanIdProperty("id");
    }
}
