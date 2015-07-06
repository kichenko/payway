/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.terminal.encashment.container;

import com.vaadin.data.util.BeanContainer;
import lombok.extern.slf4j.Slf4j;

/**
 * BanknoteNominalEncashmentContainerBean
 *
 * @author Sergey Kichenko
 * @created 06.07.15 00:00
 */
@Slf4j
public class BanknoteNominalEncashmentContainerBean extends BeanContainer<String, BanknoteNominalEncashment> {

    private static final long serialVersionUID = -3496881705838064114L;

    public BanknoteNominalEncashmentContainerBean() {
        super(BanknoteNominalEncashment.class);
        setBeanIdProperty("id");
    }
}