/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.validator;

import com.payway.advertising.model.DbAgentFile;
import com.payway.commons.webapp.validator.Validator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * DbAgentFileValidator
 *
 * @author Sergey Kichenko
 * @created 14.05.15 00:00
 */
@NoArgsConstructor
@AllArgsConstructor
public class AgentFileValidator implements Validator {

    private Validator expressionValidator;

    @Override
    public boolean validate(Object data) {

        if (data instanceof DbAgentFile) {
            DbAgentFile file = (DbAgentFile) data;
            return (expressionValidator != null ? expressionValidator.validate(file.getExpression()) : false) && !StringUtils.isBlank(file.getName()) && file.getSeqNo() != null && file.getKind() != null && !StringUtils.isBlank(file.getDigest());
        }

        return false;
    }
}
