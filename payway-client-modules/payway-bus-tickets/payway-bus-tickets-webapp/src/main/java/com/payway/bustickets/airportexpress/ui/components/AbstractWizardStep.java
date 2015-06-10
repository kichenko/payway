/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.components;

import com.vaadin.ui.VerticalLayout;
import lombok.extern.slf4j.Slf4j;

/**
 * AbstractWizardStep
 *
 * @author Sergey Kichenko
 * @created 08.06.15 00:00
 */
@Slf4j
public abstract class AbstractWizardStep extends VerticalLayout {

    public boolean validate() {
        return false;
    }
}
