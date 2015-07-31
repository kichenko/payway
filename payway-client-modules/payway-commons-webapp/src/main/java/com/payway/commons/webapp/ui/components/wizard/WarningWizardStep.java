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
 * WarningWizardStep
 *
 * @author Sergey Kichenko
 * @created 02.07.15 00:00
 */
@Getter
public final class WarningWizardStep extends AbstractWizardStep {

    private static final long serialVersionUID = -46604939612244693L;

    @UiField
    protected Label lbWarning;

    @UiField
    protected Label lbReason;

    public WarningWizardStep() {
        init();
    }

    public WarningWizardStep(String reason) {
        init();
        lbReason.setValue(reason);
    }

    public WarningWizardStep(String warning, String reason) {
        init();
        lbWarning.setValue(warning);
        lbReason.setValue(reason);
    }

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("WarningWizardStep.xml", this));
        lbWarning.setValue(FontAwesome.WARNING.getHtml() + " Warning");
    }
}
