/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.terminal.encashment;

import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStep;
import com.payway.vaadin.addons.ui.textfield.digit.DigitTextField;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.ui.TextField;
import lombok.Getter;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * SearchWizardStep
 *
 * @author Sergey Kichenko
 * @created 03.07.15 00:00
 */
@Getter
public final class SearchWizardStep extends AbstractWizardStep {

    private static final long serialVersionUID = -8297534233174351589L;

    @UiField
    private TextField editTerminal;

    @UiField
    private DigitTextField editReport;

    public SearchWizardStep() {
        init();
    }

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("SearchWizardStep.xml", this));

        editTerminal.setRequired(true);
        editTerminal.setNullRepresentation("");
        editTerminal.addValidator(new NullValidator("Empty terminal name", false));

        editReport.setRequired(true);
        editReport.setConverter(Integer.class);
    }

    @Override
    public boolean validate() {

        editTerminal.setValidationVisible(false);
        editTerminal.setValidationVisible(false);

        try {
            editReport.validate();
            editTerminal.validate();
        } catch (Validator.InvalidValueException ex) {
            editReport.setValidationVisible(true);
            editTerminal.setValidationVisible(true);
            return false;
        }

        return true;
    }
}
