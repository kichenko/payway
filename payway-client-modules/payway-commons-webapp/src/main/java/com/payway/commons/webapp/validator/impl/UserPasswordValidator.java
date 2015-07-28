/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.validator.impl;

import com.payway.commons.webapp.validator.Validator;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * UserPasswordValidator
 *
 * @author Sergey Kichenko
 * @created 24.05.15 00:00
 */
@AllArgsConstructor
@Component(value = "app.UserPasswordValidator")
public class UserPasswordValidator implements Validator {

    @Override
    public boolean validate(Object data) {
        return StringUtils.isNotBlank((String) data);
    }
}
