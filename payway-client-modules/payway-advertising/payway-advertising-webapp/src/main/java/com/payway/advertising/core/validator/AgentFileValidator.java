/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.validator;

import com.payway.advertising.model.DbAgentFile;

/**
 * DbAgentFileValidator
 *
 * @author Sergey Kichenko
 * @created 14.05.15 00:00
 */
public class AgentFileValidator implements Validator {

    @Override
    public boolean validate(Object data) {
        DbAgentFile file = (DbAgentFile) data;
        return (file != null && file.getKind() != null /*&& !StringUtils.isBlank(file.getDigest())*/);
    }
}
