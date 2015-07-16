/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.components.wizard;

/**
 * WizardStepValidationException
 *
 * @author Sergey Kichenko
 * @created 16.07.15 00:00
 */
public class WizardStepValidationException extends Exception {
    
    private static final long serialVersionUID = 428147708007809991L;
    
    public WizardStepValidationException() {
        //
    }
    
    public WizardStepValidationException(String message) {
        super(message);
    }
    
    public WizardStepValidationException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
