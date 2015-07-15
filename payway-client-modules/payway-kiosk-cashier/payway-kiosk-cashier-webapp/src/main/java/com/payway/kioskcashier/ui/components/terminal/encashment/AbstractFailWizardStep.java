/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.terminal.encashment;

import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStep;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Label;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * AbstractFailWizardStep
 *
 * @author Sergey Kichenko
 * @created 02.07.15 00:00
 */
@Getter
@NoArgsConstructor
public abstract class AbstractFailWizardStep extends AbstractWizardStep {

    private static final long serialVersionUID = -46604939612244693L;

    @UiField
    protected Label lbFail;

    @UiField
    protected Label lbReason;

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("FailWizardStep.xml", this));
        lbFail.setValue(FontAwesome.BAN.getHtml() + " Failed");
    }
}
