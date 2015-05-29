/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.validator;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

/**
 * FileNameValidator
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
@Component(value = "fileNameValidator")
public class FileNameValidator implements Validator {

    private final static Pattern pattern = Pattern.compile("^[^*&%\\s]+$");

    @Override
    public boolean validate(Object data) {

        if (data instanceof String) {
            return !((String) data).isEmpty();
        }

        return false;
    }
}
