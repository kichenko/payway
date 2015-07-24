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
 * SuccessWizardStep
 *
 * @author Sergey Kichenko
 * @created 02.07.15 00:00
 */
@Getter
public final class SuccessWizardStep extends AbstractWizardStep {

    private static final long serialVersionUID = -4034008235986621684L;

    @UiField
    private Label lbSuccess;

    @UiField
    private Label lbMessage;

    public SuccessWizardStep() {
        init();
    }

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("SuccessWizardStep.xml", this));
        lbSuccess.setValue(FontAwesome.CHECK.getHtml() + " Success");
    }
}
