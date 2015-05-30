/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.validator;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * FileAgentExpressionValidator
 *
 * @author Sergey Kichenko
 * @created 30.05.15 00:00
 */
@NoArgsConstructor
public class AgentFileExpressionValidator implements Validator {

    @Override
    public boolean validate(Object data) {
        if (data instanceof String) {
            return StringUtils.isBlank((String) data);
        }
        return false;
    }
}
