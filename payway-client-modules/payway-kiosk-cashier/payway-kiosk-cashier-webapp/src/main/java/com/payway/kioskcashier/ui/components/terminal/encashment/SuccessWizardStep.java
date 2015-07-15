/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.terminal.encashment;

import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStep;
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
public final class SuccessWizardStep extends AbstractWizardStep {

    private static final long serialVersionUID = -4034008235986621684L;

    @UiField
    private Label lbSuccess;

    @Getter
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

    public void setUp() {
        lbMessage.setValue("No surplus or shortage found");
    }
}
