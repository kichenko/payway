/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.core;

import com.payway.kioskcashier.ui.model.core.BanknoteNominalModel;
import com.vaadin.data.util.BeanContainer;
import lombok.extern.slf4j.Slf4j;

/**
 * BanknoteNominalModelBeanContainer
 *
 * @author Sergey Kichenko
 * @created 06.07.15 00:00
 */
@Slf4j
public class BanknoteNominalModelBeanContainer extends BeanContainer<Long, BanknoteNominalModel> {

    private static final long serialVersionUID = -3496881705838064114L;

    public BanknoteNominalModelBeanContainer() {
        super(BanknoteNominalModel.class);
        setBeanIdProperty("id");
    }
}
