/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.components.airportexpress;

import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * AbstractWizardStep
 *
 * @author Sergey Kichenko
 * @created 08.06.15 00:00
 */
@Slf4j
@Getter
@Setter
public abstract class AbstractWizard extends VerticalLayout {

    protected List<AbstractWizardStep> steps = new ArrayList<>();
    protected int step;
}
