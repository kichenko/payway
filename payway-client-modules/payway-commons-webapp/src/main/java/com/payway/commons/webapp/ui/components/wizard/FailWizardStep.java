/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.components.wizard;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Label;
import lombok.Getter;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * AbstractFailWizardStep
 *
 * @author Sergey Kichenko
 * @created 02.07.15 00:00
 */
@Getter
public final class FailWizardStep extends AbstractWizardStep {

    private static final long serialVersionUID = -46604939612244693L;

    @UiField
    protected Label lbFail;

    @UiField
    protected Label lbReason;
    
    public FailWizardStep() {
        init();
    }

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("FailWizardStep.xml", this));
        lbFail.setValue(FontAwesome.BAN.getHtml() + " Failed");
    }
}
