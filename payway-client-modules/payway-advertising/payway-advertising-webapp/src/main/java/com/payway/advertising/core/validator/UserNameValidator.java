/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * UserNameValidator
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
@Component(value = "userNameValidator")
public class UserNameValidator implements Validator {

    @Override
    public boolean validate(Object data) {
        return StringUtils.isNotBlank((String) data);
    }
}
