/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.validator;

import com.payway.commons.webapp.validator.Validator;
import lombok.NoArgsConstructor;

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
        return true;
    }
}
