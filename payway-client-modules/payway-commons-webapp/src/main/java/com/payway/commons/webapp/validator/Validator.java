/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.validator;

/**
 * Validator interface for implement validators
 *
 * @author Sergey Kichenko
 * @created 08.05.15 00:00
 */
public interface Validator {

    boolean validate(Object data);
}
