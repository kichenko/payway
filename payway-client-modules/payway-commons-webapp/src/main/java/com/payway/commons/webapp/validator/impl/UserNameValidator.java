/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.validator.impl;

import com.payway.commons.webapp.validator.Validator;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * UserNameValidator
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
@AllArgsConstructor
@Component(value = "app.UserNameValidator")
public class UserNameValidator implements Validator {

    private final static Pattern pattern = Pattern.compile("^[^\\/*{}&.,;'\"()|:<>?#$@!%\\t\\s\\f\\r\\n]+$");

    @Override
    public boolean validate(Object data) {

        if (data instanceof String) {
            return pattern.matcher((String) data).matches();
        }

        return false;
    }
}
