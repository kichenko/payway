/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.validator;

import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * FileNameValidator
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
@AllArgsConstructor
@Component(value = "fileNameValidator")
public class FileNameValidator implements Validator {

    private final static Pattern pattern = Pattern.compile("^[^\\/*{}&,;'\"()|:<>?#$@!%\\t\\f\\r\\n]+$");

    @Override
    public boolean validate(Object data) {

        if (data instanceof String) {
            return pattern.matcher((String) data).matches();
        }

        return false;
    }
}
